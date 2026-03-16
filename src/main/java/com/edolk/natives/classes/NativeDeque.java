package com.edolk.natives.classes;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.edolk.Callable;

public class NativeDeque extends NativeInstance {

    private final Deque<Object> deque;

    public NativeDeque(boolean instance, Deque<Object> deque) {
        super(instance);
        this.deque = deque;
    }

    public NativeDeque(boolean instance) {
        this(instance, new LinkedList<>());
    }

    public NativeDeque(Deque<Object> deque) {
        this(true, deque);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("offerFirst", Callable.create(1, false, (interpreter, args) -> {
            return deque.offerFirst(args.get(0));
        }));
        map.put("offerLast", Callable.create(1, false, (interpreter, args) -> {
            return deque.offerLast(args.get(0));
        }));
        map.put("pollFirst", Callable.create(0, false, (interpreter, args) -> {
            return deque.pollFirst();
        }));
        map.put("pollLast", Callable.create(0, false, (interpreter, args) -> {
            return deque.pollLast();
        }));
        map.put("peekFirst", Callable.create(0, false, (interpreter, args) -> {
            return deque.peekFirst();
        }));
        map.put("peekLast", Callable.create(0, false, (interpreter, args) -> {
            return deque.peekLast();
        }));

        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeDeque(true);
        });
    }

}
