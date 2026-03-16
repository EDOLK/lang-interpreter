package com.edolk.natives.classes;

import java.util.List;
import java.util.Map;

import com.edolk.Callable;
import com.edolk.Instance;
import com.edolk.Interpreter;
import com.edolk.RuntimeError;
import com.edolk.Token;
import com.edolk.TokenType;

public abstract class NativeInstance extends Instance implements Callable {

    protected final boolean instance;
    protected final Callable constructor = getConstructor();
    protected final Map<String, Object> properties = getProperties();

    public NativeInstance(boolean instance) {
        super(null);
        this.instance = instance;
    }

    protected abstract Map<String, Object> getProperties();
    protected abstract Callable getConstructor();

    @Override
    public int arity() {
        if (instance)
            return 0;
        return constructor.arity();
    }

    @Override
    public boolean varargs() {
        if (instance)
            return false;
        return constructor.varargs();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        if (instance) {
            throw new RuntimeError(
                new Token(TokenType.EOF, "", "", null, 0),
                "Can only call functions and classes."
            );
        }
        return constructor.call(interpreter, arguments);
    }

    @Override
    public Object get(Token name) {
        if (!instance) {
            throw new RuntimeError(
                new Token(TokenType.EOF, "", "", null, 0),
                "Property does not exist."
            );
        }
        return properties.get(name.lexeme);
    }

    @Override
    public void set(Token name, Object value) {
        if (!instance) {
            throw new RuntimeError(
                new Token(TokenType.EOF, "", "", null, 0),
                "Property does not exist."
            );
        }
        properties.put(name.lexeme, value);
    }

}
