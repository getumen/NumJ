package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Optional;

public class NdIndexInterval implements NdIndex {

    private int start;

    private int end;

    NdIndexInterval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    int getStart() {
        return start;
    }

    int getEnd() {
        return end;
    }
}
