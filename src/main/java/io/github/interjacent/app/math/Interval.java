package io.github.interjacent.app.math;

public record Interval(
    long begin,
    long end
) {
    /**
     * Checks if <code>this</code> is a valid interval, i.e. <code>begin &lt; end</code>.
     * @return result.
     */
    public boolean isValid() {
        return begin < end;
    }

    /**
     * Checks if the intersection of <code>this</code> and <code>other</code>
     * can be represented with one interval.
     * Must meet conditions
     * <code>this.isValid() == true</code>
     * and
     * <code>other.isValid() == true</code>.
     * @param other second interval.
     * @return result, if the conditions are met, otherwise, incorrect result.
     */
    public boolean intersects(Interval other) {
        if (begin < other.begin) {
            return end > other.begin;
        }
        else if (begin > other.begin) {
            return begin < other.end;
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
     * @param other second interval
     * @return result, if the conditions are met, otherwise, incorrect result.
     */
    public boolean hasSingleUnion(Interval other) {
        if (begin < other.begin) {
            return end >= other.begin;
        }
        else if (begin > other.begin) {
            return begin <= other.end;
        }
        else { // begin == other.begin
            return true;
        }
    }

    private static long max(long a, long b) {
        return a > b ? a : b;
    }

    private static long min(long a, long b) {
        return a < b ? a : b;
    }

    /**
     * Intersection of overlapping intervals.
     * Must meet conditions
     * <code>this.isValid() == true</code>,
     * <code>other.isValid() == true</code>,
     * and
     * <code>this.intersects(other) == true</code>.
     * @param other second interval
     * @return intersection if the conditions are met, incorrect or invalid interval otherwise
     */
    public Interval narrowMaxMin(Interval other) {
        return new Interval(max(begin, other.begin), min(end, other.end));
    }

    /**
     * Union of overlapping or neighboring intervals.
     * Must meet conditions
     * <code>this.isValid() == true</code>,
     * <code>other.isValid() == true</code>,
     * and
     * <code>this.hasSingleUnion(other) == true</code>.
     * @param other second interval
     * @return union if the conditions are met, incorrect or invalid interval otherwise
     */
    public Interval wideMinMax(Interval other) {
        return new Interval(min(begin, other.begin), max(end, other.end));
    }
}
