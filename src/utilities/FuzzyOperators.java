package utilities;

public final class FuzzyOperators {

    public static double and(double a, double b) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("Invalid value a: " + a);
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("Invalid value b: " + b);

        return Math.min(a, b);
    }

    public static double or(double a, double b) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("Invalid value a: " + a);
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("Invalid value b: " + b);

        return Math.max(a, b);
    }

    public static double not(double a) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("Invalid value a: " + a);

        return 1d - a;
    }

}
