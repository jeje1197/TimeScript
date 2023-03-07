package com.jpl.timescript.interpreter.environment;

import com.jpl.timescript.interpreter.datatypes.TSObject;

import java.util.HashMap;
import java.util.Map;

public final class Environment {
    private Map<String, TSObject> variables = new HashMap<>();
    private Environment parent = null;

    public Environment() { }
    public Environment(Environment parent) { this.parent = parent; }

    public boolean containsKey(String key) {
        return variables.containsKey(key);
    }

    public TSObject get(String key) {
        return variables.get(key);
    }

    public void set(String key, TSObject value) {
        variables.put(key, value);
    }
}
