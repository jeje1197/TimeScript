package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.environment.Environment;

public class TSInstance {
    public TSClass classDeclaration;
    public Environment environment;

    public TSInstance(TSClass classDeclaration, Environment environment) {
        this.classDeclaration = classDeclaration;
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "<"+ classDeclaration + ">";
    }
}
