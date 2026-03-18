package com.edolk.natives.classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.edolk.Callable;

public class NativeSet extends NativeInstance implements NativeCollection {

    private final Set<Object> set;

    @Override
    public Collection<Object> getAsCollection() {
        return this.set;
    }

    @Override
    public NativeCollection create(Object[] array) {
        return new NativeSet(new HashSet<>(List.of(array)));
    }

    public NativeSet(boolean instance, Set<Object> set) {
        super(instance);
        this.set = set;
    }

    public NativeSet(boolean instance) {
        this(instance, new HashSet<>());
    }

    public NativeSet(Set<Object> set) {
        this(true, set);
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
        map.put("size", Callable.create(0, false, (interpreter, args) -> {
            return (double)set.size();
        }));
        map.put("clear", Callable.create(0, false, (interpreter, args) -> {
            set.clear();
            return null;
        }));
        map.put("iterator", Callable.create(0, false, (interpreter, args) -> {
            return new NativeIterator(set.iterator());
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
