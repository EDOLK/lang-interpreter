package com.edolk.natives.classes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.edolk.Callable;
import com.edolk.dt.Datatable.ArrayMutator;
import com.edolk.dt.Datatable.CollectionMutator;
import com.edolk.dt.Datatable.FunctionalMutator;
import com.edolk.dt.Datatable.Mutator;
import com.edolk.dt.Datatable.Remover;
import com.edolk.dt.Datatable.Row;

public class NativeMutator extends NativeInstance {
    
    public Mutator mutator;

    public NativeMutator(boolean instance, Mutator mutator) {
        super(instance);
        this.mutator = mutator;
    }

    public NativeMutator(Mutator mutator) {
        this(true, mutator);
    }

    public NativeMutator() {
        this(false, null);
    }

    @Override
    protected Map<String, Object> getProperties() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Override
    protected Callable getConstructor() {
        return Callable.create(2, false, (interpreter, args) -> {
            if (args.get(0) instanceof String header) {
                Object arg2 = args.get(1);
                if (arg2 == null) {
                    return new NativeMutator(new Remover(header));
                }
                switch (arg2) {
                    case Callable callable -> {
                        Function<Row, Object> function = (row) -> {
                            return callable.call(interpreter, List.of(new NativeRow(row)));
                        };
                        Mutator mutator = new FunctionalMutator(header, function);
                        return new NativeMutator(mutator);
                    }
                    case NativeCollection collection -> {
                        Mutator mutator = new CollectionMutator(header, collection.getAsCollection());
                        return new NativeMutator(mutator);
                    }
                    case Object[] array -> {
                        Mutator mutator = new ArrayMutator(header, array);
                        return new NativeMutator(mutator);
                    }
                    default -> {

                    }
                }
            }
            return null;
        });
    }

}
