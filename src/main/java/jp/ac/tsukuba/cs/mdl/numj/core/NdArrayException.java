package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Arrays;

public class NdArrayException extends RuntimeException {
    public NdArrayException(NdArray array1, NdArray array2){
        super(Arrays.toString(array1.shape())+" "+Arrays.toString(array2.shape()));
    }
}
