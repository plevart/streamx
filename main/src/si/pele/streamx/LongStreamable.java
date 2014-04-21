/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.function.*;
import java.util.stream.LongStream;

/**
 * SAM Factory for {@link LongStream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link LongStreamable} is to {@link LongStream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link LongStream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see Streamable
 */
@FunctionalInterface
public interface LongStreamable {

    /**
     * @return Newly constructed {@link LongStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    LongStream stream();

    /**
     * @return Newly constructed {@link AC#longStream auto-closing} {@link LongStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    default LongStream autoClosingStream() {
        return AC.longStream(stream());
    }

    // non-terminal operations

    default LongStreamable filter(LongPredicate predicate) {
        return () -> stream().filter(predicate);
    }

    default LongStreamable map(LongUnaryOperator mapper) {
        return () -> stream().map(mapper);
    }

    default <U> Streamable<U> mapToObj(LongFunction<U> mapper) {
        return () -> stream().mapToObj(mapper);
    }

    default IntStreamable mapToInt(LongToIntFunction mapper) {
        return () -> stream().mapToInt(mapper);
    }

    default DoubleStreamable mapToDouble(LongToDoubleFunction mapper) {
        return () -> stream().mapToDouble(mapper);
    }

    default LongStreamable flatMap(LongFunction<? extends LongStream> mapper) {
        return () -> stream().flatMap(mapper);
    }

    default LongStreamable distinct() {
        return () -> stream().distinct();
    }

    default LongStreamable sorted() {
        return () -> stream().sorted();
    }

    default LongStreamable peek(LongConsumer consumer) {
        return () -> stream().peek(consumer);
    }

    default LongStreamable limit(long maxSize) {
        return () -> stream().limit(maxSize);
    }

    default LongStreamable skip(long n) {
        return () -> stream().skip(n);
    }

    default DoubleStreamable asDoubleStreamable() {
        return () -> stream().asDoubleStream();
    }

    default Streamable<Long> boxed() {
        return () -> stream().boxed();
    }
}
