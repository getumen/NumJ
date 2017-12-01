package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.primitives.Ints;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Factory method for NdIndex
 */

public class NdSlice {

    /**
     * 自邸された座標のすべての要素を返すことを表すNdIndexを生成する．
     *
     * @return 座標のすべての要素を返すことを表すNdIndex
     */
    public static NdIndex all() {
        return new NdIndexAll();
    }

    /**
     * 自邸された座標のstartからend-1までの要素を返すことを表すNdIndexを生成する
     *
     * @param start 要素の開始点
     * @param end   要素の終着点． endのインデックスに当たる要素は含まれない．
     * @return startからend-1までの要素を返すことを表すNdIndex
     */
    public static NdIndex interval(int start, int end) {
        return new NdIndexInterval(start, end);
    }

    /**
     * 自邸された座標の配列に含まれるインデックスに対応するの要素を返すことを表すNdIndexを生成する
     *
     * @param set インデックスの集合
     * @return 配列に含まれるインデックスに対応するの要素を返すことを表すNdIndex
     */
    public static NdIndex set(int... set) {
        return new NdIndexSet(set);
    }

    /**
     * 自邸された座標の配列に含まれるインデックスに対応するの要素を返すことを表すNdIndexを生成する
     *
     * @param set インデックスの集合
     * @return 配列に含まれるインデックスに対応するの要素を返すことを表すNdIndex
     */
    public static NdIndex set(Collection<Integer> set) {
        return new NdIndexSet(Ints.toArray(set));
    }

    /**
     * 自邸された座標の配列に含まれるインデックスに対応するの要素を返すことを表すNdIndexを生成する
     *
     * @param indices インデックスの集合
     * @return 配列に含まれるインデックスに対応するの要素を返すことを表すNdIndex
     */
    public static NdIndex set(NdArray indices) {
        if (indices.dim() != 1) {
            throw new IllegalArgumentException("dimension is not 1");
        }
        return set(IntStream.range(0, indices.size()).map(i -> (int) indices.get(i)).boxed().collect(Collectors.toList()));
    }

    /**
     * 自邸された座標のインデックスiに対応するの要素を返すことを表すNdIndexを生成する
     *
     * @param i インデックス
     * @return インデックスiに対応するの要素を返すことを表すNdIndex
     */
    public static NdIndex point(int i) {
        return new NdIndexPoint(i);
    }
}
