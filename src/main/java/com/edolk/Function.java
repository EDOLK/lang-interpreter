package com.edolk;

import java.util.List;

public class Function implements Callable {
    private Token token;
    private final Expr.FunctionLiteral literal;
    private final Environment closure;
    private final boolean isInitializer;
    private final boolean varargs;

    Function(Token token, Expr.FunctionLiteral literal, Environment closure,
            boolean isInitializer) {
        this.token = token;
        this.literal = literal;
        this.isInitializer = isInitializer;
        this.closure = closure;
        if (!literal.params.isEmpty()) {
            this.varargs = literal.params.getLast().type == TokenType.ELLIPSES;
        } else {
            this.varargs = false;
        }
    }

    Function(Expr.FunctionLiteral literal, Environment closure,
            boolean isInitializer) {
        this.literal = literal;
        this.isInitializer = isInitializer;
        this.closure = closure;
        if (!literal.params.isEmpty()) {
            this.varargs = literal.params.getLast().type == TokenType.ELLIPSES;
        } else {
            this.varargs = false;
        }
    }

    Function bind(Instance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        if (token != null) {
            return new Function(token, literal, environment,
                    isInitializer);
        }
        return new Function(literal, environment, isInitializer);
    }
    @Override
    public int arity() {
        return literal.params.size();
    }
    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        int i = 0;

        while (i < (varargs ? literal.params.size()-2 : literal.params.size())) {
            environment.define(literal.params.get(i).lexeme, arguments.get(i));
            i++;
        }

        if (varargs) {
            Object[] array = new Object[arguments.size() - i];
            int j = 0;
            while (i < arguments.size()) {
                array[j] = arguments.get(i);
                i++;
                j++;
            }
            environment.define(literal.params.get(literal.params.size()-2).lexeme, array);
        }

        try {
            interpreter.executeBlock(literal.body, environment);
        } catch (Return returnValue) {
            if (isInitializer) return closure.getAt(0, "this");
            return returnValue.value;
        }
        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }

    @Override
    public boolean varargs() {
        return this.varargs;
    }

    @Override
    public String toString() {
        return "<fn " + (token != null ? token.lexeme : "anon") + ">";
    }
}
