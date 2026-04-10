package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.dt.ConcreteDatatable;

public class NativeDatatable extends NativeInstance {

    public ConcreteDatatable table = new ConcreteDatatable();

    public NativeDatatable(boolean instance, ConcreteDatatable table) {
        super(instance);
        this.table = table;
    }

    public NativeDatatable(boolean instance) {
        this(instance, new ConcreteDatatable());
    }

    public NativeDatatable(ConcreteDatatable datatable) {
        this(true, datatable);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("size", Callable.create(0, false, (interpreter, args) -> {
            return (double)table.map.size();
        }));
        map.put("entrySize", Callable.create(0, false, (interpreter, args) -> {
            return (double)table.map.entrySet().stream()
                .mapToInt((e) -> e.getValue().size())
                .sum();
        }));
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(0, false, (interpreter, args) -> {
            return new NativeDatatable(true);
        });
    }

    @Override
    public String toString() {
        return table.toString();
    }

}
