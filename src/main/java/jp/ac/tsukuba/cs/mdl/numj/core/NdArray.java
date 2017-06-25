package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public interface NdArray {

    AtomicDoubleArray data();

    int[] shape();

    int dim();

    int size();

    NdArray copy();

    boolean checkBroadcast(NdArray other);

    NdArray elementwise(NdArray other, BinaryOperator<Double> op);

    NdArray elementwise(Number other, BinaryOperator<Double> op);

    NdArray elementwise(Function<Double, Double> op);

    NdArray axiswise(Function<int[], Double> op, int... axis);

    NdArray dot(NdArray other);

    NdArray add(NdArray other);

    NdArray add(Number other);

    NdArray sub(NdArray other);

    NdArray sub(Number other);

    NdArray mul(NdArray other);

    NdArray mul(Number other);

    NdArray div(NdArray other);

    NdArray div(Number other);

    NdArray get(NdIndex... indexes);

    double get(int[] coordinate);

    double braodcastGet(int[] coordinate);

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

    void conditionalPut(Predicate<Double> f, double value);
}
