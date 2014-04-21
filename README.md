streamx
=======

More or less useful extensions to java.util.stream API mainly for easing consumption of streams backed by
IO resources that must be closed.

Example:

    // using auto-closing Stream wrapper...

    AC.stream(Files.walk(Paths.get("/usr/share/doc")))     // walk paths in directory - close after terminal op.
        .filter(p -> p.toFile().isFile())                  // just take normal files
        .filter(p -> p.toString().endsWith(".txt"))        // and text files among them
        .flatMap(IO.function(                              // wrap IOException with UncheckedIOException
            p -> concat(Stream.of("#", "# " + p, "#"),     // concatenate path name header with
                        Files.lines(p))                    // expanded lines
        ))
        .forEach(System.out::println);                     // print them out (will also close the stream)

    // using Streamable functional interface as a factory for streams

    Streamable.IO<Path> docs = () -> Files.walk(Paths.get("/usr/share/doc")); // factory of path walk streams

    Streamable<String> lines = docs                        // factory of lines streams
        .filter(p -> p.toFile().isFile())                  // just take normal files
        .filter(p -> p.toString().endsWith(".txt"))        // and text files among them
        .flatMap(IO.function(                              // wrap IOException with UncheckedIOException
            p -> concat(Stream.of("#", "# " + p, "#"),     // concatenate path name header with
                        Files.lines(p))                    // expanded lines
        ));

    // ... invoke the factory and consume the stream...

    lines
        .autoClosingStream()                               // request an auto-closing stream
        .forEach(System.out::println);                     // print them out

    // ...or...

    try (Stream<String> linesStream = lines.stream()) {    // use try-with-resources on normal stream
        linesStream.forEach(System.out::println);          // print them out
    }


    // or almost entirely without this extra stuff...

    Function<Stream<Path>, Stream<Path>> filterTextFiles = ps ->
        ps.filter(p -> p.toFile().isFile())                // just take normal files
          .filter(p -> p.toString().endsWith(".txt"));     // and text files among them

    Function<Stream<Path>, Stream<String>> dumpLines = ps ->
        ps.flatMap(
            IO.function(                                   // wrap IOException with UncheckedIOException
                p -> concat(Stream.of("#", "# " + p, "#"), // concatenate path name header with
                            Files.lines(p))                // expanded lines
            )
        );

    Consumer<Stream<String>> printLines = ls -> ls.forEach(System.out::println);

    // ... apply this lazy transformations...

    printLines.andThen(Stream::close)
        .accept(
            filterTextFiles.andThen(dumpLines)
                .apply(
                    Files.walk(Paths.get("/usr/share/doc"))
                )
        );

