package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.TimeScript;

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

    @Override
    public boolean isTruthy() {
        return !toString().isEmpty();
    }

    @Override
    public TSObject add(TSObject other) throws Exception {
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
