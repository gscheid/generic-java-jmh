package org.yarg.field;

import java.lang.reflect.Field;
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

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class FieldTests {

    private int x = 1;

    private IntHolder holder = new IntHolder(0);
    private FinalIntHolder finalHolder = new FinalIntHolder(0);
    private VolatileIntHolder volatileHolder = new VolatileIntHolder(0);

    private static Unsafe UNSAFE;
    private static Field FIELD;
    private static long OFFSET;
    private static Field FINAL_FIELD;
    private static long FINAL_OFFSET;
    private static Field VOLATILE_FIELD;
    private static long VOLATILE_OFFSET;
    static {

        Field singleoneInstanceField;
        try {
            singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            UNSAFE = (Unsafe) singleoneInstanceField.get(null);
            FIELD = IntHolder.class.getDeclaredField("value");
            FIELD.setAccessible(true);
            OFFSET = UNSAFE.objectFieldOffset(FIELD);

            FINAL_FIELD = FinalIntHolder.class.getDeclaredField("value");
            FINAL_FIELD.setAccessible(true);
            FINAL_OFFSET = UNSAFE.objectFieldOffset(FINAL_FIELD);

            VOLATILE_FIELD = VolatileIntHolder.class.getDeclaredField("value");
            VOLATILE_FIELD.setAccessible(true);
            VOLATILE_OFFSET = UNSAFE.objectFieldOffset(VOLATILE_FIELD);
        } catch (final Exception e) {
            throw new AssertionError(e);
        }
    }

    @Benchmark
    public void testBlank() {

    }

    @Benchmark
    public int testGetDirect() {
        return holder.value;
    }

    @Benchmark
    public int testGetter() {
        return holder.getValue();
    }

    @Benchmark
    public int testGetField() throws IllegalArgumentException, IllegalAccessException {
        return FIELD.getInt(holder);
    }

    @Benchmark
    public int testGetUnsafe() {
        return UNSAFE.getInt(holder, OFFSET);
    }

    @Benchmark
    public void testSetDirect() {
        holder.value = x++;
    }

    @Benchmark
    public void testSetter() {
        holder.setValue(x++);
    }

    @Benchmark
    public void testSetField() throws IllegalArgumentException, IllegalAccessException {
        FIELD.setInt(holder, x++);
    }

    @Benchmark
    public void testSetUnsafe() {
        UNSAFE.putInt(holder, OFFSET, x++);
    }

    @Benchmark
    public int testFinalGetDirect() {
        return finalHolder.value;
    }

    @Benchmark
    public int testFinalGetter() {
        return finalHolder.getValue();
    }

    @Benchmark
    public int testFinalGetField() throws IllegalArgumentException, IllegalAccessException {
        return FINAL_FIELD.getInt(finalHolder);
    }

    @Benchmark
    public int testFinalGetUnsafe() {
        return UNSAFE.getIntVolatile(finalHolder, FINAL_OFFSET);
    }

    @Benchmark
    public void testFinalSetField() throws IllegalArgumentException, IllegalAccessException {
        FINAL_FIELD.setInt(finalHolder, x++);
    }

    @Benchmark
    public void testFinalSetUnsafe() {
        UNSAFE.putIntVolatile(finalHolder, FINAL_OFFSET, x++);
    }

    @Benchmark
    public int testVolatileGetDirect() {
        return volatileHolder.value;
    }

    @Benchmark
    public int testVolatileGetter() {
        return volatileHolder.getValue();
    }

    @Benchmark
    public int testVolatileGetField() throws IllegalArgumentException, IllegalAccessException {
        return VOLATILE_FIELD.getInt(volatileHolder);
    }

    @Benchmark
    public int testVolatileGetUnsafe() {
        return UNSAFE.getIntVolatile(volatileHolder, VOLATILE_OFFSET);
    }

    @Benchmark
    public void testVolatileSetDirect() {
        volatileHolder.value = x++;
    }

    @Benchmark
    public void testVolatileSetter() {
        volatileHolder.setValue(x++);
    }

    @Benchmark
    public void testVolatileSetField() throws IllegalArgumentException, IllegalAccessException {
        VOLATILE_FIELD.setInt(volatileHolder, x++);
    }

    @Benchmark
    public void testVolatileSetUnsafe() {
        UNSAFE.putIntVolatile(volatileHolder, VOLATILE_OFFSET, x++);
    }

    public static void main(final String[] args) throws RunnerException {
        final Options opt = new OptionsBuilder().include(FieldTests.class.getSimpleName()).forks(1).build();
        new Runner(opt).run();
    }
}
