package com.edolk.natives.functions;

import java.util.Collection;
import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;

public class Median implements Callable {

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
        if (arg0 instanceof Object[] array) {
            return Sort.sortArray(array)[(int)Math.floor(array.length/2)];
        } else if (arg0 instanceof NativeCollection collection) {
            Collection<Object> col = collection.getAsCollection();
            return Sort.sortArray(col.toArray())[(int)Math.floor(col.size()/2)];
        }
        return null;
    }

    
}
