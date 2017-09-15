package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Optional;

/**
 * NdArrayのスライスのためのインターフェイス
 */
public interface NdIndex {

    Optional<Integer> map(Integer i);

}
