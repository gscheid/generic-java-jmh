package org.yarg.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.yarg.field.IntHolder;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SynchronizationTests {

    private int x = 0;
    private final AtomicInteger atomic = new AtomicInteger();
    private final LongAdder adder = new LongAdder();
    private final ReentrantLock reentrantLock = new ReentrantLock();
    private final ReentrantLock fairReentrantLock = new ReentrantLock(true);
    private final Object intrinsicLock = new Object();

    private final ThreadLocal<IntHolder> threadLocal = new ThreadLocal<IntHolder>() {
        @Override
        protected IntHolder initialValue() {
            return new IntHolder(0);
        }
    };

    @Benchmark
    public void testBlank() {

    }

    @Benchmark
    public int testNoSync() {
        return x++;
    }

    @Benchmark
    public int testAtomic() {
        return atomic.getAndIncrement();
    }

    @Benchmark
    public long testAdder() {
        adder.increment();
        return adder.sum();
    }

    @Benchmark
    public int testReentrantLock() {
        reentrantLock.lock();
        try {
            return x++;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Benchmark
    public int testFairReentrantLock() {
        fairReentrantLock.lock();
        try {
            return x++;
        } finally {
            fairReentrantLock.unlock();
        }
    }

    @Benchmark
    public int testIntrinsicLock() {
        synchronized (intrinsicLock) {
            return x++;
        }
    }

    @Benchmark
    public int testThreadLocal() {
        return threadLocal.get().value++;
    }

    public static void main(final String[] args) throws RunnerException {
        final Options opt = new OptionsBuilder().include(SynchronizationTests.class.getSimpleName()).forks(1)
                .threads(Runtime.getRuntime().availableProcessors()).build();
        new Runner(opt).run();
    }
}
