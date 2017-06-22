package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.IntStream;

public class NdArrayImpl implements NdArray {

    protected AtomicDoubleArray data;

    protected NdIndexer iterator;

    public NdArrayImpl(int[] shape, AtomicDoubleArray data) {
        this.data = data;
        this.iterator = new NdIndexer(shape);
    }

    public NdArrayImpl(int[] shape, double[] data) {
        this(shape, new AtomicDoubleArray(data));
    }

    public NdArrayImpl(NdIndexer iterator, AtomicDoubleArray data) {
        this.data = data;
        this.iterator = iterator;
    }

    public NdArray elementwise(NdArray other, BinaryOperator<Double> op) {

        this.checkDemensions(other);

        IntStream
                .range(0, size())
                .parallel()
                .mapToObj(i -> iterator.coordinate(i))
                .forEach(
                        cords ->
                                data.lazySet(
                                        iterator.pointer(cords),
                                        op.apply(data.get(iterator.pointer(cords)), other.get(cords))
                                )
                );
        return this;
    }

    public NdArray elementwise(Number value, BinaryOperator<Double> op) {
        IntStream
                .range(0, size())
                .parallel()
                .mapToObj(i -> iterator.coordinate(i))
                .forEach(cords ->
                        data.set(iterator.pointer(cords), op.apply(data.get(iterator.pointer(cords)), value.doubleValue())));
        return this;
    }

    public NdArray elementwise(Function<Double, Double> op) {
        IntStream.range(0, size())
                .parallel()
                .mapToObj(i -> iterator.coordinate(i))
                .forEach(cords -> data.lazySet(iterator.pointer(cords), op.apply(data.get(iterator.pointer(cords)))));
        return this;
    }

    @Override
    public int size() {
        return iterator.getSize();
    }

    @Override
    public int[] shape() {
        return iterator.getShape();
    }

    @Override
    public AtomicDoubleArray data() {
        return data;
    }

    @Override
    public NdArray copy() {
        return new NdArrayImpl(
                iterator.getShape(),
                IntStream
                        .range(0, size())
                        .parallel()
                        .mapToDouble(i -> data.get(i))
                        .toArray());
    }

    @Override
    public NdArray add(NdArray other) {
        return elementwise(other, (l, r) -> l + r);
    }

    @Override
    public NdArray add(Number other) {
        return elementwise(other, (l, r) -> l * r);
    }

    @Override
    public NdArray sub(NdArray other) {
        return elementwise(other, (l, r) -> l - r);
    }

    @Override
    public NdArray sub(Number other) {
        return elementwise(other, (l, r) -> l - r);
    }

    @Override
    public boolean checkDemensions(NdArray other) {
        return IntStream
                .range(0, dim())
                .mapToObj(i -> shape()[i] == other.shape()[i] || other.shape()[i] == 1)
                .reduce((l, r) -> l && r).orElseGet(() -> false);
    }

    @Override
    public NdArray div(NdArray other) {
        return elementwise(other, (l, r) -> r == 0 ? Double.POSITIVE_INFINITY : l / r);
    }

    @Override
    public NdArray div(Number other) {
        return elementwise(other, (l, r) -> r == 0 ? Double.POSITIVE_INFINITY : l / r);
    }

    @Override
    public NdArray get(NdIndex... indices) {
        if (indices.length != dim()) {
            throw new IllegalArgumentException("indexes size does not match dimension");
        }
        return new NdArrayImpl(this.iterator.slice(indices), data);
    }

    @Override
    public double get(int... cords) {
        return data.get(iterator.pointer(cords));
    }

    @Override
    public NdArray mul(NdArray other) {
        return elementwise(other, (l, r) -> l * r);
    }

    @Override
    public NdArray mul(Number other) {
        return elementwise(other, (l, r) -> l * r);
    }

    @Override
    public NdArray reshape(int... rearrange) {
        if (size() != NdIndexer.computeSize(rearrange)) {
            throw new IllegalArgumentException();
        }
        return new NdArrayImpl(rearrange, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NdArrayImpl ndArray = (NdArrayImpl) o;

        if (!data.toString().equals(ndArray.data.toString())) return false;
        return iterator.equals(ndArray.iterator);
    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + iterator.hashCode();
        return result;
    }

    @Override
    public int dim() {
        return iterator.getDim();
    }

    @Override
    public void put(int[] cords, double value) {
        data.set(iterator.pointer(cords), value);
    }

    @Override
    public void put(NdIndex[] indices, NdArray array) {
        NdIndexer iterator = this.iterator.slice(indices);
        IntStream
                .range(0, iterator.getSize())
                .parallel()
                .mapToObj(iterator::coordinate)
                .forEach(cords -> data.lazySet(iterator.pointer(cords), array.get(cords)));
    }

    @Override
    public NdArray slice(NdIndex[] indices) {
        return null;
    }

    @Override
    public NdArray transpose() {
        return null;
    }

    @Override
    public String toString() {
        return "{" + data + '}';
    }

}
