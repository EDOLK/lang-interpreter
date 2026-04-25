package com.edolk.natives.functions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;

public class Mode implements Callable {

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
        Map<Object, Integer> countMap = new HashMap<>();
        Object arg0 = arguments.get(0);
        if (arg0 instanceof Object[] array) {
            countMap = populateCountMap(Arrays.asList(array));
        } else if (arg0 instanceof NativeCollection nc) {
            countMap = populateCountMap(nc.getAsCollection());
        }
        int lCount = 0;
        Object lObj = null;
        for (Entry<Object, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > lCount) {
                lCount = entry.getValue();
                lObj = entry.getKey();
            }
        }
        return lObj;
    }

    public static Map<Object, Integer> populateCountMap(Iterable<Object> iterable){
        Map<Object, Integer> countMap = new HashMap<>();
        for (Object object : iterable) {
            countMap.merge(object, 1, (oldVal, newVal) -> {
                return oldVal + newVal;
            });
        }
        return countMap;
    }

}
