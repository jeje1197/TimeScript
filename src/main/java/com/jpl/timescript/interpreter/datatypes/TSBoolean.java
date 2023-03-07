package com.jpl.timescript.interpreter.datatypes;

public class TSBoolean extends TSObject {
    private Boolean value;

    private TSBoolean(Boolean value) {
        super("Boolean");
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
