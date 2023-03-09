package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.TimeScript;

public final class TSBoolean extends TSObject {
    private final Boolean value;

    public TSBoolean(Boolean value) {
        super("Boolean");
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public TSObject equals(TSObject other) throws Exception {
        if (other instanceof TSBoolean) {
            return new TSBoolean(getValue() == ((TSBoolean) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject notEquals(TSObject other) throws Exception {
        if (other instanceof TSBoolean) {
            return new TSBoolean(getValue() != ((TSBoolean) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }
}
