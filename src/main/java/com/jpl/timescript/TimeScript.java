package com.jpl.timescript;

import com.jpl.timescript.lexer.Lexer;
import com.jpl.timescript.lexer.Token;
import com.jpl.timescript.parser.AstNode;
import com.jpl.timescript.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class TimeScript {
//    private static Environment globalEnvironment = new Environment();
//    private static ExecutionEngine engine = new ExecutionEngine();
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jpl [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runShell();
        }
    }

    private static void runShell() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
            hadRuntimeError = false;
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }

    public static void run(String code) {
        System.out.println("Code: " + code);
        List<Token> tokens = Lexer.getTokens(code);

        System.out.println("Program tokens:");
        for (Token token: tokens) {
            System.out.println(token);
        }

        if (hadError) {
            return;
        }

        AstNode node = Parser.parse(tokens);
        System.out.println("Program nodes:" + node);

    }

    public static void error(String message, int line) {
        System.out.println(message + ", line " + line);
        hadError = true;
    }

    public static void runtimeError(String message, int line) throws Exception {
        hadRuntimeError = true;
        throw new Exception(message + ", line " + line);
    }
}