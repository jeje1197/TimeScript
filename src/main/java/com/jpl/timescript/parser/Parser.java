package com.jpl.timescript.parser;

import com.jpl.timescript.TimeScript;
import com.jpl.timescript.lexer.Token;
import com.jpl.timescript.lexer.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Parser {
    private static List<Token> tokens;
    private static int current = 0;

    private static boolean hasNext() {
        return current + 1 < tokens.size();
    }

    private static Token advance() {
        if (hasNext()) {
            return tokens.get(current++);
        } else {
            return tokens.get(current);
        }
    }

    private static Token peek() {
        return tokens.get(current);
    }

    private static Token peekNext() {
        return hasNext() ? tokens.get(current + 1) : null;
    }

    private static boolean match(TokenType type) {
        return peek().type == type;
    }

    private static boolean match(String value) {
        return peek().value.equals(value);
    }

    private static boolean match(TokenType type, String value) {
        return match(type) && match(value);
    }

    private static boolean matchKeyword(String keyword) {
        return peek().type == TokenType.KEYWORD && peek().value.equals(keyword);
    }

    private static boolean matchNext(TokenType type) {
        return peekNext().type == type;
    }

    private static boolean matchNext(TokenType type, String value) {
        return peekNext() != null && peekNext().type == type && peekNext().value.equals(value);
    }

    private static boolean expect(TokenType type, String message) {
        if (peek().type != type) {
            TimeScript.error(message, peek().line);
            return false;
        }
        return true;
    }

    private static boolean expect(TokenType type, String value, String message) {
        if (peek().type != type || !peek().value.equals(value)) {
            TimeScript.error(message, peek().line);
            return false;
        }
        return true;
    }

    private static AstNode expectStatement() {
        AstNode statement = statement();
        if (statement == null) {
            TimeScript.error("Expected statement", peek().line);
        }
        return statement;
    }

    private static AstNode expectExpression() {
        AstNode expression = expression();
        if (expression == null) {
            TimeScript.error("Expected expression", peek().line);
        }
        return expression;
    }

    // statements
    public static List<AstNode> parse(List<Token> tokens) {
        // Reset static fields before use
        Parser.tokens = tokens;
        Parser.current = 0;

        List<AstNode> statements = statements();
        if (!match(TokenType.END)) {
            TimeScript.error("Did not reach end of file", peek().line);
        }
        return statements;
    }

    // statement*
    private static List<AstNode> statements() {
        List<AstNode> statements = new ArrayList<>();
        while (!match(TokenType.END)) {
            AstNode statement = statement();
            if (statement == null) break;
            statements.add(statement);
        }

        return statements;
    }


    /* blockStatement
     *  | variableDeclaration
     *  | variableAssignment
     *  | ifStatement
     *  | whileLoop
     *  | breakStatement
     *  | continueStatement
     *  | functionDeclaration
     *  | returnStatement
     *  | classDeclaration
     */
    private static AstNode statement() {
        if (match(TokenType.LBRACE)) {
            return blockStatement();
        } else if (matchKeyword("var")) {
            return variableDeclaration();
        } else if (match(TokenType.ID) && matchNext(TokenType.OP, "=")) {
            return variableAssignment();
        } else if (matchKeyword("if")) {
            return ifStatement();
        } else if (matchKeyword("while")) {
            return whileLoop();
        } else if (matchKeyword("break")) {
            return breakStatement();
        } else if (matchKeyword("continue")) {
            return continueStatement();
        } else if (matchKeyword("function")) {
            return functionDeclaration();
        } else if (matchKeyword("return")) {
            return returnStatement();
        } else if (matchKeyword("class")) {
            return classDeclaration();
        }
        return expression();
    }

    // "{" statement* "}"
    private static AstNode blockStatement() {
        advance();
        List<AstNode> statements = statements();
        if (!expect(TokenType.RBRACE, "Expected '}'")) return null;
        advance();
        return new AstNode.BlockStatement(statements);
    }

    // "var" ID ("=" expr)?
    private static AstNode variableDeclaration() {
        Token name;
        AstNode expression;
        advance();
        if (!expect(TokenType.ID, "Expected identifier")) return null;
        name = advance();
        if (!expect(TokenType.OP, "=", "Expected '='")) return null;
        advance();
        expression = expectExpression();
        if (expression == null) return null;
        return new AstNode.VariableDeclaration(name, expression);
    }

    // ID "=" expr
    private static AstNode variableAssignment() {
        Token name = advance();
        advance();
        AstNode expression = expectExpression();
        if (expression == null) return null;
        return new AstNode.VariableAssignment(name, expression);
    }

    // "if" "(" expr ")" statement ("else" statement)?
    private static AstNode ifStatement() {
        AstNode conditionExpression;
        AstNode ifStatement;
        AstNode elseStatement = null;
        advance();
        if (!expect(TokenType.LPAREN, "Expected '('")) return null;
        advance();
        conditionExpression = expectExpression();
        if (conditionExpression == null) return null;
        if (!expect(TokenType.RPAREN, "Expected ')'")) return null;
        advance();
        ifStatement = expectStatement();
        if (ifStatement == null) return null;
        if (matchKeyword("else")) {
            advance();
            elseStatement = expectStatement();
            if (elseStatement == null) return null;
        }
        return new AstNode.IfStatement(conditionExpression, ifStatement, elseStatement);
    }

    // "while" "(" expr ")" statement
    public static AstNode whileLoop() {
        AstNode conditionExpression, statement;
        advance();
        if (!expect(TokenType.LPAREN, "Expected '('")) return null;
        advance();
        conditionExpression = expectExpression();
        if (conditionExpression == null) return null;
        if (!expect(TokenType.RPAREN, "Expected ')'")) return null;
        advance();
        statement = expectStatement();
        if (statement == null) return null;
        return new AstNode.WhileLoop(conditionExpression, statement);
    }

    // "break"
    private static AstNode breakStatement() {
        advance();
        return new AstNode.BreakStatement();
    }

    // "continue"
    private static AstNode continueStatement() {
        advance();
        return new AstNode.ContinueStatement();
    }

    // "function" ID? "(" (ID ("," ID)* )? ")" blockStatement
    private static AstNode functionDeclaration() {
        Token name;
        List<String> arguments = new ArrayList<>();
        List<AstNode> statements;
        advance();

        if (!expect(TokenType.ID, "Expected class identifier")) return null;
        name = advance();
        if (!expect(TokenType.LPAREN, "Expected '('")) return null;
        advance();
        if (match(TokenType.ID)) {
            arguments.add(advance().value);

            while(match(TokenType.COMMA)) {
                advance();
                if (!expect(TokenType.ID,"Expected parameter")) return null;
                arguments.add(advance().value);
            }
        }

        if (!expect(TokenType.RPAREN, "Expected ')'")) return null;
        advance();
        if (!expect(TokenType.LBRACE, "Expected '{'")) return null;
        advance();
        statements = statements();
        if (!expect(TokenType.RBRACE, "Expected '}'")) return null;
        advance();
        return new AstNode.FunctionDeclaration(name, arguments, statements);
    }

    // expr "(" (expr ("," expr)* )? ")"
    private static AstNode functionCall(AstNode callee) {
        List<AstNode> arguments = new ArrayList<>();
        advance();

        AstNode expression = expression();
        if (expression != null) {
            arguments.add(expression);

            while (match(TokenType.COMMA)) {
                advance();
                expression = expectExpression();
                if (expression == null) return null;
                arguments.add(expression);
            }
        }

        if (!expect(TokenType.RPAREN, "Expected ')'")) return null;
        advance();
        return new AstNode.FunctionCall(callee, arguments);
    }

    // "return"
    private static AstNode returnStatement() {
        advance();
        AstNode expression = expression();
        return new AstNode.ReturnStatement(expression);
    }

    // "class" ID ("<-" expr)? blockStatement
    private static AstNode classDeclaration() {
        Token name;
        List<AstNode> statements;
        advance();
        if (!expect(TokenType.ID, "Expected class identifier")) return null;
        name = advance();
        if (!expect(TokenType.LBRACE, "Expected '{'")) return null;
        advance();
        statements = statements();
        if (!expect(TokenType.RBRACE, "Expected '}'")) return null;
        advance();
        return new AstNode.Class(name, statements);
    }

    // "." ID
    private static AstNode attributeAccess(AstNode atom) {
        Token name;
        if (!expect(TokenType.ID, "Expected attribute name")) return null;
        name = advance();
        return new AstNode.AttributeAccess(atom, name);
    }

    // "." ID "=" expr
    private static AstNode attributeAssign(AstNode atom) {
        Token name;
        AstNode expression;
        if (!expect(TokenType.ID, "Expected attribute name")) return null;
        name = advance();
        if (!expect(TokenType.OP, "=","Expected =")) return null;
        advance();
        expression = expectExpression();
        if (expression == null) return null;

        return new AstNode.AttributeAssign(atom, name, expression);
    }

    // "[" expr "]"
    private static AstNode indexAccess(AstNode atom) {
        advance();
        AstNode expression = expectExpression();
        if (expression == null) return null;
        if (!expect(TokenType.RBRACKET, "Expected ']'")) return null;
        advance();
        return new AstNode.IndexAccess(atom, expression);
    }

    // "[" expr "]" "=" expr
    private static AstNode indexAssign(AstNode atom) {
        advance();
        AstNode expression = expectExpression();
        if (expression == null) return null;

        AstNode.IndexAccess indexAccess = (AstNode.IndexAccess) atom;
        return new AstNode.IndexAssign(indexAccess.iterable, indexAccess.index, expression);
    }


    private static AstNode expression() {
        return binaryOperation("comparison1", Arrays.asList("&&", "||"), "comparison1");
    }

    private static AstNode comparison1() {
        return binaryOperation("comparison2", Arrays.asList("==", "!="), "comparison2");
    }

    private static AstNode comparison2() {
        return binaryOperation("term", Arrays.asList("<", "<=", ">", ">="), "term");
    }

    private static AstNode term() {
        return binaryOperation("factor", Arrays.asList("+", "-"), "factor");
    }

    private static AstNode factor() {
        return binaryOperation("modifier", Arrays.asList("*", "/", "%"), "modifier");
    }

    private static AstNode modifier() {
        AstNode atom = atom();

        while (atom != null && (match(TokenType.LPAREN) || match(TokenType.DOT) || match(TokenType.LBRACKET))) {
            if (match(TokenType.LPAREN)) {
                atom = functionCall(atom);
            } else if (match(TokenType.DOT)) {
                advance();
                if (match(TokenType.ID) && matchNext(TokenType.OP, "=")) {
                    atom = attributeAssign(atom);
                } else {
                    atom = attributeAccess(atom);
                }
            } else {
                atom = indexAccess(atom);
                if (match(TokenType.OP, "=")) {
                    atom = indexAssign(atom);
                }
            }
        }
        return atom;
    }

    private static AstNode atom() {
        Token token = peek();

        switch(token.type) {
            case NUMBER:
                advance();
                return new AstNode.Number(token);
            case STRING:
                advance();
                return new AstNode.String(token);
            case ID:
                advance();
                return new AstNode.VariableAccess(token);
            case LPAREN: {
                advance();
                AstNode expression = expectExpression();
                if (expression == null) return null;
                if (!expect(TokenType.RPAREN, "Expected )")) return null;
                advance(); // skip )
                return expression;
            }
            case KEYWORD:
                if (match("true") || match("false")) {
                    advance();
                    return new AstNode.Boolean(token);
                } else if (matchKeyword("null")) {
                    advance();
                    return new AstNode.Null();
                }
                break;
            case OP:
                if (match("+") || match("-") || match("!")) {
                    advance();
                    AstNode expression = expectExpression();
                    if (expression == null) return null;
                    return new AstNode.UnaryOp(token, expression);
                }
                break;
            default:
                return null;
        }
        return null;
    }

    private static AstNode binaryOperation(String function1,
                                 List<String> operators, String function2) {
        AstNode left = accessFunction(function1);
        while (left != null && match(TokenType.OP) && operators.contains(peek().value)) {
            Token operator = advance();
            AstNode right = accessFunction(function2);
            if (right == null) {
                TimeScript.error("Expected expression", peek().line);
            }
            left = new AstNode.BinaryOp(operator, left, right);
        }

        return left;
    }

    private static AstNode accessFunction(String function) {
        switch(function) {
            case "comparison1":
                return comparison1();
            case "comparison2":
                return comparison2();
            case "term":
                return term();
            case "factor":
                return factor();
            case "modifier":
                return modifier();
            case "atom":
                return atom();
            default:
                return null;
        }
    }
}
