package com.jpl.timescript.interpreter.nativeapi;

import com.jpl.timescript.TimeScript;
import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.datatypes.*;
import com.jpl.timescript.interpreter.environment.Environment;

import java.util.ArrayList;
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
                return new TSNull();
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
            public TSObject call(ExecutionEngine engine, Environment environment) throws Exception {
                TSObject object = environment.get("object");
                if (object instanceof TSIterable) {
                    TSIterable iterable = (TSIterable) object;
                    return new TSNumber((double) iterable.getSize());
                }
                TimeScript.runtimeError("Non iterable object passed to len function");
                return new TSNull();
            }
        });

        addData("charAt", new TSFunction(Arrays.asList("object", "index")) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) throws Exception {
                TSObject object = environment.get("object");
                TSObject index = environment.get("index");
                if (object instanceof TSIterable) {
                    return ((TSIterable) object).getIndex(index);
                }
                TimeScript.runtimeError("Non iterable object passed to charAt function");
                return new TSNull();
            }
        });

        addData("list", new TSFunction(List.of()) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                return new TSList(new ArrayList<>());
            }
        });

        addData("add", new TSFunction(Arrays.asList("iterable", "value")) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                TSIterable iterable = (TSIterable) environment.get("iterable");
                TSObject value = environment.get("value");
                return iterable.append(value);
            }
        });

        // Exit Method
        addData("exit", new TSFunction(List.of()) {
            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                // Indicate an invoked program termination
                System.exit(71);
                return new TSNull();
            }
        });
    }
}
