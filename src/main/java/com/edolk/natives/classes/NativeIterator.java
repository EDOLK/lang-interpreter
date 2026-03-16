package com.edolk.natives.classes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.Instance;
import com.edolk.Token;
import com.edolk.TokenType;

public class NativeIterator extends NativeInstance {

    private final Iterator<Object> iterator;

    public NativeIterator(boolean instance, Iterator<Object> iterator) {
        super(instance);
        this.iterator = iterator;
    }
    public NativeIterator(boolean instance) {
        this(instance, Collections.emptyList().iterator());
    }
    public NativeIterator(Iterator<Object> iterator) {
        this(true, iterator);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("hasNext", Callable.create(0, false, (interpreter, args) -> {
            return iterator.hasNext();
        }));
        map.put("next", Callable.create(0, false, (interpreter, args) -> {
            return iterator.next();
        }));
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(1, false, (interpreter, args) -> {
            Object arg1 = args.get(0);
            if (arg1 instanceof Instance instance) {
                Object iteratorObj = instance.get(new Token(TokenType.IDENTIFIER, "iterator", "", null, 0));
                if (iteratorObj instanceof Callable callable) {
                    return callable.call(interpreter, List.of());
                }
            } else if (arg1 instanceof Object[] array){
                return new NativeIterator(Arrays.asList(array).iterator());
            }
            return null;
        });
    }

}
