package com.jpl.timescript.interpreter;

import com.jpl.timescript.TimeScript;
import com.jpl.timescript.interpreter.datatypes.*;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.parser.AstNode;

import java.util.ArrayList;
import java.util.List;

public final class ExecutionEngine implements AstNode.Visitor<TSObject> {
    private Environment environment;
    private static final TSBoolean trueObject = new TSBoolean(true);
    private static final TSBoolean falseObject = new TSBoolean(false);
    private static final TSNull nullObject = new TSNull();
    private static final TSNumber negativeOne = new TSNumber((double) -1);
    private boolean shouldBreak = false;
    private boolean shouldContinue = false;
    private boolean shouldReturn = false;
    private TSObject returnValue = nullObject;

    public ExecutionEngine(Environment environment) {
        this.environment = environment;
    }

    public TSObject visit(List<AstNode> statements) throws Exception {
        TSObject returnValue = nullObject;
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
        return node.value ? trueObject : falseObject;
    }

    public TSObject visitNull(AstNode.Null node) {
        return nullObject;
    }

    public TSObject visitUnaryOp(AstNode.UnaryOp node) throws Exception {
        TSObject expression = node.expression.visit(this);

        switch(node.op) {
            case "+":
                return expression;
            case "-":
                return expression.multiply(negativeOne);
            case "!":
                return new TSBoolean(!expression.isTruthy());
            default:
                TimeScript.runtimeError("Unimplemented unary operator: " + node.op);
        }
        return nullObject;
    }

    public TSObject visitBinaryOp(AstNode.BinaryOp node) throws Exception {
        TSObject leftValue = node.left.visit(this);
        TSObject rightValue = node.right.visit(this);

        switch (node.op) {
            case "+":
                return leftValue.add(rightValue);
            case "-":
                return leftValue.subtract(rightValue);
            case "*":
                return leftValue.multiply(rightValue);
            case "/":
                return leftValue.divide(rightValue);
            case "%":
                return leftValue.mod(rightValue);

            case "<":
                return leftValue.lessThan(rightValue);
            case "<=":
                return leftValue.lessThanOrEquals(rightValue);
            case ">":
                return leftValue.greaterThan(rightValue);
            case ">=":
                return leftValue.greaterThanOrEquals(rightValue);
            case "==":
                return leftValue.equals(rightValue);
            case "!=":
                return leftValue.notEquals(rightValue);

            case "&&":
                return leftValue.and(rightValue);
            case "||":
                return leftValue.or(rightValue);
            default:
                TimeScript.runtimeError("Unimplemented binary operator: " + node.op);
        }
        return nullObject;
    }

    @Override
    public TSObject visitBlockStatement(AstNode.BlockStatement node) throws Exception {
        Environment previous = environment;
        environment = new Environment(environment);

        TSObject returnValue = nullObject;
        for (AstNode statement: node.statements) {
            returnValue = statement.visit(this);
            if (shouldBreak || shouldContinue) break;
            if (shouldReturn) break;
        }

        environment = previous;
        return returnValue;
    }

