package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Arrays;

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

    public static NdArray arange(int num, int... shape) {
        int size = size(shape);
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return new NdArrayImpl(shape, array);
    }
}
