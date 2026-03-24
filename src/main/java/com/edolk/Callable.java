package com.edolk;

import java.util.List;
import java.util.function.BiFunction;

public interface Callable extends NeuCallable {
    public int arity();
    public boolean varargs();
    @Override
    default boolean acceptsArgs(List<Object> arguments) {
        if (varargs()) {
            if (arguments.size() < arity()-2) {
                return false;
            }
        } else if (arguments.size() != arity()){
            return false;
        }
        return true;
    }
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
