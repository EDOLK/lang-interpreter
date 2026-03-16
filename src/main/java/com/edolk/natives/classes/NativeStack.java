package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.edolk.Callable;

public class NativeStack extends NativeInstance {

    private final Stack<Object> stack;

    public NativeStack(boolean instance, Stack<Object> stack) {
        super(instance);
        this.stack = stack;
    }

    public NativeStack(boolean instance) {
        this(instance, new Stack<>());
    }

    public NativeStack(Stack<Object> stack) {
        this(true, stack);
    }
    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("push", Callable.create(1, false, (interpreter, args) -> {
            return stack.push(args.get(0));
        }));
        map.put("pop", Callable.create(0, false, (interpreter, args) -> {
            return stack.pop();
        }));
        map.put("peek", Callable.create(0, false, (interpreter, args) -> {
            return stack.peek();
        }));
        map.put("empty", Callable.create(0, false, (interpreter, args) -> {
            return stack.empty();
        }));
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeStack(true);
        });
    }

}
