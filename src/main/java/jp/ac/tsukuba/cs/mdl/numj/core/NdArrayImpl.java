package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.AtomicDoubleArray;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * NdArrayImplはNumpyのndarrayを元にした、NdArrayのImplementationである．
 * すべての要素は{@code double}型であるわされる．
 *
 * @author getumen
 * @since 0.0.1
 */
public class NdArrayImpl implements NdArray {

    protected AtomicDoubleArray data;

    protected NdIndexer iterator;


    public NdArrayImpl(int[] shape) {
        this.iterator = new NdIndexer(shape);
        this.data = new AtomicDoubleArray(iterator.getSize());
    }

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

    private static double div(double l, double r) {
        if (l == 0) {
            return 0;
        } else if (r == 0) {
            System.err.println("Divided by zero in div");
            return Float.POSITIVE_INFINITY;
        } else {
            return l / r;
        }
    }

    @Override
    public NdArray elementwise(NdArray other, BinaryOperator<Double> op) {

        boolean broadcast = this.checkBroadcast(other);

        if (broadcast) {
            int[] shape = shape();
            int[] otherShape = other.shape();
            int[] newShape = new int[dim()];
            for (int i = 0; i < dim(); i++) {
                newShape[i] = Math.max(shape[i], otherShape[i]);
            }
            NdIndexer indexer = new NdIndexer(newShape);
            AtomicDoubleArray data = new AtomicDoubleArray(
                    Arrays.stream(newShape).reduce((l, r) -> l * r).orElseThrow(RuntimeException::new));

            IntStream.range(0, indexer.getSize())
                    .parallel()
                    .mapToObj(indexer::coordinate)
                    .forEach(
                            coordinate ->
                                    data.lazySet(
                                            indexer.pointer(coordinate),
                                            op.apply(
                                                    this.broadcastGet(coordinate),
                                                    other.broadcastGet(coordinate)
                                            )
                                    )
                    );
            return new NdArrayImpl(indexer, data);
        } else {
            AtomicDoubleArray data = new AtomicDoubleArray(size());
            NdIndexer indexer = new NdIndexer(shape());
            IntStream
                    .range(0, size())
                    .parallel()
                    .forEach(
                            index ->
                                    data.lazySet(
                                            index,
                                            op.apply(this.get(index), other.get(index))
                                    )
                    );
            return new NdArrayImpl(indexer, data);
        }
    }

    @Override
    public NdArray elementwise(Number value, BinaryOperator<Double> op) {
        AtomicDoubleArray data = new AtomicDoubleArray(size());
        NdIndexer indexer = new NdIndexer(shape());
        IntStream
                .range(0, size())
                .parallel()
                .forEach(index ->
                        data.lazySet(
                                index,
                                op.apply(
                                        this.get(index),
                                        value.doubleValue()
                                )
                        )
                );
        return new NdArrayImpl(indexer, data);
    }

    @Override
    public NdArray elementwise(Function<Double, Double> op) {
        NdIndexer indexer = new NdIndexer(shape());
        AtomicDoubleArray data = new AtomicDoubleArray(size());
        IntStream.range(0, size())
                .parallel()
                .forEach(index ->
                        data.lazySet(
                                index,
                                op.apply(this.get(index))
                        )
                );
        return new NdArrayImpl(indexer, data);
    }

    @Override
    public NdArray elementwisei(Function<int[], Double> op) {
        IntStream.range(0, size())
                .parallel()
                .mapToObj(iterator::coordinate)
                .forEach(
                        coordinate ->
                                data.lazySet(
                                        iterator.pointer(coordinate),
                                        op.apply(coordinate)
                                )
                );
        return this;
    }

