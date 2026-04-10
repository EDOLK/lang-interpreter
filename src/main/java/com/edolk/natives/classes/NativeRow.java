package com.edolk.natives.classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.dt.Datatable;
import com.edolk.dt.Datatable.Row;

public class NativeRow extends NativeInstance implements NativeCollection {

    private Row row;

    public NativeRow(boolean instance, Row row) {
        super(instance);
        this.row = row;
    }

    public NativeRow(Row row) {
        this(true, row);
    }

    public NativeRow(Datatable dt, int index) {
        this(true, dt.row(index));
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("getIndex", Callable.create(0, false, (interpreter, args) -> {
            return (double)row.getIndex();
        }));
        map.put("getObjects", Callable.create(0, false, (interpreter, args) -> {
            return new NativeList(row.getObjects());
        }));
        map.put("select", Callable.create(1, false, (interpreter, args) -> {
            if (args.get(0) instanceof String str) {
                Object result = row.select(str);
                if (result instanceof Number num) {
                    return num.doubleValue();
                }
                return result;
            }
            return null;
        }));
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return null;
    }

    @Override
    public Collection<Object> getAsCollection() {
        return row.getObjects();
    }

    @Override
    public NativeCollection create(Object[] array) {
        return new NativeRow(row.getSource().row(row.getIndex()));
    }
}
