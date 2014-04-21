streamx
=======

More or less useful extensions to java.util.stream API mainly for easing consumption of streams backed by
IO resources that must be closed.

Example:

        // using auto-closing Stream wrapper...

        AC.stream(Files.walk(Paths.get("/usr/share/doc")))     // walk paths in directory - close after terminal op.
            .filter(p -> p.toFile().isFile())                  // just take normal files
            .filter(p -> p.toString().endsWith(".txt"))        // and text files among them
            .peek(p -> System.out.printf("\n#\n# %s\n#\n", p)) // print out file path as header
            .flatMap(IO.function(Files::lines))                // dump lines (flatMap closes the lines stream)
            .forEach(System.out::println);                     // print them out (will also close the stream)

        // ...using Streamable functional interface as a factory for streams

        Streamable.IO<Path> docs = () -> Files.walk(Paths.get("/usr/share/doc")); // factory of path walk streams

        Streamable<String> lines = docs                        // factory of lines streams
            .filter(p -> p.toFile().isFile())                  // just take normal files
            .filter(p -> p.toString().endsWith(".txt"))        // and text files among them
            .peek(p -> System.out.printf("\n#\n# %s\n#\n", p)) // print out file path as header
            .flatMap(IO.function(Files::lines));               // dump lines (flatMap closes the lines stream)

        // ... invoke the factory and consume the stream...

        lines
            .autoClosingStream()                               // request an auto-closing stream
            .forEach(System.out::println);                     // print them out

        // or

        try (Stream<String> linesStream = lines.stream()) {    // use try-with-resources on normal stream
            linesStream.forEach(System.out::println);          // print them out
        }

