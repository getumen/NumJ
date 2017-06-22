package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Optional;

public class NdIndexPoint implements NdIndex {

    protected int point;

    public NdIndexPoint(int point) {
        this.point = point;
    }

    public Optional<Integer> map(Integer i) {
        if (i == point) {
            return Optional.of(i);
        }
        return Optional.empty();
    }
}
