/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package si.pele.streamx;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * A set of function types and static generic methods taking and returning them, that wrap any {@link IOException}
 * thrown by the underlying function with an {@link UncheckedIOException}.
 */
public final class IO {

    private IO() {} // no instances

    public static <T, R> Function<T, R> function(Function<T, R> f) { return f; }

    public static <T> Consumer<T> consumer(Consumer<T> c) { return c; }

    /**
     * An {@link IOException} wrapping {@link java.util.function.Function}
     */
    @FunctionalInterface
    public interface Function<T, R> extends java.util.function.Function<T, R> {

        R applyIO(T t) throws IOException;

        @Override
        default R apply(T t) {
            try {
                return applyIO(t);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }
    }


    /**
     * An {@link IOException} wrapping {@link java.util.function.Consumer}
     */
    @FunctionalInterface
    public static interface Consumer<T> extends java.util.function.Consumer<T> {

        void acceptIO(T t) throws IOException;

        @Override
        default void accept(T t) {
            try {
                acceptIO(t);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }
    }
}
