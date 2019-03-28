# Adroitness-Mobile

Status for master branch:

[//]: # (this is a comment: see this link for badges using travis-CI, codecov, etc: https://github.com/mlindauer/SMAC3/blob/warmstarting_multi_model/README.md) 
![build](https://img.shields.io/badge/build-passing-green.svg?cacheSeconds=2592000) 
![test](https://img.shields.io/badge/test-passing-green.svg?cacheSeconds=2592000) 
![coverage](https://img.shields.io/badge/coverage-90%25-yellowgreen.svg?cacheSeconds=2592000) 
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3fe51892461d4539ad15f9fde60d743c)](https://www.codacy.com/app/ojrlopez27/Adroitness-Mobile?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ojrlopez27/adroitness-mobile&amp;utm_campaign=Badge_Grade)

Implementation:

![version](https://img.shields.io/badge/version-2.0-blue.svg?cacheSeconds=2592000)
![language](https://img.shields.io/badge/language-Java-yellowgreen.svg?cacheSeconds=2592000) 
![language](https://img.shields.io/badge/language-Android-yellowgreen.svg?cacheSeconds=2592000) 


# Overview

Adroitness is an efficient Android-based middleware for building robust, high-performance interactive apps, alleviating the burdensome task of dealing with low-level architectural decisions and fine-grained implementation details by focusing on the separation of concerns and abstracting away the complexity of orchestrating device sensors and effectors, decision-making processes, and connection to remote services, while providing scaffolding for the development of higher-level functional features of interactive high-performance mobile apps.

In this repository you will find two different Android Studio projects:

1. **adroitness-core**: this is an Android Library for the self-contained Middleware which contains all the Sensors, Effectors, Services and Decision-Making mechanisms available in Adroitness.
2. **client**: this is an Android App which serves as an example to illustrate how you can use Adroitness Middleware.


## How to cite our work

Please cite the [ICSA 2018 paper](http://www.cs.cmu.edu/~oscarr/pdf/publications/2018_icsa.pdf "Oscar J. Romero's Homepage") if you use Adroitness in your work:

```
@inproceedings{adroitness:2018,
  title = {An Efficient Mobile-based Middleware Architecture for Building Robust, High-performance Apps},
  author = {{Romero}, {Oscar} J. and {Akoju}, {Sushma}},
  booktitle = {International Conference on Software Architecture},
  year          = "2018",
  pages         = "00--00"
}
```
