package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NdArrayImplTest {
    @Test
    public void dot() throws Exception {
        NdArray arange1 = NumJ.arange(3, 4);
        NdArray arange2 = NumJ.arange(4, 2);
        assertArrayEquals(new int[]{3, 2}, arange1.dot(arange2).shape());
        assertEquals(NumJ.create(new double[]{28, 34, 76, 98, 124, 162}, new int[]{3, 2}), arange1.dot(arange2));
        NdArray vec = NumJ.arange(4);
        assertEquals(1, arange1.dot(vec).dim());
        assertArrayEquals(new int[]{3}, arange1.dot(vec).shape());
        assertEquals(NumJ.create(new double[]{14, 38, 62}, new int[]{3}), arange1.dot(vec));
    }

    @Test
    public void dotN3() {
        // O(N^3) if in 2 seconds
        assertArrayEquals(new int[]{100, 100}, NumJ.arange(100, 100).dot(NumJ.arange(100, 100)).shape());
    }

    @Test
    public void argmax() throws Exception {
        assertEquals(Integer.valueOf(5), NumJ.arange(3, 2).argmax());
        assertEquals(NumJ.ones(3), NumJ.arange(3, 2).argmax(1));
    }

    @Test
    public void elementwise() throws Exception {
        assertEquals(
                NumJ.ones(5, 6).div(10).elementwise(Math::log),
                NumJ.ones(50, 60).div(10).slice(
                        new NdIndex[]{
                                NdSlice.interval(0, 5),
                                NdSlice.interval(10, 16)
                        }
                ).elementwise(Math::log));
    }

    @Test
    public void max() throws Exception {
        assertEquals(8., NumJ.arange(3, 3).max(), 0.0000001);
        assertEquals(NumJ.create(new double[]{3, 7, 11}, 3), NumJ.arange(3, 4).max(1));
        assertEquals(NumJ.create(new double[]{8, 9, 10, 11}, 3), NumJ.arange(3, 4).max(0));
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
                NumJ.create(new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, 3, 4),
                ones.add(NumJ.arange(1, 4))
        );
        assertEquals(
                NumJ.create(new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, 3, 4),
                ones.add(NumJ.arange(4, 1).transpose())
        );
        assertEquals(NumJ.create(new double[]{0, 2, 4, 6, 8, 10}, new int[]{2, 3}), NumJ.arange(2, 3).add(NumJ.arange(2, 3)));
        assertEquals(NumJ.create(new double[]{1, 2, 3, 4, 5, 6}, 2, 3), NumJ.arange(2, 3).add(1));

        assertEquals(
                NumJ.create(new double[]{1, 2, 3, 4, 5, 6}, 2, 3),
                NumJ.arange(3, 3)
                        .sub(3)
                        .slice(new NdIndex[]{
                                new NdIndexInterval(1, 3),
                                new NdIndexAll()
                        }).add(1));
    }

    @Test
    public void addN() throws Exception {
        assertArrayEquals(new int[]{1000, 1000}, NumJ.arange(1000, 1000).add(NumJ.arange(1000, 1000)).shape());
    }

    @Test
    public void sub() throws Exception {
        assertEquals(NumJ.create(new double[]{-1, 0, 1, 2, 3, 4}, 2, 3), NumJ.arange(2, 3).sub(NumJ.ones(2, 3)));
        assertEquals(NumJ.create(new double[]{-1, 0, 1, 2, 3, 4}, 2, 3), NumJ.arange(2, 3).sub(1));
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
        for (int i = 0; i < 12; i++) {
            assertEquals(i, NumJ.arange(3, 4).get(new int[]{i / 4, i % 4}), 1e-5);
        }
        NdArray a = NumJ.arange(6000);
        for (int i = 0; i < 6000; i++) {
            assertEquals(i, a.get(new int[]{i}), 1e-5);
        }
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
        NdArray zero = NumJ.zeros(3, 4);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                zero.put(new int[]{i, j}, 1);
            }
        }
        assertEquals(NumJ.ones(3, 4), zero);
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