package com.jpl.timescript.interpreter;

import com.jpl.timescript.interpreter.datatypes.TSBoolean;
import com.jpl.timescript.interpreter.datatypes.TSNumber;
import com.jpl.timescript.interpreter.datatypes.TSObject;
import com.jpl.timescript.interpreter.datatypes.TSString;
import com.jpl.timescript.parser.AstNode;

import java.util.List;

public final class ExecutionEngine implements AstNode.Visitor<TSObject> {

    public void visit(List<AstNode> statements) {
        for (AstNode node: statements) {
            node.visit(this);
        }
    }

    public TSObject visitNumber(AstNode.Number node) {
        return new TSNumber(node.value);
    }

    public TSObject visitString(AstNode.String node) {
        return new TSString(node.value);
    }

    public TSObject visitBoolean(AstNode.Boolean node) {
        return new TSBoolean(node.value);
    }

    public TSObject visitUnaryOp(AstNode.UnaryOp node) {
        return null;
    }

    public TSObject visitBinaryOp(AstNode.BinaryOp node) {
        return null;
    }
}
