package jp.ac.tsukuba.cs.mdl.numj.core;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Arrays;
import java.util.Random;
import java.util.function.IntToDoubleFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * NdArrayを生成するユーティリティクラス
 */
public class NumJ {


    private static int size(int... shape) {
        return Arrays.stream(shape).reduce(1, (l, r) -> l * r);
    }

    /**
     * すべての要素がvalueで産められたshapeの形を持つNdArrayを生成する．
     *
     * @param value 要素の値
     * @param shape NdArrayの形
     * @return すべての要素がvalueで産められたshapeの形を持つNdArray
     */
    public static NdArray createByNumber(Number value, int... shape) {
        NdArray res = new NdArrayImpl(shape);
        if (value.doubleValue() == 0) {
            return res;
        } else {
            return res.elementwise(i -> value.doubleValue());
        }
    }

    /**
     * doubleの配列の要素を持つshapeの形をしたNdArrayを生成する．
     *
     * @param array 各要素の値を表すdouble型の配列
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray create(double[] array, int... shape) {
        return new NdArrayImpl(shape, array);
    }

    /**
     * すべての要素が1で埋められたNdArray
     *
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray ones(int... shape) {
        return createByNumber(1, shape);
    }

    /**
     * すべての要素が0で埋められたNdArray
     *
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray zeros(int... shape) {
        return createByNumber(0, shape);
    }

    /**
     * i番目の要素を指すインデックスを値に変換するラムダ式を用いてNdArrayを生成する
     * <code  class="language-java">
     * NumJ.generator(i -> 2.*i, 3,4);
     * </code>
     *
     * @param f     要素を生成する関数
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray generator(IntToDoubleFunction f, int... shape) {
        int size = size(shape);
        return new NdArrayImpl(shape, IntStream.range(0, size).mapToDouble(f).toArray());
    }

    /**
     * 引数を取らないラムダ式を用いてNdArrayを生成する．
     * <code  class="language-java">
     * NumJ.generator(()->random.nextDouble(), 3,4);
     * </code>
     *
     * @param f     ラムダ式
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray generator(Supplier<Double> f, int... shape) {
        int size = size(shape);
        return new NdArrayImpl(shape, IntStream.range(0, size).mapToDouble(i -> f.get()).toArray());
    }

    /**
     * 0からsize-1までの要素が順番に並んでshapeの形をしたNdArrayを生成する
     *
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray arange(int... shape) {
        return generator(i -> i, shape);
    }

    /**
     * 対角要素からk右にずれた要素がが１であるn x mのNdArray
     *
     * @param n 座標0の要素数
     * @param m 座標1の要素数
     * @param k 要素がいくつずれるか -m,-n < k < m,n
     * @return NdArray
     */
    public static NdArray eye(int n, int m, int k) {
        NdArray result = NumJ.zeros(n, m);
        for (int i = -k; i < Math.max(n, m) - k; i++) {
            if (0 <= i && i < n) {
                result.put(new int[]{i, i + k}, 1.);
            }
        }
        return result;
    }

    /**
     * 対角要素からk右にずれた要素がが１であるn x mのNdArray
     *
     * @param n 正方行列の大きさ
     * @param k 要素がいくつずれるか -n < k < n
     * @return NdArray
     */
    public static NdArray eye(int n, int k) {
        return eye(n, n, k);
    }

    /**
     * 単位行列
     *
     * @param n 大きさ
     * @return NdArray
     */
    public static NdArray identity(int n) {
        return eye(n, n, 0);
    }

    /**
     * 正規分布に従う乱数から生成されたNdArray
     *
     * @param mu    Gaussの平均
     * @param sigma Gaussの標準偏差
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray normal(double mu, double sigma, int... shape) {
        NormalDistribution normalDist = new NormalDistribution(mu, sigma * sigma);
        return generator(() -> normalDist.sample(), shape);
    }

    /**
     * 一様に従う乱数から生成されたNdArray
     *
     * @param shape NdArrayのshape
     * @return NdArray
     */
    public static NdArray uniform(int... shape) {
        Random random = new Random();
        return generator(() -> random.nextDouble(), shape);
    }
}
