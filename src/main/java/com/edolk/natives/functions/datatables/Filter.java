package com.edolk.natives.functions.datatables;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeDatatable;
import com.edolk.natives.classes.NativeRow;

public class Filter implements Callable {

    @Override
    public int arity() {
        return 2;
    }

    @Override
    public boolean varargs() {
        return false;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (arguments.get(0) instanceof NativeDatatable dt) {
            if (arguments.get(1) instanceof Callable call) {
                return new NativeDatatable(
                    dt.table.filter(
                        (row) -> {
                            return (boolean)call.call(interpreter,List.of( new NativeRow(row)));
                        }
                    )
                );
            }
            return dt;
        }
        return null;
    }

}
