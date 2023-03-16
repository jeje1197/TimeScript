package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.TimeScript;

public final class TSString extends TSObject implements TSIterable{
    private final String value;

    public TSString(String value) {
        super("String");
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isTruthy() {
        return !toString().isEmpty();
    }

    @Override
    public int getSize() {
        return value.length();
    }

    @Override
    public TSObject getIndex(TSObject index) {
        if (index instanceof TSNumber) {
            TSNumber indexValue = (TSNumber) index;
            return new TSString(String.valueOf(value.charAt((int) indexValue.getValue())));
        }
        return null;
    }

    @Override
    public TSObject setIndex(TSObject index, TSObject value) throws Exception {
        TimeScript.runtimeError("Strings are immutable");
        return null;
    }

    @Override
    public TSObject add(TSObject other) {
        return new TSString(this + other.toString());
    }

    @Override
    public TSObject lessThan(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSBoolean(toString().compareTo(other.toString()) < 0);
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject lessThanOrEquals(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSBoolean(toString().compareTo(other.toString()) <= 0);
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject greaterThan(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSBoolean(toString().compareTo(other.toString()) > 0);
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject greaterThanOrEquals(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSBoolean(toString().compareTo(other.toString()) >= 0);
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject equals(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSBoolean(toString().equals(other.toString()));
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject notEquals(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSBoolean(!toString().equals(other.toString()));
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }
}
