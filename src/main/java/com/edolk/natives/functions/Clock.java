package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;

public class Clock implements Callable {

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double)System.currentTimeMillis() / 1000.0;
    }

    @Override
    public String toString() { return "<native fn>"; }

    @Override
    public boolean varargs() {
        return false;
    }
}
