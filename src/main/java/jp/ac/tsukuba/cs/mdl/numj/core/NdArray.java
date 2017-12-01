package jp.ac.tsukuba.cs.mdl.numj.core;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * NumPyを元にしたN-Dimensional Array．
 * N次元のテンソルを一次元の配列を用いて表し、各要素へのアクセスを定数時間で行うことができる．
 */
public interface NdArray {

    /**
     * @return NdArrayのshapeを返す．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.ndarray.shape.html'>numpy.ndarray,shape</a>と同じ．
     */
    int[] shape();

    /**
     * @return NdArrayのdimensionを返す．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.ndarray.ndim.html'>numpy.ndarray.ndim</a>と同じ．
     */
    int dim();

    /**
     * @return NdArrayのsizeを返す．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.ndarray.size.html'>numpy.ndarray.size</a>と同じ．
     */
    int size();

    /**
     * @return NdArrayのdeep copyを返す．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.copy.html'>numpy.copy</a>と同じ．
     */
    NdArray copy();

    /**
     * @return Broadcastが必要かどうかを判定する．
     */
    boolean checkBroadcast(NdArray other);

    /**
     * 要素ごとの計算を行う．状態を変更しない．計算は並列化して実行される．
     * @param other: NdArray
     * @param op: 2つの引数を取る演算子．ラムダ式等．
     * @return このインスタンスの要素を左側とotherの要素を右側の引数としてopを適用した結果を返す．
     */
    NdArray elementwise(NdArray other, BinaryOperator<Double> op);

    /**
     * 要素ごとの計算を行う．状態を変更しない．計算は並列化して実行される．
     * @param other: Number
     * @param op: 2つの引数を取る演算子．ラムダ式等．
     * @return このインスタンスの要素を左側とotherの要素を右側の引数としてopを適用した結果を返す．
     */
    NdArray elementwise(Number other, BinaryOperator<Double> op);

    /**
     * 要素ごとの計算を行う．状態を変更しない．計算は並列化して実行される．
     * @param op: 1つの引数を取る演算子．ラムダ式等．
     * @return このインスタンスの要素を引数としてopを適用した結果を返す．
     */
    NdArray elementwise(Function<Double, Double> op);

    /**
     * 要素ごとの計算を行う．インスタンの状態を変更して、このインスタンスの参照を返す(in place).．
     * @param op: 1つの引数を取る演算子．ラムダ式等．
     * @return このインsタンスの要素を引数としてopを適用し、このインスタンスの参照を返す．
     */
    NdArray elementwisei(Function<int[], Double> op);

    /**
     * ドット積を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.dot.html'>numpy.dot</a>と同じ．
     * a.dot(b)[i,j,k,m] = sum(a[i,j,:] * b[k,:,m])を返す．
     * @param other: 左側のNdArray
     * @return ドット積の結果．
     */
    NdArray dot(NdArray other);

    /**
     * 和を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.add.html'>numpy.add</a>と同じ．
     * @param other: 右側のNdArray
     * @return 和の結果．
     */
    NdArray add(NdArray other);

    /**
     * 和を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.add.html'>numpy.add</a>と同じ．
     * @param other: 右側のNumber
     * @return 和の結果．
     */
    NdArray add(Number other);

    /**
     * 差を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.subtract.html'>numpy.subtract</a>と同じ．
     * @param other: 右側のNdArray
     * @return 差の結果．
     */
    NdArray sub(NdArray other);

    /**
     * 差を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.subtract.html'>numpy.subtract</a>と同じ．
     * @param other: 右側のNumber
     * @return 差の結果．
     */
    NdArray sub(Number other);

    /**
     * 積を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.subtract.html'>numpy.multiply</a>と同じ．
     * @param other: 右側のNdArray
     * @return 積の結果．
     */
    NdArray mul(NdArray other);

    /**
     * 積を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.multiply.html'>numpy.multiply</a>と同じ．
     * @param other: 右側のNumber
     * @return 積の結果．
     */
    NdArray mul(Number other);

