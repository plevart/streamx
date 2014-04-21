/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.function.*;
import java.util.stream.DoubleStream;

/**
 * SAM Factory for {@link DoubleStream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link DoubleStreamable} is to {@link DoubleStream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link DoubleStream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see Streamable
 */
@FunctionalInterface
public interface DoubleStreamable {

    /**
     * @return Newly constructed {@link DoubleStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    DoubleStream stream();

    /**
     * @return Newly constructed {@link AC#doubleStream auto-closing} {@link DoubleStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    default DoubleStream autoClosingStream() {
        return AC.doubleStream(stream());
    }

    // non-terminal operations

    default DoubleStreamable filter(DoublePredicate predicate) {
        return () -> stream().filter(predicate);
    }

    default DoubleStreamable map(DoubleUnaryOperator mapper) {
        return () -> stream().map(mapper);
    }

    default <U> Streamable<U> mapToObj(DoubleFunction<U> mapper) {
        return () -> stream().mapToObj(mapper);
    }

    default IntStreamable mapToInt(DoubleToIntFunction mapper) {
        return () -> stream().mapToInt(mapper);
    }

    default LongStreamable mapToLong(DoubleToLongFunction mapper) {
        return () -> stream().mapToLong(mapper);
    }

    default DoubleStreamable flatMap(DoubleFunction<? extends DoubleStream> mapper) {
        return () -> stream().flatMap(mapper);
    }

    default DoubleStreamable distinct() {
        return () -> stream().distinct();
    }

    default DoubleStreamable sorted() {
        return () -> stream().sorted();
    }

    default DoubleStreamable peek(DoubleConsumer consumer) {
        return () -> stream().peek(consumer);
    }

    default DoubleStreamable limit(long maxSize) {
        return () -> stream().limit(maxSize);
    }

    default DoubleStreamable skip(long n) {
        return () -> stream().skip(n);
    }

    default Streamable<Double> boxed() {
        return () -> stream().boxed();
    }
}
