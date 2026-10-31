package entities.number;

import java.util.Arrays;
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
        double value = (b + c) / 2d;
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
            return CastUtilities.convertToType(d, getNumericTypeClass());
        else {
            double result = d - alphaValue * (d - c);
            return CastUtilities.convertToType(result, getNumericTypeClass());
        }
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

    public TrapezoidalFuzzyNumber<T> add(String newName, TrapezoidalFuzzyNumber<T> other) {
        double newA = this.a + other.a;
        double newB = this.b + other.b;
        double newC = this.c + other.c;
        double newD = this.d + other.d;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());
        T convNewD = CastUtilities.convertToType(newD, getNumericTypeClass());

        return newName == null
                ? new TrapezoidalFuzzyNumber<T>(convNewA, convNewB, convNewC, convNewD, getNumericTypeClass())
                : new TrapezoidalFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, convNewD, getNumericTypeClass());
    }

    public TrapezoidalFuzzyNumber<T> subtract(String newName, TrapezoidalFuzzyNumber<T> other) {
        double newA = this.a - other.d;
        double newB = this.b - other.c;
        double newC = this.c - other.b;
        double newD = this.d - other.a;
        T convNewA = CastUtilities.convertToType(newA, getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(newB, getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(newC, getNumericTypeClass());
        T convNewD = CastUtilities.convertToType(newD, getNumericTypeClass());

        return newName == null
                ? new TrapezoidalFuzzyNumber<T>(convNewA, convNewB, convNewC, convNewD, getNumericTypeClass())
                : new TrapezoidalFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, convNewD, getNumericTypeClass());
    }

    public TrapezoidalFuzzyNumber<T> multiply(String newName, double scalar) {
        double[] values = new double[4];

        values[0] = this.a * scalar;
        values[1] = this.b * scalar;
        values[2] = this.c * scalar;
        values[3] = this.d * scalar;

        Arrays.sort(values);

        T convNewA = CastUtilities.convertToType(values[0], getNumericTypeClass());
        T convNewB = CastUtilities.convertToType(values[1], getNumericTypeClass());
        T convNewC = CastUtilities.convertToType(values[2], getNumericTypeClass());
        T convNewD = CastUtilities.convertToType(values[3], getNumericTypeClass());

        return newName == null
                ? new TrapezoidalFuzzyNumber<T>(this.getName(), convNewA, convNewB, convNewC, convNewD,
                        getNumericTypeClass())
                : new TrapezoidalFuzzyNumber<T>(newName, convNewA, convNewB, convNewC, convNewD, getNumericTypeClass());
    }

}