    /**
     * 商を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.multiply.html'>numpy.divide</a>と同じ．
     * @param other: 右側のNdArray
     * @return 商の結果．
     * @throws RuntimeException: ゼロ割が発生した時に例外を投げる．
     */
    NdArray div(NdArray other);

    /**
     * 商を計算する．状態を変更しない．
     * 機能は<a href='https://docs.scipy.org/doc/numpy/reference/generated/numpy.multiply.html'>numpy.dividey</a>と同じ．
     * @param other: 右側のNumber
     * @return 商の結果．
     * @throws RuntimeException: ゼロ割が発生した時に例外を投げる．
     */
    NdArray div(Number other);

    /**
     * otherと同じ要素かを判定する．
     * @param other: 右側のNdArray
     * @return if leftElement == rightElement then 1 else 0.
     */
    NdArray eq(NdArray other);

    /**
     * otherと同じ要素かを判定する．
     * @param other: 右側のNumber
     * @return if leftElement == rightElement then 1 else 0.
     */
    NdArray eq(Number other);

    /**
     * otherと > かを判定する．
     * @param other: 右側のNdArray
     * @return if leftElement > rightElement then 1 else 0.
     */
    NdArray gt(NdArray other);

    /**
     * otherと > かを判定する．
     * @param other: 右側のNumber
     * @return if leftElement > rightElement then 1 else 0.
     */
    NdArray gt(Number other);

    /**
     * otherと < かを判定する．
     * @param other: 右側のNdArray
     * @return if leftElement < rightElement then 1 else 0.
     */
    NdArray lt(NdArray other);

    /**
     * otherと < かを判定する．
     * @param other: 右側のNumber
     * @return if leftElement < rightElement then 1 else 0.
     */
    NdArray lt(Number other);

    /**
     * otherと >= かを判定する．
     * @param other: 右側のNdArray
     * @return if leftElement >= rightElement then 1 else 0.
     */
    NdArray gte(NdArray other);

    /**
     * otherと >= かを判定する．
     * @param other: 右側のNumber
     * @return if leftElement >= rightElement then 1 else 0.
     */
    NdArray gte(Number other);

    /**
     * otherと <= かを判定する．
     * @param other: 右側のNdArray
     * @return if leftElement <= rightElement then 1 else 0.
     */
    NdArray lte(NdArray other);

    /**
     * otherと <= かを判定する．
     * @param other: 右側のNumber
     * @return if leftElement <= rightElement then 1 else 0.
     */
    NdArray lte(Number other);

    /**
     * 座標ごとの演算を行う．
     * @param op: 2引数の演算
     * @return すべての座標に対してopをreduce処理した結果を返す．
     */
    Double axisOperation(BinaryOperator<Double> op);

    /**
     * 座標ごとの演算を行う．
     * @param op: 2引数の演算
     * @return 指定されたの座標に対してopをreduce処理した結果を返す．
     */
    NdArray axisOperation(BinaryOperator<Double> op, int... axis);

    /**
     * 座標ごとの演算を行う．
     * @param op: 2引数の演算
     * @return すべての座標に対してopをreduce処理した結果、Piarの座標の左側を返す．．
     */
    Integer axisArgOperation(BinaryOperator<Pair<Integer, Double>> op);

    /**
     * 座標ごとの演算を行う．
     * @param op: 2引数の演算
     * @return 指定された座標に対してopをreduce処理した結果、Piarの座標の左側を返す．．
     */
    NdArray axisArgOperation(BinaryOperator<Pair<Integer, Double>> op, int axis);

    /**
     * @return 最大の要素のインデックスを返す．
     * 最大の要素のインデックスを返す
     */
    Integer argmax();

    /**
     * @return 指定された座標の最大の要素のインデックスを返す．
     * 指定された座標の最大の要素のインデックスを返す．
     */
    NdArray argmax(int axis);

