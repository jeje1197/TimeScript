package com.jpl.timescript.interpreter.datatypes;

import java.util.List;

public class TSList extends TSObject implements TSIterable {
    private List<TSObject> values;

    public TSList(List<TSObject> values) {
        super("List");
        this.values = values;
    }

    @Override
    public int getSize() {
        return values.size();
    }

    @Override
    public TSObject getIndex(TSObject index) {
        if (index instanceof TSNumber) {
            TSNumber indexValue = (TSNumber) index;
            return values.get((int) indexValue.getValue());
        }

        return null;
    }

    @Override
    public TSObject setIndex(TSObject index, TSObject value) throws Exception {
        if (index instanceof TSNumber) {
            TSNumber indexValue = (TSNumber) index;
            values.set((int) indexValue.getValue(), value);
        }

        return null;
    }

    @Override
    public TSObject append(TSObject value) {
        values.add(value);
        return null;
    }


    @Override
    public String toString() {
        return values.toString();
    }
}
