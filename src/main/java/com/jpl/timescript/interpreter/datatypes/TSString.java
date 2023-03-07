package com.jpl.timescript.interpreter.datatypes;

public final class TSString extends TSObject {
    private String value;

    public TSString(String value) {
        super("String");
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
