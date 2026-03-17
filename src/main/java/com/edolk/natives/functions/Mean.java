package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;

public class Mean implements Callable {

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
        Object arg0 = arguments.get(0);
        double sum = (double)Sum.sum(arg0);
        if (arg0 instanceof Object[] array) {
            return sum/array.length;
        } else if (arg0 instanceof NativeCollection collection){
            return sum/collection.getAsCollection().size();
        }
        return null;
    }

}
