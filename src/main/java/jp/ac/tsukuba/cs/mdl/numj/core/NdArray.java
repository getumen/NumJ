package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.util.concurrent.AtomicDoubleArray;

public interface NdArray {

    AtomicDoubleArray data();

    int[] shape();

    int dim();

    int size();

    NdArray copy();

    boolean checkDemensions(NdArray other);

    NdArray add(NdArray other);

    NdArray add(Number other);

    NdArray sub(NdArray other);

    NdArray sub(Number other);

    NdArray mul(NdArray other);

    NdArray mul(Number other);

    NdArray div(NdArray other);

    NdArray div(Number other);

    NdArray get(NdIndex... indexes);

    double get(int[] cords);

    void put(int[] indexes, double value);

    void put(NdIndex[] indexes, NdArray array);

    NdArray slice(NdIndex[] indices);

    NdArray transpose();

    NdArray transpose(int... dim);

    NdArray reshape(int... rearrange);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
