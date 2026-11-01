package entities.number;

import java.util.Objects;

import utilities.CastUtilities;

/**
 * Represents an S-shape (or Z-shape) fuzzy number used for incremental
 * transitions.
 * 
 * @param <T> the numeric type extending Number
 */
public class ZShapeFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private final double a;
    private final double b;

    /**
     * Constructs an S-shape fuzzy number.
     * 
     * @param a                lower limit parameter
     * @param b                upper limit parameter
     * @param numericTypeClass the class type of the numbers
     * @throws IllegalArgumentException if 'a' is not strictly less than 'b'
     */
    public ZShapeFuzzyNumber(T a, T b, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        if (this.a >= this.b)
            throw new IllegalArgumentException("Parameter 'a' must be strictly less than 'b'");
    }

    /**
     * Constructs an S-shape fuzzy number.
     * 
     * @param name             the name of the fuzzy number
     * @param a                lower limit parameter
     * @param b                upper limit parameter
     * @param numericTypeClass the class type of the numbers
     * @throws IllegalArgumentException if 'a' is not strictly less than 'b'
     */
    public ZShapeFuzzyNumber(String name, T a, T b, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        if (this.a >= this.b)
            throw new IllegalArgumentException("Parameter 'a' must be strictly less than 'b'");
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType(a, getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(b, getNumericTypeClass());
    }

    @Override
    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);
        return CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
    }

    @Override
    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(b, getNumericTypeClass());
        if (alphaValue == 1d)
            return CastUtilities.convertToType(a, getNumericTypeClass());

        double result;
        if (alphaValue >= 0.5)
            // alpha = 1 - 2 * ((x - a) / (b - a))^2 => x = a + (b - a) * sqrt((1 - alpha) /
            // 2)
            result = a + (b - a) * Math.sqrt((1.0 - alphaValue) / 2.0);
        else
            // alpha = 2 * ((b - x) / (b - a))^2 => x = b - (b - a) * sqrt(alpha / 2)
            result = b - (b - a) * Math.sqrt(alphaValue / 2.0);
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        if (x <= a)
            return 1.0;
        if (x >= b)
            return 0.0;

        double mid = (a + b) / 2.0;
        if (x <= mid)
            return 1.0 - 2.0 * Math.pow((x - a) / (b - a), 2.0);
        else
            return 2.0 * Math.pow((b - x) / (b - a), 2.0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ZShapeFuzzyNumber<?> that))
            return false;
        return Double.compare(that.a, a) == 0 &&
                Double.compare(that.b, b) == 0 &&
                Objects.equals(that.getName(), getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), a, b);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("ZShapeFuzzyNumber")
                .append("[")
                .append(getName() != null ? getName() + ", " : "")
                .append("a=")
                .append(a)
                .append(", b=")
                .append(b)
                .append("]");

        return builder.toString();
    }

}
