package com.jpl.timescript.interpreter;

import com.jpl.timescript.parser.AstNode;

import java.util.List;

public final class ExecutionEngine {

    public void visit(List<AstNode> statements) {
        for (AstNode node: statements) {
            visit(node);
        }
    }

    public Object visit(AstNode node) {
        return null;
    }

    public Object visitNumber(AstNode.Number node) {
        return node.value;
    }

    public Object visitString(AstNode.String node) {
        return node.value;
    }

    public Object visitBoolean(AstNode.Boolean node) {
        return node.value;
    }
}
