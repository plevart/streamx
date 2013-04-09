/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.stream.FlatMapper;
import java.util.stream.LongStream;

/**
 * SAM Factory for {@link java.util.stream.LongStream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link LongStreamable} is to {@link java.util.stream.LongStream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link java.util.stream.LongStream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see Streamable
 */
public interface LongStreamable {

    /**
     * @return Newly constructed {@link java.util.stream.LongStream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    LongStream stream();

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

    default LongStreamable flatMap(FlatMapper.OfLongToLong mapper) {
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

    default LongStreamable substream(long startingOffset) {
        return () -> stream().substream(startingOffset);
    }

    default LongStreamable substream(long startingOffset, long endingOffset) {
        return () -> stream().substream(startingOffset, endingOffset);
    }

    default Streamable<Long> boxed() {
        return () -> stream().boxed();
    }
}
