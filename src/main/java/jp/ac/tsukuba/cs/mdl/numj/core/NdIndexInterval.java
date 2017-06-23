package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Optional;

public class NdIndexInterval implements NdIndex {

    private int start;

    private int end;

    public NdIndexInterval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Optional<Integer> map(Integer i) {
        if (start <= i && i < end) {
            return Optional.of(i);
        }
        return Optional.empty();
    }
}
