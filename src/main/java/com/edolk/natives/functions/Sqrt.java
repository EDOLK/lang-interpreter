package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;

public class Sqrt implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public boolean varargs() {
        return false;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (arguments.get(0) instanceof Number number) {
            return Math.sqrt(number.doubleValue());
        }
        return null;
    }
}
