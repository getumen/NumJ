package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Collection;
import java.util.Iterator;

public interface NdIndex extends Iterator<Integer>{

    Collection<Integer> indexes();

}
