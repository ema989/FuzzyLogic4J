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
        if (alphaValue <= 0 || alphaValue > 1)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        double result = c - a * Math.pow((1 - alphaValue) / alphaValue, 1 / (2 * b));
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    public T getUpperBound(double alphaValue) {
        if (alphaValue <= 0 || alphaValue > 1)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        double result = c + a * Math.pow((1 - alphaValue) / alphaValue, 1 / (2 * b));
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        return 1 / (1 + Math.pow(Math.abs((x - c) / a), (2 * b)));
    }

}
