/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package test;

import si.pele.streamx.AC;
import si.pele.streamx.IO;
import si.pele.streamx.Streamable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author peter
 */
public class Test {

    public static void main(String[] args) throws IOException {

        // using auto-closing Stream wrapper...

        AC.stream(Files.walk(Paths.get("/usr/share/doc")))     // walk paths in directory - close after terminal op.
            .filter(p -> p.toFile().isFile())                  // just take normal files
            .filter(p -> p.toString().endsWith(".txt"))        // and text files among them
            .peek(p -> System.out.printf("\n#\n# %s\n#\n", p)) // print out file path as header
            .flatMap(IO.function(Files::lines))                // dump lines (flatMap closes the lines stream)
            .forEach(System.out::println);                     // print them out

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
    }
}
