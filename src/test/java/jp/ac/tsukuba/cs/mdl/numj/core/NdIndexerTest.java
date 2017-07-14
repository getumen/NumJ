package jp.ac.tsukuba.cs.mdl.numj.core;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NdIndexerTest {

    @Test
    public void composite() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{10, 20, 30, 40});
        NdIndexer transposed = indexer.transpose(2, 0, 3, 1);
        assertEquals(
                indexer.pointer(new int[]{9, 19, 19, 20}),
                transposed.pointer(new int[]{19, 9, 20, 19})
        );
        NdIndexer sliced = transposed.slice(new NdIndex[]{
                new NdIndexInterval(10, 20),
                new NdIndexAll(),
                new NdIndexSet(2, 4, 6, 8, 10, 12, 14, 16, 18, 20),
                new NdIndexAll()
        });
        assertEquals(
                indexer.pointer(new int[]{9, 19, 19, 20}),
                sliced.pointer(new int[]{9, 9, 9, 19})
        );
        assertArrayEquals(new int[]{10, 10, 10, 20}, sliced.getShape());
        NdIndexer reshaped = sliced.reshape(20000);
        assertEquals(
<<<<<<< HEAD
                indexer.pointer(new int[]{9, 19, 19, 20}),
                reshaped.pointer(new int[]{19999})
=======
                indexer.pointer(new int[]{9, 19, 19, 20 }),
                reshaped.pointer(new int[]{19999}) // java.lang.IllegalArgumentException at NdIndexerTest.java:34
>>>>>>> ba81edb57f315a93ef82b4e85a8951dc3c79884e
        );
        NdIndexer tmp = reshaped.reshape(10, 10, 10, 20).transpose(1, 3, 0, 2);
        assertEquals(
                indexer.pointer(new int[]{0, 0, 10, 2}),
                tmp.pointer(new int[]{0, 0, 0, 0})
        );

    }

    @Test
    public void pointer() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{3, 4, 5});
        assertEquals(0, indexer.pointer(new int[]{0, 0, 0}));
        assertEquals(5, indexer.pointer(new int[]{0, 1, 0}));
        assertEquals(20, indexer.pointer(new int[]{1, 0, 0}));
    }

    @Test
    public void coordinate() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{3, 4, 5});
        assertArrayEquals(new int[]{0, 1, 1}, indexer.coordinate(6));
        assertArrayEquals(new int[]{2, 3, 4}, indexer.coordinate(3 * 4 * 5 - 1));
        assertArrayEquals(new int[]{0, 0, 0}, indexer.coordinate(0));
    }

    @Test
    public void pointers() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{3, 4, 5});
        assertArrayEquals(IntStream.range(0, 3 * 4 * 5).toArray(), indexer.getPointers());
    }

    @Test
    public void slice() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});

        /*
        Index All Test
         */
        NdIndexer sliceAll = indexer.slice(new NdIndex[]{
                new NdIndexAll(),
                new NdIndexAll(),
                new NdIndexAll()
        });
        assertEquals(5 * 6 * 7, sliceAll.getSize());
        assertEquals(3, sliceAll.getDim());
        assertArrayEquals(new int[]{5, 6, 7}, sliceAll.getShape());
        assertEquals(
                indexer.pointer(new int[]{2, 2, 3}),
                sliceAll.pointer(new int[]{2, 2, 3})
        );
        assertArrayEquals(indexer.coordinate(100), sliceAll.coordinate(100));

        /*
        Index Interval
         */
        NdIndexer sliceInterval = indexer.slice(new NdIndex[]{
                new NdIndexInterval(1, 3),
                new NdIndexInterval(1, 4),
                new NdIndexInterval(1, 5)
        });
        assertEquals(2 * 3 * 4, sliceInterval.getPointers().length);
        assertEquals(2 * 3 * 4, sliceInterval.getSize());
        assertEquals(3, sliceInterval.getDim());
        assertArrayEquals(new int[]{2, 3, 4}, sliceInterval.getShape());
        assertEquals(
                indexer.pointer(new int[]{2, 3, 4}),
                sliceInterval.pointer(new int[]{1, 2, 3})
        );
        assertArrayEquals(
                new int[]{1, 2, 3},
                sliceInterval.coordinate(
                        sliceInterval.pointerIndex(
                                sliceInterval.pointer(new int[]{1, 2, 3})
                        )
                )
        );

        /*
        Index Set
         */
        NdIndexer sliceSet = indexer.slice(new NdIndex[]{
                new NdIndexSet(1, 3),
                new NdIndexSet(1, 3, 5),
                new NdIndexSet(1, 2, 4, 6)
        });
        assertEquals(2 * 3 * 4, sliceSet.getSize());
        assertEquals(3, sliceSet.getDim());
        assertArrayEquals(new int[]{2, 3, 4}, sliceSet.getShape());
        assertEquals(
                indexer.pointer(new int[]{3, 3, 4}),
                sliceSet.pointer(new int[]{1, 1, 2})
        );
        assertArrayEquals(
                new int[]{1, 2, 3},
                sliceSet.coordinate(
                        sliceSet.pointerIndex(
                                sliceSet.pointer(new int[]{1, 2, 3})
                        )
                )
        );

        /*
        point
         */
        NdIndexer point = indexer.slice(new NdIndex[]{
                new NdIndexPoint(3),
                new NdIndexPoint(4),
                new NdIndexPoint(5),
        });
        assertEquals(1, point.getSize());

        NdIndexer sliceSetSet = sliceSet.slice(
                new NdIndex[]{
                        new NdIndexAll(),
                        new NdIndexInterval(1, 2),
                        new NdIndexSet(2, 3)
                }
        );
        assertEquals(
                indexer.pointer(new int[]{3, 3, 4}),
                sliceSetSet.pointer(new int[]{1, 0, 0})
        );
    }

    @Test
    public void transpose() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});
        NdIndexer transposed = indexer.transpose(2, 0, 1);
        assertArrayEquals(new int[]{7, 5, 6}, transposed.getShape());
        assertEquals(
                indexer.pointer(new int[]{3, 4, 5}),
                transposed.pointer(new int[]{5, 3, 4})
        );
        assertEquals(
                indexer.pointer(new int[]{3, 4, 5}),
                indexer
                        .transpose(1, 2, 0)
                        .transpose(1, 2, 0)
                        .transpose(1, 2, 0)
                        .pointer(new int[]{3, 4, 5})

        );
    }

    @Test
    public void reshape() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});
        assertArrayEquals(new int[]{70, 3, 1}, indexer.reshape(70, 3, 1).getShape());
        assertArrayEquals(new int[]{5, 6, 7}, indexer.getShape());
        NdIndexer reshaped = indexer.reshape(7, 10, 3);
        assertEquals(indexer.pointer(new int[]{0, 0, 0}), reshaped.pointer(new int[]{0, 0, 0}));
        assertEquals(indexer.pointer(new int[]{4, 5, 6}), reshaped.pointer(new int[]{6, 9, 2}));
        assertEquals(
                indexer.pointer(new int[]{4, 5, 6}),
                indexer
                        .reshape(210, 1, 1)
                        .reshape(15, 2, 7)
                        .reshape(5, 6, 7)
                        .pointer(new int[]{4, 5, 6})
        );
    }

    @Test
    public void getDim() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});
        assertEquals(3, indexer.getDim());
    }

    @Test
    public void getSize() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});
        assertEquals(210, indexer.getSize());
    }

    @Test
    public void getShape() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});
        assertArrayEquals(new int[]{5, 6, 7}, indexer.getShape());
    }

    @Test
    public void getStride() throws Exception {
        NdIndexer indexer = new NdIndexer(new int[]{5, 6, 7});
        assertArrayEquals(new int[]{42, 7, 1}, indexer.getStride());
    }

}