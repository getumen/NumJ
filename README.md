NumJ: simple N-dimensional Array for Java 
-----------------------------

# Version
*0.0.2*

# Install

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