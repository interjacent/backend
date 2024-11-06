package io.github.interjacent.app.math;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntervalSetTest {
    private static Stream<Arguments> provideValidAdding() {
        return Stream.of(
            Arguments.of(
                List.of(
                    new Interval<Long>(0L, 1L)
                ),
                List.of(
                    new Interval<Long>(0L, 1L)
                )
            ),
            Arguments.of(
                List.of(
                    new Interval<Long>(0L, 1L),
                    new Interval<Long>(2L, 4L),
                    new Interval<Long>(10L, 15L),
                    new Interval<Long>(20L, 30L),
                    new Interval<Long>(40L, 48L)
                ),
                List.of(
                    new Interval<Long>(0L, 1L),
                    new Interval<Long>(2L, 4L),
                    new Interval<Long>(10L, 15L),
                    new Interval<Long>(20L, 30L),
                    new Interval<Long>(40L, 48L)
                )
            ),
            Arguments.of(
                List.of(
                    new Interval<Long>(0L, 2L),
                    new Interval<Long>(1L, 4L),
                    new Interval<Long>(3L, 6L),
                    new Interval<Long>(5L, 8L),
                    new Interval<Long>(7L, 10L),
                    new Interval<Long>(9L, 11L)
                ),
                List.of(
                    new Interval<Long>(0L, 11L)
                )
            ),
            Arguments.of(
                List.of(
                    new Interval<Long>(0L, 10L),
                    new Interval<Long>(12L, 15L),
                    new Interval<Long>(5L, 6L)
                ),
                List.of(
                    new Interval<Long>(0L, 10L),
                    new Interval<Long>(12L, 15L)
                )
            )
        );
    }

    private static Stream<Arguments> provideValidIntersection() {
        return Stream.of(
            Arguments.of(
                List.of(
                    new Interval<Long>(0L, 1L)
                ),
                List.of(
                    new Interval<Long>(0L, 1L)
                ),
                List.of(
                    new Interval<Long>(0L, 1L)
                )
            ),
            Arguments.of(
                List.of(
                    new Interval<Long>(0L, 10L),
                    new Interval<Long>(20L, 30L)
                ),
                List.of(
                    new Interval<Long>(5L, 15L),
                    new Interval<Long>(25L, 35L)
                ),
                List.of(
                    new Interval<Long>(5L, 10L),
                    new Interval<Long>(25L, 30L)
                )
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidAdding")
    final void validAdding(List<Interval<Long>> intervalsIn, List<Interval<Long>> intervalsOut) {
        IntervalSet<Long> set = new IntervalSet<>();
        for (int i = 0; i < intervalsIn.size(); i++) {
            set.add(intervalsIn.get(i));
            assertTrue(set.isValid(), "valid " + i);
        }
        assertTrue(set.getIntervals().equals(intervalsOut));
    }

    @ParameterizedTest
    @MethodSource("provideValidIntersection")
    final void validIntersection(List<Interval<Long>> intervals1,
                                 List<Interval<Long>> intervals2,
                                 List<Interval<Long>> intervalsResult) {
        IntervalSet<Long> set1 = new IntervalSet<>();
        IntervalSet<Long> set2 = new IntervalSet<>();
        for (var interval : intervals1) {
            set1.add(interval);
        }
        for (var interval : intervals2) {
            set2.add(interval);
        }
        assertTrue(set1.intersection(set2).getIntervals().equals(intervalsResult));
    }
}
