package com.jpl.timescript.lexer;


import com.jpl.timescript.TimeScript;

import java.util.*;
import java.util.regex.Matcher;

// A Lexer is a component that converts a stream of text into a stream of tokens
public final class Lexer {
    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
            "true", "false", "null",
            "var",
            "if", "else",
            "while", "break", "continue",
            "function", "return",
            "class"
    ));

    private static final PatternNode[] patterns = {
            new PatternNode(TokenType.DISCARD, "^( |\t|\n)+"), // whitespace
            new PatternNode(TokenType.DISCARD, "^(#.*\n?)"), // comments

            new PatternNode(TokenType.NUMBER, "^(\\d+(\\.\\d+|\\.\\d+)?)"),
            new PatternNode(TokenType.STRING, "^\"(.|\n)*?\""),
            new PatternNode(TokenType.ID, "^[A-Za-z_]\\w*"),
            new PatternNode(TokenType.OP, "^(\\+|-|\\*|/|%)"),
            new PatternNode(TokenType.OP, "^(<=|<<|<|>=|>>|>|==|=|!=|!|&{2}|\\|{2})"),
            new PatternNode(TokenType.DOT, "^\\."),
            new PatternNode(TokenType.COMMA, "^,"),
            new PatternNode(TokenType.COLON, "^:"),
            new PatternNode(TokenType.SEMICOLON, "^;"),
            new PatternNode(TokenType.LPAREN, "^\\("),
            new PatternNode(TokenType.RPAREN, "^\\)"),
            new PatternNode(TokenType.LBRACE, "^\\{"),
            new PatternNode(TokenType.RBRACE, "^\\}"),
            new PatternNode(TokenType.LBRACKET, "^\\["),
            new PatternNode(TokenType.RBRACKET, "^\\]"),
    };

    public static List<Token> getTokens(String text) {
        List<Token> tokens = new ArrayList<>();

        int currentLine = 1;
        while (!text.isEmpty()) {
            boolean patternFound = false;

            for (PatternNode node: patterns) {
                Matcher matcher = node.pattern.matcher(text);
                if (matcher.find()) {
                    String value = matcher.group(0);
                    patternFound = true;

                    if (node.type != TokenType.DISCARD) {
                        TokenType type = node.type;
                        String lexeme = value;

                        if (node.type == TokenType.STRING) {
                            lexeme = value.substring(1, value.length() - 1);
                        } else if (node.type == TokenType.ID) {
                            if (keywords.contains(value)) type = TokenType.KEYWORD;
                        }
                        tokens.add(new Token(type, lexeme, currentLine));
                    }

                    for (char c: value.toCharArray()) {
                        if (c == '\n') currentLine++;
                    }
                    text = text.substring(value.length());
                    break;
                }
            }

            if (!patternFound) {
                TimeScript.error("No token found: " + text.charAt(0), currentLine);
                text = text.substring(1);
            }
        }

        tokens.add(new Token(TokenType.END, "\0", currentLine));
        return tokens;
    }
}

