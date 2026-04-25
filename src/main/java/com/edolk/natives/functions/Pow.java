package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;

public class Pow implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public boolean varargs() {
        return false;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (arguments.get(0) instanceof Number base) {
            if (arguments.get(1) instanceof Number exponent) {
                return Math.pow(base.doubleValue(), exponent.doubleValue());
            }
        }
        return null;
    }

    
}
