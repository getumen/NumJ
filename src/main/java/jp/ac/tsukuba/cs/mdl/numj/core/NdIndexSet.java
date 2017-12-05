package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.primitives.Ints;

import java.util.Optional;

public class NdIndexSet implements NdIndex {

    private int[] set;

    NdIndexSet(int... set) {
        this.set = set;
    }

    int[] getSet() {
        return set;
    }
}
