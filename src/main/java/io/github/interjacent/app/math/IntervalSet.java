package io.github.interjacent.app.math;

import java.util.*;

/**
 * Represents a set of numbers as non-intersecting intervals
 * sorted by ascendence. 
 */
public class IntervalSet<N extends Comparable<N>> {
    /**
     * Sorted by ascendence
     */
    private List<Interval<N>> intervals;
    
    /**
     * Creates an empty IntervalSet
     */
    public IntervalSet() {
        intervals = new ArrayList<>();
    }

    /**
     * Creates an IntervalSet with one interval
     */
    public IntervalSet(Interval<N> interval) {
        intervals = new ArrayList<>();
        if (interval.begin().equals(interval.end())) {
            return;
        }
        if (interval.begin().compareTo(interval.end()) > 0) {
            throw new IllegalArgumentException("creating interval set with interval with begin > end");
        }
        intervals.add(interval);
    }

    /**
     * Unionizes overlapping intervals.
     */
    private void simplify() {
        if (intervals.isEmpty()) {
            return;
        }

        List<Interval<N>> newIntervals = new ArrayList<>();
        Interval<N> toAdd = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            Interval<N> next = intervals.get(i);
            if (toAdd.hasSingleUnion(next)) {
                toAdd = toAdd.wideMinMax(next);
            }
            else {
                newIntervals.add(toAdd);
                toAdd = next;
            }
        }
        newIntervals.add(toAdd);
        intervals = newIntervals;
    }

    /**
     * Adds an interval to the set with a union operation.
     * @param interval &mdash; interval to append.
     */
    public void add(Interval<N> interval) {
        if (!interval.isValid()) {
            return;
        }
        
        boolean added = false;
        List<Interval<N>> newIntervals = new ArrayList<>();
        for (int i = 0; i < intervals.size(); i++) {
            Interval<N> next = intervals.get(i);
            if (!added && next.begin().compareTo(interval.begin()) > 0) {
                newIntervals.add(interval);
            }
            newIntervals.add(next);
        }

        simplify();
    }

    public List<Interval<N>> getIntervals() {
        return intervals;
    }

    /**
     * Calculates the intersection of two sets that are defined as a list of intervals.
     * Better to use with streams.
     * @param one &mdash; first set.
     * @param two &mdash; second set.
     * @return resulting set.
     */
    public IntervalSet<N> intersection(IntervalSet<N> two) {
        IntervalSet<N> retval = new IntervalSet<>();
        for (Interval<N> a : getIntervals()) {
            for (Interval<N> b : two.getIntervals()) {
                if (a.intersects(b)) {
                    retval.add(a.narrowMaxMin(b));
                }
            }
        }
        return retval;
    }
}
