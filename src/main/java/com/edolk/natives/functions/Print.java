package com.edolk.natives.functions;

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

    public static String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        if (object instanceof Object[] arr) {
            String text = "[";
            for (int i = 0; i < arr.length; i++) {
                Object obj = arr[i];
                if (obj instanceof String str) {
                    str = "\"" + str + "\"";
                    text += str;
                } else {
                    text += stringify(obj);
                }
                if (i != arr.length-1) {
                    text += ", ";
                }
            }
            text += "]";
            return text;
        }

        return object.toString();
    }

    @Override
    public boolean varargs() {
        return false;
    }
}
