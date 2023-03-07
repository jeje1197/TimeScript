package com.jpl.timescript.interpreter.datatypes;

public final class TSBoolean extends TSObject {
    private Boolean value;

    public TSBoolean(Boolean value) {
        super("Boolean");
        this.value = value;
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
