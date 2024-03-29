package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.environment.Environment;

public class TSInstance extends TSObject implements TSStructure {
    public TSClass classDeclaration;
    public Environment environment;

    public TSInstance(TSClass classDeclaration, Environment environment) {
        super(classDeclaration.className);
        this.classDeclaration = classDeclaration;
        this.environment = environment;

        classDeclaration.environment.getVariables().forEach((key, value) -> environment.setLocally(key, value));
    }

    @Override
    public TSObject getField(String key) {
        return environment.getLocally(key);
    }

    @Override
    public void setField(String key, TSObject value) {
        environment.setLocally(key, value);
    }

    @Override
    public String toString() {
        return "<"+ classDeclaration.className + " object>";
    }
}
