/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * SAM Factory for {@link Stream} instances.<p>
 * What {@link Iterable} is to {@link java.util.Iterator}, {@link Streamable} is to {@link Stream} and more.
 * It is also a builder of non-terminal operations with an API parallel to {@link Stream}'s API for
 * non-terminal operations.
 *
 * @see #stream()
 * @see IntStreamable
 * @see LongStreamable
 * @see DoubleStreamable
 */
@FunctionalInterface
public interface Streamable<T> {

    /**
     * @return Newly constructed {@link Stream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    Stream<T> stream();

    /**
     * @return Newly constructed {@link AC#stream auto-closing} {@link Stream} with all the stacked non-terminal operations
     *         applied and ready to be consumed.
     */
    default Stream<T> autoClosingStream() {
        return AC.stream(stream());
    }

    /**
     * A variant of {@link Streamable} that wraps any {@link IOException} thrown by the
     * {@link #streamIO()} method with an {@link UncheckedIOException}.
     */
    interface IO<T> extends Streamable<T> {

        Stream<T> streamIO() throws IOException;

        default Stream<T> stream() {
            try {
                return streamIO();
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }
    }

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

    default <R> Streamable<R> flatMap(Function<T, ? extends Stream<? extends R>> mapper) {
        return () -> stream().flatMap(mapper);
    }

    default IntStreamable flatMapToInt(Function<T, ? extends IntStream> mapper) {
        return () -> stream().flatMapToInt(mapper);
    }

    default LongStreamable flatMapToLong(Function<T, ? extends LongStream> mapper) {
        return () -> stream().flatMapToLong(mapper);
    }

    default DoubleStreamable flatMapToDouble(Function<T, ? extends DoubleStream> mapper) {
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

    default Streamable<T> skip(long n) {
        return () -> stream().skip(n);
    }
}
