

# Sample Codes

## NdArrayの生成

### １で埋められた(2,3)の行列の生成
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class OnesSample{
    void ones(){
        // 全ての要素が１で埋められたN次元の配列を作るために専用のstatic methodが用意されている、
        NdArray mat = NumJ.ones(2, 3);
        System.out.println(mat);
    }
    
    void generateByLambda(){
        // ラムダ式を用いて生成できる、
        NdArray mat = NumJ.generator(()->1.0, 2, 3);
        System.out.println(mat);
    }
    
    void createByArray(){
        // 配列から行列を生成できる．
        double[] array = new double[6];
        java.util.Arrays.fill(array, 1.0);
        NdArray mat = NumJ.create(array, 2, 3);
        System.out.println(mat);
    }
    
    void createByNumber(){
        // 配列の要素が１種類のとき、全ての要素が同じN次元配列を生成できる
        NdArray mat = NumJ.createByNumber(1.0, 2, 3);
        System.out.println(mat);
    }
    /*
    [
    [1.0, 1.0, 1.0, ]
    [1.0, 1.0, 1.0, ]
    ]
     */
}

```

他の専用のstatic methodとして、`NumJ.ones(int... shape)`がある．


### N次元の要素が[0, 1, 2, 3, ..., 23]となっている(2,3,4)のテンソルを生成
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class ArangeSample{
    void arange(){
        // 専用のstatic methodが用意されている、
        NdArray mat = NumJ.arange(2, 3, 4);
        System.out.println(mat);
    }
    
    void generateByLambda(){
        // ラムダ式を用いて生成できる、
        NdArray mat = NumJ.generator(i -> i, 2, 3, 4);
        System.out.println(mat);
    }
    
    void createByArray(){
        // 配列から行列を生成できる．
        double[] array = new double[24];
        for(i=0; i<array.length; i++){
            array[i] = i;
        }
        NdArray mat = NumJ.create(array, 2, 3, 4);
        System.out.println(mat);
    }
    /*
    [[
    [0.0, 1.0, 2.0, 3.0, ]
    [4.0, 5.0, 6.0, 7.0, ]
    [8.0, 9.0, 10.0, 11.0, ]
    ][
    [12.0, 13.0, 14.0, 15.0, ]
    [16.0, 17.0, 18.0, 19.0, ]
    [20.0, 21.0, 22.0, 23.0, ]
    ]]
     */
}

```

### 一様分布(Uniform), ガウス分布(Gaussian)に従う分布から生成された乱数を要素に持つN次元配列は以下のように生成できる．
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class UniformSample{
    void uniform(){
        // 専用のstatic methodが用意されている、
        NdArray mat = NumJ.uniform(2, 3, 4);
        System.out.println(mat);
    }
    
    void generateByLambda(){
        // ラムダ式を用いて生成できる、
        java.util.Random random = new java.util.Random();
        NdArray mat = NumJ.generator(() -> random.nextDouble() , 2, 3, 4);
        System.out.println(mat);
    }
    
    /*
    [[
    [0.4383698940460399, 0.009201445894680416, 0.6646855857619598, 0.9578175349352749, ]
    [0.3463530977962025, 0.43357993888458335, 0.7928946100377577, 0.8433863625507728, ]
    [0.30101478409914095, 0.755966939396928, 0.059678347104225926, 0.9709363896660044, ]
    ][
    [0.07058291635969338, 0.7097493132364516, 0.7840814451618854, 0.07355341694210626, ]
    [0.36216027994272126, 0.6981264101408965, 0.012980722083258178, 0.7192828976082446, ]
    [0.07172042586999983, 0.7208878276048653, 0.5716073497505998, 0.6400765102737849, ]
    ]]
     */
}

class GaussianSample{
    void normal(){
        // 専用のstatic methodが用意されている、
        double mu = 0.0; // gaussianの平均
        double sigma = 1.0; // gaussianの標準偏差
        NdArray mat = NumJ.normal(mu, sigma, 2, 3, 4);
        System.out.println(mat);
        /*
        [[
        [0.15056180927401727, 0.3729898640079502, 0.1837690780637584, -0.2842162483657402, ]
        [0.9144191794417116, 1.494291010885211, -0.2799073970807418, -0.05605324788030526, ]
        [-0.7490640404400403, 0.5797771049112694, 0.3696286871595557, -1.9068906515075168, ]
        ][
        [-1.1168182314149675, 0.39925634075969807, -1.2622405631523816, -1.5086969843747993, ]
        [0.023217270799086313, -0.8729059164070305, -0.028740835070794776, -1.3682415760119662, ]
        [0.06469094085696804, -1.471170642412973, 0.5109657138267006, -2.1869367323005613, ]
        ]]
         */
    }
}
```

### (5,5)の単位行列の作成
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class IdentitySample{
    void identity(){
        // 専用のstatic methodが用意されている、
        NdArray mat = NumJ.identity(5);
        System.out.println(mat);
        /*
        [
        [1.0, 0.0, 0.0, 0.0, 0.0, ]
        [0.0, 1.0, 0.0, 0.0, 0.0, ]
        [0.0, 0.0, 1.0, 0.0, 0.0, ]
        [0.0, 0.0, 0.0, 1.0, 0.0, ]
        [0.0, 0.0, 0.0, 0.0, 1.0, ]
        ]
         */
    }
}
```

