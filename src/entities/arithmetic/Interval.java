package entities.arithmetic;

/**
 * Record representing a continuous numeric interval $[min, max]$ for interval
 * arithmetic.
 *
 * @param min lower interval bound
 * @param max upper interval bound
 */
public record Interval(
        double min,
        double max) {

    /**
     * Compact constructor enforcing $min \le max$.
     *
     * @throws IllegalArgumentException if $min > max$
     */
    public Interval {
        if (min > max)
            throw new IllegalArgumentException("min can't be greater than max");
    }

    /**
     * Performs interval addition: $[a, b] + [c, d] = [a + c, b + d]$.
     *
     * @param other second interval
     * @return sum interval
     */
    public Interval add(Interval other) {
        return new Interval(this.min + other.min, this.max + other.max);
    }

    /**
     * Performs interval subtraction: $[a, b] - [c, d] = [a - d, b - c]$.
     *
     * @param other second interval
     * @return difference interval
     */
    public Interval subtract(Interval other) {
        return new Interval(this.min - other.max, this.max - other.min);
    }

    /**
     * Performs interval multiplication.
     *
     * @param other second interval
     * @return product interval
     */
    public Interval multiply(Interval other) {
        double p1 = this.min * other.min;
        double p2 = this.min * other.max;
        double p3 = this.max * other.min;
        double p4 = this.max * other.max;

        double min = Math.min(Math.min(p1, p2), Math.min(p3, p4));
        double max = Math.max(Math.max(p1, p2), Math.max(p3, p4));

        return new Interval(min, max);
    }

    /**
     * Performs interval division.
     *
     * @param other divisor interval
     * @return quotient interval
     * @throws ArithmeticException if divisor interval contains zero ($min \le 0 \le
     *                             max$)
     */
    public Interval divide(Interval other) {
        if (other.min <= 0 && other.max >= 0) {
            throw new ArithmeticException("Divisione per un intervallo che contiene lo zero.");
        }
        double p1 = this.min / other.min;
        double p2 = this.min / other.max;
        double p3 = this.max / other.min;
        double p4 = this.max / other.max;

        double min = Math.min(Math.min(p1, p2), Math.min(p3, p4));
        double max = Math.max(Math.max(p1, p2), Math.max(p3, p4));

        return new Interval(min, max);
    }

}
