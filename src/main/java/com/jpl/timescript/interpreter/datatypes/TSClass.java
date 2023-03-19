package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.environment.Environment;

import java.util.HashMap;
import java.util.Map;

public class TSClass extends TSObject implements TSStructure, TSCallable {
    public String className;
    public Environment environment;

    public TSClass(String className, Environment environment) {
        super("Class");
        this.className = className;
        this.environment = environment;
    }

    public TSObject getField(String key) {
        return environment.get(key);
    }

    public void setField(String key, TSObject value) {
        environment.setLocally(key, value);
    }

    @Override
    public String toString() {
        return "<class definition for '" + className + "'>";
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public TSObject call(ExecutionEngine engine, Environment environment) throws Exception {
        return null;
    }
}
