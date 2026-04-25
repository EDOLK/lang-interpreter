package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.dt.ConcreteDatatable;
import com.edolk.dt.Datatable;

public class NativeDatatable extends NativeInstance {

    public Datatable table = new ConcreteDatatable();

    public NativeDatatable(boolean instance, Datatable table) {
        super(instance);
        this.table = table;
    }

    public NativeDatatable(boolean instance) {
        this(instance, new ConcreteDatatable());
    }

    public NativeDatatable(Datatable datatable) {
        this(true, datatable);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("size", Callable.create(0, false, (interpreter, args) -> {
            return (double)table.size();
        }));
        map.put("entrySize", Callable.create(0, false, (interpreter, args) -> {
            return (double)table.entrySize();
        }));
        map.put("row", Callable.create(1, false, (interpreter, args) -> {
            if (args.get(0) instanceof Number number) {
                return new NativeRow(table.row(number.intValue()));
            }
            return null;
        }));
        map.put("col", Callable.create(1, false, (interpreter, args) -> {
            if (args.get(0) instanceof String header) {
                return new NativeColumn(table.column(header));
            }
            return null;
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
