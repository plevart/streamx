/*
 * Written by Peter Levart <peter.levart@gmail.com>
 * and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */
package test;

import si.pele.streamx.Streamable;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author peter
 */
public class Test {

    static class Person {
        final int age;
        final boolean hasCoupon;
        double price;

        Person(int age, boolean hasCoupon) {
            this.age = age;
            this.hasCoupon = hasCoupon;
        }

        int getAge() {
            return age;
        }

        boolean hasCoupon() {
            return hasCoupon;
        }

        double getPrice() {
            return price;
        }

        void setPrice(double price) {
            this.price = price;
        }
    }

    static void test1(List<Person> people) {

        people.parallelStream()
            .filter(p -> p.getAge() >= 12)
            .filter(p -> p.getAge() < 65)
            .forEach(p -> {
                p.setPrice(9.25);
            });

        people.parallelStream()
            .filter(p -> p.getAge() >= 12)
            .filter(p -> p.getAge() < 65)
            .filter(p -> p.hasCoupon())
            .forEach(p -> {
                p.setPrice(p.getPrice() - 2.00);
            });
    }

    static void test2(List<Person> people) {

        UnaryOperator<Stream<Person>> commonOps = (stream) ->
            stream
                .filter(p -> p.getAge() >= 12)
                .filter(p -> p.getAge() < 65);

        commonOps
            .apply(people.parallelStream())
            .forEach(p -> {
                p.setPrice(9.25);
            });

        commonOps
            .apply(people.parallelStream())
            .filter(p -> p.hasCoupon())
            .forEach(p -> {
                p.setPrice(p.getPrice() - 2.00);
            });
    }

    static void test4(List<Person> people) {

        Streamable<Person> peoples = people::parallelStream;

        Streamable<Person> filteredPeoples = peoples
            .filter(p -> p.getAge() >= 12)
            .filter(p -> p.getAge() < 65);

        filteredPeoples.stream()
            .forEach(p -> {
                p.setPrice(9.25);
            });

        filteredPeoples.stream()
            .filter(p -> p.hasCoupon())
            .forEach(p -> {
                p.setPrice(p.getPrice() - 2.00);
            });
    }

    public static void main(String[] args) {

    }
}
