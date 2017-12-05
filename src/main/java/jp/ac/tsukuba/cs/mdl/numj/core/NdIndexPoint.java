package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Optional;

public class NdIndexPoint implements NdIndex {

    private int point;

    NdIndexPoint(int point) {
        this.point = point;
    }

    int getPoint() {
        return point;
    }
}
