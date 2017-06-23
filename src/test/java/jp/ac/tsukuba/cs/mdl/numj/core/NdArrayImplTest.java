package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class NdArrayImplTest {

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
                ones.add(NumJ.arange(4, 1, 4))
        );
        assertEquals(
                new NdArrayImpl(new int[]{3, 4}, new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}),
                ones.add(NumJ.arange(4, 4, 1).transpose())
        );
    }

    @Test
    public void add1() throws Exception {
    }

    @Test
    public void sub() throws Exception {
    }

    @Test
    public void sub1() throws Exception {
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