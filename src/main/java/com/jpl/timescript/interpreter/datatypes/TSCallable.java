package com.jpl.timescript.interpreter.datatypes;

import com.jpl.timescript.interpreter.ExecutionEngine;

public interface TSCallable {
    int arity();
    TSObject call(ExecutionEngine engine);
}
