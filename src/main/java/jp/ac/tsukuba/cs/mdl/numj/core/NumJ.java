package jp.ac.tsukuba.cs.mdl.numj.core;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Arrays;
import java.util.Random;
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

    public static NdArray generator(IntToDoubleFunction f, int... shape) {
        int size = size(shape);
        return new NdArrayImpl(shape, IntStream.range(0, size).mapToDouble(f).toArray());
    }

    public static NdArray generator(Supplier<Double> f, int... shape) {
        int size = size(shape);
        return new NdArrayImpl(shape, IntStream.range(0, size).mapToDouble(i -> f.get()).toArray());
    }

    public static NdArray arange(int... shape) {
        return generator(i -> i, shape);
    }

    public static NdArray eye(int n, int m, int k) {
        NdArray result = NumJ.zeros(n, m);
        if (-n <= k && k < n && -m <= k && k < m) {
            for (int i = 0; i < Math.min(n, m) - Math.abs(k); i++) {
                result.put(new int[]{(k + i + n) % n, (k + i + m) % m}, 1.);
            }
        }
        return result;
    }

    public static NdArray eye(int n, int k) {
        return eye(n, n, k);
    }

    public static NdArray identity(int n) {
        return eye(n, n, 0);
    }

    public static NdArray normal(double mu, double sigma, int... shape) {
        NormalDistribution normalDist = new NormalDistribution(mu, sigma * sigma);
        return generator(() -> normalDist.sample(), shape);
    }

    public static NdArray uniform(int... shape) {
        Random random = new Random();
        return generator(() -> random.nextDouble(), shape);
    }
}
