/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.FlatMapper;
import java.util.stream.IntStream;

/**
 * SAM Factory for {@link java.util.stream.IntStream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link IntStreamable} is to {@link java.util.stream.IntStream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link java.util.stream.IntStream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see Streamable
 */
public interface IntStreamable {

    /**
     * @return Newly constructed {@link java.util.stream.IntStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    IntStream stream();

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

    default IntStreamable flatMap(FlatMapper.OfIntToInt mapper) {
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

    default IntStreamable substream(long startingOffset) {
        return () -> stream().substream(startingOffset);
    }

    default IntStreamable substream(long startingOffset, long endingOffset) {
        return () -> stream().substream(startingOffset, endingOffset);
    }

    default Streamable<Integer> boxed() {
        return () -> stream().boxed();
    }
}
