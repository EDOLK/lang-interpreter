package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.edolk.Callable;

public class NativeSet extends NativeInstance {

    private Set<Object> set = new HashSet<>();

    public NativeSet(boolean instance) {
        super(instance);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("add", Callable.create(1, false, (interpreter, args) -> {
            return set.add(args.get(0));
        }));
        map.put("remove", Callable.create(1, false, (interpreter, args) -> {
            return set.remove(args.get(0));
        }));
        map.put("contains", Callable.create(1, false, (interpreter, args) -> {
            return set.contains(args.get(0));
        }));

        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeSet(true);
        });
    }

    @Override
    public String toString() {
        if (instance) {
            return set.toString();
        }
        return "NativeSet class";
    }

}
