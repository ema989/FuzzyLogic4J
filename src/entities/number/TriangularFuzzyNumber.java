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

    public TriangularFuzzyNumber<T> add(String newName, TriangularFuzzyNumber<T> other) {
        double newA = this.a + other.a;
        double newB = this.b + other.b;
        double newC = this.c + other.c;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());

        return newName == null ? new TriangularFuzzyNumber<T>(convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

    public TriangularFuzzyNumber<T> subtract(String newName, TriangularFuzzyNumber<T> other) {
        double newA = this.a - other.c;
        double newB = this.b - other.b;
        double newC = this.c - other.a;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());

        return newName == null ? new TriangularFuzzyNumber<T>(convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

    public TriangularFuzzyNumber<T> multiply(String newName, double scalar) {
        if (scalar <= 0d)
            throw new IllegalArgumentException("scalar value can't be negative");

        double newA = this.a * scalar;
        double newB = this.b * scalar;
        double newC = this.c * scalar;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());

        return newName == null
                ? new TriangularFuzzyNumber<T>(this.getName(), convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

}
