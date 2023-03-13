package com.jpl.timescript.lexer;

// A Token is the smallest meaningful unit in a programming language
public final class Token {
    public TokenType type;
    public String value;
    public int line;

    Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    @Override
    public String toString() {
        return "(" + type + ", " + value + ")";
    }
}