    /**
     * @return 最小の要素のインデックスを返す．
     * 最小の要素のインデックスを返す
     */
    Integer argmin();

    /**
     * @return 指定された座標の最小の要素のインデックスを返す．
     * 指定された座標の最小の要素のインデックスを返す．
     */
    NdArray argmin(int axis);

    /**
     * @return すべての要素の最大値を返す．
     * すべての要素の最大値を返す．
     */
    Double max();

    /**
     * @param axis 座標
     * @return 指定された座標の要素の最大値を返す．
     * 指定された座標の要素の最大値を返す．
     */
    NdArray max(int... axis);

    /**
     * @return すべての要素の最小値を返す．
     * すべての要素の最小値を返す．
     */
    Double min();

    /**
     * @param axis 座標
     * @return 指定された座標の要素に最小値を返す、
     * 指定された座標の要素に最小値を返す、
     */
    NdArray min(int... axis);

    /**
     * @return すべての要素の和を返す．
     * すべての要素の和を返す．
     */
    Double sum();

    /**
     * 指定された座標の要素の和を返す．
     * @param axis: 要素
     * @return 指定された座標の要素の和を返す．
     */
    NdArray sum(int... axis);

    /**
     * @deprecated slice
     * slice処理を行う．
     */
    @Deprecated
    NdArray get(NdIndex... indexes);

    /**
     * 指定された座標の値を取得する、
     * @param coordinate: 座標
     * @return 座標の値．
     */
    double get(int... coordinate);

    /**
     * NdArrayを一次元の配列として見た時のindexに対応する値を返す．
     * @param index: NdArrayを一次元の配列として見た時のindex
     * @return 対応する値
     */
    double get(int index);

    /**
     * @param op: 条件式
     * @return opを満たすとき1,満たさない時0を返す、
     */
    NdArray where(Predicate<Double> op);

    /**
     * 指定された座標の要素を取得する．もし、座標がないときはブロードキャストしたあとの座標を取得する．
     * @param coordinate: 座標
     * @return 座標の要素
     */
    double broadcastGet(int[] coordinate);

    /**
     * 指定された座標に要素をputする．
     * @param coodinate: 座標
     * @param value
     */
    void put(int[] coodinate, double value);

    /**
     * 指定されたスライスにarrayをputする．
     * @param indexes: スライス．
     * @param array: putするNdArray
     */
    void put(NdIndex[] indexes, NdArray array);

    /**
     * 条件にあった部分に値をputする．
     * @param where: 値が非ゼロの部分が新を表すNdArray
     * @param value: putする値．
     */
    void put(NdArray where, Number value);

    /**
     * 条件にあった部分に値をputする．
     * @param where: 値が非ゼロの部分が新を表すNdArray
     * @param value: putするNdArray．
     */
    void put(NdArray where, NdArray value);

    /**
     * スライス．
     * @param indices: 次元分のSlice
     * @return スライスに含まれるNdArray
     */
    NdArray slice(NdIndex... indices);

    /**
     * 転置を行う．
     * @return 転地されたNdArray
     */
    NdArray transpose();

    /**
     * 指定された次元に対して転置を行う．
     * @return 転地されたNdArray
     */
    NdArray transpose(int... dim);

    /**
     * 指定されたshapeに変換する．
     * @param rearrange: 新しいshape
     * @return reshapeされたNdArray
     */
    NdArray reshape(int... rearrange);

    /**
     * 等しいかどうかを判定する．
     * @param o: 比較対象となるNdArray
     * @return 等しいかどうかの判定結果
     */
    @Override
    boolean equals(Object o);


    @Override
    int hashCode();

    /**
     * 条件にあった部分にputする．
     * @param f: 条件式
     * @param value: putする値．
     */
    void conditionalPut(Predicate<Double> f, double value);

    /**
     * NdArrayをベクトルとしてみた時のindexに対応するpointerを返す．
     * @param index: インデックス．
     * @return ポインター
     */
    int pointer(int index);
}
