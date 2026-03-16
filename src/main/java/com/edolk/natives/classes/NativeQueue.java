package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.edolk.Callable;

public class NativeQueue extends NativeInstance {

    private final Queue<Object> queue;

    public NativeQueue(boolean instance, Queue<Object> queue) {
        super(instance);
        this.queue = queue;
    }

    public NativeQueue(boolean instance) {
        this(instance, new LinkedList<>());
    }

    public NativeQueue(Queue<Object> queue) {
        this(true, queue);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("offer", Callable.create(1, false, (interpreter, args) -> {
            return queue.offer(args.get(0));
        }));

        map.put("poll", Callable.create(0, false, (interpreter, args) -> {
            return queue.poll();
        }));

        map.put("peek", Callable.create(0, false, (interpreter, args) -> {
            return queue.peek();
        }));

        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeQueue(true);
        });
    }

}
