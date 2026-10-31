package entities.number;

import java.util.Arrays;
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
    public T getLowerBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(a, getNumericTypeClass());
        else {
            double result = a + alphaValue * (b - a);
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
    }

    @Override
    public T getUpperBound(double alphaValue) {
        if (alphaValue < 0d || alphaValue > 1d)
            throw new IllegalArgumentException("alphaValue isn't valid: " + alphaValue);

        if (alphaValue == 0d)
            return CastUtilities.convertToType(c, getNumericTypeClass());
        else {
            double result = c - alphaValue * (c - b);
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
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
        double[] values = new double[3];

        values[0] = this.a * scalar;
        values[1] = this.b * scalar;
        values[2] = this.c * scalar;

        Arrays.sort(values);

        T convNewA = CastUtilities.convertToType(values[0], getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(values[1], getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(values[2], getNumericTypeClass());

        return newName == null
                ? new TriangularFuzzyNumber<T>(this.getName(), convNewA, convNewB, convNewC, getNumericTypeClass())
                : new TriangularFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, getNumericTypeClass());
    }

}
