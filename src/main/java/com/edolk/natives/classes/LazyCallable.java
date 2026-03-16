package com.edolk.natives.classes;

import java.util.List;

import com.edolk.Callable;
import com.edolk.Interpreter;

public class LazyCallable implements Callable {

    private Callable innerCallable;
    private Callable initializer;

    public LazyCallable(Callable initializer) {
        this.initializer = initializer;
    }

    public int arity() {
        if (innerCallable != null) {
            return innerCallable.arity();
        }
        return initializer.arity();
    }

    public boolean varargs() {
        if (innerCallable != null) {
            return innerCallable.varargs();
        }
        return initializer.varargs();
    }

    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (innerCallable == null) {
            Object result = initializer.call(interpreter, arguments);
            if (result instanceof Callable callable) {
                this.innerCallable = callable;
            } else {
                return null;
            }
        }
        return innerCallable.call(interpreter, arguments);
    }

}
