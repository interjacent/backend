package io.github.interjacent.app.math;

/**
 * Represents an interval of integers [begin; end)
 */
public record Interval<N extends Comparable<N>>(
    N begin,
    N end
) {
    /**
     * Checks if <code>this</code> is a valid interval, i.e. <code>begin &lt; end</code>.
     * @return result.
     */
    public boolean isValid() {
        return begin.compareTo(end) < 0;
    }

    /**
     * Checks if the intersection of <code>this</code> and <code>other</code>
     * can be represented with one interval.
     * Must meet conditions
     * <code>this.isValid() == true</code>
     * and
     * <code>other.isValid() == true</code>.
     * @param other &mdash; second interval.
     * @return result, if the conditions are met, otherwise, incorrect result.
     */
    public boolean intersects(Interval<N> other) {
        if (begin.compareTo(other.begin) < 0) {
            return end.compareTo(other.begin) > 0;
        }
        else if (begin.compareTo(other.begin) > 0) {
            return begin.compareTo(other.end) < 0;
        }
        else { // begin == other.begin
            return true;
        }
    }

    /**
     * Checks if the union of the <code>this</code> and <code>other</code>
     * can be represented with one interval.
     * Must meet conditions
     * <code>this.isValid() == true</code>
     * and
     * <code>other.isValid() == true</code>.
     * @param other &mdash; second interval
     * @return result, if the conditions are met, otherwise, incorrect result.
     */
    public boolean hasSingleUnion(Interval<N> other) {
        if (begin.compareTo(other.begin) < 0) {
            return end.compareTo(other.begin) >= 0;
        }
        else if (begin.compareTo(other.begin) < 0) {
            return begin.compareTo(other.end) <= 0;
        }
        else { // begin == other.begin
            return true;
        }
    }

    private N max(N a, N b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    private N min(N a, N b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    /**
     * Intersection of overlapping intervals.
     * Must meet conditions
     * <code>this.isValid() == true</code>,
     * <code>other.isValid() == true</code>,
     * and
     * <code>this.intersects(other) == true</code>.
     * @param other &mdash; second interval
     * @return intersection if the conditions are met, incorrect or invalid interval otherwise
     */
    public Interval<N> narrowMaxMin(Interval<N> other) {
        return new Interval<>(max(begin, other.begin), min(end, other.end));
    }

    /**
     * Union of overlapping or neighboring intervals.
     * Must meet conditions
     * <code>this.isValid() == true</code>,
     * <code>other.isValid() == true</code>,
     * and
     * <code>this.hasSingleUnion(other) == true</code>.
     * @param other &mdash; second interval
     * @return union if the conditions are met, incorrect or invalid interval otherwise
     */
    public Interval<N> wideMinMax(Interval<N> other) {
        return new Interval<>(min(begin, other.begin), max(end, other.end));
    }
}
