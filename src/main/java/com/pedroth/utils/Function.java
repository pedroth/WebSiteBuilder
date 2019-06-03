package com.pedroth.utils;

public interface Function<X,Y> {
    Y apply(X input) throws Throwable;
}
