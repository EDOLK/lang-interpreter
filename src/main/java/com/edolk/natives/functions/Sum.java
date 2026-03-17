package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;

public class Sum implements Callable {

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
        return sum(arguments.get(0));
    }

    public static Object sum(Object arg0) {
        if (arg0 instanceof Object[] array) {
            return arraySum(array);
        } else if (arg0 instanceof NativeCollection collection){
            return arraySum(collection.getAsCollection().toArray());
        }
        return null;
    }

    public static double arraySum(Object[] array){
        double sum = 0;
        for (Object object : array) {
            sum += (double)object;
        }
        return sum;
    }

    
}
