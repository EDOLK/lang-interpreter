package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.edolk.Callable;
import com.edolk.dt.ConcreteReducer;
import com.edolk.dt.Datatable.Column;
import com.edolk.dt.Datatable.Reducer;

public class NativeReducer extends NativeInstance {

    public Reducer reducer;

    public NativeReducer(boolean instance, Reducer reducer) {
        super(instance);
        this.reducer = reducer;
    }

    public NativeReducer(Reducer reducer) {
        this(true, reducer);
    }

    public NativeReducer() {
        this(false, null);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("as", Callable.create(1, false, (interpreter, args) -> {
            if (args.get(0) instanceof String str) {
                this.reducer = reducer.as(str);
            }
            return this;
        }));
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(2, false, (interpreter, args) -> {
            if (args.get(0) instanceof String header) {
                if (args.get(1) instanceof Callable callable){
                    Function<Column, Object> function = (col) -> {
                        return callable.call(interpreter, List.of(new NativeColumn(col)));
                    };
                    Reducer reducer = new ConcreteReducer(header, function);
                    return new NativeReducer(reducer);
                }
            }
            return null;
        });
    }
    
}
