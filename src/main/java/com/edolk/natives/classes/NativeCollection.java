package com.edolk.natives.classes;

import java.util.Collection;

public interface NativeCollection {
    public Collection<Object> getAsCollection();
    public NativeCollection create(Object[] array);
}
