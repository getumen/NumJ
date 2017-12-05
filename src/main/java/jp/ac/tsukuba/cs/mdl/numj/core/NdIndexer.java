package jp.ac.tsukuba.cs.mdl.numj.core;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * N次配列を１次配列にマッピングするクラス
 */
class NdIndexer {

    private int dim;

    private int size;

    private int[] shape;

    private int[] stride;

    private int[] permute;

    private int[][] dimPointer;

    private NdIndexer parent;

    NdIndexer(int[] shape) {
        this(null, shape);
    }

    NdIndexer(NdIndexer parent, int[] shape) {
        this(parent, shape, createStride(shape), IntStream.range(0, shape.length).toArray(), createNewDimPointer(shape));
    }

    NdIndexer(NdIndexer parent, int[] shape, int[] stride, int[] permute, int[][] dimPointer) {

        this.parent = parent;

        dim = shape.length;

        this.shape = shape;

        this.stride = stride;

        this.permute = permute;

        this.dimPointer = dimPointer;

        size = computeSize(shape);


    }

    void printState() {
        System.out.println(this);
    }

    static int computeSize(int[] shape) {
        return Arrays.stream(shape).reduce((l, r) -> l * r).orElse(0);
    }

    static int[][] createNewDimPointer(int[] shape) {
        int[][] newDimPointer = new int[shape.length][];
        for (int i = 0; i < shape.length; ++i) {
            newDimPointer[i] = IntStream.range(0, shape[i]).toArray();
        }
        return newDimPointer;
    }

    static int[] createStride(int... shape) {
        int[] stride = new int[shape.length];
        stride[shape.length - 1] = 1;
        for (int i = shape.length - 2; i >= 0; i--) {
            stride[i] = stride[i + 1] * shape[i + 1];
        }
        return stride;
    }

    NdIndexer transpose(int... permute) {
        int[] newPermute = new int[dim];
        for (int i = 0; i < dim; i++) {
            newPermute[i] = this.permute[permute[i]];
        }
        return new NdIndexer(parent, shape, stride, newPermute, dimPointer);
    }

    NdIndexer reshape(int... shape) {
        return new NdIndexer(this, shape);
    }

    @Override
    public String toString() {
        return "NdIndexer{" +
                "dim=" + dim +
                ", size=" + size +
                ", shape=" + Arrays.toString(shape) +
                ", stride=" + Arrays.toString(stride) +
                ", permute=" + Arrays.toString(permute) +
                ", dimPointer=" + Arrays.toString(dimPointer) +
                ", parent=" + parent +
                '}';
    }

    int pointer(int[] coordinate) {
        if (dim != coordinate.length) {
            throw new IllegalArgumentException("Dimension not match " + dim + " != " + coordinate.length);
        }
        int index = 0;
        if (parent != null) {
            return pointer(index(coordinate));
        }
        for (int i = 0; i < dim; i++) {
            index += dimPointer[permute[i]][coordinate[i]] * stride[permute[i]];
        }
        return index;
    }

    int index(int... coordinate) {
        int index = 0;
        int[] vStride = createStride(shape);
        for (int i = 0; i < dim; i++) {
            index += coordinate[permute[i]] * vStride[permute[i]];
        }
        return index;
    }

    Stream<Integer> stream() {
        return IntStream.range(0, size).map(this::pointer).boxed();
    }

    Stream<Integer> parallelStream() {
        return IntStream.range(0, size).parallel().map(this::pointer).boxed();
    }

    int pointer(int index) {
        if (parent == null) {
            return pointer(viewCoordinate(index));
        } else {
            return parent.pointer(index);
        }
    }

    int broadcastPointer(int[] coordinate) {
        int index = 0;

        for (int i = 0; i < dim; i++) {
            index += (dimPointer[permute[i]][coordinate[i]% shape[permute[i]]] ) * stride[permute[i]];
        }
        return index;
    }

    int[] viewCoordinate(int index) {
        int[] result = new int[dim];
        int[] vStride = createStride(shape);
        int[] inv = new int[dim];
        for (int i = 0; i < dim; i++) {
            inv[permute[i]] = i;
        }
        for (int i = 0; i < dim - 1; i++) {
            result[inv[i]] = index / vStride[i];
            index %= vStride[i];
        }
        result[inv[dim - 1]] = index;
        return result;
    }


    int[] coordinate(int index) {
        int[] result = new int[dim];
        for (int i = 0; i < dim; i++) {
            result[permute[i]] = index / stride[permute[i]];
            index %= stride[permute[i]];
        }
        return result;
    }

    NdIndexer slice(NdIndex... indices) {
        if (indices.length != dim) {
            throw new IllegalArgumentException();
        }

        int[] newShape = new int[dim];
        int[][] newDimPointer = new int[dim][];
        for (int j = 0; j < dim; j++) {
            final int i = j;
            final int cj = permute[j];
            if (indices[i] instanceof NdIndexPoint) {
                NdIndexPoint point = (NdIndexPoint) indices[i];
                newShape[cj] = 1;
                newDimPointer[cj] = new int[]{dimPointer[cj][point.getPoint()]};
            } else if (indices[i] instanceof NdIndexInterval) {
                NdIndexInterval interval = (NdIndexInterval) indices[i];
                newShape[cj] = interval.getEnd() - interval.getStart();
                newDimPointer[cj] = IntStream.range(interval.getStart(), interval.getEnd()).map(p -> dimPointer[cj][p]).toArray();
            } else if (indices[i] instanceof NdIndexAll) {
                newShape[cj] = shape[cj];
                newDimPointer[cj] = dimPointer[cj];
            } else if (indices[i] instanceof NdIndexSet) {
                NdIndexSet set = (NdIndexSet) indices[i];
                newShape[cj] = set.getSet().length;
                newDimPointer[cj] = Arrays.stream(set.getSet()).map(p -> dimPointer[cj][p]).toArray();
            }
        }

        return new NdIndexer(parent, newShape, stride, permute, newDimPointer);
    }

    int getDim() {
        return dim;
    }


    int getSize() {
        return size;
    }

    int[] getShape() {
        int[] v = new int[dim];
        for (int i = 0; i < dim; i++) {
            v[i] = shape[permute[i]];
        }
        return v;
    }

    int[] getStride() {
        return stride;
    }
}
