package com.jpl.timescript.interpreter.datatypes;

public interface TSIterable {
    int getSize();
    TSObject getIndex(TSObject index);
}
