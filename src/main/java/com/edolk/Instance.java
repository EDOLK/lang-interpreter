package com.edolk;

import java.util.HashMap;
import java.util.Map;

public class Instance {
    private Klass klass;
    private final Map<String, Object> fields = new HashMap<>();

    public Instance(Klass klass) {
        this.klass = klass;
    }

    public Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        Function method = klass.findMethod(name.lexeme);
        if (method != null) return method.bind(this);
        throw new RuntimeError(name, 
                "Undefined property '" + name.lexeme + "'.");
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    @Override
    public String toString() {
        return klass.toString() + " instance";
    }

    
}
