package entities.number;

import java.util.Objects;

import utilities.CastUtilities;

public class TriangularFuzzyNumber<T extends Number> extends FuzzyNumber<T> {

    private double a;
    private double b;
    private double c;

    public TriangularFuzzyNumber(T a, T b, T c, Class<T> numericTypeClass) {
        super(numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();

        if (!(this.a <= this.b && this.b <= this.c))
            throw new IllegalArgumentException("The values ​​must be: a <= b <= c");
    }

    public TriangularFuzzyNumber(String name, T a, T b, T c, Class<T> numericTypeClass) {
        super(name, numericTypeClass);
        this.a = Objects.requireNonNull(a, "a value can't be null").doubleValue();
        this.b = Objects.requireNonNull(b, "b value can't be null").doubleValue();
        this.c = Objects.requireNonNull(c, "c value can't be null").doubleValue();

        if (!(this.a <= this.b && this.b <= this.c))
            throw new IllegalArgumentException("The values ​​must be: a <= b <= c");
    }

    @Override
    public T getPeak() {
        return CastUtilities.convertToType(b, getNumericTypeClass());
    }

    @Override
    public T getLowerBound() {
        return CastUtilities.convertToType(a, getNumericTypeClass());
    }

    @Override
    public T getUpperBound() {
        return CastUtilities.convertToType(c, getNumericTypeClass());
    }

    @Override
    public double evaluate(T value) {
        return evaluate(value.doubleValue());
    }

    @Override
    public double evaluate(double x) {
        if (x < a || x > c)
            return 0d;
        if (x >= a && x <= b)
            return (x - a) / (b - a);
        else // (x >= b && x <= c)
            return (c - x) / (c - b);
    }

}
