package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class NdIndexerTest {
    @Test
    public void pointer() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{3,4,5});
        assertEquals(0, indexer.pointer(new int[]{0,0,0}));
        assertEquals(5, indexer.pointer(new int[]{0,1,0}));
        assertEquals(20, indexer.pointer(new int[]{1,0,0}));
    }

    @Test
    public void coordinate() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{3,4,5});
        assertArrayEquals(new int[]{0,1,1}, indexer.coordinate(6));
        assertArrayEquals(new int[]{2,3,4}, indexer.coordinate(3*4*5-1));
        assertArrayEquals(new int[]{0,0,0}, indexer.coordinate(0));
    }

    @Test
    public void pointers() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{3,4,5});
        assertArrayEquals(IntStream.range(0, 3*4*5).toArray(), indexer.pointers());
    }

    @Test
    public void slice() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5,6,7});

        /*
        Index All Test
         */
        NdIndexer sliceAll = indexer.slice(new NdIndex[]{
                new NdIndexAll(),
                new NdIndexAll(),
                new NdIndexAll()
        });
        assertEquals(5*6*7, sliceAll.getSize());
        assertEquals(3, sliceAll.getDim());
        assertArrayEquals(new int[]{5,6,7}, sliceAll.getShape());
        assertEquals(
                indexer.pointer(new int[]{2,2,3}),
                sliceAll.pointer(new int[]{2,2,3})
        );
        assertArrayEquals(indexer.coordinate(100), sliceAll.coordinate(100));

        /*
        Index Interval
         */
        NdIndexer sliceInterval = indexer.slice(new NdIndex[]{
                new NdIndexInterval(1,3),
                new NdIndexInterval(1,4),
                new NdIndexInterval(1,5)
        });
        assertEquals(2*3*4, sliceInterval.pointers().length);
        assertEquals(2*3*4, sliceInterval.getSize());
        assertEquals(3, sliceInterval.getDim());
        assertArrayEquals(new int[]{2,3,4}, sliceInterval.getShape());
        assertEquals(
                indexer.pointer(new int[]{2,3,4}),
                sliceInterval.pointer(new int[]{1,2,3})
        );
        assertArrayEquals(
                new int[]{1,2,3},
                sliceInterval.coordinate(
                        sliceInterval.pointerIndex(
                                sliceInterval.pointer(new int[]{1,2,3})
                        )
                )
        );

        /*
        Index Set
         */
        NdIndexer sliceSet = indexer.slice(new NdIndex[]{
                new NdIndexSet(1, 3),
                new NdIndexSet(1,3,5),
                new NdIndexSet(1,2,4,6)
        });
        assertEquals(2*3*4, sliceSet.getSize());
        assertEquals(3, sliceSet.getDim());
        assertArrayEquals(new int[]{2,3,4}, sliceSet.getShape());
        assertEquals(
                indexer.pointer(new int[]{3,3,4}),
                sliceSet.pointer(new int[]{1,1,2})
        );
        assertArrayEquals(
                new int[]{1,2,3},
                sliceSet.coordinate(
                        sliceSet.pointerIndex(
                                sliceSet.pointer(new int[]{1,2,3})
                        )
                )
        );
    }

    @Test
    public void getDim() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5,6,7});
        assertEquals(3, indexer.getDim());
    }

    @Test
    public void getSize() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5,6,7});
        assertEquals(210, indexer.getSize());
    }

    @Test
    public void getShape() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5,6,7});
        assertArrayEquals(new int[]{5,6,7}, indexer.getShape());
    }

    @Test
    public void getStride() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5,6,7});
        assertArrayEquals(new int[]{42, 7, 1}, indexer.getStride());
    }

}