package com.edolk.natives.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.edolk.Callable;

public class NativeList extends NativeInstance implements NativeCollection {

    private final List<Object> list;

    @Override
    public Collection<Object> getAsCollection() {
        return list;
    }

    @Override
    public NativeCollection create(Object[] array) {
        return new NativeList(new ArrayList<>(List.of(array)));
    }

    public NativeList(boolean instance, List<Object> list) {
        super(instance);
        this.list = list;
    }

    public NativeList(boolean instance){
        this(instance, new ArrayList<>());
    }

    public NativeList(List<Object> list){
        this(true, list);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("addAt", Callable.create(2, false, (interpreter, args) -> {
            if (args.get(0) instanceof Double d) {
                list.add((int)Math.floor(d), args.get(1));
            }
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
            return (double)list.size();
        }));
        map.put("contains", Callable.create(1, false, (interpreter, args) -> {
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
        map.put("forEach", Callable.create(1, false, (interpreter, args) -> {
            Object obj = args.get(0);
            if (obj instanceof Callable cal) {
                list.forEach((element) -> {
                    cal.call(interpreter, List.of(element));
                });
            }
            return null;
        }));
        map.put("isEmpty", Callable.create(0, false, (interpreter, args) -> {
            return list.isEmpty();
        }));
        map.put("iterator", Callable.create(0, false, (interpreter, args) -> {
            return new NativeIterator(list.iterator());
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
