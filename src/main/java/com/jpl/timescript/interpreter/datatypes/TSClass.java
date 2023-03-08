package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.environment.Environment;

public class TSClass extends TSObject implements TSStructure{
    public String className;
    public Environment environment;

    public TSClass(String className, Environment environment) {
        super("Class");
        this.className = className;
        this.environment = environment;
        this.environment.setLocally("this", this);
    }

    public TSObject getField(String key) {
        return environment.get(key);
    }

    public void setField(String key, TSObject value) {
        environment.set(key, value);
    }

    @Override
    public String toString() {
        return "<class definition for '" + className + "'>";
    }
}