## 要素ごとの四則演算
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class ElementWiseSample{
    void cal(){
        NdArray arange = NumJ.arange(2, 3);
        NdArray twos = NumJ.createByNumber(2, 2, 3);
        System.out.println(arange.add(twos));
        /*
        [
        [2.0, 3.0, 4.0, ]
        [5.0, 6.0, 7.0, ]
        ]
        */
        System.out.println(arange.sub(twos));
        /*
        [
        [-2.0, -1.0, 0.0, ]
        [1.0, 2.0, 3.0, ]
        ]
         */
        System.out.println(arange.mul(twos));
        /*
        [
        [0.0, 2.0, 4.0, ]
        [6.0, 8.0, 10.0, ]
        ]
         */
        System.out.println(arange.div(twos));
        /*
        [
        [0.0, 0.5, 1.0, ]
        [1.5, 2.0, 2.5, ]
        ]
         */
    }
}
```

## N次元配列とスカラ値の四則演算
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class ElementWiseSample{
    void cal(){
        NdArray arange = NumJ.arange(2, 3);
        System.out.println(arange.add(2));
        /*
        [
        [2.0, 3.0, 4.0, ]
        [5.0, 6.0, 7.0, ]
        ]
        */
        System.out.println(arange.sub(2));
        /*
        [
        [-2.0, -1.0, 0.0, ]
        [1.0, 2.0, 3.0, ]
        ]
         */
        System.out.println(arange.mul(2));
        /*
        [
        [0.0, 2.0, 4.0, ]
        [6.0, 8.0, 10.0, ]
        ]
         */
        System.out.println(arange.div(2));
        /*
        [
        [0.0, 0.5, 1.0, ]
        [1.5, 2.0, 2.5, ]
        ]
         */
    }
}
```

## NdArrayの情報
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class NdArrayInfoSample{
    void info(){
        NdArray arange = NumJ.arange(2, 3, 4);
        System.out.println(arange.dim()); //output: 3 -> 3次元の配列
        System.out.println(java.util.Arrays.toString(arange.shape())); //output: [2, 3, 4] -> shapeを返す
        System.out.println(arange.size()); // 2*3*4=24 要素の全てのサイズを返す．
    }
}
```

## dot product
```java
import jp.ac.tsukuba.cs.mdl.numj.core.NumJ;
import jp.ac.tsukuba.cs.mdl.numj.core.NdArray;

class DotProductSample{
    void prod(){
        NdArray mat = NumJ.arange(2, 3);
        NdArray mat2 = NumJ.generator(i->2*i, 3, 4);
        System.out.println(mat.dot(mat2));
        System.out.println(Arrays.toString(mat.dot(mat2).shape()));
        /*
        (l, m) dot (m, n) = (l, n)の行列になる、
        [
        [40.0, 46.0, 52.0, 58.0, ]
        [112.0, 136.0, 160.0, 184.0, ]
        ]
        行列の形は[2, 4]となる．
        */
    }
    
    void vec(){
        NdArray mat = NumJ.arange(2, 3);
        NdArray vec = NumJ.generator(i -> 2 * i, 3);
        System.out.println(mat.dot(vec));
        System.out.println(Arrays.toString(mat.dot(vec).shape()));
        System.out.println(vec.dot(mat.transpose()));
        System.out.println(Arrays.toString(vec.dot(mat.transpose()).shape()));
        /*
        行列とベクトルの計算
        [ 10.0, 28.0, ]
        [2]
        [ 10.0, 28.0, ]
        [2]
        */
    }
}
```

## スライス
```java
class SliceSample{
    void slice(){
        NdArray mat = NumJ.arange(2, 3);
        System.out.println(mat);
        /*
        もとの行列
        [
        [0.0, 1.0, 2.0, ]
        [3.0, 4.0, 5.0, ]
        ]
         */
        System.out.println(mat.slice(NdSlice.all(), NdSlice.all()));
        /*
        allで指定の座標の全ての要素を取る．
        [
        [0.0, 1.0, 2.0, ]
        [3.0, 4.0, 5.0, ]
        ]
         */
        System.out.println(mat.slice(NdSlice.all(), NdSlice.point(1)));
        /*
        pointにより座標の１点のみとる、
        [
        [1.0, ]
        [4.0, ]
        ]
         */
        System.out.println(mat.slice(NdSlice.all(), NdSlice.interval(1, 3)));
        /*
        intervalにより、座標の区間[start, end)をとる．
        この場合、[1, 3)
        [
        [1.0, 2.0, ]
        [4.0, 5.0, ]
        ]
         */
        System.out.println(mat.slice(NdSlice.all(), NdSlice.set(0, 2)));
        /*
        setは指定した座標に含まれる座標のみをスライシングする．
        [
        [0.0, 2.0, ]
        [3.0, 5.0, ]
        ]
         */
    }
}
```

## Reduction Operators
```java
class ReductionSamples{
    void reduction(){
        NdArray arange = NumJ.arange(2,3,4);
        System.out.println(arange.sum()); //276.0 -> 全要素の和を取る．
        System.out.println(arange.sum(0, 2));
        /*
        指定した要素に関して和を取る．
        [ 60.0, 92.0, 124.0, ]
         */
        System.out.println(arange.sum(0));
        /*
        [
        [12.0, 14.0, 16.0, 18.0, ]
        [20.0, 22.0, 24.0, 26.0, ]
        [28.0, 30.0, 32.0, 34.0, ]
        ]
         */
        System.out.println(arange.max()); // 23.0
        System.out.println(arange.max(0, 2));
        /*
        [ 15.0, 19.0, 23.0, ]
         */
        System.out.println(arange.max(0));
        /*
        [
        [12.0, 13.0, 14.0, 15.0, ]
        [16.0, 17.0, 18.0, 19.0, ]
        [20.0, 21.0, 22.0, 23.0, ]
        ]
         */
        // 同様にminも定義される．

        System.out.println(arange.argmax()); //23 最大値を取る要素のインデックスを返す、
        System.out.println(arange.argmax(1));
        /*
        [
        [2.0, 2.0, 2.0, 2.0, ]
        [2.0, 2.0, 2.0, 2.0, ]
        ]
         */
        // 同様にargminも定義される．
    }
}
```

## get, put
```java
class GetSample{
    void get(){
        NdArray arange = NumJ.arange(2,3,4);
        System.out.println(arange.get(23)); //23.0 引数がint型でひとつの時、N次元配列を１次元配列とみなした時の要素を取れる．
        System.out.println(arange.get(0,1,1)); //5.0 引数がint型の配列の時、N次元配列の対応する座標の要素を取得できる．
    }
    
