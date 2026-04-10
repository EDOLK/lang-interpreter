package com.edolk.natives.functions.datatables;

import static com.edolk.App.castTo;

import java.util.List;
import java.util.stream.Collectors;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeDatatable;

public class Select implements Callable {

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
                dt.table.select(
                    arguments.subList(1, arguments.size()).stream()
                        .map(castTo(String.class))
                        .collect(Collectors.toList())
                )
            );
        }
        return null;
        
    }

}
