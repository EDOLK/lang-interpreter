package com.edolk.natives.functions.datatables;

import java.util.List;
import java.util.stream.Collectors;

import static com.edolk.App.castTo;
import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.natives.classes.NativeDatatable;

public class SortBy implements Callable {

    @Override
    public int arity() {
        return 4;
    }

    @Override
    public boolean varargs() {
        return true;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (arguments.get(0) instanceof NativeDatatable dt) {
            if (arguments.get(1) instanceof String header) {
                if (arguments.size() > 2) {
                    return new NativeDatatable(
                        dt.table.sortBy(
                            header,
                            arguments.subList(2, arguments.size()).stream()
                                .map(castTo(String.class))
                                .collect(Collectors.toList()),
                            false
                        )
                    );
                }
                return new NativeDatatable(
                    dt.table.sortBy(header, false)
                );
            }
            return dt;
        }
        return null;
    }

    
}
