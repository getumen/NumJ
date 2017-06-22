package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Collection;
import java.util.List;

public class NdIndexSet implements NdIndex{

    protected int pointer;
    protected List<Integer> indexes;

    public NdIndexSet(List<Integer> indexes) {
        this.indexes = indexes;
        pointer = 0;
    }

    @Override

    public Collection<Integer> indexes() {
        return indexes;
    }

    @Override
    public boolean hasNext() {
        return pointer < indexes.size();
    }

    @Override
    public Integer next() {
        return indexes.get(pointer++);
    }
}