    void put(){
        NdArray arange = NumJ.arange(2, 3);
        int[] coordinate = new int[]{0, 1};
        arange.put(coordinate, 100);
        System.out.println(arange);
        /*
        対応するザ行の値を変更する．
        [
        [0.0, 100.0, 2.0, ]
        [3.0, 4.0, 5.0, ]
        ]
         */
        NdArray mask = arange.where(elem -> elem > 2);
        System.out.println(mask);
        /*
        このようにすると条件に合う要素が１を取り、合わない要素が０となるN次元配列を返す．
        [
        [0.0, 1.0, 0.0, ]
        [1.0, 1.0, 1.0, ]
        ]
        */
        arange.put(mask, -1);
        System.out.println(arange);
        /*
        put(mask, value)ではmaskの０より大きい要素の値に対応する座標のみ変更できる、
        [
        [0.0, -1.0, 2.0, ]
        [-1.0, -1.0, -1.0, ]
        ]
        */
        arange.put(new NdIndex[]{
                        NdSlice.interval(1, 2),
                        NdSlice.all()
                },
                NumJ.arange(1, 3));
        System.out.println(arange);
        /*
        スライスを使って一致する軸のみ値を置き換えることができる．
        [
        [0.0, -1.0, 2.0, ]
        [0.0, 1.0, 2.0, ]
        ]
         */
    }
}
```

## 比較演算
```java
class CompareSamples{
    void comp(){
        NdArray arange = NumJ.arange(2, 3);
        System.out.println(arange.gt(3));
        /*
        element > 3　となる要素のみ１となる、ほかは０
        [
        [0.0, 0.0, 0.0, ]
        [0.0, 1.0, 1.0, ]
        ]
        gt = greater than
        gte = greater than or equal
        lt = less than
        lte = less than or equal
        eq = equal
         */
        System.out.println(arange.eq(NumJ.arange(2, 3)));
        /*
        NdArray同士でも同様に定義されている．
        [
        [1.0, 1.0, 1.0, ]
        [1.0, 1.0, 1.0, ]
        ]
         */
    }
}
```

## 転置
```java
class TransposeExample{
    void trans(){
        NdArray arange = NumJ.arange(2, 3);
        System.out.println(arange.transpose());
        System.out.println(Arrays.toString(arange.transpose().shape()));
        /*
        転置
        [
        [0.0, 3.0, ]
        [1.0, 4.0, ]
        [2.0, 5.0, ]
        ]
        shape: [3, 2]
         */
        NdArray fourDim = NumJ.arange(2, 3, 4, 5);
        System.out.println(fourDim.transpose());
        System.out.println(Arrays.toString(fourDim.transpose().shape()));
        /*
        転置
        [[[
        [0.0, 60.0, ]
        [1.0, 61.0, ]
        [2.0, 62.0, ]
        [3.0, 63.0, ]
        [4.0, 64.0, ]
        ]...[
        [55.0, 115.0, ]
        [56.0, 116.0, ]
        [57.0, 117.0, ]
        [58.0, 118.0, ]
        [59.0, 119.0, ]
        ]]]
        shape: [3, 4, 5, 2]
         */
        System.out.println(Arrays.toString(fourDim.transpose(1, 3, 2, 0).shape()));
        /*
        ３次テンソル以上では自分で転置の順序を決められる、
        [3, 5, 4, 2]
         */
    }
}
```