package com.edolk.natives.functions;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Instance;
import com.edolk.Interpreter;
import com.edolk.Klass;
import com.edolk.natives.classes.NativeInstance;

public class IsTypeOf implements Callable {

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
        switch (arguments.get(1)) {
            case NativeInstance nativeInstance -> {
                if (arguments.get(0) instanceof NativeInstance nt2) {
                    return nativeInstance.getClass().isInstance(nt2);
                }
            }
            case Klass klass -> {
                if (arguments.get(0) instanceof Instance instance) {
                    return instance.getKlass() == klass;
                }
            }
            default -> {
            }
        }
        return false;
    }

    
}
