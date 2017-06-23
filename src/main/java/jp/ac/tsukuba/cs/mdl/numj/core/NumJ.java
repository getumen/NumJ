package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntToDoubleFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class NumJ {

    private static int size(int... shape) {
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

    public static NdArray generator(IntToDoubleFunction f, int... shape){
        int size = Arrays.stream(shape).reduce((l,r)->l*r).orElseThrow(IllegalArgumentException::new);
        return new NdArrayImpl(shape, IntStream.range(0,size).mapToDouble(f).toArray());
    }

    public static NdArray generator(Supplier<Double> f, int... shape){
        int size = Arrays.stream(shape).reduce((l,r)->l*r).orElseThrow(IllegalArgumentException::new);
        return new NdArrayImpl(shape, IntStream.range(0,size).mapToDouble(i -> f.get()).toArray());
    }

    public static NdArray arange(int num, int... shape) {
        return generator(i->i, shape);
    }
}
