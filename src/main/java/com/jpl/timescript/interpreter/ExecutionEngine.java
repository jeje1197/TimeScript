package com.jpl.timescript.interpreter;

import com.jpl.timescript.TimeScript;
import com.jpl.timescript.interpreter.datatypes.*;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.parser.AstNode;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionEngine implements AstNode.Visitor<TSObject> {
    private Environment environment;
    private static final TSNull nullObject = new TSNull();
    private boolean shouldBreak = false;
    private boolean shouldContinue = false;
    private boolean shouldReturn = false;
    private TSObject returnValue = null;

    public ExecutionEngine(Environment environment) {
        this.environment = environment;
    }

    public TSObject visit(List<AstNode> statements) throws Exception {
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
        return nullObject;
    }

    public TSObject visitUnaryOp(AstNode.UnaryOp node) {
        return nullObject;
    }

    public TSObject visitBinaryOp(AstNode.BinaryOp node) {
        return nullObject;
    }

    @Override
    public TSObject visitBlockStatement(AstNode.BlockStatement node) throws Exception {
        TSObject returnValue = null;
        for (AstNode statement: node.statements) {
            returnValue = statement.visit(this);
            if (shouldBreak || shouldContinue) break;
            if (shouldReturn) break;
        }
        return returnValue;
    }

    @Override
    public TSObject visitVariableDeclaration(AstNode.VariableDeclaration node) throws Exception {
        if (environment.containsKeyLocally(node.name)) {
            TimeScript.runtimeError("'" + node.name + "' has already been declared");
        }
        environment.setLocally(node.name, node.expression.visit(this));
        return null;
    }

    @Override
    public TSObject visitVariableAssignment(AstNode.VariableAssignment node) throws Exception {
        if (!environment.containsKey(node.name)) {
            TimeScript.runtimeError("'" + node.name + "' has not been declared.");
        }
        environment.set(node.name, node.expression.visit(this));
        return null;
    }

    @Override
    public TSObject visitVariableAccess(AstNode.VariableAccess node) throws Exception {
        if (!environment.containsKey(node.name)) {
            TimeScript.runtimeError("'" + node.name + "' has not been declared.");
        }
        return environment.get(node.name);
    }

    @Override
    public TSObject visitIfStatement(AstNode.IfStatement node) throws Exception {
        if (node.conditionExpression.visit(this).isTruthy()) {
            return node.ifStatement.visit(this);
        } else {
            return node.elseStatement != null ? node.elseStatement.visit(this) : null;
        }
    }

    @Override
    public TSObject visitWhileLoop(AstNode.WhileLoop node) throws Exception {
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
    public TSObject visitFunctionCall(AstNode.FunctionCall node) throws Exception {
        TSObject callee = node.callee.visit(this);

        List<TSObject> arguments = new ArrayList<>();
        for (AstNode argument: node.arguments) {
            arguments.add(argument.visit(this));
        }

        TSFunction function = null;
        if (callee instanceof TSClass) {
//            TSClass classDefinition = (TSClass) callee;
//            TSInstance instance = classDefinition.
//            function = (TSFunction) classDefinition.getField("constructor");
        } else if (!(callee instanceof TSFunction)) {
            System.out.println("Cannot be called");
            return null;
        } else {
            function = (TSFunction) callee;
        }

        if (arguments.size() != function.arity()) {
            TimeScript.runtimeError("Expected " + function.arity() + " arguments, but" +
                    " received " + arguments.size());
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
    public TSObject visitReturnStatement(AstNode.ReturnStatement node) throws Exception {
        shouldReturn = true;
        returnValue = node.expression.visit(this);
        return null;
    }

    @Override
    public TSObject visitClass(AstNode.Class node) throws Exception {
        if (environment.containsKey(node.className)) {
            TimeScript.runtimeError("Cannot create class. Identifier already exists.");
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

    @Override
    public TSObject visitAttributeAccess(AstNode.AttributeAccess node) throws Exception {
        TSObject structure = node.structure.visit(this);
        if (!(structure instanceof TSClass)) {
            TimeScript.runtimeError("Does not have attributes");
            return null;
        }

        TSClass classDefinition = (TSClass) structure;
//        if (!classDefinition.environment.containsKeyLocally(node.name)) {
//            TimeScript.runtimeError();
//        }
        return classDefinition.getField(node.name);
    }
}
