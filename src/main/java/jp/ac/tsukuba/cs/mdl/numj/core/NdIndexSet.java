package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.primitives.Ints;

import java.util.Optional;

public class NdIndexSet implements NdIndex {

    private int[] set;

    public NdIndexSet(int... set) {
        this.set = set;
    }

    @Override
    public Optional<Integer> map(Integer i) {
        if (Ints.contains(set, i)) {
            return Optional.of(i);
        } else {
            return Optional.empty();
        }
    }
}
