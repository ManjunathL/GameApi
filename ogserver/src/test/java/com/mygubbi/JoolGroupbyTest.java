package com.mygubbi;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;

import java.util.List;
import java.util.stream.Collectors;

import static org.jooq.lambda.tuple.Tuple.tuple;

/**
 * Created by Sunil on 29-05-2016.
 */

public class JoolGroupbyTest
{
    private void testGroupby()
    {
        List<Foo> list =

// FROM Foo
                Seq.of(
                        new Foo(1, "P1", 300, 400),
                        new Foo(2, "P2", 2600, 1400),
                        new Foo(3, "P3", 30, 20),
                        new Foo(3, "P3", 70, 20),
                        new Foo(1, "P1", 360, 40),
                        new Foo(4, "P4", 320, 200),
                        new Foo(4, "P4", 500, 900))

// GROUP BY f1, f2
                        .groupBy(
                                x -> tuple(x.f1, x.f2),

// SELECT SUM(f3), SUM(f4)
                                Tuple.collectors(
                                        Collectors.summingInt(x -> x.f3),
                                        Collectors.summingInt(x -> x.f4)
                                )
                        )

// Transform the Map<Tuple2<Integer, String>, Tuple2<Integer, Integer>> type to List<Foo>
                        .entrySet()
                        .stream()
                        .map(e -> new Foo(e.getKey().v1, e.getKey().v2, e.getValue().v1, e.getValue().v2))
                        .collect(Collectors.toList());

        System.out.println(list);
    }

    public static void main(String[] args)
    {
        new JoolGroupbyTest().testGroupby();
    }

    public static class Foo
    {
        public int f1;
        public String f2;
        public int f3;
        public int f4;

        public Foo(int f1, String f2, int f3, int f4)
        {
            this.f1 = f1;
            this.f2 = f2;
            this.f3 = f3;
            this.f4 = f4;
        }

        @Override
        public String toString()
        {
            return "Foo{" +
                    "f1=" + f1 +
                    ", f2='" + f2 + '\'' +
                    ", f3=" + f3 +
                    ", f4=" + f4 +
                    '}';
        }
    }
}


