/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A set of stream wrappers that <b>A</b>utomatically <b>C</b>lose the underlying stream after a terminal operation.
 *
 * @see #stream(Stream)
 * @see #intStream(IntStream)
 * @see #longStream(LongStream)
 * @see #doubleStream(DoubleStream)
 */
public final class AC {

    private AC() {} // no instances

    /**
     * @return a {@link Stream} wrapper that automatically closes underlying stream after a terminal operation
     *         except after {@link Stream#iterator()} or {@link Stream#spliterator()} which are lazy terminal operations.
     */
    public static <T> Stream<T> stream(Stream<T> s) { return new ACStream<>(s); }

    /**
     * @return an {@link IntStream} wrapper that automatically closes underlying stream after a terminal operation
     *         except after {@link IntStream#iterator()} or {@link IntStream#spliterator()} which are lazy terminal operations.
     */
    public static IntStream intStream(IntStream s) { return new ACIntStream(s); }

    /**
     * @return a {@link LongStream} wrapper that automatically closes underlying stream after a terminal operation
     *         except after {@link LongStream#iterator()} or {@link LongStream#spliterator()} which are lazy terminal operations.
     */
    public static LongStream longStream(LongStream s) { return new ACLongStream(s); }

    /**
     * @return a {@link DoubleStream} wrapper that automatically closes underlying stream after a terminal operation
     *         except after {@link DoubleStream#iterator()} or {@link DoubleStream#spliterator()} which are lazy terminal operations.
     */
    public static DoubleStream doubleStream(DoubleStream s) { return new ACDoubleStream(s); }

    /**
     * {@link BaseStream} auto-closing wrapper.
     */
    abstract static class ACBaseStream<T, S extends BaseStream<T, S>> implements BaseStream<T, S> {

        final S s;

        ACBaseStream(S s) { this.s = s; }

        abstract S wrap(S s);

        //
        // intermediary operations: delegate + wrap

        public S onClose(Runnable closeHandler) {return wrap(s.onClose(closeHandler));}

        public S sequential() {return wrap(s.sequential());}

        public S parallel() {return wrap(s.parallel());}

        public S unordered() {return wrap(s.unordered());}

        //
        // query and close: just delegate

        public boolean isParallel() {return s.isParallel();}

        public void close() {s.close();}
    }

    /**
     * {@link Stream} auto-closing wrapper.
     */
    static final class ACStream<T> extends ACBaseStream<T, Stream<T>> implements Stream<T> {

        ACStream(Stream<T> s) { super(s); }

        Stream<T> wrap(Stream<T> s) { return (s == this.s) ? this : stream(s); }

        //
        // intermediary operations: delegate + wrap

        public Stream<T> filter(Predicate<? super T> predicate) {return wrap(s.filter(predicate));}

        public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {return stream(s.map(mapper));}

        public IntStream mapToInt(ToIntFunction<? super T> mapper) {return intStream(s.mapToInt(mapper));}

        public LongStream mapToLong(ToLongFunction<? super T> mapper) {return longStream(s.mapToLong(mapper));}

        public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
            return doubleStream(s.mapToDouble(mapper));
        }

