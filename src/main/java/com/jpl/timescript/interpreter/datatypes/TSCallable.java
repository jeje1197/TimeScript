package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.ExecutionEngine;
import com.jpl.timescript.interpreter.environment.Environment;

public interface TSCallable {
    int arity();
    TSObject call(ExecutionEngine engine, Environment environment) throws Exception;
}
