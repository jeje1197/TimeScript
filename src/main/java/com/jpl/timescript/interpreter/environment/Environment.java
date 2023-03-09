package com.jpl.timescript.interpreter.environment;

import com.jpl.timescript.interpreter.datatypes.TSObject;

import java.util.HashMap;
import java.util.Map;

public final class Environment {
    private final Map<String, TSObject> variables = new HashMap<>();
    private Environment parent = null;

    public Environment() { }
    public Environment(Environment parent) { this.parent = parent; }

    public Map<String, TSObject> getVariables() { return variables; }
    public Environment getParent() { return parent; }

    public boolean containsKeyLocally(String key) { return variables.containsKey(key);}
    public boolean containsKey(String key) {
        Environment current = this;
        while (current != null) {
            if (containsKeyLocally(key)) return true;
            current = current.parent;
        }
        return false;
    }

    public TSObject getLocally(String key) {
        return variables.get(key);
    }

    public TSObject get(String key) {
        Environment current = this;
        while (current != null) {
            if (containsKeyLocally(key)) {
                return current.getLocally(key);
            }
            current = current.parent;
        }
        System.out.println("'" + key + "' has not been declared");
        return variables.get(key);
    }

    public void setLocally(String key, TSObject value) {
        variables.put(key, value);
    }
    public void set(String key, TSObject value) {
        Environment current = this;
        while (current != null) {
            if (containsKeyLocally(key)) {
                current.setLocally(key, value);
                return;
            }
            current = current.parent;
        }
        System.out.println("'" + key + "' has not been declared");
    }
}
