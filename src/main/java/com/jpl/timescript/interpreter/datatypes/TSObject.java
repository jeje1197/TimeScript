package com.jpl.timescript.interpreter.datatypes;

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
public class TSObject {
    // Header info
    ObjectHeader header;

    public TSObject(String className) {
        this.header = new ObjectHeader(className);
    }

    public boolean isTruthy() { return true; }

    @Override
    public String toString() {
        return "<class '" + header.className + "'>";
    }
}
