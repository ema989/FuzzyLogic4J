package entities.number;

import java.util.Objects;

import utilities.CastUtilities;

public class GaussianFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double sigma;
    private double c;

    public GaussianFuzzyNumber(T sigma, T c, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.sigma = Objects.requireNonNull(sigma, "sigma value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
    }

    public GaussianFuzzyNumber(String name, T sigma, T c, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.sigma = Objects.requireNonNull(sigma, "sigma value can't be null").doubleValue();
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
            double result = c - sigma * Math.sqrt(2 * Math.log(1 / alphaValue));
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(Double.POSITIVE_INFINITY, getNumericTypeClass());
        else {
            double result = c + sigma * Math.sqrt(2 * Math.log(1 / alphaValue));
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        if (Math.abs(sigma) < 1e-9)
            return (Math.abs(x - c) < 1e-9) ? 1.0 : 0.0;

        return Math.exp(-(Math.pow((x - c), 2d) / (2d * Math.pow(sigma, 2d))));
    }
    }

}
