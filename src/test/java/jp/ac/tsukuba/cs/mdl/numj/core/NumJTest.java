package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NumJTest {

    @Test
    public void ones() {
        assertEquals(NumJ.create(new double[]{1, 1, 1, 1}, 2, 2), NumJ.ones(2, 2));
        assertNotEquals(NumJ.create(new double[]{1, 1, 1, 2}, 2, 2), NumJ.ones(2, 2));
    }

    @Test
    public void zeros() {
        assertNotEquals(NumJ.create(new double[]{2, 2, 2, 2}, 2, 2), NumJ.zeros(2, 2));
        assertEquals(NumJ.create(new double[]{0, 0, 0, 0}, 2, 2), NumJ.zeros(2, 2));
    }

    @Test
    public void arange() {
        assertEquals(NumJ.create(new double[]{0, 1, 2, 3}, 2, 2), NumJ.arange(2, 2));
    }

    @Test
    public void eye() {
        System.out.println(NumJ.eye(5, 6, 0));
    }

    public static void main(String[] args) {
        NdArray arange = NumJ.arange(2, 3);
        System.out.println(arange.transpose());
        System.out.println(Arrays.toString(arange.transpose().shape()));
        /*
        転置
        [
        [0.0, 3.0, ]
        [1.0, 4.0, ]
        [2.0, 5.0, ]
        ]
        shape: [3, 2]
         */
        NdArray fourDim = NumJ.arange(2, 3, 4, 5);
        System.out.println(fourDim.transpose());
        System.out.println(Arrays.toString(fourDim.transpose().shape()));
        /*
        転置
        [[[
        [0.0, 60.0, ]
        [1.0, 61.0, ]
        [2.0, 62.0, ]
        [3.0, 63.0, ]
        [4.0, 64.0, ]
        ]...[
        [55.0, 115.0, ]
        [56.0, 116.0, ]
        [57.0, 117.0, ]
        [58.0, 118.0, ]
        [59.0, 119.0, ]
        ]]]
        shape: [3, 4, 5, 2]
         */
        System.out.println(Arrays.toString(fourDim.transpose(1, 3, 2, 0).shape()));
        /*
        ３次テンソル以上では自分で転置の順序を決められる、
        [3, 5, 4, 2]
         */
    }
}