package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.parser.AstNode;

import java.util.List;

public class TSFunction extends TSObject implements TSCallable {
    public List<String> arguments;
    public List<AstNode> statements;
    public TSFunction() { super("Function"); }
    public TSFunction(List<String> arguments, List<AstNode> statements) {
        super("Function");
        this.arguments = arguments;
        this.statements = statements;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public TSObject call(ExecutionEngine engine) {
        return engine.visit(statements);
    }
}
