package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.TimeScript;

public final class TSNumber extends TSObject {
    private Double value;

    public TSNumber(Double value) {
        super("Number");
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean isTruthy() {
        return getValue() != 0;
    }

    @Override
    public String toString() {
        Integer integerValue = value.intValue();
        if (value.doubleValue() == integerValue.intValue())
            return integerValue.toString();
        return value.toString();
    }

    @Override
    public TSObject add(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSNumber(getValue() + ((TSNumber) other).getValue());
        }
        else if (other instanceof TSString) {
            return new TSString(this + other.toString());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject subtract(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSNumber(getValue() - ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject multiply(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSNumber(getValue() * ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject divide(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSNumber(getValue() / ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject mod(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSNumber(getValue() % ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject lessThan(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSBoolean(getValue() < ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject lessThanOrEquals(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSBoolean(getValue() <= ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject greaterThan(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSBoolean(getValue() > ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject greaterThanOrEquals(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSBoolean(getValue() >= ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject equals(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSBoolean(getValue() == ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject notEquals(TSObject other) throws Exception {
        if (other instanceof TSNumber) {
            return new TSBoolean(getValue() != ((TSNumber) other).getValue());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }
}
