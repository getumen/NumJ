package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.collect.Lists;

import java.util.Collection;

public class NdIndexPoint implements NdIndex {

    protected int point;
    protected int pointer;

    public NdIndexPoint(int point) {
        this.point = point;
        pointer = 0;
    }

    @Override

    public Collection<Integer> indexes() {
        return Lists.newArrayList(point);
    }

    @Override
    public boolean hasNext() {
        return pointer < 1;
    }

    @Override
    public Integer next() {
        pointer++;
        return point;
    }
}
