package com.jpl.timescript.interpreter.datatypes;

public interface Arithmetic {
    TSObject add(TSObject other) throws Exception;
    TSObject subtract(TSObject other) throws Exception;
    TSObject multiply(TSObject other) throws Exception;
    TSObject divide(TSObject other) throws Exception;
    TSObject mod(TSObject other) throws Exception;
}
