package com.edolk;

import java.util.List;

public interface Callable {
    int arity();
    boolean varargs();
    Object call(Interpreter interpreter, List<Object> arguments);
}
