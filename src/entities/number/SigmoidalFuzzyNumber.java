package entities.number;

import java.util.Objects;

import utilities.CastUtilities;

/**
 * Represents a Sigmoidal fuzzy number characterized by a sigmoidal membership
 * function.
 * 
 * @param <T> the numeric type extending Number
 */
public class SigmoidalFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private final double a; // Slope / steepness
    private final double c; // Crossover point (where membership is 0.5)

    /**
     * Constructs a SigmoidalFuzzyNumber.
     * 
     * @param a                slope parameter (must not be zero)
     * @param c                inflection point parameter
     * @param numericTypeClass the class type of the numbers
     * @throws IllegalArgumentException if slope 'a' is zero
     */
    public SigmoidalFuzzyNumber(T a, T c, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        if (Math.abs(this.a) < 1e-9)
            throw new IllegalArgumentException("Slope 'a' cannot be zero.");
    }

    /**
     * Constructs a SigmoidalFuzzyNumber.
     * 
     * @param name             the name of the fuzzy number
     * @param a                slope parameter (must not be zero)
     * @param c                inflection point parameter
     * @param numericTypeClass the class type of the numbers
     * @throws IllegalArgumentException if slope 'a' is zero
     */
    public SigmoidalFuzzyNumber(String name, T a, T c, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        if (Math.abs(this.a) < 1e-9)
            throw new IllegalArgumentException("Slope 'a' cannot be zero.");
    }

    @Override
    public T getPeak() {
        // For a sigmoidal curve, peak (membership 1.0) is at +infinity (if a > 0) or
        // -infinity (if a < 0)
        double peakVal = a > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        return CastUtilities.convertToType(peakVal, getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass());
    }

    @Override
    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
        if (alphaValue == 1d)
            return a > 0 ? CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass())
                    : CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());

        // Inverting alpha = 1 / (1 + exp(-a * (x - c))) => x = c - (1/a) * ln((1/alpha)
        // - 1)
        double x = c - (1.0 / a) * Math.log((1.0 / alphaValue) - 1.0);
        return a > 0 ? CastUtilities.convertToType(x, getNumericTypeClass())
                : CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
    }

    @Override
    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass());
        if (alphaValue == 1d)
            return a > 0 ? CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass())
                    : CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());

        double x = c - (1.0 / a) * Math.log((1.0 / alphaValue) - 1.0);
        return a > 0 ? CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass())
                : CastUtilities.convertToType(x, getNumericTypeClass());
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        return 1.0 / (1.0 + Math.exp(-a * (x - c)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SigmoidalFuzzyNumber<?> that))
            return false;
        return Double.compare(that.a, a) == 0 &&
                Double.compare(that.c, c) == 0 &&
                Objects.equals(that.getName(), getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), a, c);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("SigmoidalFuzzyNumber")
                .append("[")
                .append(getName() != null ? getName() + ", " : "")
                .append("a=")
                .append(a)
                .append(", c=")
                .append(c)
                .append("]");

        return builder.toString();
    }

}