    @Override
    public Double axisOperation(BinaryOperator<Double> op) {
        return Arrays.stream(iterator.getPointers())
                .parallel()
                .mapToDouble(i -> data.get(i))
                .reduce(op::apply)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public Integer axisArgOperation(BinaryOperator<Pair<Integer, Double>> op) {
        return IntStream.range(0, size())
                .parallel()
                .mapToObj(i -> Pair.of(i, data.get(pointer(i))))
                .reduce(op)
                .map(Pair::getLeft)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public NdArray axisOperation(BinaryOperator<Double> op, int... axis) {
        int[] newShape;
        int[] shape = shape();
        int dim = dim();
        if (axis == null || axis.length == dim) {
            newShape = new int[]{1};
            return new NdArrayImpl(newShape, new double[]{axisOperation(op)});
        } else {
            newShape = new int[dim - axis.length];
            int p = 0;
            for (int i = 0; i < dim(); i++) {
                if (!Ints.contains(axis, i)) {
                    newShape[p++] = shape[i];
                }
            }
            NdArray result = new NdArrayImpl(newShape);
            result.elementwisei(coordinate -> {
                NdIndex[] indices = new NdIndex[dim];
                int cnt = 0;
                for (int i = 0; i < dim; i++) {
                    if (Ints.contains(axis, i)) {
                        indices[i] = new NdIndexAll();
                        cnt++;
                    } else {
                        indices[i] = new NdIndexPoint(coordinate[i - cnt]);
                    }
                }
                return this.slice(indices).axisOperation(op);
            });
            return result;
        }
    }

    @Override
    public NdArray axisArgOperation(BinaryOperator<Pair<Integer, Double>> op, int axis) {
        int[] newShape;
        int[] shape = shape();
        int dim = dim();

        newShape = new int[dim - 1];
        int p = 0;
        for (int i = 0; i < dim(); i++) {
            if (axis != i) {
                newShape[p++] = shape[i];
            }
        }
        NdArray result = new NdArrayImpl(newShape);
        result.elementwisei(coordinate -> {
            NdIndex[] indices = new NdIndex[dim];
            int pointer = 0;
            for (int i = 0; i < dim; i++) {
                if (axis == i) {
                    indices[i] = new NdIndexAll();
                } else {
                    indices[i] = new NdIndexPoint(coordinate[pointer++]);
                }
            }

            return Double.valueOf(this.slice(indices).axisArgOperation(op));
        });
        return result;

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
                        .mapToDouble(i -> data.get(iterator.indexToPointer(i)))
                        .toArray());
    }

    @Override
    public NdArray dot(NdArray other) {

        int[] shape = shape();
        int[] otherShape = other.shape();
        if (other.dim() == 1) {
            if (shape[shape.length - 1] != other.size()) {
                throw new NdArrayException(this, other);
            }
            NdArray result = new NdArrayImpl(Arrays.copyOfRange(shape, 0, shape.length - 1));
            result.elementwisei(coordinate -> {
                        final int index = IntStream.range(0, dim() - 1)
                                .map(i -> coordinate[i] * iterator.getStride()[i])
                                .sum();
                        return IntStream.range(0, shape[shape.length - 1])
                                .parallel()
                                .mapToDouble(i -> get(index + i) * other.get(i))
                                .sum();
                    }
            );
            return result;
        } else {
            if (shape[shape.length - 1] != otherShape[otherShape.length - 2]) {
                throw new NdArrayException(this, other);
            }
            int[] newShape = new int[shape.length + otherShape.length - 2];
            for (int i = 0; i < shape.length - 1; i++) {
                newShape[i] = shape[i];
            }
            for (int i = 0; i < otherShape.length - 2; i++) {
                newShape[i + shape.length - 1] = otherShape[i];
            }
            newShape[newShape.length - 1] = otherShape[otherShape.length - 1];
            NdArray result = new NdArrayImpl(newShape);
            int[] otherStride = NdIndexer.createStride(otherShape);
            result.elementwisei(coordinate -> {
                final int index = IntStream.range(0, dim() - 1)
                        .map(i -> coordinate[i] * iterator.getStride()[i])
                        .sum();
                final int otherIndex = IntStream.range(0, other.dim())
                        .filter(i -> i != other.dim() - 2)
                        .map(i -> otherStride[i] * coordinate[(i > other.dim() - 2 ? i - 1 : i) + shape.length - 1]).sum();

                double ret = 0;
                for (int i = 0; i < shape[shape.length - 1]; i++) {
                    ret += this.get(index + i) * other.get(otherStride[other.dim() - 2] * i + otherIndex);
                }
                return ret;
            });
            return result;
        }
    }

    @Override
    public Integer argmax() {
        return this.axisArgOperation((l, r) -> l.getRight() >= r.getRight() ? l : r);
    }

    @Override
    public NdArray argmax(int axis) {
        return this.axisArgOperation((l, r) -> l.getRight() >= r.getRight() ? l : r, axis);
    }

    @Override
    public Integer argmin() {
        return this.axisArgOperation((l, r) -> l.getRight() <= r.getRight() ? l : r);
    }

    @Override
    public NdArray argmin(int axis) {
        return this.axisArgOperation((l, r) -> l.getRight() <= r.getRight() ? l : r, axis);
    }

    @Override
    public Double max() {
        return this.axisOperation((l, r) -> l >= r ? l : r);
    }

    @Override
    public NdArray max(int... axis) {
        return this.axisOperation((l, r) -> l >= r ? l : r, axis);
    }

    @Override
    public Double min() {
        return this.axisOperation((l, r) -> l <= r ? l : r);
    }

    @Override
    public NdArray min(int... axis) {
        return this.axisOperation((l, r) -> l <= r ? l : r, axis);
    }

    @Override
    public Double sum() {
        return this.axisOperation((l, r) -> l + r);
    }

    @Override
    public NdArray sum(int... axis) {
        return this.axisOperation((l, r) -> l + r, axis);
    }

    @Override
    public NdArray add(NdArray other) {
        return elementwise(other, (l, r) -> l + r);
    }

    @Override
    public NdArray add(Number other) {
        return elementwise(other, (l, r) -> l + r);
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
    public boolean checkBroadcast(NdArray other) {
        boolean broadcast = false;
        int[] shape = shape(), otherShape = other.shape();
        for (int i = 0; i < dim(); i++) {
            if (shape.length == otherShape.length && shape[i] == otherShape[i]) {
                continue;
            } else if (shape.length == otherShape.length && shape[i] == 1 || otherShape[i] == 1) {
                broadcast = true;
            } else throw new NdArrayException(this, other);
        }
        return broadcast;
    }

    @Override
    public NdArray div(NdArray other) {
        return elementwise(other, (l, r) -> div(l, r));
    }

    @Override
    public NdArray div(Number other) {
        return elementwise(other, (l, r) -> div(l, r));
    }

    @Override
    public NdArray get(NdIndex... indices) {
        if (indices.length != dim()) {
            throw new IllegalArgumentException("indexes size does not match dimension");
        }
        return new NdArrayImpl(this.iterator.slice(indices), data);
    }

    @Override
    public double get(int... coordinate) {
        return data.get(iterator.pointer(coordinate));
    }

    @Override
    public double get(int index) {
        return data.get(iterator.indexToPointer(index));
    }

    @Override
    public double broadcastGet(int[] coordinate) {
        if (coordinate.length != dim()) {
            throw new IllegalArgumentException();
        }
        return data.get(iterator.broadcastPointer(coordinate));
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
    public NdArray eq(NdArray other) {
        return elementwise(other, (l, r) -> l.compareTo(r) == 0 ? 1. : 0.);
    }

    @Override
    public NdArray eq(Number other) {
        return elementwise(other, (l, r) -> l.compareTo(r) == 0 ? 1. : 0.);
    }

    @Override
    public NdArray gt(NdArray other) {
        return elementwise(other, (l, r) -> l.compareTo(r) > 0 ? 1. : 0.);
    }

    @Override
    public NdArray gt(Number other) {
        return elementwise(other, (l, r) -> l.compareTo(r) > 0 ? 1. : 0.);
    }

    @Override
    public NdArray lt(NdArray other) {
        return elementwise(other, (l, r) -> l.compareTo(r) < 0 ? 1. : 0.);
    }

    @Override
    public NdArray lt(Number other) {
        return elementwise(other, (l, r) -> l.compareTo(r) < 0 ? 1. : 0.);
    }

    @Override
    public NdArray gte(NdArray other) {
        return elementwise(other, (l, r) -> l.compareTo(r) >= 0 ? 1. : 0.);
    }

    @Override
    public NdArray gte(Number other) {
        return elementwise(other, (l, r) -> l.compareTo(r) >= 0 ? 1. : 0.);
    }

    @Override
    public NdArray lte(NdArray other) {
        return elementwise(other, (l, r) -> l.compareTo(r) <= 0 ? 1. : 0.);
    }

    @Override
    public NdArray lte(Number other) {
        return elementwise(other, (l, r) -> l.compareTo(r) <= 0 ? 1. : 0.);
    }

    @Override
    public NdArray reshape(int... rearrange) {
        if (size() != NdIndexer.computeSize(rearrange)) {
            throw new IllegalArgumentException();
        }
        return new NdArrayImpl(iterator.reshape(rearrange), data);
    }

    @Override
    public void conditionalPut(Predicate<Double> f, double value) {
        Arrays.stream(iterator.getPointers()).parallel().filter(p -> f.test(data.get(p))).forEach(p -> data.lazySet(p, value));
    }

    @Override
    public NdArray where(Predicate<Double> op) {
        return this.copy().elementwisei(coordinate -> op.test(data.get(iterator.pointer(coordinate))) ? 1. : 0.);
    }

    @Override
    public void put(NdArray where, Number value) {
        this.elementwisei(coordinate -> where.get(coordinate) > 0 ? value.doubleValue() : this.get(coordinate));
    }

    @Override
    public void put(NdArray where, NdArray value) {
        this.elementwisei(coordinate -> where.get(coordinate) > 0 ? value.get(coordinate) : this.get(coordinate));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NdArrayImpl ndArray = (NdArrayImpl) o;

        return this.toString().equals(ndArray.toString());
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
    public void put(int[] coodinate, double value) {
        data.lazySet(iterator.pointer(coodinate), value);
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
    public NdArray slice(NdIndex... indices) {
        return new NdArrayImpl(iterator.slice(indices), data);
    }

    @Override
    public NdArray transpose() {
        return new NdArrayImpl(
                iterator.transpose(
                        IntStream
                                .range(0, iterator.getDim())
                                .map(i -> (i + 1) % iterator.getDim())
                                .toArray()
                ),
                data
        );
    }

    @Override
    public NdArray transpose(int... dim) {
        return new NdArrayImpl(iterator.transpose(dim), data);
    }

    private StringBuffer stringfyMatrix(StringBuffer sb, int index, int[] channel) {
        final String BR = System.getProperty("line.separator");
        sb.append("[");
        if (index == dim() - 2) {
            sb.append(BR);
        }
        for (int i = 0; i < iterator.getShape()[index]; i++) {
            if (index == dim() - 2) {
                sb.append("[");
                for (int j = 0; j < iterator.getShape()[dim() - 1]; j++) {
                    sb.append(data.get(iterator.pointer(Ints.concat(channel, new int[]{i, j}))));
                    sb.append(", ");
                }
                sb.append("]");
                sb.append(BR);
            } else {
                sb = stringfyMatrix(sb, index + 1, Ints.concat(channel, new int[]{i}));
            }

        }
        sb.append("]");
        return sb;
    }

    @Override
    public String toString() {
        if (dim() > 1) {
            return stringfyMatrix(new StringBuffer(), 0, new int[0]).toString();
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("[ ");
            for (int i = 0; i < data.length(); i++) {
                sb.append(data.get(i));
                sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    @Override
    public int pointer(int index) {
        return iterator.indexToPointer(index);
    }
}
