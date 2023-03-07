package com.jpl.timescript.lexer;

import java.util.regex.Pattern;

class PatternNode {
    Pattern pattern;
    TokenType type;

    PatternNode(TokenType type, String pattern) {
        this.pattern = Pattern.compile(pattern);
        this.type = type;
    }
}
