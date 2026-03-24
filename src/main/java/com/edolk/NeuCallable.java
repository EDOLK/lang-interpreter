package com.edolk;

import java.util.List;

public interface NeuCallable {
    public boolean acceptsArgs(List<Object> arguments);
    public Object call(Interpreter interpreter, List<Object> arguments);
}
