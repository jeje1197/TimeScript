package com.jpl.timescript.interpreter.datatypes;

public class TSNull extends TSObject {
    public TSNull() {
        super("Null");
    }

    @Override
    public boolean isTruthy() {
        return false;
    }

    @Override
    public String toString() {
        return "null";
    }
}
