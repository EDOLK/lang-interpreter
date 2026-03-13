package com.edolk;

import java.util.List;

public class Function implements Callable {
    private Token token;
    private final Expr.FunctionLiteral literal;
    private final Environment closure;
    private final boolean isInitializer;

    Function(Token token, Expr.FunctionLiteral literal, Environment closure,
            boolean isInitializer) {
        this.token = token;
        this.literal = literal;
        this.isInitializer = isInitializer;
        this.closure = closure;
    }

    Function(Expr.FunctionLiteral literal, Environment closure,
            boolean isInitializer) {
        this.literal = literal;
        this.isInitializer = isInitializer;
        this.closure = closure;
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
        for (int i = 0; i < literal.params.size(); i++) {
            environment.define(literal.params.get(i).lexeme, arguments.get(i));
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
    public String toString() {
        return "<fn " + (token != null ? token.lexeme : "anon") + ">";
    }
}
