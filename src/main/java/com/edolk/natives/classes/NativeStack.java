package com.edolk.natives.classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.edolk.Callable;

public class NativeStack extends NativeInstance implements NativeCollection {

    private final Stack<Object> stack;

    @Override
    public Collection<Object> getAsCollection() {
        return stack;
    }

    @Override
    public NativeCollection create(Object[] array) {
        Stack<Object> stack = new Stack<>();
        for (int i = array.length-1; i >= 0 ; i--) {
            stack.push(array[i]);
        }
        return new NativeStack(stack);
    }

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
        map.put("size", Callable.create(0, false, (interpreter, args) -> {
            return (double)stack.size();
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
