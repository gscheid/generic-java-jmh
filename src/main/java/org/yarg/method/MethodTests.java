package org.yarg.method;

import java.util.concurrent.TimeUnit;

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

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MethodTests {

    private int x = 1;
    private int y = 1;
    private Adder adder = new AdderImpl();
    private AbstractAdder abstractAdder = new AdderImpl();
    private AdderImpl adderImpl = new AdderImpl();
    private Adder otherAdder = new LazyAdder();

    private Adder staticFunction = StaticAdder::add;
    private Adder instanceFunction = adderImpl::add;
    private Adder localFunction = (x, y) -> x + y;

    @Benchmark
    public void testBlank() {

    }

    @Benchmark
    public int testDirect() {
        return x + y;
    }

    @Benchmark
    public int testLocalInstanceMethod() {
        return addInstance(x, y);
    }

    private int addInstance(final int x, final int y) {
        return x + y;
    }

    @Benchmark
    public int testLocalStaticMethod() {
        return addStatic(x, y);
    }

    private static int addStatic(final int x, final int y) {
        return x + y;
    }

    @Benchmark
    public int testInterfaceInstanceMethod() {
        return adder.add(x, y);
    }

    @Benchmark
    public int testDifferentInterfaceInstanceMethod() {
        return otherAdder.add(x, y);
    }

    @Benchmark
    public int testAbstractInstanceMethod() {
        return abstractAdder.add(x, y);
    }

    @Benchmark
    public int testInstanceMethod() {
        return adderImpl.add(x, y);
    }

    @Benchmark
    public int testStaticMethod() {
        return StaticAdder.add(x, y);
    }

    @Benchmark
    public int testInstanceFunction() {
        return instanceFunction.add(x, y);
    }

    @Benchmark
    public int testStaticFunction() {
        return staticFunction.add(x, y);
    }

    @Benchmark
    public int testLocalFunction() {
        return localFunction.add(x, y);
    }

    public static void main(final String[] args) throws RunnerException {
        final Options opt = new OptionsBuilder().include(MethodTests.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }
}
