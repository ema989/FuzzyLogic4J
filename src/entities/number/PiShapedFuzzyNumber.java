package entities.number;

import java.util.Objects;

import utilities.CastUtilities;

/**
 * Represents a Pi-shaped fuzzy number defined by four parameters (a, b, c, d).
 * The membership function transitions smoothly from 0 to 1 and back to 0.
 * 
 * @param <T> the numeric type extending Number
 */
public class PiShapedFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private final double a;
    private final double b;
    private final double c;
    private final double d;

    /**
     * Constructs a PiShapedFuzzyNumber with specified parameters.
     * 
     * @param a                lower foot parameter
     * @param b                lower peak parameter
     * @param c                upper peak parameter
     * @param d                upper foot parameter
     * @param numericTypeClass the class type of the numbers
     * @throws IllegalArgumentException if parameters do not satisfy a <= b <= c <=
     *                                  d
     */
    public PiShapedFuzzyNumber(T a, T b, T c, T d, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        this.d = Objects.requireNonNull(d, "d value can't be null").doubleValue();
        if (!(this.a <= this.b && this.b <= this.c && this.c <= this.d))
            throw new IllegalArgumentException("The values must satisfy: a <= b <= c <= d");
    }

    /**
     * Constructs a PiShapedFuzzyNumber with specified parameters.
     * 
     * @param name             the name of the fuzzy number
     * @param a                lower foot parameter
     * @param b                lower peak parameter
     * @param c                upper peak parameter
     * @param d                upper foot parameter
     * @param numericTypeClass the class type of the numbers
     * @throws IllegalArgumentException if parameters do not satisfy a <= b <= c <=
     *                                  d
     */
    public PiShapedFuzzyNumber(String name, T a, T b, T c, T d, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        this.d = Objects.requireNonNull(d, "d value can't be null").doubleValue();
        if (!(this.a <= this.b && this.b <= this.c && this.c <= this.d))
            throw new IllegalArgumentException("The values must satisfy: a <= b <= c <= d");
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType((b + c) / 2.0, getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(a, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(d, getNumericTypeClass());
    }

    @Override
    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(a, getNumericTypeClass());
        if (alphaValue == 1d)
            return CastUtilities.convertToType(b, getNumericTypeClass());
        if (a == b)
            return CastUtilities.convertToType(a, getNumericTypeClass());

        double result;
        if (alphaValue <= 0.5)
            result = a + (b - a) * Math.sqrt(alphaValue / 2.0);
        else
            result = b - (b - a) * Math.sqrt((1.0 - alphaValue) / 2.0);
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    @Override
    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(d, getNumericTypeClass());
        if (alphaValue == 1d)
            return CastUtilities.convertToType(c, getNumericTypeClass());
        if (c == d)
            return CastUtilities.convertToType(d, getNumericTypeClass());

        double result;
        if (alphaValue >= 0.5)
            result = c + (d - c) * Math.sqrt((1.0 - alphaValue) / 2.0);
        else
            result = d - (d - c) * Math.sqrt(alphaValue / 2.0);
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        if (x <= a || x >= d)
            return 0.0;
        if (x >= b && x <= c)
            return 1.0;

        if (x > a && x < b) {
            double midLeft = (a + b) / 2.0;
            if (x <= midLeft)
                return 2.0 * Math.pow((x - a) / (b - a), 2.0);
            else
                return 1.0 - 2.0 * Math.pow((b - x) / (b - a), 2.0);
        } else { // x > c && x < d
            double midRight = (c + d) / 2.0;
            if (x <= midRight)
                return 1.0 - 2.0 * Math.pow((x - c) / (d - c), 2.0);
            else
                return 2.0 * Math.pow((d - x) / (d - c), 2.0);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PiShapedFuzzyNumber<?> that))
            return false;
        return Double.compare(that.a, a) == 0 &&
                Double.compare(that.b, b) == 0 &&
                Double.compare(that.c, c) == 0 &&
                Double.compare(that.d, d) == 0 &&
                Objects.equals(that.getName(), getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), a, b, c, d);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("PiShapedFuzzyNumber")
                .append("[")
                .append(getName() != null ? getName() + ", " : "")
                .append("a=")
                .append(a)
                .append(", b=")
                .append(b)
                .append(", c=")
                .append(c)
                .append(", d=")
                .append(d)
                .append("]");

        return builder.toString();
    }

}
