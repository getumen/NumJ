package jp.ac.tsukuba.cs.mdl.numj.probability;

import org.junit.Test;

import static org.junit.Assert.*;

public class CombinationTest {
    @Test
    public void combination() throws Exception {
        assertEquals(75287520, Combination.combination(100,5));
    }

    @Test
    public void permutation() throws Exception {
        assertEquals(9034502400L, Combination.permutation(100, 5));
    }

}