package io.github.interjacent.app.math;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntervalTest {
    private static Stream<Arguments> providePositiveIntersects() {
        return Stream.of(
            Arguments.of(10L, 20L, 15L, 25L, 15L, 20L),
            Arguments.of(-4L,  4L,  0L,  8L,  0L,  4L),
            Arguments.of(10L, 40L, 20L, 30L, 20L, 30L)
        );
    }

    private static Stream<Arguments> provideNegativeIntersects() {
        return Stream.of(
            Arguments.of(10L, 15L, 20L, 25L),
            Arguments.of(-4L,  0L,  4L,  8L),
            Arguments.of(10L, 20L, 20L, 30L)
        );
    }

    private static Stream<Arguments> providePositiveHasSingleUnion() {
        return Stream.of(
            Arguments.of(10L, 20L, 15L, 25L, 10L, 25L),
            Arguments.of(-4L,  4L,  0L,  8L, -4L,  8L),
            Arguments.of(10L, 40L, 20L, 30L, 10L, 40L),
            Arguments.of(10L, 20L, 20L, 30L, 10L, 30L)
        );
    }

    private static Stream<Arguments> provideNegativeHasSingleUnion() {
        return Stream.of(
            Arguments.of(10L, 15L, 20L, 25L),
            Arguments.of(-4L,  0L,  4L,  8L)
        );
    }

    @ParameterizedTest
    @MethodSource("providePositiveIntersects")
    void positiveIntersects(long b1, long e1, long b2, long e2, long b3, long e3) {
        Interval<Long> i1, i2, i3;
        i1 = new Interval<Long>(b1, e1);
        i2 = new Interval<Long>(b2, e2);
        i3 = new Interval<Long>(b3, e3);
        assertTrue(i1.intersects(i2), "12");
        assertTrue(i2.intersects(i1), "21");
        assertTrue(i1.narrowMaxMin(i2).equals(i3), "123");
        assertTrue(i2.narrowMaxMin(i1).equals(i3), "213");
    }

    @ParameterizedTest
    @MethodSource("provideNegativeIntersects")
    void negativeIntersects(long b1, long e1, long b2, long e2) {
        Interval<Long> i1, i2;
        i1 = new Interval<Long>(b1, e1);
        i2 = new Interval<Long>(b2, e2);
        assertFalse(i1.intersects(i2), "12");
        assertFalse(i2.intersects(i1), "21");
    }

    @ParameterizedTest
    @MethodSource("providePositiveHasSingleUnion")
    void positiveHasSingleUnion(long b1, long e1, long b2, long e2, long b3, long e3) {
        Interval<Long> i1, i2, i3;
        i1 = new Interval<Long>(b1, e1);
        i2 = new Interval<Long>(b2, e2);
        i3 = new Interval<Long>(b3, e3);
        assertTrue(i1.hasSingleUnion(i2), "12");
        assertTrue(i2.hasSingleUnion(i1), "21");
        assertTrue(i1.wideMinMax(i2).equals(i3), "123");
        assertTrue(i2.wideMinMax(i1).equals(i3), "213");
    }

    @ParameterizedTest
    @MethodSource("provideNegativeHasSingleUnion")
    void negativeHasSingleUnion(long b1, long e1, long b2, long e2) {
        Interval<Long> i1, i2;
        i1 = new Interval<Long>(b1, e1);
        i2 = new Interval<Long>(b2, e2);
        assertFalse(i1.hasSingleUnion(i2), "12");
        assertFalse(i2.hasSingleUnion(i1), "21");
    }

}
