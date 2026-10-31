package entities.number;

import java.util.Arrays;
import java.util.Objects;

import utilities.CastUtilities;

/**
 * Represents a Triangular Fuzzy Number defined by three parameters $(a, b, c)$
 * such that $a \le b \le c$.
 *
 * @param <T> the numeric type of domain
 */
public class TriangularFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double a;
    private double b;
    private double c;

    /**
     * Constructs an unnamed Triangular Fuzzy Number.
     *
     * @param a                left support bound
     * @param b                peak/modal value
     * @param c                right support bound
     * @param numericTypeClass domain type class
     * @throws IllegalArgumentException if not $a \le b \le c$
     */
    public TriangularFuzzyNumber(T a, T b, T c, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();

        if (!(this.a <= this.b && this.b <= this.c))
            throw new IllegalArgumentException("The values ​​must be: a <= b <= c");
    }

    /**
     * Constructs a named Triangular Fuzzy Number.
     *
     * @param name             fuzzy number name
     * @param a                left support bound
     * @param b                peak/modal value
     * @param c                right support bound
     * @param numericTypeClass domain type class
     * @throws IllegalArgumentException if not $a \le b \le c$
     */
    public TriangularFuzzyNumber(String name, T a, T b, T c, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();

        if (!(this.a <= this.b && this.b <= this.c))
            throw new IllegalArgumentException("The values ​​must be: a <= b <= c");
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType(b, getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(a, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(c, getNumericTypeClass());
    }

    @Override
    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(a, getNumericTypeClass());
        else {
            double result = a + alphaValue * (b - a);
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    @Override
    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(c, getNumericTypeClass());
        else {
            double result = c - alphaValue * (c - b);
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        if (x < a || x > c)
            return 0d;
        if (x >= a && x <= b)
            return (x - a) / (b - a);
        else // (x >= b && x <= c)
            return (c - x) / (c - b);
    }

    /**
     * Performs fuzzy addition with another triangular fuzzy number.
     *
     * @param newName result name
     * @param other   operand
     * @return new added triangular fuzzy number
     */
    public TriangularFuzzyNumber<T> add(String newName, TriangularFuzzyNumber<T> other) {
        double newA = this.a + other.a;
        double newB = this.b + other.b;
        double newC = this.c + other.c;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());

        return newName == null ? new TriangularFuzzyNumber<T>(convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

    /**
     * Performs fuzzy subtraction with another triangular fuzzy number.
     *
     * @param newName result name
     * @param other   operand
     * @return new subtracted triangular fuzzy number
     */
    public TriangularFuzzyNumber<T> subtract(String newName, TriangularFuzzyNumber<T> other) {
        double newA = this.a - other.c;
        double newB = this.b - other.b;
        double newC = this.c - other.a;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());

        return newName == null ? new TriangularFuzzyNumber<T>(convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

    /**
     * Performs scalar multiplication on this triangular fuzzy number.
     *
     * @param newName result name
     * @param scalar  scalar coefficient
     * @return scaled triangular fuzzy number
     */
    public TriangularFuzzyNumber<T> multiply(String newName, double scalar) {
        double[] values = new double[3];

        values[0] = this.a * scalar;
        values[1] = this.b * scalar;
        values[2] = this.c * scalar;

        Arrays.sort(values);

        T convNewA = CastUtilities.convertToType(values[0], getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(values[1], getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(values[2], getNumericTypeClass());

        return newName == null
                ? new TriangularFuzzyNumber<T>(this.getName(), convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof TriangularFuzzyNumber<?> that))
            return false;

        return Double.compare(that.a, a) == 0 &&
                Double.compare(that.b, b) == 0 &&
                Double.compare(that.c, c) == 0 &&
                Objects.equals(that.getName(), getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), a, b, c);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("TriangularFuzzyNumber")
                .append("[")
                .append(getName() != null ? getName() + ", " : "")
                .append("a=")
                .append(a)
                .append(", b=")
                .append(b)
                .append(", c=")
                .append(c)
                .append("]");

        return builder.toString();
    }

}
