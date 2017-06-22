package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collector;

/**
 * Factory method for NdIndex
 */

public class NdSlice {

    public static NdIndex all(){
        return new NdIndexAll();
    }

    public static NdIndex interval(int start, int end){
        return new NdIndexInterval(start, end);
    }

    public static NdIndex set(int... set){
        return new NdIndexSet(Ints.asList(set));
    }

    public static NdIndex set(Iterator<Integer> set){
        return new NdIndexSet(Lists.newArrayList(set));
    }
}
