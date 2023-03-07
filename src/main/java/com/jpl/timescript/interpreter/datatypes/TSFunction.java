package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.parser.AstNode;

import java.util.List;

public class TSFunction extends TSObject implements TSCallable {
    public List<String> argumentNames;
    public List<AstNode> statements;
    public boolean isNative;

    public TSFunction(List<String> argumentNames) {
        super("Function");
        this.argumentNames = argumentNames;
        this.isNative = true;
    }

    public TSFunction(List<String> argumentNames, List<AstNode> statements) {
        super("Function");
        this.argumentNames = argumentNames;
        this.statements = statements;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public TSObject call(ExecutionEngine engine, Environment environment) {
        return engine.visit(statements);
    }
}
