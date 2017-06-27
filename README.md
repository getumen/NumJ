# NumJ:  Java N-dimensional Array 

NumJ is N-dimensional Array library for Java that supports parallelized operations.
NumJ requires JDK 1.8 or higher.

## Version
*0.0.2*

## Installation

- For gradle
```$gradle
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath group: 'com.layer', name: 'gradle-git-repo-plugin', version: '2.0.2'
    }
}

apply plugin: 'git-repo'

// add the following depemdencies.

repositories {
    github("getumen", "NumJ", "gh-pages", "repository")
}
dependencies {
    compile 'jp.ac.tsukuba.cs.mdl:numj:0.0.2'
}

```

## Code Example

```java
public class NumJTest {

    @Test
    public void ones() {
        assertEquals(NumJ.create(new double[]{1, 1, 1, 1}, new int[]{2, 2}), NumJ.ones(2, 2));
        assertNotEquals(NumJ.create(new double[]{1, 1, 1, 2}, new int[]{2, 2}), NumJ.ones(2, 2));
    }

    @Test
    public void zeros(){
        assertNotEquals(NumJ.create(new double[]{2,2,2,2}, 2,2), NumJ.zeros(2,2));
        assertEquals(NumJ.create(new double[]{0,0,0,0}, 2,2), NumJ.zeros(2,2));
    }

    @Test
    public void arange(){
        assertEquals(NumJ.create(new double[]{0,1,2,3},2,2), NumJ.arange(2,2));
    }
}
```
```java

public class NdArrayImplTest {
    @Test
    public void dot() throws Exception {
        NdArray arange1 = NumJ.arange(3, 4);
        NdArray arange2 = NumJ.arange(4, 2);
        assertArrayEquals(new int[]{3, 2}, arange1.dot(arange2).shape());
        assertEquals(NumJ.create(new double[]{28, 34, 76, 98, 124, 162}, new int[]{3, 2}), arange1.dot(arange2));
        NdArray vec = NumJ.arange(4);
        assertEquals(1, arange1.dot(vec).dim());
        assertArrayEquals(new int[]{3}, arange1.dot(vec).shape());
        assertEquals(NumJ.create(new double[]{14, 38, 62}, new int[]{3}), arange1.dot(vec));
    }

    @Test
    public void argmax() throws Exception {
        assertEquals(Integer.valueOf(5), NumJ.arange(3, 2).argmax());
        assertEquals(NumJ.ones(3), NumJ.arange(3, 2).argmax(1));
    }

    @Test
    public void size() throws Exception {
        assertEquals(3 * 4 * 5, NumJ.ones(3, 4, 5).size());
    }

    @Test
    public void add() throws Exception {
        NdArray ones = NumJ.ones(3, 4);
        assertEquals(
                NumJ.create(new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, 3, 4),
                ones.add(NumJ.arange(1, 4))
        );
        assertEquals(
                NumJ.create(new double[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}, 3, 4),
                ones.add(NumJ.arange(4, 1).transpose())
        );
    }

    @Test
    public void add1() throws Exception {
        assertEquals(NumJ.create(new double[]{1, 2, 3, 4, 5, 6}, 2, 3), NumJ.arange(2, 3).add(1));
    }

    @Test
    public void sub() throws Exception {
        assertEquals(NumJ.create(new double[]{-1, 0, 1, 2, 3, 4}, 2, 3), NumJ.arange(2, 3).sub(NumJ.ones(2, 3)));
    }

    @Test
    public void sub1() throws Exception {
        assertEquals(NumJ.create(new double[]{-1, 0, 1, 2, 3, 4}, 2, 3), NumJ.arange(2, 3).sub(1));
    }

}

```