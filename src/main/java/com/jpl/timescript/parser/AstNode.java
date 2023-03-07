package com.jpl.timescript.parser;

import com.jpl.timescript.lexer.Token;

public abstract class AstNode {
    static class Number extends AstNode {
        public double value;
        public Number(Token token) {
            this.value = Double.parseDouble(token.value);
        }
    }

    static class String extends AstNode {
        public java.lang.String value;
        public String(Token token) {
            this.value = token.value;
        }
    }

    static class Boolean extends AstNode {
        public boolean value;
        public Boolean(Token token) {
            this.value = java.lang.Boolean.parseBoolean(token.value);
        }
    }

    static class UnaryOp extends AstNode {
        public Token op;
        public AstNode expression;
        public UnaryOp(Token operator, AstNode expression) {
            this.op = operator;
            this.expression = expression;
        }
    }

    static class BinaryOp extends AstNode {
        public Token op;
        public AstNode left, right;
        public BinaryOp(Token operator, AstNode left, AstNode right) {
            this.op = operator;
            this.left = left;
            this.right = right;
        }
    }

    static class VariableAccess extends AstNode {
        public java.lang.String name;
        public VariableAccess (Token token) {
            this.name = token.value;
        }
    }
}
