package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.AtomicDoubleArray;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NdArrayImpl implements NdArray {

    protected AtomicDoubleArray data;
    protected int[] shape;
    protected int dim;


    protected int size;
    protected int[] columnStride;
    protected int[] rowStride;

    public NdArrayImpl(int[] shape, double[] data) {
        this(shape, new AtomicDoubleArray(data));
    }

    public NdArrayImpl(int[] shape, AtomicDoubleArray data) {
        this.shape = shape;
        this.data = data;
        dim = shape.length;
        size = size(shape);
        if (size != data.length()) {
            throw new IllegalArgumentException("shape={ " + Arrays.toString(shape) + "} data length=" + data.length());
        }
        this.columnStride = columnStride(dim, shape);
        this.rowStride = rowStride(dim, shape);
    }

    public int size(int[] shape) {
        return Arrays.stream(shape).reduce(1, (l, r) -> l * r);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int[] shape() {
        return shape;
    }

    @Override
    public int[] columnStride() {
        return columnStride;
    }

    @Override
    public int[] rowStride() {
        return rowStride;
    }

    @Override
    public AtomicDoubleArray data() {
        return data;
    }

    private static int[] columnStride(int dim, int[] shape) {
        int[] columnStride = new int[dim];
        columnStride[0] = shape[0];
        for (int i = 1; i < dim; i++) {
            columnStride[i] = columnStride[i - 1] * shape[i];
        }
        return columnStride;
    }

    private static int[] rowStride(int dim, int[] shape) {
        int[] rowStride = new int[dim];
        rowStride[dim - 1] = 1;
        for (int i = dim - 2; i >= 0; i--) {
            rowStride[i] = rowStride[i + 1] * shape[i];
        }
        return rowStride;
    }

    @Override
    public NdArray copy() {
        return new NdArrayImpl(Arrays.copyOf(shape, shape.length),
                IntStream.range(0, size).parallel().mapToDouble(i -> data.get(i)).toArray());
    }

    @Override
    public NdArray copyShape() {
        int size = size(shape);
        return new NdArrayImpl(Arrays.copyOf(shape, shape.length), new double[size]);
    }

    @Override
    public NdArray add(NdArray other) {
        return NumJ.elementwise(this, other, (l, r) -> l + r);
    }

    @Override
    public NdArray add(Number other) {
        return NumJ.elementwise(this, other, (l, r) -> l * r);
    }

    @Override
    public NdArray sub(NdArray other) {
        return NumJ.elementwise(this, other, (l, r) -> l - r);
    }

    @Override
    public NdArray sub(Number other) {
        return NumJ.elementwise(this, other, (l, r) -> l - r);
    }

    @Override
    public boolean checkDemensions(NdArray other) {
        return IntStream.range(0, dim)
                .mapToObj(i -> shape[i] == other.shape()[i] || other.shape()[i] == 1)
                .reduce((l, r) -> l && r).orElseGet(() -> false);
    }

    @Override
    public int columns() {
        if (this.isMatrix()) return shape[0];
        else return -1;
    }

    @Override
    public int rows() {
        if (isMatrix()) return shape[1];
        else return -1;
    }

    @Override
    public NdArray cond(Predicate<Double> condition) {
        return NumJ.elementwise(this, e -> condition.test(e) ? 1. : 0.);
    }

    @Override
    public NdArray comsum(int dimension) {
        throw new NotImplementedException("comsum is not implemented");
    }

    @Override
    public double distance1(NdArray other) {
        return IntStream.range(0, size).parallel()
                .mapToDouble(i -> Math.abs(data.get(i)))
                .sum();
    }

    @Override
    public double distance2(NdArray other) {
        return Math.abs(
                IntStream.range(0, size).parallel()
                        .mapToDouble(i -> Math.pow(data.get(i), 2))
                        .sum());
    }

    @Override
    public NdArray div(NdArray other) {
        return NumJ.elementwise(this, other, (l, r) -> r == 0 ? Double.POSITIVE_INFINITY : l / r);
    }

    @Override
    public NdArray div(Number other) {
        return NumJ.elementwise(this, other, (l, r) -> r == 0 ? Double.POSITIVE_INFINITY : l / r);
    }

    public Pair<Integer[], Integer[]> ndIndex2index(NdIndex[] indices){
        List<List<Integer>> indexSet = Lists.newArrayList();
        Integer[] shape = new Integer[dim];
        for(int i=0;i<indices.length;i++){
            List<Integer> nIndex = Lists.newArrayList();
            if(indices[i] instanceof NdIndexAll){
                nIndex = IntStream.range(0,shape[i]).boxed().collect(Collectors.toList());
            }else{
                nIndex.addAll(indices[i].indexes());
            }
            shape[i] = nIndex.size();
            indexSet.add(nIndex);
        }
        List<List<Integer>> cartesianProduct = Lists.cartesianProduct(indexSet);
        Integer[] idx = cartesianProduct.stream().map(e->index(Ints.toArray(e))).toArray(Integer[]::new);
        return Pair.of(shape, idx);
    }

    @Override
    public NdArray get(NdIndex... indices) {
        if (indices.length != dim) {
            throw new IllegalArgumentException("indexes size does not match dimension");
        }
        Pair<Integer[], Integer[]> shapeIndex = ndIndex2index(indices);
        Integer[] shape = shapeIndex.getLeft();
        Integer[] idx = shapeIndex.getRight();
        double[] array = new double[idx.length];
        for(int i=0;i<idx.length;i++){
                array[i] = data.get(idx[i]);
        }
        return new NdArrayImpl(ArrayUtils.toPrimitive(shape), array);
    }

    @Override
    public NdArray getColumn(int column) {
        if (isMatrix())
            return null;
        return null;
    }

    @Override
    public NdArray getColumns(int... columns) {
        return null;
    }

    @Override
    public NdArray getRow(int row) {
        return null;
    }

    @Override
    public NdArray getRows(int... rows) {
        return null;
    }

    @Override
    public boolean getBoolean(int i) {
        return false;
    }

    @Override
    public boolean getBoolean(int i, int j) {
        return false;
    }

    @Override
    public boolean getBoolean(int... indexes) {
        return false;
    }

    @Override
    public NdArray eq(NdArray other) {
        return null;
    }

    @Override
    public NdArray eq(Number other) {
        return null;
    }

    @Override
    public NdArray gt(NdArray other) {
        return null;
    }

    @Override
    public NdArray gt(Number other) {
        return null;
    }

    @Override
    public NdArray gte(NdArray other) {
        return null;
    }

    @Override
    public NdArray gte(Number other) {
        return null;
    }

    @Override
    public int index(int i, int j) {
        return index(new int[]{i, j});
    }

    @Override
    public int[] indexes(int index) {
        int[] result = new int[dim];
        for (int i = 0; i < dim; i++) {
            result[i] = index / rowStride[i];
            index %= rowStride[i];
        }
        return result;
    }

    @Override
    public int index(int[] n) {
        return IntStream.range(0, dim).map(i -> rowStride[i] * n[i]).sum();
    }

    @Override
    public boolean isVector() {
        return false;
    }

    @Override
    public boolean isMatrix() {
        return false;
    }

    @Override
    public NdArray tensorAlongDimension(int index, int... dimension) {
        return null;
    }

    @Override
    public int tensorssAlongDimension(int dimension) {
        return 0;
    }

    @Override
    public NdArray vectorAlongDimension(int index, int dimension) {
        return null;
    }

    @Override
    public int vectorAlongDimension(int dimension) {
        return 0;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public NdArray lt(NdArray other) {
        return null;
    }

    @Override
    public NdArray lt(Number other) {
        return null;
    }

    @Override
    public NdArray lte(NdArray other) {
        return null;
    }

    @Override
    public NdArray lte(Number other) {
        return null;
    }

    @Override
    public NdArray max(int... dimension) {
        return null;
    }

    @Override
    public Number maxNumber() {
        return null;
    }

    @Override
    public NdArray mean(int... dimension) {
        return null;
    }

    @Override
    public Number meanNumber() {
        return null;
    }

    @Override
    public NdArray min(int... dimension) {
        return null;
    }

    @Override
    public Number minNumber() {
        return null;
    }

    @Override
    public NdArray mmul(NdArray other) {
        return null;
    }

    @Override
    public NdArray mul(NdArray other) {
        return null;
    }

    @Override
    public NdArray mul(Number other) {
        return null;
    }

    @Override
    public NdArray neg() {
        return null;
    }

    @Override
    public NdArray neq(NdArray other) {
        return null;
    }

    @Override
    public NdArray neq(Number other) {
        return null;
    }

    @Override
    public NdArray norm1(int... dimension) {
        return null;
    }

    @Override
    public Number norm1() {
        return null;
    }

    @Override
    public NdArray norm2(int... dimension) {
        return null;
    }

    @Override
    public Number norm2() {
        return null;
    }

    @Override
    public NdArray permute(int... rearrange) {
        return null;
    }

    @Override
    public NdArray prod(int... dimension) {
        return null;
    }

    @Override
    public Number prodNumber() {
        return null;
    }

    @Override
    public int dim() {
        return 0;
    }

    @Override
    public void put(int i, double value) {

    }

    @Override
    public void put(int i, int j, double value) {

    }

    @Override
    public void put(int[] indexes, double value) {

    }

    @Override
    public void put(NdIndex[] indices, NdArray array){
        Pair<Integer[], Integer[]> shapeIndex = ndIndex2index(indices);
        Integer[] shape = shapeIndex.getLeft();
        Integer[] index = shapeIndex.getRight();
        if(!Arrays.deepEquals(shape, Arrays.stream(array.shape()).boxed().toArray())){
            throw new IllegalArgumentException("Indices does not match array");
        }
        IntStream.range(0, index.length).parallel().forEach(i->data.lazySet(index[i], array.data().get(i)));
    }

    @Override
    public NdArray ravel() {
        return null;
    }

    @Override
    public NdArray slice(int i) {
        return null;
    }

    @Override
    public NdArray slice(int i, int dimension) {
        return null;
    }

    @Override
    public int slices() {
        return 0;
    }

    @Override
    public int[] stride() {
        return new int[0];
    }

    @Override
    public int stride(int dimension) {
        return 0;
    }

    @Override
    public NdArray sum(int... dimension) {
        return null;
    }

    @Override
    public Number sumNumber() {
        return null;
    }

    @Override
    public NdArray transpose() {
        return null;
    }

    @Override
    public NdArray transposei() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NdArrayImpl ndArray = (NdArrayImpl) o;

        if (dim != ndArray.dim) return false;
        if (size != ndArray.size) return false;
        if (!Arrays.equals(shape, ndArray.shape)) return false;
        if (!data.toString().equals(ndArray.data.toString())) return false;
        if (!Arrays.equals(columnStride, ndArray.columnStride)) return false;
        return Arrays.equals(rowStride, ndArray.rowStride);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(shape);
        result = 31 * result + data.hashCode();
        result = 31 * result + dim;
        result = 31 * result + size;
        result = 31 * result + Arrays.hashCode(columnStride);
        result = 31 * result + Arrays.hashCode(rowStride);
        return result;
    }

    @Override
    public String toString() {
        return "{" + data + '}';
    }

}
