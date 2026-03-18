package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;

public class Split implements Callable {

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
        Object arg0 = arguments.get(0);
        Object arg1 = arguments.get(1);
        if (arg0 instanceof String str && arg1 instanceof String reg) {
            return (Object[])str.split(reg);
        }
        return null;
    }

    
}
