package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.primitives.Ints;

import java.util.Collection;

/**
 * Factory method for NdIndex
 */

public class NdSlice {

    public static NdIndex all() {
        return new NdIndexAll();
    }

    public static NdIndex interval(int start, int end) {
        return new NdIndexInterval(start, end);
    }

    public static NdIndex set(int... set) {
        return new NdIndexSet(set);
    }

    public static NdIndex set(Collection<Integer> set) {
        return new NdIndexSet(Ints.toArray(set));
    }

    public static NdIndex point(int i){
        return new NdIndexPoint(i);
    }
}
