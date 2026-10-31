package entities.number;

import java.util.Objects;

import entities.arithmetic.Interval;
import entities.rule.FuzzyExpression;

/**
 * Abstract base class representing a generic Fuzzy Number defined over a
 * specific numeric domain type.
 * <p>
 * A Fuzzy Number is a convex, normalized fuzzy set defined on the real numbers,
 * characterized by its
 * membership function and explicit alpha-cuts.
 * </p>
 *
 * @param <T> the numeric type of the underlying domain (e.g., {@link Double},
 *            {@link Float}, {@link Integer})
 */
public abstract class FuzzyNumber<T extends Number> implements FuzzyExpression<T> {

    private String name;
    private Class<T> numericTypeClass;

    /**
     * Constructs a {@code FuzzyNumber} with the specified numeric type class.
     *
     * @param numericTypeClass the class object representing the numeric type
     *                         {@code T}
     * @throws NullPointerException if {@code numericTypeClass} is {@code null}
     */
    public FuzzyNumber(Class<T> numericTypeClass) {
        this.numericTypeClass = Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");
    }

    /**
     * Constructs a named {@code FuzzyNumber} with the specified numeric type class.
     *
     * @param name             the name of the fuzzy number
     * @param numericTypeClass the class object representing the numeric type
     *                         {@code T}
     * @throws NullPointerException if {@code numericTypeClass} is {@code null}
     */
    public FuzzyNumber(String name, Class<T> numericTypeClass) {
        this.name = name;
        this(numericTypeClass);
    }

    /**
     * Returns the name of this fuzzy number.
     *
     * @return the name, or {@code null} if unnamed
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this fuzzy number.
     *
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the class object representing the numeric type of this fuzzy number.
     *
     * @return the numeric type class
     */
    protected Class<T> getNumericTypeClass() {
        return numericTypeClass;
    }

    /**
     * Returns the peak value (modal point) where the degree of membership equals
     * 1.0.
     *
     * @return the peak value of type {@code T}
     */
    public abstract T getPeak();

    /**
     * Returns the absolute lower bound of the support of this fuzzy number ($\alpha
     * = 0$).
     *
     * @return the lower bound value of type {@code T}
     */
    public abstract T getLowerBound();

    /**
     * Returns the absolute upper bound of the support of this fuzzy number ($\alpha
     * = 0$).
     *
     * @return the upper bound value of type {@code T}
     */
    public abstract T getUpperBound();

    /**
     * Returns the lower bound value at a specific alpha-level cut.
     *
     * @param alphaValue the alpha cut value, within the range $[0, 1]$
     * @return the lower bound value at the given alpha level
     * @throws IllegalArgumentException if {@code alphaValue} is outside $[0, 1]$
     */
    public abstract T getLowerBound(double alphaValue);

    /**
     * Returns the upper bound value at a specific alpha-level cut.
     *
     * @param alphaValue the alpha cut value, within the range $[0, 1]$
     * @return the upper bound value at the given alpha level
     * @throws IllegalArgumentException if {@code alphaValue} is outside $[0, 1]$
     */
    public abstract T getUpperBound(double alphaValue);

    /**
     * Evaluates the membership degree for a given object of type {@code T}.
     *
     * @param value the domain value
     * @return the membership degree in $[0, 1]$
     */
    @Override
    public abstract double evaluate(T value);

    /**
     * Evaluates the membership degree for a primitive double domain value.
     *
     * @param x the domain value
     * @return the membership degree in $[0, 1]$
     */
    public abstract double evaluate(double x);

    /**
     * Calculates the alpha-cut interval for a given alpha level $\alpha \in [0,
     * 1]$.
     *
     * @param alpha the alpha cut value in $[0, 1]$
     * @return an {@link Interval} representing $[L_\alpha, U_\alpha]$
     * @throws IllegalArgumentException if {@code alpha} is not within $[0, 1]$
     */
    public Interval getAlphaCut(double alpha) {
        if (alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("Alpha must be within the range [0, 1]. Value: " + alpha);
        double lower = getLowerBound(alpha).doubleValue();
        double upper = getUpperBound(alpha).doubleValue();
        return new Interval(lower, upper);
    }

}
