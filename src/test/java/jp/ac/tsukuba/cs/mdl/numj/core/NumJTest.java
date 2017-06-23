package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NumJTest {

    @Test
    public void ones() {
        assertEquals(NumJ.create(new double[]{1, 1, 1, 1}, new int[]{2, 2}), NumJ.ones(2, 2));
        assertNotEquals(NumJ.create(new double[]{1, 1, 1, 2}, new int[]{2, 2}), NumJ.ones(2, 2));
    }

    @Test
    public void zeros(){
        assertNotEquals(NumJ.create(new double[]{2,2,2,2}, 2,2), NumJ.zeros(2,2));
        assertEquals(NumJ.create(new double[]{0,0,0,0}, 2,2), NumJ.zeros(2,2));
    }

    @Test
    public void arange(){
        assertEquals(NumJ.create(new double[]{0,1,2,3},2,2), NumJ.arange(4, 2,2));
        System.out.println(NumJ.arange(9,3,3));
        System.out.println(NumJ.arange(16,2,2,2,2));
    }
}