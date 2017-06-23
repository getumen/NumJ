package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class NdIndexer {

    private int[] pointers;

    private int dim;

    private int size;

    private int[] shape;

    private int[] stride;

    public static int computeSize(int[] shape) {
        return Arrays.stream(shape).reduce((l,r)->l*r).orElse(0);
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

        stride = createStride(shape);

        size = pointers.length;

        this.pointers = pointers;
    }


    private static int[] createStride(int... shape) {
        int[] stride = new int[shape.length];
        stride[shape.length - 1] = 1;
        for (int i = shape.length - 2; i >= 0; i--) {
            stride[i] = stride[i + 1] * shape[i+1];
        }
        return stride;
    }

    public int pointer(int[] coordinate) {
        return pointers[IntStream.range(0, dim).map(i -> coordinate[i] * stride[i]).sum()];
    }

    public int pointerIndex(int pointer){
        return Arrays.binarySearch(pointers, pointer);
    }

    public int[] coordinate(int index) {
        int[] result = new int[dim];
        for (int i = 0; i < dim; i++) {
            result[i] = index / stride[i];
            index %= stride[i];
        }
        return result;
    }

    public int[] pointers() {
        return pointers;
    }

    public NdIndexer slice(NdIndex[] indices) {
        if (indices.length != dim) {
            throw new IllegalArgumentException();
        }

        int[] shape = new int[dim];
        List<List<Integer>> lst = Lists.newArrayList();
        for (int i = 0; i < this.shape.length; i++) {
            List<Integer> l = Lists.newArrayList();
            for (int j = 0; j < this.shape[i]; j++) {
                indices[i].map(j).ifPresent(l::add);
            }
            shape[i] = l.size();
            lst.add(l);
        }

        List<Integer> newPointer = Lists.newArrayList();
        for (List<Integer> cords : Lists.cartesianProduct(lst)) {
            newPointer.add(pointers[pointer(Ints.toArray(cords))]);
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
        return shape;
    }


    public int[] getStride() {
        return stride;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NdIndexer ndIndexer = (NdIndexer) o;

        if (dim != ndIndexer.dim) return false;
        if (size != ndIndexer.size) return false;
        if (!Arrays.equals(pointers, ndIndexer.pointers)) return false;
        if (!Arrays.equals(shape, ndIndexer.shape)) return false;
        return Arrays.equals(stride, ndIndexer.stride);
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
