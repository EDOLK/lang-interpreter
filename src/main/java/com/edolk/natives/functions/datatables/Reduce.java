package com.edolk.natives.functions.datatables;

import java.util.List;
import java.util.stream.Collectors;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeDatatable;
import com.edolk.natives.classes.NativeReducer;

public class Reduce implements Callable {

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
                dt.table.reduce(
                    arguments.subList(1, arguments.size()).stream()
                        .map((o) -> ((NativeReducer)o).reducer)
                        .collect(Collectors.toList())
                )
            );
        }
        return null;
    }

}
