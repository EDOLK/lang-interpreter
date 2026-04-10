package com.edolk.natives.functions.datatables;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;
import com.edolk.dt.Datatables;
import com.edolk.natives.classes.NativeDatatable;

public class Readcsv implements Callable {

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
        if (arguments.get(0) instanceof String path) {
            if (arguments.size() > 1 && arguments.get(1) instanceof Object[] headerObjs) {
                if (headerObjs.length > 0) {
                    String[] headers = new String[headerObjs.length];
                    int i = 0;
                    for (Object object : headerObjs) {
                        if (object instanceof String str) {
                            headers[i++] = str;
                        }
                    }
                    return new NativeDatatable(Datatables.readCsv(path, false, headers));
                }
            }
            return new NativeDatatable(Datatables.readCsv(path, true, new String[0]));
        }
        return null;
    }

    
}
