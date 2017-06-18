package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.IntStream;

public class NumJ {

    public static void checkDimension(NdArray array1, NdArray array2) {
        if (!array1.checkDemensions(array2)) {
            throw new NdArrayException(array1, array2);
        }
    }

    public static NdArray elementwise(NdArray array1, NdArray array2, BinaryOperator<Double> op) {

        checkDimension(array1, array2);

        AtomicDoubleArray result = new AtomicDoubleArray(array1.size());

        if (Arrays.deepEquals(IntStream.of(array1.shape()).boxed().toArray(), IntStream.of(array2.shape()).boxed().toArray())) {
            IntStream.range(0, array1.size()).parallel().forEach(i -> {
                result.set(i, op.apply(array1.data().get(i), array2.data().get(i)));
            });
        } else {
            IntStream.range(0, array1.size()).parallel().forEach(i -> {
                int[] indexes = array1.indexes(i);
                for (int j = 0; j < array1.dim(); j++) {
                    if (array2.shape()[j] == 1) {
                        indexes[j] = 0;
                    }
                }
                result.set(i, op.apply(array1.data().get(i), array2.data().get(array1.index(indexes))));
            });
        }
        return new NdArrayImpl(array1.shape(), result);
    }

    public static NdArray elementwise(NdArray array1, Number value, BinaryOperator<Double> op) {
        AtomicDoubleArray data1 = array1.data();
        AtomicDoubleArray result = new AtomicDoubleArray(array1.size());
        IntStream.range(0, array1.size()).parallel().forEach(i -> result.set(i, op.apply(data1.get(i), value.doubleValue())));
        return new NdArrayImpl(array1.shape(), result);
    }

    public static NdArray elementwise(NdArray array, Function<Double, Double> op) {
        AtomicDoubleArray data = array.data();
        AtomicDoubleArray result = new AtomicDoubleArray(array.size());
        IntStream.range(0, array.size()).parallel().forEach(i -> result.set(i, op.apply(data.get(i))));
        return new NdArrayImpl(array.shape(), result);
    }

    public static int size(int... shape) {
        return Arrays.stream(shape).reduce(1, (l, r) -> l * r);
    }

    public static NdArray createByNumber(Number value, int... shape) {
        double[] array = new double[size(shape)];
        Arrays.fill(array, value.doubleValue());
        return new NdArrayImpl(shape, array);
    }

    public static NdArray create(double[] array, int... shape) {
        return new NdArrayImpl(shape, array);
    }

    public static NdArray ones(int... shape) {
        return createByNumber(1, shape);
    }

    public static NdArray zeros(int... shape) {
        return createByNumber(0, shape);
    }
}
