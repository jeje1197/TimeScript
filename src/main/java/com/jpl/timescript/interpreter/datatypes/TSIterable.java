package com.jpl.timescript.interpreter.datatypes;

public interface TSIterable {
    int getSize();
    TSObject getIndex(TSObject index);
    TSObject setIndex(TSObject index, TSObject value) throws Exception;
}
