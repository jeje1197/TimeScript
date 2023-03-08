package com.jpl.timescript.interpreter.datatypes;

public interface Comparable {
    TSObject lessThan(TSObject other) throws Exception;
    TSObject lessThanOrEquals(TSObject other) throws Exception;
    TSObject greaterThan(TSObject other) throws Exception;
    TSObject greaterThanOrEquals(TSObject other) throws Exception;
    TSObject equals(TSObject other) throws Exception;
    TSObject notEquals(TSObject other) throws Exception;
}
