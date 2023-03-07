package com.jpl.timescript.interpreter.datatypes;

public final class TSNumber extends TSObject {
    private Double value;


    public TSNumber(Double value) {
        super("Number");
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
