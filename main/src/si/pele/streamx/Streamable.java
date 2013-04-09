/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.FlatMapper;
import java.util.stream.Stream;

/**
 * SAM Factory for {@link java.util.stream.Stream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link Streamable} is to {@link java.util.stream.Stream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link java.util.stream.Stream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see IntStreamable
 * @see LongStreamable
 * @see DoubleStreamable
 */
public interface Streamable<T> {

    /**
     * @return Newly constructed {@link java.util.stream.Stream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    Stream<T> stream();

    // non-terminal operations

    default Streamable<T> filter(Predicate<? super T> predicate) {
        return () -> stream().filter(predicate);
    }

    default <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {
        return () -> stream().map(mapper);
    }

    default IntStreamable mapToInt(ToIntFunction<? super T> mapper) {
        return () -> stream().mapToInt(mapper);
    }

    default LongStreamable mapToLong(ToLongFunction<? super T> mapper) {
        return () -> stream().mapToLong(mapper);
    }

    default DoubleStreamable mapToDouble(ToDoubleFunction<? super T> mapper) {
        return () -> stream().mapToDouble(mapper);
    }

    default <R> Streamable<R> flatMap(Function<T, Stream<? extends R>> mapper) {
        return () -> stream().flatMap(mapper);
    }

    default <R> Streamable<R> flatMap(FlatMapper<? super T, R> mapper) {
        return () -> stream().flatMap(mapper);
    }

    default IntStreamable flatMapToInt(FlatMapper.ToInt<? super T> mapper) {
        return () -> stream().flatMapToInt(mapper);
    }

    default LongStreamable flatMapToLong(FlatMapper.ToLong<? super T> mapper) {
        return () -> stream().flatMapToLong(mapper);
    }

    default DoubleStreamable flatMapToDouble(FlatMapper.ToDouble<? super T> mapper) {
        return () -> stream().flatMapToDouble(mapper);
    }

    default Streamable<T> distinct() {
        return () -> stream().distinct();
    }

    default Streamable<T> sorted() {
        return () -> stream().sorted();
    }

    default Streamable<T> sorted(Comparator<? super T> comparator) {
        return () -> stream().sorted(comparator);
    }

    default Streamable<T> peek(Consumer<? super T> consumer) {
        return () -> stream().peek(consumer);
    }

    default Streamable<T> limit(long maxSize) {
        return () -> stream().limit(maxSize);
    }

    default Streamable<T> substream(long startingOffset) {
        return () -> stream().substream(startingOffset);
    }

    default Streamable<T> substream(long startingOffset, long endingOffset) {
        return () -> stream().substream(startingOffset, endingOffset);
    }
}
