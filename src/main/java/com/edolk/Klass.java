package com.edolk;

import java.util.List;
import java.util.Map;

public class Klass implements Callable {
    final String name;
    private final Map<String, Function> methods;

    Klass(String name, Map<String, Function> methods) {
        this.name = name;
        this.methods = methods;
    }

    Function findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        Function initializer = findMethod("init");
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Instance instance = new Instance(this);
        Function initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public boolean varargs() {
        Function initializer = findMethod("init");
        if (initializer == null) return false;
        return initializer.varargs();
    }
    
}
