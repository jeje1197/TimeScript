package com.jpl.timescript;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.datatypes.TSNull;
import com.jpl.timescript.interpreter.datatypes.TSObject;
import com.jpl.timescript.interpreter.environment.Environment;
import com.jpl.timescript.interpreter.nativeapi.NativeApi;
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
    private static final Environment globalEnvironment = new Environment();
    private static ExecutionEngine engine;
    private static boolean initialized = false;
    static {
        init();
    }

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

    public static boolean init() {
        if (!initialized) {
            NativeApi.addNativeData(globalEnvironment);
            initialized = true;
        }
        return true;
    }

    public static void runShell() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) {
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
    }

    public static void run(String code) {
//        System.out.println("Lexing");
        List<Token> tokens = Lexer.getTokens(code);
        if (hadError) return;

//        System.out.println("Scanning");
        List<AstNode> statements = Parser.parse(tokens);
        if (hadError) return;

        engine = new ExecutionEngine(globalEnvironment);
        try {
            TSObject result = engine.visit(statements);
            if (!(result instanceof TSNull)) {
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println("Runtime Exception: " + e.getMessage());
        }
    }

    // Method for static error messages during parsing stages
    public static void error(String message, int line) {
        System.out.println(message + ", line " + line);
        hadError = true;
    }

    // Method for dynamic error message reporting
    public static void runtimeError(String message) throws Exception {
        hadRuntimeError = true;
        throw new Exception(message);
    }
}
