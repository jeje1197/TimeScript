package com.jpl.timescript.interpreter.datatypes;

public interface TSStructure {
    public TSObject getField(String key);
    public void setField(String key, TSObject value);
}
