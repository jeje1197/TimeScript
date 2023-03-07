package com.jpl.timescript.interpreter.nativeapi;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.datatypes.TSFunction;
import com.jpl.timescript.interpreter.datatypes.TSObject;
import com.jpl.timescript.interpreter.environment.Environment;

import java.util.Arrays;

public final class NativeApi {
    public static void addNativeData(Environment globalEnvironment) {
        addNativeVariables(globalEnvironment);
        addNativeFunctions(globalEnvironment);
    }

    public static void addNativeVariables(Environment globalEnvironment) {}

    public static void addNativeFunctions(Environment globalEnvironment) {
        globalEnvironment.setLocally("println", new TSFunction(Arrays.asList("text")) {
            @Override
            public int arity() {
                return 1;
            }

            @Override
            public TSObject call(ExecutionEngine engine, Environment environment) {
                System.out.println(environment.get("text"));
                return null;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }
}
