package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Optional;

public class NdIndexAll implements NdIndex {

    @Override
    public Optional<Integer> map(Integer i) {
        return Optional.of(i);
    }

}
