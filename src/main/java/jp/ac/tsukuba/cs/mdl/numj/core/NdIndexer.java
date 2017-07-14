package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NdIndexer {

    private int[] pointers;

    private int dim;

    private int size;

    private int[] shape;

    private int[] stride;

    public static int computeSize(int[] shape) {
        return Arrays.stream(shape).reduce((l, r) -> l * r).orElse(0);
    }

    public NdIndexer(int[] shape) {

        dim = shape.length;

        this.shape = shape;

        stride = createStride(shape);

        size = computeSize(shape);

        this.pointers = IntStream.range(0, size).toArray();
    }

    public NdIndexer(int[] shape, int[] pointers) {

        dim = shape.length;

        this.shape = shape;

        this.stride = createStride(shape);

        size = computeSize(shape);

        this.pointers = pointers;
    }

    public NdIndexer transpose(int... permute) {
        int[] newPointers = new int[size];
        int[] newShape = new int[dim];
        for (int i = 0; i < dim; i++) {
            newShape[i] = shape[permute[i]];
        }
        int[] newStride = createStride(newShape);
        for (int i = 0; i < size; i++) {
            int[] coordinate = coordinate(i);
            newPointers[IntStream.range(0, dim).map(j -> coordinate[permute[j]] * newStride[j]).sum()] = pointers[i];
        }
        return new NdIndexer(newShape, newPointers);
    }

    public NdIndexer reshape(int... shape) {
        return new NdIndexer(shape, pointers);
    }

    private static int[] createStride(int... shape) {
        int[] stride = new int[shape.length];
        stride[shape.length - 1] = 1;
        for (int i = shape.length - 2; i >= 0; i--) {
            stride[i] = stride[i + 1] * shape[i + 1];
        }
        return stride;
    }

    public int pointer(int[] coordinate) {
        if (dim != coordinate.length) {
            throw new IllegalArgumentException("Dimension not match " + dim + " != " + coordinate.length);
        }
        int index = 0;
        for (int i = 0; i < dim; i++) {
            index += coordinate[i] * stride[i];
        }
        return pointers[index];
    }

    public int broadcastPointer(int[] coordinate) {
        int index = 0;
        for (int i = 0; i < dim; i++) {
            index += (coordinate[i] % shape[i]) * stride[i];
        }
        return pointers[index];
    }

    /**
     * pointer to index O(log N)
     * slow!
     *
     * @param pointer: ポインタ．
     * @return インデックス
     */
    public int pointerToIndex(int pointer) {
        return Arrays.binarySearch(pointers, pointer);
    }

    public int indexToPointer(int index) {
        return pointers[index];
    }

    public int[] coordinate(int index) {
        int[] result = new int[dim];
        for (int i = 0; i < dim; i++) {
            result[i] = index / stride[i];
            index %= stride[i];
        }
        return result;
    }

    public int[] getPointers() {
        return pointers;
    }

    public NdIndexer slice(NdIndex[] indices) {
        if (indices.length != dim) {
            throw new IllegalArgumentException();
        }

        int[] shape = new int[dim];
        List<List<Integer>> lst = Lists.newArrayList();
        for (int i = 0; i < dim; i++) {
            List<Integer> l = Lists.newArrayList();
            if (indices[i] instanceof NdIndexPoint) {
                l.add(((NdIndexPoint) indices[i]).getPoint());
            } else if (indices[i] instanceof NdIndexInterval) {
                NdIndexInterval interval = (NdIndexInterval) indices[i];
                for (int j = interval.getStart(); j < interval.getEnd(); j++) {
                    l.add(j);
                }
            } else {
                for (int j = 0; j < this.shape[i]; j++) {
                    indices[i].map(j).ifPresent(l::add);
                }
            }
            shape[i] = l.size();
            lst.add(l);
        }

        List<Integer> newPointer = Lists.newArrayList();
        for (List<Integer> coordinate : Lists.cartesianProduct(lst)) {
            newPointer.add(pointer(Ints.toArray(coordinate)));
        }

        return new NdIndexer(shape, Ints.toArray(newPointer));
    }

    public int getDim() {
        return dim;
    }


    public int getSize() {
        return size;
    }

    public int[] getShape() {
        int[] shapeView = new int[dim];
        for (int i = 0; i < dim; i++) {
            shapeView[i] = shape[i];
        }
        return shapeView;
    }

    public int[] getStride() {
        return stride;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NdIndexer indexer = (NdIndexer) o;

        if (dim != indexer.dim) return false;
        if (size != indexer.size) return false;
        if (!Arrays.equals(pointers, indexer.pointers)) return false;
        if (!Arrays.equals(shape, indexer.shape)) return false;
        return Arrays.equals(stride, indexer.stride);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(pointers);
        result = 31 * result + dim;
        result = 31 * result + size;
        result = 31 * result + Arrays.hashCode(shape);
        result = 31 * result + Arrays.hashCode(stride);
        return result;
    }
}