        public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
            return stream(s.flatMap(mapper));
        }

        public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
            return intStream(s.flatMapToInt(mapper));
        }

        public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
            return longStream(s.flatMapToLong(mapper));
        }

        public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
            return doubleStream(s.flatMapToDouble(mapper));
        }

        public Stream<T> distinct() {return wrap(s.distinct());}

        public Stream<T> sorted() {return wrap(s.sorted());}

        public Stream<T> sorted(Comparator<? super T> comparator) {return wrap(s.sorted(comparator));}

        public Stream<T> peek(Consumer<? super T> action) {return wrap(s.peek(action));}

        public Stream<T> limit(long maxSize) {return wrap(s.limit(maxSize));}

        public Stream<T> skip(long n) {return wrap(s.skip(n));}

        //
        // terminal operations: delegate + close

        public void forEach(Consumer<? super T> action) { try (Stream<T> s = this.s) {s.forEach(action);} }

        public void forEachOrdered(Consumer<? super T> action) {
            try (Stream<T> s = this.s) {s.forEachOrdered(action);}
        }

        public Object[] toArray() { try (Stream<T> s = this.s) {return s.toArray();} }

        public <A> A[] toArray(IntFunction<A[]> generator) { try (Stream<T> s = this.s) {return s.toArray(generator);} }

        public T reduce(T identity, BinaryOperator<T> accumulator) {
            try (Stream<T> s = this.s) {return s.reduce(identity, accumulator);}
        }

        public Optional<T> reduce(BinaryOperator<T> accumulator) {
            try (Stream<T> s = this.s) {return s.reduce(accumulator);}
        }

        public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
            try (Stream<T> s = this.s) {return s.reduce(identity, accumulator, combiner); }
        }

        public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
            try (Stream<T> s = this.s) {return s.collect(supplier, accumulator, combiner); }
        }

        public <R, A> R collect(Collector<? super T, A, R> collector) {
            try (Stream<T> s = this.s) {return s.collect(collector);}
        }

        public Optional<T> min(Comparator<? super T> comparator) {
            try (Stream<T> s = this.s) {return s.min(comparator);}
        }

        public Optional<T> max(Comparator<? super T> comparator) {
            try (Stream<T> s = this.s) {return s.max(comparator);}
        }

        public long count() { try (Stream<T> s = this.s) {return s.count();} }

        public boolean anyMatch(Predicate<? super T> predicate) {
            try (Stream<T> s = this.s) {return s.anyMatch(predicate);}
        }

        public boolean allMatch(Predicate<? super T> predicate) {
            try (Stream<T> s = this.s) {return s.allMatch(predicate);}
        }

        public boolean noneMatch(Predicate<? super T> predicate) {
            try (Stream<T> s = this.s) {return s.noneMatch(predicate);}
        }

        public Optional<T> findFirst() { try (Stream<T> s = this.s) {return s.findFirst();} }

        public Optional<T> findAny() { try (Stream<T> s = this.s) {return s.findAny();} }

        //
        // lazy terminal operations: just delegate (we can't close immediately)

        public Iterator<T> iterator() { try (Stream<T> s = this.s) {return s.iterator();} }

        public Spliterator<T> spliterator() { try (Stream<T> s = this.s) {return s.spliterator();} }
    }

    /**
     * {@link IntStream} auto-closing wrapper.
     */
    static final class ACIntStream extends ACBaseStream<Integer, IntStream> implements IntStream {

        ACIntStream(IntStream s) { super(s); }

        @Override
        IntStream wrap(IntStream s) { return (s == this.s) ? this : intStream(s); }

        //
        // intermediary operations: delegate + wrap

        public IntStream filter(IntPredicate predicate) {return wrap(s.filter(predicate));}

        public IntStream map(IntUnaryOperator mapper) {return wrap(s.map(mapper));}

        public <U> Stream<U> mapToObj(IntFunction<? extends U> mapper) {return stream(s.mapToObj(mapper));}

        public LongStream mapToLong(IntToLongFunction mapper) {return longStream(s.mapToLong(mapper));}

        public DoubleStream mapToDouble(IntToDoubleFunction mapper) {return doubleStream(s.mapToDouble(mapper));}

        public IntStream flatMap(IntFunction<? extends IntStream> mapper) {return wrap(s.flatMap(mapper));}

        public IntStream distinct() {return wrap(s.distinct());}

        public IntStream sorted() {return wrap(s.sorted());}

        public IntStream peek(IntConsumer action) {return wrap(s.peek(action));}

        public IntStream limit(long maxSize) {return wrap(s.limit(maxSize));}

        public IntStream skip(long n) {return wrap(s.skip(n));}

        public LongStream asLongStream() {return longStream(s.asLongStream());}

        public DoubleStream asDoubleStream() {return doubleStream(s.asDoubleStream());}

        public Stream<Integer> boxed() {return stream(s.boxed());}

        //
        // terminal operations: delegate + close

        public void forEach(IntConsumer action) { try (IntStream s = this.s) {s.forEach(action);} }

        public void forEachOrdered(IntConsumer action) { try (IntStream s = this.s) {s.forEachOrdered(action);} }

        public int[] toArray() { try (IntStream s = this.s) {return s.toArray();} }

        public int reduce(int identity, IntBinaryOperator op) {
            try (IntStream s = this.s) {return s.reduce(identity, op);}
        }

        public OptionalInt reduce(IntBinaryOperator op) { try (IntStream s = this.s) {return s.reduce(op);} }

        public <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R, R> combiner) {
            try (IntStream s = this.s) {return s.collect(supplier, accumulator, combiner);}
        }

        public int sum() { try (IntStream s = this.s) {return s.sum();} }

        public OptionalInt min() { try (IntStream s = this.s) {return s.min();} }

        public OptionalInt max() { try (IntStream s = this.s) {return s.max();} }

        public long count() { try (IntStream s = this.s) {return s.count();} }

        public OptionalDouble average() { try (IntStream s = this.s) {return s.average();} }

        public IntSummaryStatistics summaryStatistics() { try (IntStream s = this.s) {return s.summaryStatistics();} }

        public boolean anyMatch(IntPredicate predicate) { try (IntStream s = this.s) {return s.anyMatch(predicate);} }

        public boolean allMatch(IntPredicate predicate) { try (IntStream s = this.s) {return s.allMatch(predicate);} }

        public boolean noneMatch(IntPredicate predicate) { try (IntStream s = this.s) {return s.noneMatch(predicate);} }

        public OptionalInt findFirst() { try (IntStream s = this.s) {return s.findFirst();} }

        public OptionalInt findAny() { try (IntStream s = this.s) {return s.findAny();} }

        //
        // lazy terminal operations: just delegate (we can't close immediately)

        public PrimitiveIterator.OfInt iterator() {return s.iterator();}

        public Spliterator.OfInt spliterator() {return s.spliterator();}
    }

    /**
     * {@link LongStream} auto-closing wrapper.
     */
    static final class ACLongStream extends ACBaseStream<Long, LongStream> implements LongStream {

        ACLongStream(LongStream s) { super(s); }

        @Override
        LongStream wrap(LongStream s) { return (s == this.s) ? this : longStream(s); }

        //
        // intermediary operations: delegate + wrap

        public LongStream filter(LongPredicate predicate) {return wrap(s.filter(predicate));}

        public LongStream map(LongUnaryOperator mapper) {return wrap(s.map(mapper));}

        public <U> Stream<U> mapToObj(LongFunction<? extends U> mapper) {return stream(s.mapToObj(mapper));}

        public IntStream mapToInt(LongToIntFunction mapper) {return intStream(s.mapToInt(mapper));}

        public DoubleStream mapToDouble(LongToDoubleFunction mapper) {return doubleStream(s.mapToDouble(mapper));}

        public LongStream flatMap(LongFunction<? extends LongStream> mapper) {return wrap(s.flatMap(mapper));}

        public LongStream distinct() {return wrap(s.distinct());}

        public LongStream sorted() {return wrap(s.sorted());}

        public LongStream peek(LongConsumer action) {return wrap(s.peek(action));}

        public LongStream limit(long maxSize) {return wrap(s.limit(maxSize));}

        public LongStream skip(long n) {return wrap(s.skip(n));}

        public DoubleStream asDoubleStream() {return doubleStream(s.asDoubleStream());}

        public Stream<Long> boxed() {return stream(s.boxed());}

        //
        // terminal operations: delegate + close

        public void forEach(LongConsumer action) { try (LongStream s = this.s) {s.forEach(action);} }

        public void forEachOrdered(LongConsumer action) { try (LongStream s = this.s) {s.forEachOrdered(action);} }

        public long[] toArray() { try (LongStream s = this.s) {return s.toArray();} }

        public long reduce(long identity, LongBinaryOperator op) {
            try (LongStream s = this.s) {return s.reduce(identity, op);}
        }

        public OptionalLong reduce(LongBinaryOperator op) { try (LongStream s = this.s) {return s.reduce(op);} }

        public <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator, BiConsumer<R, R> combiner) {
            try (LongStream s = this.s) { return s.collect(supplier, accumulator, combiner); }
        }

        public long sum() { try (LongStream s = this.s) {return s.sum();} }

        public OptionalLong min() { try (LongStream s = this.s) {return s.min();} }

        public OptionalLong max() { try (LongStream s = this.s) {return s.max();} }

        public long count() { try (LongStream s = this.s) {return s.count();} }

        public OptionalDouble average() { try (LongStream s = this.s) {return s.average();} }

        public LongSummaryStatistics summaryStatistics() { try (LongStream s = this.s) {return s.summaryStatistics();} }

        public boolean anyMatch(LongPredicate predicate) { try (LongStream s = this.s) {return s.anyMatch(predicate);} }

        public boolean allMatch(LongPredicate predicate) { try (LongStream s = this.s) {return s.allMatch(predicate);} }

        public boolean noneMatch(LongPredicate predicate) {
            try (LongStream s = this.s) {return s.noneMatch(predicate);}
        }

        public OptionalLong findFirst() { try (LongStream s = this.s) {return s.findFirst();} }

        public OptionalLong findAny() { try (LongStream s = this.s) {return s.findAny();} }

        //
        // lazy terminal operations: just delegate (we can't close immediately)

        public PrimitiveIterator.OfLong iterator() {return s.iterator();}

        public Spliterator.OfLong spliterator() {return s.spliterator();}
    }

    /**
     * {@link DoubleStream} auto-closing wrapper.
     */
    static final class ACDoubleStream extends ACBaseStream<Double, DoubleStream> implements DoubleStream {

        ACDoubleStream(DoubleStream s) { super(s); }

        @Override
        DoubleStream wrap(DoubleStream s) { return (s == this.s) ? this : doubleStream(s); }

        //
        // intermediary operations: delegate + wrap

        public DoubleStream filter(DoublePredicate predicate) {return wrap(s.filter(predicate));}

        public DoubleStream map(DoubleUnaryOperator mapper) {return wrap(s.map(mapper));}

        public <U> Stream<U> mapToObj(DoubleFunction<? extends U> mapper) {return stream(s.mapToObj(mapper));}

        public IntStream mapToInt(DoubleToIntFunction mapper) {return intStream(s.mapToInt(mapper));}

        public LongStream mapToLong(DoubleToLongFunction mapper) {return longStream(s.mapToLong(mapper));}

        public DoubleStream flatMap(DoubleFunction<? extends DoubleStream> mapper) {return wrap(s.flatMap(mapper));}

        public DoubleStream distinct() {return wrap(s.distinct());}

        public DoubleStream sorted() {return wrap(s.sorted());}

        public DoubleStream peek(DoubleConsumer action) {return wrap(s.peek(action));}

        public DoubleStream limit(long maxSize) {return wrap(s.limit(maxSize));}

        public DoubleStream skip(long n) {return wrap(s.skip(n));}

        public Stream<Double> boxed() {return stream(s.boxed());}

        //
        // terminal operations: delegate + close

        public void forEach(DoubleConsumer action) { try (DoubleStream s = this.s) {s.forEach(action);} }

        public void forEachOrdered(DoubleConsumer action) { try (DoubleStream s = this.s) {s.forEachOrdered(action);} }

        public double[] toArray() { try (DoubleStream s = this.s) {return s.toArray();} }

        public double reduce(double identity, DoubleBinaryOperator op) {
            try (DoubleStream s = this.s) {return s.reduce(identity, op);}
        }

        public OptionalDouble reduce(DoubleBinaryOperator op) { try (DoubleStream s = this.s) {return s.reduce(op);} }

        public <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> accumulator, BiConsumer<R, R> combiner) {
            try (DoubleStream s = this.s) { return s.collect(supplier, accumulator, combiner); }
        }

        public double sum() { try (DoubleStream s = this.s) {return s.sum();} }

        public OptionalDouble min() { try (DoubleStream s = this.s) {return s.min();} }

        public OptionalDouble max() { try (DoubleStream s = this.s) {return s.max();} }

        public long count() { try (DoubleStream s = this.s) {return s.count();} }

        public OptionalDouble average() { try (DoubleStream s = this.s) {return s.average();} }

        public DoubleSummaryStatistics summaryStatistics() {
            try (DoubleStream s = this.s) {return s.summaryStatistics();}
        }

        public boolean anyMatch(DoublePredicate predicate) {
            try (DoubleStream s = this.s) {return s.anyMatch(predicate);}
        }

        public boolean allMatch(DoublePredicate predicate) {
            try (DoubleStream s = this.s) {return s.allMatch(predicate);}
        }

        public boolean noneMatch(DoublePredicate predicate) {
            try (DoubleStream s = this.s) {return s.noneMatch(predicate);}
        }

        public OptionalDouble findFirst() { try (DoubleStream s = this.s) {return s.findFirst();} }

        public OptionalDouble findAny() { try (DoubleStream s = this.s) {return s.findAny();} }

        //
        // lazy terminal operations: just delegate (we can't close immediately)

        public PrimitiveIterator.OfDouble iterator() { return s.iterator(); }

        public Spliterator.OfDouble spliterator() { return s.spliterator(); }
    }
}
