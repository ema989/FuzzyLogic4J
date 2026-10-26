package entities;

import java.util.Objects;

import interfaces.MembershipFunction;
import utilities.CastUtilities;

public class GenericFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double minX;
    private double center;
    private double maxX;
    private MembershipFunction function;

    public GenericFuzzyNumber(T minX, T center, T maxX, MembershipFunction fn, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.minX = Objects.requireNonNull(minX, "minX value can't be null").doubleValue();
        this.center = Objects.requireNonNull(center, "center value can't be null").doubleValue();
        this.maxX = Objects.requireNonNull(maxX, "maxX value can't be null").doubleValue();
        this.function = Objects.requireNonNull(fn, "fn value can't be null");
    }

    public GenericFuzzyNumber(String name, T minX, T center, T maxX, MembershipFunction fn, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.minX = Objects.requireNonNull(minX, "minX value can't be null").doubleValue();
        this.center = Objects.requireNonNull(center, "center value can't be null").doubleValue();
        this.maxX = Objects.requireNonNull(maxX, "maxX value can't be null").doubleValue();
        this.function = Objects.requireNonNull(fn, "fn value can't be null");
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType(center, getNumericTypeClass());
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
    public double evaluate(T value) {
        return function.calculate(value.doubleValue());
    }
}
