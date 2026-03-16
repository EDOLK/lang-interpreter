package com.edolk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edolk.Expr.ArrayLiteral;
import com.edolk.Expr.FunctionLiteral;
import com.edolk.Expr.GetArray;
import com.edolk.Expr.SetArray;
import com.edolk.Expr.SliceArray;
import com.edolk.Stmt.Source;
import com.edolk.nativefuncs.Clock;
import com.edolk.nativefuncs.Print;
import com.edolk.natives.classes.NativeList;
import com.edolk.natives.classes.NativeSet;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    final Environment globals = new Environment();
    private Environment environment = globals;
    private final Map<Expr, Integer> locals = new HashMap<>();

    Interpreter() {
        globals.define("clock", new Clock());
        globals.define("print", new Print());
        globals.define("List", new NativeList(false));
        globals.define("Set", new NativeSet(false));
    }

    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Engine.runtimeError(error);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    void resolve(Expr expr, int depth) {
        locals.put(expr, depth);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    // Expressions

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(left)) return left;
        } else {
            if (!isTruthy(left)) return left;
        }

        return evaluate(expr.right);
    }

    @Override
    public Object visitSetExpr(Expr.Set expr) {
        Object object = evaluate(expr.object);

        if (!(object instanceof Instance)) { 
            throw new RuntimeError(expr.name,
                    "Only instances have fields.");
        }

        Object value = evaluate(expr.value);
        ((Instance)object).set(expr.name, value);
        return value;
    }

    @Override
    public Object visitThisExpr(Expr.This expr) {
        return lookUpVariable(expr.keyword, expr);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            default:
        }

        // Unreachable.
        return null;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right); 

        switch (expr.operator.type) {
            case BANG_EQUAL: return !isEqual(left, right);
            case EQUAL_EQUAL: return isEqual(left, right);
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case PLUS:
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                } 
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                throw new RuntimeError(expr.operator,
                        "Operands must be two numbers or two strings.");
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            default:
        }

        // Unreachable.
        return null;
    }

    @Override
    public Object visitCallExpr(Expr.Call expr) {
        Object callee = evaluate(expr.callee);

        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) { 
            arguments.add(evaluate(argument));
        }
        if (!(callee instanceof Callable)) {
            throw new RuntimeError(expr.paren,
                    "Can only call functions and classes.");
        }
        Callable function = (Callable)callee;
        if (function.varargs()) {
            if (arguments.size() < function.arity()-2) {
                throw new RuntimeError(expr.paren, "Expected " +
                        (function.arity()-2) + " arguments but got " +
                        arguments.size() + ".");
            }
        } else if(arguments.size() != function.arity()){
            throw new RuntimeError(expr.paren, "Expected " +
                    function.arity() + " arguments but got " +
                    arguments.size() + ".");
        }
        return function.call(this, arguments);
    }

    @Override
    public Object visitGetExpr(Expr.Get expr) {
        Object object = evaluate(expr.object);
        if (object instanceof Instance) {
            return ((Instance) object).get(expr.name);
        } else if (object instanceof Object[] array){
            if (expr.name.lexeme.equals("length")) {
                return (double)array.length;
            }
        }

        throw new RuntimeError(expr.name,
                "Only instances have properties.");
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return lookUpVariable(expr.name, expr);
    }

    private Object lookUpVariable(Token name, Expr expr) {
        Integer distance = locals.get(expr);
        if (distance != null) {
            return environment.getAt(distance, name.lexeme);
        } else {
            return globals.get(name);
        }
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        Integer distance = locals.get(expr);
        if (distance != null) {
            environment.assignAt(distance, expr.name, value);
        } else {
            globals.assign(expr.name, value);
        }
        return value;
    }

    @Override
    public Object visitFunctionLiteralExpr(FunctionLiteral expr) {
        Function function = new Function(expr, environment, false);
        // environment.define(expr.toString(), function);
        return function;
    }

    @Override
    public Object visitArrayLiteralExpr(ArrayLiteral expr) {
        int size = 0;
        if (expr.sizeExpr != null) {
            Object sizeValue = evaluate(expr.sizeExpr);
            if (sizeValue instanceof Double d && isWholeNumber(d)) {
                size = (int)Math.floor(d);
            } else {
                throw new RuntimeError(expr.rightBracket, "Array size must be whole number.");
            }
        } else {
            size = expr.elements.size();
        }
        if (size < expr.elements.size()) {
            throw new RuntimeError(expr.rightBracket, "Array elements exceed array size.");
        }
        Object[] array = new Object[size];
        for (int i = 0; i < expr.elements.size(); i++) {
            array[i] = evaluate(expr.elements.get(i));
        }
        return array;
    }

    @Override
    public Object visitGetArrayExpr(GetArray expr) {
        Object obj = evaluate(expr.array);
        if (obj instanceof Object[] array) {
            int index = toArrayIndex(array.length, evaluate(expr.index), expr.rightBracket);
            return array[index];
        }
        throw new RuntimeError(expr.rightBracket, "Only arrays can be indexed.");
    }

    @Override
    public Object visitSetArrayExpr(SetArray expr) {
        Object obj = evaluate(expr.array);
        if (obj instanceof Object[] array) {
            int index = toArrayIndex(array.length, evaluate(expr.index), expr.rightBracket);
            Object value = evaluate(expr.value);
            array[index] = value;
            return value;
        }
        throw new RuntimeError(expr.rightBracket, "Only arrays can be indexed.");
    }

    @Override
    public Object visitSliceArrayExpr(SliceArray expr) {
        Object obj = evaluate(expr.array);
        if (obj instanceof Object[] array) {
            int leftIndex = 0;
            int rightIndex = array.length;
            int step = 1;
            if (expr.leftIndex != null) {
                leftIndex = toArrayIndex(array.length, evaluate(expr.leftIndex), expr.rightBracket);
            }
            if (expr.rightIndex != null) {
                if (!(evaluate(expr.rightIndex) instanceof Double d) || !isWholeNumber(d)) {
                    throw new RuntimeError(expr.rightBracket, "Index must be whole number.");
                }
                if (d > array.length) {
                    throw new RuntimeError(expr.rightBracket, "Index out of bounds.");
                }
                rightIndex = (int)Math.floor(d);
            }
            if (expr.step != null) {
                if (evaluate(expr.step) instanceof Double d && isWholeNumber(d)) {
                    step = (int)Math.floor(d);
                }
            }
            List<Object> preArray = new ArrayList<>();
            for (int i = leftIndex; i < rightIndex; i += step) {
                preArray.add(array[i]);
            }
            return preArray.toArray(new Object[preArray.size()]);
        }
        throw new RuntimeError(expr.rightBracket, "Only arrays can be sliced.");
    }

    public int toArrayIndex(int arrayLength, Object index, Token rightBracket){
        if (index instanceof Double d && isWholeNumber(d)) {
            if (d < arrayLength) {
                return (int)Math.floor(d);
            }
            throw new RuntimeError(rightBracket, "Index out of bounds.");
        }
        throw new RuntimeError(rightBracket, "Index must be whole number.");
    }

    public Object getFromArray(Object[] array, int index){
        return array[index];
    }

    // Statements

    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
        Function function = new Function(stmt.name, stmt.literal, environment, false);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        Object value = null;
        if (stmt.value != null) value = evaluate(stmt.value);

        throw new Return(value);
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitSourceStmt(Source stmt) {
        Object value = evaluate(stmt.value);
        if (value instanceof String path) {
            try {
                Engine.runFile(path);
            } catch (Exception e) {
                throw new RuntimeError(stmt.keyword, "Could not open file " + path + ".");
            }
        }
        return null;
    }

    @Override
    public Void visitClassStmt(Stmt.Class stmt) {
        environment.define(stmt.name.lexeme, null);
        Map<String, Function> methods = new HashMap<>();
        for (Stmt.Function method : stmt.methods) {
            Function function = new Function(method.name, method.literal, environment, method.name.lexeme.equals("init"));
            methods.put(method.name.lexeme, function);
        }

        Klass klass = new Klass(stmt.name.lexeme, methods);
        environment.assign(stmt.name, klass);
        return null;
    }

    void executeBlock(List<Stmt> statements,
            Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;

            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }

    private void checkNumberOperands(Token operator,
            Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    public boolean isWholeNumber(Object obj){
        if (obj instanceof Double d) {
            return isWholeNumber(d);
        }
        return false;
    }

    private boolean isWholeNumber(Double d) {
        return d % 1.0 == 0 && d >= 0;
    }

}
