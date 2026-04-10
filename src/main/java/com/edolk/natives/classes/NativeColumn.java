package com.edolk.natives.classes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.dt.Datatable;
import com.edolk.dt.Datatable.Column;

public class NativeColumn extends NativeInstance implements NativeCollection{

    private Column column;

    public NativeColumn(boolean instance, Column column) {
        super(instance);
        this.column = column;
    }

    public NativeColumn(Column column) {
        this(true, column);
    }

    public NativeColumn(Datatable dt, String header) {
        this(true, dt.column(header));
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("getHeader", Callable.create(0, false, (interpreter, args) -> {
            return (String)column.getHeader();
        }));
        map.put("getObjects", Callable.create(0, false, (interpreter, args) -> {
            return new NativeList(column.getObjects());
        }));
        map.put("select", Callable.create(1, false, (interpreter, args) -> {
            if (args.get(0) instanceof Double index) {
                return column.select((int)Math.floor(index));
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
        return this.column.getObjects();
    }

    @Override
    public NativeCollection create(Object[] array) {
        return new NativeColumn(this.column.getSource().column(column.getHeader()));
    }

}
