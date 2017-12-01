package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import javax.swing.plaf.SliderUI;
import java.util.Arrays;

import static org.junit.Assert.*;

public class NdArrayImplTest {
    @Test
    public void eq() throws Exception {
        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).eq(NumJ.arange(3, 4)));
        assertNotEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).eq(NumJ.ones(3, 4)));
    }

    @Test
    public void gt() throws Exception {
        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).add(1).gt(NumJ.arange(3, 4)));
        assertEquals(NumJ.zeros(3, 4), NumJ.arange(3, 4).sub(1).gt(NumJ.arange(3, 4)));
    }

    @Test
    public void lt() throws Exception {
        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).sub(1).lt(NumJ.arange(3, 4)));
        assertEquals(NumJ.zeros(3, 4), NumJ.arange(3, 4).add(1).lt(NumJ.arange(3, 4)));
    }

    @Test
    public void gte() throws Exception {
        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).add(1).gte(NumJ.arange(3, 4)));
        assertEquals(NumJ.zeros(3, 4), NumJ.arange(3, 4).sub(1).gte(NumJ.arange(3, 4)));
        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).gte(NumJ.arange(3, 4)));

    }

    @Test
    public void lte() throws Exception {

        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).sub(1).lte(NumJ.arange(3, 4)));
        assertEquals(NumJ.zeros(3, 4), NumJ.arange(3, 4).add(1).lte(NumJ.arange(3, 4)));
        assertEquals(NumJ.ones(3, 4), NumJ.arange(3, 4).lte(NumJ.arange(3, 4)));

    }

    @Test
    public void sum() throws Exception {
        assertEquals(Double.valueOf(15), NumJ.arange(6).sum());
        assertEquals(NumJ.create(new double[]{3, 5, 7}, 2), NumJ.arange(2, 3).sum(0));
        assertEquals(NumJ.create(new double[]{3, 12}, 2), NumJ.arange(2, 3).sum(1));
    }

    @Test
    public void argmin() throws Exception {
        assertEquals(Integer.valueOf(0), NumJ.arange(3, 2).argmin());
        assertEquals(NumJ.zeros(2), NumJ.ones(3, 2).argmin(0));
    }

    @Test
    public void dot() throws Exception {
        NdArray arange1 = NumJ.arange(3, 4);
        NdArray arange2 = NumJ.arange(4, 2);
        assertArrayEquals(new int[]{3, 2}, arange1.dot(arange2).shape());
        assertEquals(NumJ.create(new double[]{28, 34, 76, 98, 124, 162}, 3, 2), arange1.dot(arange2));
        NdArray vec = NumJ.arange(4);
        assertEquals(1, arange1.dot(vec).dim());
        assertArrayEquals(new int[]{3}, arange1.dot(vec).shape());
        assertEquals(NumJ.create(new double[]{14, 38, 62}, 3), arange1.dot(vec));

        NdArray array1 = NumJ.arange(100, 100).slice(NdSlice.interval(10, 20), NdSlice.interval(20, 30));
        NdArray array2 = NumJ.arange(100, 100).slice(NdSlice.interval(10, 20), NdSlice.interval(20, 30));
        assertEquals(NumJ.create(new double[]{22434000, 22448700, 22463400, 22478100, 22492800, 22507500,
                22522200, 22536900, 22551600, 22566300,
                22448700, 22463410, 22478120, 22492830, 22507540, 22522250,
                22536960, 22551670, 22566380, 22581090,
                22463400, 22478120, 22492840, 22507560, 22522280, 22537000,
                22551720, 22566440, 22581160, 22595880,
                22478100, 22492830, 22507560, 22522290, 22537020, 22551750,
                22566480, 22581210, 22595940, 22610670,
                22492800, 22507540, 22522280, 22537020, 22551760, 22566500,
                22581240, 22595980, 22610720, 22625460,
                22507500, 22522250, 22537000, 22551750, 22566500, 22581250,
                22596000, 22610750, 22625500, 22640250,
                22522200, 22536960, 22551720, 22566480, 22581240, 22596000,
                22610760, 22625520, 22640280, 22655040,
                22536900, 22551670, 22566440, 22581210, 22595980, 22610750,
                22625520, 22640290, 22655060, 22669830,
                22551600, 22566380, 22581160, 22595940, 22610720, 22625500,
                22640280, 22655060, 22669840, 22684620,
                22566300, 22581090, 22595880, 22610670, 22625460, 22640250,
                22655040, 22669830, 22684620, 22699410}, 10, 10), array1.transpose().dot(array2));

        assertEquals(NumJ.create(new double[]{
                        80, 92, 104, 116,
                        92, 107, 122, 137,
                        104, 122, 140, 158,
                        116, 137, 158, 179}, 4, 4),
                NumJ.arange(3, 4).transpose().dot(NumJ.arange(3, 4)));

    }

    @Test
    public void dotN3() {
        // O(N^3) if in 2 seconds
        assertArrayEquals(new int[]{100, 100}, NumJ.arange(100, 100).dot(NumJ.arange(100, 100)).shape());
    }

    @Test
    public void dotN2() {
        // O(N^3) if in 2 seconds
        assertArrayEquals(new int[]{1000, 1000}, NumJ.arange(1000, 1000).dot(NumJ.arange(1000, 1000)).shape());
    }

    @Test
    public void argmax() throws Exception {
        assertEquals(Integer.valueOf(5), NumJ.arange(3, 2).argmax());
        assertEquals(NumJ.ones(3), NumJ.arange(3, 2).argmax(1));
        assertEquals(NumJ.ones(2).mul(2), NumJ.arange(3, 2).argmax(0));
    }

    @Test
    public void elementwise() throws Exception {
        assertEquals(
                NumJ.ones(5, 6).div(10).elementwise(Math::log),
                NumJ.ones(50, 60).div(10).slice(
                        NdSlice.interval(0, 5),
                        NdSlice.interval(10, 16)
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
        assertEquals(3 * 4 * 5, NumJ.ones(3, 5, 4).size());
        assertNotEquals(3 * 4, NumJ.ones(3, 4, 5).size());
        assertEquals(
                3 * 2 * 1,
                NumJ.ones(3, 4, 5)
                        .slice(
                                NdSlice.all(),
                                NdSlice.interval(1, 3),
                                NdSlice.point(2))
                        .size());

    }

    @Test
    public void shape() throws Exception {
        assertArrayEquals(new int[]{3, 4, 5}, NumJ.ones(3, 4, 5).shape());
        assertFalse(Arrays.equals(new int[]{3, 4, 5}, NumJ.ones(3, 4, 4).shape()));
        assertFalse(Arrays.equals(new int[]{3, 4, 5}, NumJ.ones(3, 20).shape()));
        assertArrayEquals(
                new int[]{3, 2, 1}, NumJ.ones(3, 4, 5)
                        .slice(
                                NdSlice.all(),
                                NdSlice.interval(1, 3),
                                NdSlice.point(2))
                .shape()
        );
    }

    @Test
    public void copy() throws Exception {
        NdArray a = NumJ.arange(100);
        NdArray b = a.copy();
        assertFalse(a == b);
        assertEquals(a, b);
        assertEquals(
                NumJ.create(new double[]{90, 91, 92, 93, 94, 95, 96, 97, 98, 99}, 10)
                , a.slice(NdSlice.interval(90, 100)).copy()
        );
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
        assertEquals(NumJ.create(new double[]{0, 2, 4, 6, 8, 10}, 2, 3), NumJ.arange(2, 3).add(NumJ.arange(2, 3)));
        assertEquals(NumJ.create(new double[]{1, 2, 3, 4, 5, 6}, 2, 3), NumJ.arange(2, 3).add(1));

        assertEquals(
                NumJ.create(new double[]{1, 2, 3, 4, 5, 6}, 2, 3),
                NumJ.arange(3, 3)
                        .sub(3)
                        .slice(
                                new NdIndexInterval(1, 3),
                                new NdIndexAll()
                        ).add(1));

        NdArray array1 = NumJ.arange(100, 100).slice(NdSlice.interval(10, 20), NdSlice.interval(20, 30));
        NdArray array2 = NumJ.arange(100, 100).slice(NdSlice.interval(10, 20), NdSlice.interval(20, 30));
        assertEquals(NumJ.create(new double[]{
                2040, 2042, 2044, 2046, 2048, 2050, 2052, 2054, 2056, 2058,
                2240, 2242, 2244, 2246, 2248, 2250, 2252, 2254, 2256, 2258,
                2440, 2442, 2444, 2446, 2448, 2450, 2452, 2454, 2456, 2458,
                2640, 2642, 2644, 2646, 2648, 2650, 2652, 2654, 2656, 2658,
                2840, 2842, 2844, 2846, 2848, 2850, 2852, 2854, 2856, 2858,
                3040, 3042, 3044, 3046, 3048, 3050, 3052, 3054, 3056, 3058,
                3240, 3242, 3244, 3246, 3248, 3250, 3252, 3254, 3256, 3258,
                3440, 3442, 3444, 3446, 3448, 3450, 3452, 3454, 3456, 3458,
                3640, 3642, 3644, 3646, 3648, 3650, 3652, 3654, 3656, 3658,
                3840, 3842, 3844, 3846, 3848, 3850, 3852, 3854, 3856, 3858}, 10, 10), array1.add(array2));
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
    public void checkBroadcast() throws Exception {
        assertTrue(NumJ.create(new double[]{-1, 0, 1, 2, 3, 4}, 1, 3).checkBroadcast(NumJ.arange(3, 3)));
        assertFalse(NumJ.create(new double[]{-1, 0, 1, 2, 3, 4}, 2, 3).checkBroadcast(NumJ.arange(2, 3)));
    }

    @Test
    public void div() throws Exception {
        assertEquals(NumJ.ones(3, 4), NumJ.generator(() -> 4., 3, 4).div(4));
        assertEquals(NumJ.generator(() -> Double.POSITIVE_INFINITY, 3, 4), NumJ.generator(() -> 4., 3, 4).div(0));
    }

    @Test
    public void get() throws Exception {
        for (int i = 0; i < 12; i++) {
            assertEquals(i, NumJ.arange(3, 4).get(new int[]{i / 4, i % 4}), 1e-5);
        }

        assertEquals(5, NumJ.arange(3, 4, 5)
                .slice(NdSlice.all(), NdSlice.interval(1, 3), NdSlice.all())
                .get(new int[]{0, 0, 0}), 1e-5);

        assertEquals(50, NumJ.arange(3, 4, 5)
                .slice(NdSlice.all(), NdSlice.interval(1, 3), NdSlice.all())
                .get(new int[]{2, 1, 0}), 1e-5);

        NdArray a = NumJ.arange(6000);
        for (int i = 0; i < 6000; i++) {
            assertEquals(i, a.get(new int[]{i}), 1e-5);
            assertEquals(i, a.slice(NdSlice.point(i)).get(new int[]{0}), 1e-5);
            assertEquals(i, a.slice(NdSlice.point(i)).reshape(1, 1).get(new int[]{0, 0}), 1e-5);
        }

        assertEquals(NumJ.create(new double[]{5,7,9,11}, 2,2), NumJ.arange(3,4).get(
                NdSlice.interval(1,3),
                NdSlice.set(1,3)
        ));
    }

    @Test
    public void mul() throws Exception {
        assertEquals(NumJ.generator(()->3., 3,4), NumJ.ones(3,4).mul(3));
        assertEquals(NumJ.generator(()->1., 3,4), NumJ.ones(3,4).mul(NumJ.ones(3, 4)));
    }

    @Test
    public void reshape() throws Exception {
        assertEquals(NumJ.arange(3, 4), NumJ.arange(2, 6).reshape(3, 4));
    }

    @Test
    public void equals() throws Exception {
    }

    @Test
    public void dim() throws Exception {
    }

    @Test
    public void put() throws Exception {
        int c = 0;
        NdArray zero = NumJ.zeros(3, 4);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                zero.put(new int[]{i, j}, c++);
            }
        }
        assertEquals(NumJ.arange(3, 4), zero);

        c = 0;
        zero = NumJ.zeros(3, 4).reshape(4, 3);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                zero.put(new int[]{i, j}, c++);
            }
        }
        assertEquals(NumJ.arange(4, 3), zero);
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