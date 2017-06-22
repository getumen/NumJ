package jp.ac.tsukuba.cs.mdl.numj.core;

import com.google.common.util.concurrent.AtomicDoubleArray;

import java.util.function.Predicate;

public interface NdArray {

    AtomicDoubleArray data();
    int[] shape();
    int dim();
    int size();
    int[] columnStride();
    int[] rowStride();

    NdArray copy();

    NdArray copyShape();

    NdArray add(NdArray other);

    NdArray add(Number other);

    NdArray sub(NdArray other);

    NdArray sub(Number other);

    boolean checkDemensions(NdArray other);

    int columns();

    int rows();

    NdArray cond(Predicate<Double> condition);

    NdArray comsum(int dimension);

    double distance1(NdArray other);

    double distance2(NdArray other);

    NdArray div(NdArray other);

    NdArray div(Number other);

    NdArray get(NdIndex... indexes);

    NdArray getColumn(int column);

    NdArray getColumns(int... columns);

    NdArray getRow(int row);

    NdArray getRows(int... rows);

    boolean getBoolean(int i);

    boolean getBoolean(int i, int j);

    boolean getBoolean(int... indexes);

    NdArray eq(NdArray other);

    NdArray eq(Number other);

    NdArray gt(NdArray other);

    NdArray gt(Number other);

    NdArray gte(NdArray other);

    NdArray gte(Number other);

    int index(int i, int j);

    int index(int[] indexes);

    int[] indexes(int index);

    boolean isVector();

    boolean isMatrix();

    NdArray tensorAlongDimension(int index, int... dimension);

    int tensorssAlongDimension(int dimension);

    NdArray vectorAlongDimension(int index, int dimension);

    int vectorAlongDimension(int dimension);

    int length();


    NdArray lt(NdArray other);

    NdArray lt(Number other);

    NdArray lte(NdArray other);

    NdArray lte(Number other);

    NdArray max(int... dimension);

    Number maxNumber();

    NdArray mean(int... dimension);

    Number meanNumber();

    NdArray min(int... dimension);

    Number minNumber();

    NdArray mmul(NdArray other);

    NdArray mul(NdArray other);

    NdArray mul(Number other);

    NdArray neg();

    NdArray neq(NdArray other);

    NdArray neq(Number other);

    NdArray norm1(int... dimension);

    Number norm1();

    NdArray norm2(int... dimension);

    Number norm2();

    NdArray permute(int... rearrange);

    NdArray prod(int... dimension);

    Number prodNumber();

    void put(int i, double value);

    void put(int i, int j, double value);

    void put(int[] indexes, double value);

    void put(NdIndex[] indexes, NdArray array);

    NdArray ravel();

    NdArray slice(int i);

    NdArray slice(int i, int dimension);

    int slices();

    int[] stride();

    int stride(int dimension);

    NdArray sum(int... dimension);

    Number sumNumber();

    NdArray transpose();

    NdArray transposei();
}
