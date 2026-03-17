package com.edolk.natives.functions;

import java.util.Arrays;
import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;

public class Sort implements Callable {

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
        return sort(arguments.get(0));
    }

    public static Object sort(Object object){
        if (object instanceof Object[] array) {
            return sortArray(Arrays.copyOf(array, array.length));
        } else if (object instanceof NativeCollection collection){
            return collection.create(sortArray(collection.getAsCollection().toArray()));
        }
        return object;
    }

    public static Object[] sortArray(Object[] array){
        Arrays.sort(array);
        return array;
    }

}
