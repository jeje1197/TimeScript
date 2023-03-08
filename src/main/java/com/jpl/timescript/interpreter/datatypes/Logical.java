package com.jpl.timescript.interpreter.datatypes;

public interface Logical {
    TSObject and(TSObject other);
    TSObject or(TSObject other);
}
