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

        if (!(this.a <= this.b && this.b <= this.c && this.c <= this.d))
            throw new IllegalArgumentException("The values ​​must be: a <= b <= c <= d");
    }

    public TrapezoidalFuzzyNumber(String name, T a, T b, T c, T d, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();
        this.d = Objects.requireNonNull(d, "d value can't be null").doubleValue();

        if (!(this.a <= this.b && this.b <= this.c && this.c <= this.d))
            throw new IllegalArgumentException("The values ​​must be: a <= b <= c <= d");
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
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
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
