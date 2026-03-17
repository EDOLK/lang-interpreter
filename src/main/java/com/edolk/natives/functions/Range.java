package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;

public class Range implements Callable {

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
        Object sorted = Sort.sort(arg0);
        if (sorted instanceof Object[] array) {
            return arrayRange(array);
        } else if(sorted instanceof NativeCollection collection){
            return arrayRange(collection.getAsCollection().toArray());
        }
        return null;
    }

    public static double arrayRange(Object[] array){
        return (double)array[array.length-1] - (double)array[0];
    }

    
}
