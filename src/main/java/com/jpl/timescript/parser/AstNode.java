package com.jpl.timescript.parser;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.lexer.Token;

import java.util.List;

public abstract class AstNode {
    public abstract <T> T visit(Visitor<T> visitor);

    public interface Visitor<T> {
        T visitNumber(Number node);
        T visitString(String node);
        T visitBoolean(Boolean node);
        T visitUnaryOp(UnaryOp node);
        T visitBinaryOp(BinaryOp node);
        T visitVariableDeclaration(VariableDeclaration node);
        T visitVariableAssignment(VariableAssignment node);
        T visitVariableAccess(VariableAccess node);
        T visitIfStatement(IfStatement node);
        T visitWhileLoop(WhileLoop node);
        T visitBreakStatement(BreakStatement node);
        T visitContinueStatement(ContinueStatement node);
        T visitFunction(Function node);
        T visitFunctionCall(FunctionCall node);
    }


    public static class Number extends AstNode {
        public double value;
        public Number(Token token) {
            this.value = Double.parseDouble(token.value);
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitNumber(this);
        }
    }

    public static class String extends AstNode {
        public java.lang.String value;
        public String(Token token) {
            this.value = token.value;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitString(this);
        }
    }

    public static class Boolean extends AstNode {
        public boolean value;
        public Boolean(Token token) {
            this.value = java.lang.Boolean.parseBoolean(token.value);
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitBoolean(this);
        }
    }

    public static class UnaryOp extends AstNode {
        public Token op;
        public AstNode expression;
        public UnaryOp(Token operator, AstNode expression) {
            this.op = operator;
            this.expression = expression;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitUnaryOp(this);
        }
    }

    public static class BinaryOp extends AstNode {
        public Token op;
        public AstNode left, right;
        public BinaryOp(Token operator, AstNode left, AstNode right) {
            this.op = operator;
            this.left = left;
            this.right = right;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitBinaryOp(this);
        }
    }

    public static class VariableDeclaration extends AstNode {
        public java.lang.String name;
        public AstNode expression;
        public VariableDeclaration(Token name, AstNode expression) {
            this.name = name.value;
            this.expression = expression;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitVariableDeclaration(this);
        }
    }

    public static class VariableAssignment extends AstNode {
        public java.lang.String name;
        public AstNode expression;
        public VariableAssignment(Token name, AstNode expression) {
            this.name = name.value;
            this.expression = expression;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitVariableAssignment(this);
        }
    }

    public static class VariableAccess extends AstNode {
        public java.lang.String name;
        public VariableAccess (Token token) {
            this.name = token.value;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitVariableAccess(this);
        }
    }

    public static class IfStatement extends AstNode {
        public AstNode conditionExpression, ifStatement, elseStatement;

        public IfStatement(AstNode conditionExpression, AstNode ifStatement, AstNode elseStatement) {
            this.conditionExpression = conditionExpression;
            this.ifStatement = ifStatement;
            this.elseStatement = elseStatement;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitIfStatement(this);
        }
    }

    public static class WhileLoop extends AstNode {
        public AstNode conditionExpression, statement;

        public WhileLoop(AstNode conditionExpression, AstNode statement) {
            this.conditionExpression = conditionExpression;
            this.statement = statement;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitWhileLoop(this);
        }
    }

    public static class BreakStatement extends AstNode {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitBreakStatement(this);
        }
    }

    public static class ContinueStatement extends AstNode {
        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitContinueStatement(this);
        }
    }

    public static class Function extends AstNode {
        public List<java.lang.String> arguments;
        public List<AstNode> statements;

        public Function(List<java.lang.String> arguments, List<AstNode> statements) {
            this.arguments = arguments;
            this.statements = statements;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitFunction(this);
        }
    }

    public static class FunctionCall extends AstNode {
        public AstNode callee;
        public List<AstNode> arguments;

        public FunctionCall(AstNode callee, List<AstNode> arguments) {
            this.callee = callee;
            this.arguments = arguments;
        }

        @Override
        public <T> T visit(Visitor<T> visitor) {
            return visitor.visitFunctionCall(this);
        }
    }
}
