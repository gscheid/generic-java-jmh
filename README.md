A simple project to test out the features of [jmh](http://openjdk.java.net/projects/code-tools/jmh/) and use for reference when creating actual benchmarks.

Compile.
```mvn clean install```

Run.
```java -jar target/benchmarks.jar -wf 5 -wi 5 -f 5 -i 5```

Run a single set of tests.
```java -jar target/benchmarks.jar SynchronizationTests -wf 5 -wi 5 -f 5 -i 5 -t max```