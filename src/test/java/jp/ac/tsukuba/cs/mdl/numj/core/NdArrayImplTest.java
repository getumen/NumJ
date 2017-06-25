package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class NdArrayImplTest {
    @Test
    public void dot() throws Exception {
        NdArray arange1 = NumJ.arange(3, 4);
        NdArray arange2 = NumJ.arange(4, 2);
        assertArrayEquals(new int[]{3, 2}, arange1.dot(arange2).shape());
        assertEquals(new NdArrayImpl(new int[]{3, 2}, new double[]{28, 34, 76, 98, 124, 162}), arange1.dot(arange2));
        NdArray vec = NumJ.arange(4);
        assertEquals(1, arange1.dot(vec).dim());
        assertArrayEquals(new int[]{3}, arange1.dot(vec).shape());
        assertEquals(new NdArrayImpl(new int[]{3}, new double[]{14, 38, 62}), arange1.dot(vec));
    }

    @Test
    public void argmax() throws Exception {
    }

    @Test
    public void argmax1() throws Exception {
    }

    @Test
    public void max() throws Exception {
    }

    @Test
    public void max1() throws Exception {
    }

    @Test
    public void size() throws Exception {
        assertEquals(3 * 4 * 5, NumJ.ones(3, 4, 5).size());
    }

    @Test
    public void shape() throws Exception {
    }

    @Test
    public void data() throws Exception {
    }

    @Test
    public void copy() throws Exception {
    }

    @Test
    public void add() throws Exception {
        NdArray ones = NumJ.ones(3, 4);
        assertEquals(
                new NdArrayImpl(new int[]{3, 4}, new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}),
                ones.add(NumJ.arange(1, 4))
        );
        assertEquals(
                new NdArrayImpl(new int[]{3, 4}, new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}),
                ones.add(NumJ.arange(4, 1).transpose())
        );
    }

    @Test
    public void add1() throws Exception {
        assertEquals(new NdArrayImpl(new int[]{2, 3}, new double[]{1, 2, 3, 4, 5, 6}), NumJ.arange(2, 3).add(1));
    }

    @Test
    public void sub() throws Exception {
        assertEquals(new NdArrayImpl(new int[]{2, 3}, new double[]{-1, 0, 1, 2, 3, 4}), NumJ.arange(2, 3).sub(NumJ.ones(2, 3)));
    }

    @Test
    public void sub1() throws Exception {
        assertEquals(new NdArrayImpl(new int[]{2, 3}, new double[]{-1, 0, 1, 2, 3, 4}), NumJ.arange(2, 3).sub(1));
    }

    @Test
    public void checkDemensions() throws Exception {
    }

    @Test
    public void div() throws Exception {
    }

    @Test
    public void div1() throws Exception {
    }

    @Test
    public void get() throws Exception {
    }

    @Test
    public void get1() throws Exception {
    }

    @Test
    public void mul() throws Exception {
    }

    @Test
    public void mul1() throws Exception {
    }

    @Test
    public void reshape() throws Exception {
    }

    @Test
    public void equals() throws Exception {
    }

    @Test
    public void dim() throws Exception {
    }

    @Test
    public void put() throws Exception {
    }

    @Test
    public void put1() throws Exception {
    }

    @Test
    public void slice() throws Exception {
    }

    @Test
    public void transpose() throws Exception {
    }

    @Test
    public void transpose1() throws Exception {
    }

}