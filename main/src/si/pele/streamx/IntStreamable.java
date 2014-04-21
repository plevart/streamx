/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.function.*;
import java.util.stream.IntStream;

/**
 * SAM Factory for {@link IntStream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link IntStreamable} is to {@link IntStream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link IntStream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see Streamable
 */
@FunctionalInterface
public interface IntStreamable {

    /**
     * @return Newly constructed {@link IntStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    IntStream stream();

    /**
     * @return Newly constructed {@link AC#intStream auto-closing} {@link IntStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    default IntStream autoClosingStream() {
        return AC.intStream(stream());
    }

    // non-terminal operations

    default IntStreamable filter(IntPredicate predicate) {
        return () -> stream().filter(predicate);
    }

    default IntStreamable map(IntUnaryOperator mapper) {
        return () -> stream().map(mapper);
    }

    default <U> Streamable<U> mapToObj(IntFunction<U> mapper) {
        return () -> stream().mapToObj(mapper);
    }

    default LongStreamable mapToLong(IntToLongFunction mapper) {
        return () -> stream().mapToLong(mapper);
    }

    default DoubleStreamable mapToDouble(IntToDoubleFunction mapper) {
        return () -> stream().mapToDouble(mapper);
    }

    default IntStreamable flatMap(IntFunction<? extends IntStream> mapper) {
        return () -> stream().flatMap(mapper);
    }

    default IntStreamable distinct() {
        return () -> stream().distinct();
    }

    default IntStreamable sorted() {
        return () -> stream().sorted();
    }

    default IntStreamable peek(IntConsumer consumer) {
        return () -> stream().peek(consumer);
    }

    default IntStreamable limit(long maxSize) {
        return () -> stream().limit(maxSize);
    }

    default IntStreamable skip(long n) {
        return () -> stream().skip(n);
    }

    default LongStreamable asLongStreamable() {
        return () -> stream().asLongStream();
    }

    default DoubleStreamable asDoubleStreamable() {
        return () -> stream().asDoubleStream();
    }

    default Streamable<Integer> boxed() {
        return () -> stream().boxed();
    }
}
