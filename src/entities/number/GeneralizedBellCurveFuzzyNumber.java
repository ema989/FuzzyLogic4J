package entities.number;

import java.util.Objects;

import utilities.CastUtilities;

public class GeneralizedBellCurveFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double a;
    private double b;
    private double c;

    public GeneralizedBellCurveFuzzyNumber(T a, T b, T c, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
    }

    public GeneralizedBellCurveFuzzyNumber(String name, T a, T b, T c, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType(c, super.getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass());
    }

    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(Double.NEGATIVE_INFINITY, getNumericTypeClass());
        else {
            double result = c - a * Math.pow((1d - alphaValue) / alphaValue, 1d / (2d * b));
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass());
        else {
            double result = c + a * Math.pow((1d - alphaValue) / alphaValue, 1d / (2d * b));
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        return 1d / (1d + Math.pow(Math.abs((x - c) / a), (2d * b)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof GeneralizedBellCurveFuzzyNumber<?> that))
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

        builder.append("GeneralizedBellCurveFuzzyNumber")
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