    @Override
    public TSObject visitVariableDeclaration(AstNode.VariableDeclaration node) throws Exception {
        if (environment.containsKeyLocally(node.name)) {
            TimeScript.runtimeError("'" + node.name + "' has already been declared");
        }

        environment.setLocally(node.name, node.expression != null ?
                node.expression.visit(this) : nullObject );
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
            return node.elseStatement != null ? node.elseStatement.visit(this) : nullObject;
        }
    }

    @Override
    public TSObject visitWhileLoop(AstNode.WhileLoop node) throws Exception {
        TSObject returnValue = nullObject;
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
    public TSObject visitFunctionDeclaration(AstNode.FunctionDeclaration node) {
        environment.setLocally(node.name, new TSFunction(node.arguments, node.statements));
        return nullObject;
    }

    @Override
    public TSObject visitFunctionCall(AstNode.FunctionCall node) throws Exception {
        TSObject callee = node.callee.visit(this);

        List<TSObject> arguments = new ArrayList<>();
        for (AstNode argument: node.arguments) {
            arguments.add(argument.visit(this));
        }

        if (!(callee instanceof TSCallable)) {
            TimeScript.runtimeError("Cannot be called");
            return null;
        }

        TSFunction function;
        TSObject valueAssignedAsThis = null;
        if (callee instanceof TSClass classDefinition) {
            valueAssignedAsThis = visitConstructor(classDefinition);
            if (classDefinition.getField("constructor") instanceof TSFunction constructor) {
                function = constructor;
            } else {
                return valueAssignedAsThis;
            }
        } else {
            function = (TSFunction) callee;
        }

        if (arguments.size() != function.arity()) {
            TimeScript.runtimeError("Expected " + function.arity() + " arguments, but" +
                    " received " + arguments.size());
        }

        boolean accessedAsSuper = false;
        Environment previous = environment;
        environment = new Environment(environment);
        if (node.callee instanceof AstNode.AttributeAccess attributeAccessNode) {
            valueAssignedAsThis = attributeAccessNode.structure.visit(this);
        } else if (node.callee instanceof AstNode.VariableAccess variableAccessNode) {
            if (variableAccessNode.name.equals("super")) {
                accessedAsSuper = true;
            }
        }

        // If function is a constructor/belongs to a structure, pass the structure into the method as
        // 'this'
        if (valueAssignedAsThis != null) {
            environment.setLocally("this", valueAssignedAsThis);
        }

        // If function is obtained from 'super' variable access, then access the variable and
        //
        if (accessedAsSuper) {
            environment.setLocally("this", environment.get("this"));
        }

        for (int i = 0; i < function.arity(); i++) {
            environment.setLocally(function.argumentNames.get(i), arguments.get(i));
        }

        TSObject immediateValue = function.call(this, environment);
        environment = previous;
        shouldReturn = false;
        if (callee instanceof TSClass) return valueAssignedAsThis;
        return function.isNative ? immediateValue : returnValue;
    }

    public TSInstance visitConstructor(TSClass classDefinition) {
        return new TSInstance(classDefinition, new Environment());
    }

    @Override
    public TSObject visitReturnStatement(AstNode.ReturnStatement node) throws Exception {
        shouldReturn = true;
        returnValue = node.expression != null ? node.expression.visit(this): nullObject;
        return null;
    }

    @Override
    public TSObject visitClass(AstNode.Class node) throws Exception {
        if (environment.containsKey(node.className)) {
            TimeScript.runtimeError("Cannot create class. Identifier already exists.");
            return null;
        }

        // Add attributes to class definition
        Environment previous = environment;
        environment = new Environment(environment);
        for (AstNode declaration: node.statements) {
            declaration.visit(this);
        }
        TSClass classDefinition = new TSClass(node.className, environment);
        environment = previous;

        environment.setLocally(node.className, classDefinition);
        return nullObject;
    }

    @Override
    public TSObject visitAttributeAccess(AstNode.AttributeAccess node) throws Exception {
        TSObject object = node.structure.visit(this);
        if (!(object instanceof TSStructure)) {
            TimeScript.runtimeError("Does not have attributes");
            return null;
        }

        TSStructure structure = (TSStructure) object;
        return structure.getField(node.name);
    }

    @Override
    public TSObject visitAttributeAssign(AstNode.AttributeAssign node) throws Exception {
        TSObject object = node.structure.visit(this);
        if (!(object instanceof TSStructure)) {
            TimeScript.runtimeError("Does not have attributes");
            return null;
        }

        TSStructure structure = (TSStructure) object;
        structure.setField(node.name, node.expression.visit(this));
        return nullObject;
    }

    @Override
    public TSObject visitIndexAccess(AstNode.IndexAccess node) throws Exception {
        TSObject object = node.iterable.visit(this);
        if (!(object instanceof TSIterable)) {
            TimeScript.runtimeError("Object is not an iterable");
            return null;
        }

        TSIterable iterable = (TSIterable) object;
        return iterable.getIndex(node.index.visit(this));
    }

    @Override
    public TSObject visitIndexAssign(AstNode.IndexAssign node) throws Exception {
        TSObject object = node.iterable.visit(this);
        if (!(object instanceof TSIterable)) {
            TimeScript.runtimeError("Object is not an iterable");
            return null;
        }

        TSIterable iterable = (TSIterable) object;
        iterable.setIndex(node.index.visit(this), node.expr.visit(this));
        return nullObject;
    }
}
