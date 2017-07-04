package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static NdIndex set(NdArray indices) {
        if (indices.dim() != 1) {
            throw new IllegalArgumentException("dimension is not 1");
        }
        AtomicDoubleArray data = indices.data();
        return set(IntStream.range(0, indices.size()).map(i -> (int) data.get(i)).boxed().collect(Collectors.toList()));
    }

    public static NdIndex point(int i) {
        return new NdIndexPoint(i);
    }
}
