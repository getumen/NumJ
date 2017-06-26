package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.util.concurrent.AtomicDoubleArray;
import org.apache.commons.lang3.tuple.Pair;

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

    NdArray elementwisei(Function<int[], Double> op);

    NdArray dot(NdArray other);

    NdArray add(NdArray other);

    NdArray add(Number other);

    NdArray sub(NdArray other);

    NdArray sub(Number other);

    NdArray mul(NdArray other);

    NdArray mul(Number other);

    NdArray div(NdArray other);

    NdArray div(Number other);

    Double axisOperation(BinaryOperator<Double> op);

    NdArray axisOperation(BinaryOperator<Double> op, int... axis);

    Integer axisArgOperation(BinaryOperator<Pair<Integer, Double>> op);

    NdArray axisArgOperation(BinaryOperator<Pair<Integer, Double>> op, int axis);

    Integer argmax();

    NdArray argmax(int axis);

    Double max();

    NdArray max(int... axis);

    Double sum();

    NdArray sum(int... axis);

    NdArray get(NdIndex... indexes);

    double get(int[] coordinate);

    NdArray where(Predicate<Double> op);

    double braodcastGet(int[] coordinate);

    void put(int[] indexes, double value);

    void put(NdIndex[] indexes, NdArray array);

    void put(NdArray where, Number value);

    void put(NdArray where, NdArray value);

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
