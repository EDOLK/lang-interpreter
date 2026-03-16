package com.edolk.natives.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.edolk.Callable;
import com.edolk.Instance;
import com.edolk.Token;
import com.edolk.TokenType;

public class NativeStream extends NativeInstance {

    private final Stream<Object> stream;

    public NativeStream(boolean instance, Stream<Object> stream) {
        super(instance);
        this.stream = stream;
    }
    public NativeStream(Stream<Object> stream) {
        this(true, stream);
    }
    public NativeStream(boolean instance) {
        this(instance, Stream.empty());
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("filter", Callable.create(1, false, (interpreter, args) -> {
            Object obj = args.get(0);
            if (obj instanceof Callable cal) {
                return stream.filter((element) -> {
                    return (boolean)cal.call(interpreter, List.of(element));
                });
            }
            return null;
        }));
        map.put("map", Callable.create(1, false, (interpreter, args) -> {
            Object obj = args.get(0);
            if (obj instanceof Callable cal) {
                return stream.map((element) -> {
                    return cal.call(interpreter, List.of(element));
                });
            }
            return null;
        }));
        map.put("collect", Callable.create(1, false, (interpreter, args) -> {
            Object obj = args.get(0);
            if (obj instanceof String str) {
                return switch(str.toLowerCase()){
                    case "list" -> new NativeList(stream.collect(Collectors.toList()));
                    case "set" -> new NativeSet(stream.collect(Collectors.toSet()));
                    case "array" -> stream.toArray();
                    default -> null;
                };
            }
            return null;
        }));
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(1, false, (interpreter, args) -> {
            Object arg1 = args.get(0);
            if (arg1 instanceof Instance instance) {
                Object streamObj = instance.get(new Token(TokenType.IDENTIFIER, "stream", "", null, 0));
                if (streamObj instanceof Callable callable) {
                    return callable.call(interpreter, List.of());
                }
            } else if (arg1 instanceof Object[] array){
                return new NativeStream(Arrays.asList(array).stream());
            }
            return null;
        });
    }

    
}
