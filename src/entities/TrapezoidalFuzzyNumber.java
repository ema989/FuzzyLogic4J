package entities;

import java.util.Objects;

import utilities.CastUtilities;

public class TrapezoidalFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double a;
    private double b;
    private double c;
    private double d;

    public TrapezoidalFuzzyNumber(T a, T b, T c, T d, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        this.d = Objects.requireNonNull(d, "d value can't be null").doubleValue();
    }

    public TrapezoidalFuzzyNumber(String name, T a, T b, T c, T d, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        this.d = Objects.requireNonNull(d, "d value can't be null").doubleValue();
    }

    @Override
    public T getPeak() {
        double value = (b + c) / 2;
        return CastUtilities.convertToType(value, getNumericTypeClass());
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
    public double evaluate(T value) {
        double x = value.doubleValue();

        if (x < a || x > d)
            return 0d;
        if (x >= a && x < b)
            return (x - a) / (b - a);
        if (x >= b && x <= c)
            return 1d;
        else // (x > c && x <= d)
            return (d - x) / (d - c);
    }

}
