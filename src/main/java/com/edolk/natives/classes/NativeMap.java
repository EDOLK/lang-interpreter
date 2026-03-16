package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.Map;

import com.edolk.Callable;

public class NativeMap extends NativeInstance {

    private final Map<Object, Object> map;

    public NativeMap(boolean instance, Map<Object, Object> map) {
        super(instance);
        this.map = map;
    }

    public NativeMap(boolean instance) {
        this(instance, new HashMap<>());
    }

    public NativeMap(Map<Object, Object> map) {
        this(true, map);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> m = new HashMap<>();
        m.put("get", Callable.create(1, false, (interpreter, args) -> {
            return map.get(args.get(0));
        }));
        m.put("remove", Callable.create(1, false, (interpreter, args) -> {
            return map.remove(args.get(0));
        }));
        m.put("put", Callable.create(2, false, (interpreter, args) -> {
            return map.put(args.get(0), args.get(1));
        }));
        m.put("containsKey", Callable.create(1, false, (interpreter, args) -> {
            return map.containsKey(args.get(0));
        }));
        m.put("containsValue", Callable.create(1, false, (interpreter, args) -> {
            return map.containsValue(args.get(0));
        }));
        m.put("size", Callable.create(0, false, (interpreter, args) -> {
            return map.size();
        }));
        m.put("isEmpty", Callable.create(0, false, (interpreter, args) -> {
            return map.isEmpty();
        }));
        return m;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeMap(true);
        });
    }

    
}
