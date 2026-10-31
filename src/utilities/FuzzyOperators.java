package utilities;

/**
 * Utility static helper functions providing primitive fuzzy logical operations
 * (AND, OR, NOT).
 */
public final class FuzzyOperators {

    /**
     * Fuzzy AND operator using minimum t-norm.
     *
     * @param a degree of membership in $[0, 1]$
     * @param b degree of membership in $[0, 1]$
     * @return minimum of a and b
     * @throws IllegalArgumentException if inputs fall outside $[0, 1]$
     */
    public static double and(double a, double b) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("Invalid value a: " + a);
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("Invalid value b: " + b);

        return Math.min(a, b);
    }

    /**
     * Fuzzy OR operator using maximum t-conorm.
     *
     * @param a degree of membership in $[0, 1]$
     * @param b degree of membership in $[0, 1]$
     * @return maximum of a and b
     * @throws IllegalArgumentException if inputs fall outside $[0, 1]$
     */
    public static double or(double a, double b) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("Invalid value a: " + a);
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("Invalid value b: " + b);

        return Math.max(a, b);
    }

    /**
     * Standard Fuzzy NOT operator ($1 - a$).
     *
     * @param a degree of membership in $[0, 1]$
     * @return standard complement
     * @throws IllegalArgumentException if input falls outside $[0, 1]$
     */
    public static double not(double a) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("Invalid value a: " + a);

        return 1d - a;
    }

}
