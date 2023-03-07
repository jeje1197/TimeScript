package com.jpl.timescript.interpreter;

import com.jpl.timescript.interpreter.datatypes.TSBoolean;
import com.jpl.timescript.interpreter.datatypes.TSNumber;
import com.jpl.timescript.interpreter.datatypes.TSObject;
import com.jpl.timescript.interpreter.datatypes.TSString;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.parser.AstNode;

import java.util.List;

public final class ExecutionEngine implements AstNode.Visitor<TSObject> {
    Environment environment;
    private boolean shouldBreak = false;
    private boolean shouldContinue = false;
    public ExecutionEngine(Environment environment) {
        this.environment = environment;
    }

    public TSObject visit(List<AstNode> statements) {
        TSObject returnValue = null;
        for (AstNode node: statements) {
            returnValue = node.visit(this);
        }
        return returnValue;
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

    @Override
    public TSObject visitVariableDeclaration(AstNode.VariableDeclaration node) {
        environment.set(node.name, node.expression.visit(this));
        return null;
    }

    @Override
    public TSObject visitVariableAssignment(AstNode.VariableAssignment node) {
        environment.set(node.name, node.expression.visit(this));
        return null;
    }

    @Override
    public TSObject visitVariableAccess(AstNode.VariableAccess node) {
        return environment.get(node.name);
    }

    @Override
    public TSObject visitIfStatement(AstNode.IfStatement node) {
        if (node.conditionExpression.visit(this).isTruthy()) {
            return node.ifStatement.visit(this);
        } else {
            return node.elseStatement != null ? node.elseStatement.visit(this) : null;
        }
    }

    @Override
    public TSObject visitWhileLoop(AstNode.WhileLoop node) {
        TSObject returnValue = null;
        while(node.conditionExpression.visit(this).isTruthy()) {
            returnValue = node.statement.visit(this);
            if (shouldBreak) {
                shouldBreak = false;
                break;
            } else if (shouldContinue) {
                shouldContinue = false;
                break;
            }
        }
        return returnValue;
    }

    @Override
    public TSObject visitBreakStatement(AstNode.BreakStatement node) {
        shouldBreak = true;
        return null;
    }

    @Override
    public TSObject visitContinueStatement(AstNode.ContinueStatement node) {
        shouldContinue = true;
        return null;
    }
}
