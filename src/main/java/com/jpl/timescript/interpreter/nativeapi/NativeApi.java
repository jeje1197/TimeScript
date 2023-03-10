package com.jpl.timescript.interpreter.nativeapi;

import com.jpl.timescript.TimeScript;
import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.datatypes.*;
import com.jpl.timescript.interpreter.environment.Environment;

import java.util.Arrays;
import java.util.List;

public final class NativeApi {
    private static Environment environment;

    public static void addNativeData(Environment environment) {
        NativeApi.environment = environment;
        addNativeFunctions();
    }

    public static void addData(String variableName, TSObject data) {
        NativeApi.environment.setLocally(variableName, data);
    }

    private static void addNativeFunctions() {

        // Print Method
        addData("println", new TSFunction(List.of("text")) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                System.out.println(environment.get("text"));
                return null;
            }
        });

        // Type Method
        addData("type", new TSFunction(List.of("object")) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                return new TSString(environment.get("object").getType());
            }
        });

        addData("len", new TSFunction(List.of("object")) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                TSObject object = environment.get("object");
                if (object instanceof TSIterable) {
                    TSIterable iterable = (TSIterable) object;
                    return new TSNumber((double) iterable.getSize());
                }
                return null;
            }
        });

        addData("charAt", new TSFunction(Arrays.asList("object", "index")) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                TSObject object = environment.get("object");
                TSObject index = environment.get("index");
                if (object instanceof TSIterable) {
                    return ((TSIterable) object).getIndex(index);
                }
                return null;
            }
        });

        // Exit Method
        addData("exit", new TSFunction(List.of()) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                // Indicate an invoked program termination
                System.exit(71);
                return null;
            }
        });
    }
}
