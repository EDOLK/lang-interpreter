package com.edolk;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FunctionSet implements NeuCallable {

    private Set<Callable> functions = new HashSet<>();

    // callable + callable = functionSet
    // FunctionSet + callable = functionSet

    public static NeuCallable combine(NeuCallable c1, Callable c2){
        if (c1 instanceof FunctionSet fs) {
            fs.add(c2);
            return fs;
        }
        if (c1 instanceof Callable cc1){
            // TODO: add vararg support later
            if (cc1.varargs() || c2.varargs()) {
                return c2;
            }
            if (cc1.arity() == c2.arity()) {
                return c2;
            }
            FunctionSet set = new FunctionSet();
            set.add(cc1);
            set.add(c2);
            return set;
        }
        return c2;
    }

    public boolean add(Callable callable){
        // TODO: add vararg support later
        if (callable.varargs())
            return false;
        Iterator<Callable> iterator = functions.iterator();
        while (iterator.hasNext()) {
            Callable next = iterator.next();
            if (next.arity() == callable.arity()) {
                iterator.remove();
                break;
            }
        }
        return functions.add(callable);
    }

    @Override
    public boolean acceptsArgs(List<Object> arguments) {
        for (Callable call : functions) {
            if (call.arity() == arguments.size()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        for (Callable call : functions) {
            if (call.arity() == arguments.size()) {
                return call.call(interpreter, arguments);
            }
        }
        return null;
    }

}
