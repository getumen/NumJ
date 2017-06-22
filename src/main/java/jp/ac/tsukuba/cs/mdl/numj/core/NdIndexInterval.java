package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NdIndexInterval implements NdIndex{

    protected int start;
    protected int end;
    protected int pointer;

    @Override
    public Collection<Integer> indexes() {
        return IntStream.range(start, end).boxed().collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return pointer < (end - start);
    }

    public NdIndexInterval(int start, int end) {
        this.start = start;
        this.end = end;
        pointer = 0;
    }

    @Override
    public Integer next() {
        return start + pointer;
    }
}
