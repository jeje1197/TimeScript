package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.TimeScript;

class ObjectHeader {
    String className;
    // Inheritance List
    // Method List
    ObjectHeader(String className) {
        this.className = className;
    }
}

// This is the base object class for every piece of data in the
// TimeScript language
public class TSObject implements Arithmetic, Comparable, Logical {
    // Header info
    ObjectHeader header;

    public TSObject(String className) {
        this.header = new ObjectHeader(className);
    }

    public String getType() { return header.className; }
    public boolean isTruthy() { return true; }

    @Override
    public String toString() {
        return "<class '" + header.className + "'>";
    }

    @Override
    public TSObject add(TSObject other) throws Exception {
        if (other instanceof TSString) {
            return new TSString(this + other.toString());
        }
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject subtract(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject multiply(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject divide(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject mod(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject lessThan(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject lessThanOrEquals(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject greaterThan(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject greaterThanOrEquals(TSObject other) throws Exception {
        TimeScript.runtimeError("Invalid operation");
        return null;
    }

    @Override
    public TSObject equals(TSObject other) throws Exception {
        return new TSBoolean(this == other);
    }

    @Override
    public TSObject notEquals(TSObject other) throws Exception {
        return new TSBoolean(this != other);
    }

    @Override
    public TSObject and(TSObject other) {
        return new TSBoolean(isTruthy() && other.isTruthy());
    }

    @Override
    public TSObject or(TSObject other) {
        return new TSBoolean(isTruthy() || other.isTruthy());
    }
}
