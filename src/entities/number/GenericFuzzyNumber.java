package entities.number;

import java.util.Objects;

import interfaces.MembershipFunction;
import utilities.CastUtilities;

public class GenericFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double minX;
    private double peak;
    private double maxX;
    private MembershipFunction function;

    public GenericFuzzyNumber(T minX, T peak, T maxX, MembershipFunction fn, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.minX = Objects.requireNonNull(minX, "minX value can't be null").doubleValue();
        this.peak = Objects.requireNonNull(peak, "peak value can't be null").doubleValue();
        this.maxX = Objects.requireNonNull(maxX, "maxX value can't be null").doubleValue();
        this.function = Objects.requireNonNull(fn, "fn value can't be null");
    }

    public GenericFuzzyNumber(String name, T minX, T peak, T maxX, MembershipFunction fn, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.minX = Objects.requireNonNull(minX, "minX value can't be null").doubleValue();
        this.peak = Objects.requireNonNull(peak, "peak value can't be null").doubleValue();
        this.maxX = Objects.requireNonNull(maxX, "maxX value can't be null").doubleValue();
        this.function = Objects.requireNonNull(fn, "fn value can't be null");
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType(peak, getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(minX, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(maxX, getNumericTypeClass());
    }

    @Override
    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(minX, getNumericTypeClass());

        // If the lower bound already satisfies the alpha-cut
        if (evaluate(minX) >= alphaValue)
            return CastUtilities.convertToType(minX, getNumericTypeClass());

        // If the peak does not reach the alpha-cutoff
        if (evaluate(peak) < alphaValue)
            return CastUtilities.convertToType(peak, getNumericTypeClass());

        // Search via Bisection Method on the left flank [minX, peak]
        double low = minX;
        double high = peak;
        double result = peak;

        for (int i = 0; i < 50; i++) { // 50 iterations ensure extreme precision (~2^-50)
            double mid = low + (high - low) / 2.0;
            if (evaluate(mid) >= alphaValue) {
                result = mid;
                high = mid; // Look for a value even further to the left to find the start of the interval
            } else {
                low = mid; // Look further to the right
            }
        }
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    @Override
    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(maxX, getNumericTypeClass());

        // If the upper limit already satisfies the alpha-cut
        if (evaluate(maxX) >= alphaValue)
            return CastUtilities.convertToType(maxX, getNumericTypeClass());

        // If the peak does not reach the alpha-cutoff
        if (evaluate(peak) < alphaValue)
            return CastUtilities.convertToType(peak, getNumericTypeClass());

        // Search via Bisection Method on the right side [peak, maxX]
        double low = peak;
        double high = maxX;
        double result = peak;

        for (int i = 0; i < 50; i++) {
            double mid = low + (high - low) / 2.0;
            if (evaluate(mid) >= alphaValue) {
                result = mid;
                low = mid; // Look for a value further to the right to find the end of the interval
            } else {
                high = mid; // Look further to the left
            }
        }
        return CastUtilities.convertToType(result, getNumericTypeClass());
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        return function.calculate(x);
    }

}
