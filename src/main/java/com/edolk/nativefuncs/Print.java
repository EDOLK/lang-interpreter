package com.edolk.nativefuncs;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;

public class Print implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        for (Object object : arguments) {
            System.out.println(stringify(object));
        }
        return null;
    }

    @Override
    public String toString() { return "<native fn>"; }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }
}
