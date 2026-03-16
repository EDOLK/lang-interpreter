package com.edolk.natives.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.nativefuncs.Print;

public class NativeList extends NativeInstance {

    private List<Object> list = new ArrayList<>();

    public NativeList(boolean instance) {
        super(instance);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("addAt", Callable.create(2, false, (interpreter, args) -> {
            list.add((int)args.get(0), args.get(1));
            return null;
        }));
        map.put("add", Callable.create(1, false, (interpreter, args) -> {
            return list.add(args.get(0));
        }));
        map.put("remove", Callable.create(1, false, (interpreter, args) -> {
            return list.remove(args.get(0));
        }));
        map.put("removeAt", Callable.create(1, false, (interpreter, args) -> {
            Object indexObj = args.get(0);
            if (indexObj instanceof Double index) {
                return list.remove((int)Math.floor(index));
            }
            return null;
        }));
        map.put("size", Callable.create(0, false, (interpreter, args) -> {
            return list.size();
        }));
        map.put("contains", Callable.create(0, false, (interpreter, args) -> {
            return list.contains(args.get(0));
        }));
        map.put("get", Callable.create(1, false, (interpreter, args) -> {
            Object indexObj = args.get(0);
            if (indexObj instanceof Double index) {
                return list.get((int)Math.floor(index));
            }
            return null;
        }));
        map.put("getFirst", Callable.create(0, false, (interpreter, args) -> {
            return list.getFirst();
        }));
        map.put("getLast", Callable.create(0, false, (interpreter, args) -> {
            return list.getLast();
        }));

        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeList(true);
        });
    }

    @Override
    public String toString() {
        if (instance) {
            return list.toString();
        }
        return "NativeList class";
    }

}
