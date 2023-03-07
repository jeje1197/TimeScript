package com.jpl.timescript.interpreter;

import com.jpl.timescript.interpreter.datatypes.*;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.parser.AstNode;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionEngine implements AstNode.Visitor<TSObject> {
    private Environment environment;
    private boolean shouldBreak = false;
    private boolean shouldContinue = false;
    private boolean shouldReturn = false;
    private TSObject returnValue = null;

    public ExecutionEngine(Environment environment) {
        this.environment = environment;
    }

    public TSObject visit(List<AstNode> statements) {
        TSObject returnValue = null;
        for (AstNode node: statements) {
            returnValue = node.visit(this);
            if (shouldBreak || shouldContinue) break;
            if (shouldReturn) break;
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

    public TSObject visitNull(AstNode.Null node) {
        return new TSNull();
    }

    public TSObject visitUnaryOp(AstNode.UnaryOp node) {
        return null;
    }

    public TSObject visitBinaryOp(AstNode.BinaryOp node) {
        return null;
    }

    @Override
    public TSObject visitVariableDeclaration(AstNode.VariableDeclaration node) {
        environment.setLocally(node.name, node.expression.visit(this));
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
            } else if (shouldReturn) {
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

    @Override
    public TSObject visitFunction(AstNode.Function node) {
        return new TSFunction(node.arguments, node.statements);
    }

    @Override
    public TSObject visitFunctionCall(AstNode.FunctionCall node) {
        TSObject callee = node.callee.visit(this);

        List<TSObject> arguments = new ArrayList<>();
        for (AstNode argument: node.arguments) {
            arguments.add(argument.visit(this));
        }

        if (!(callee instanceof TSFunction)) {
            System.out.println("Cannot be called");
            return null;
        }

        TSFunction function = (TSFunction) callee;
        if (arguments.size() != function.arity()) {
            System.out.println("Expected " + function.arity() + " arguments, but" +
                    " received " + arguments.size());
            return null;
        }

        Environment functionEnvironment = new Environment(environment);
        for (int i = 0; i < function.arity(); i++) {
            functionEnvironment.setLocally(function.argumentNames.get(i), arguments.get(i));
        }

        environment = functionEnvironment;
        TSObject immediateValue = function.call(this, functionEnvironment);
        environment = functionEnvironment.getParent();

        shouldReturn = false;
        return function.isNative ? immediateValue : returnValue;
    }

    @Override
    public TSObject visitReturnStatement(AstNode.ReturnStatement node) {
        shouldReturn = true;
        returnValue = node.expression.visit(this);
        return null;
    }

    @Override
    public TSObject visitClass(AstNode.Class node) {
        if (environment.containsKey(node.className)) {
            System.out.println("Cannot create class. Identifier already exists.");
            return null;
        }

        Environment classEnvironment = new Environment(environment);
        environment = classEnvironment;
        for (AstNode declaration: node.statements) {
            declaration.visit(this);
        }
        environment = classEnvironment.getParent();

        TSClass classDefinition = new TSClass(node.className, classEnvironment);
        environment.setLocally(node.className, classDefinition);
        return null;
    }
}
