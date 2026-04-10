package com.edolk.natives.functions.datatables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeCollection;
import com.edolk.natives.classes.NativeDatatable;
import static com.edolk.App.castTo;

public class GroupBy implements Callable {

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public boolean varargs() {
        return true;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (arguments.get(0) instanceof NativeDatatable dt) {
            return new NativeDatatable(
                dt.table.groupBy(
                    arguments.subList(1, arguments.size()).stream()
                        .map(castTo(String.class))
                        .collect(Collectors.toList())
                )
            );
        }
        return null;
    }

    public <T> void populate(Object pList, List<T> list, Class<T> clazz){
        if (pList instanceof Object[] array) {
            for (Object object : array) {
                if (clazz.isInstance(object))
                    list.add(clazz.cast(object));
            }
        } else if (pList instanceof NativeCollection collection){
            collection.getAsCollection().forEach((object) -> {
                if (clazz.isInstance(object))
                    list.add(clazz.cast(object));
            });
        }
    }

}
