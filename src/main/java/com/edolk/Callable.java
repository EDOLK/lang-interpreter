package com.edolk;

import java.util.List;
import java.util.function.BiFunction;

public interface Callable {
    int arity();
    boolean varargs();
    Object call(Interpreter interpreter, List<Object> arguments);
    public static Callable create(int arity, boolean varargs, BiFunction<Interpreter, List<Object>, Object> call){
        return new Callable() {
            @Override
            public int arity() {
                return arity;
            }
            @Override
            public boolean varargs() {
                return varargs;
            }
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return call.apply(interpreter, arguments);
            }
        };
    }
}
