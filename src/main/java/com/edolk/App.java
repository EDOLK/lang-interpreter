package com.edolk;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        Engine.main(args);
    }

    public static <T,U> java.util.function.Function<T,U> castTo(Class<U> clazz){
        return (obj) -> clazz.cast(obj);
    }
}
