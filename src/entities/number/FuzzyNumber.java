package entities.number;

import java.util.Objects;

import entities.rule.FuzzyExpression;

public abstract class FuzzyNumber<T extends Number> implements FuzzyExpression<T> {

    private String name;
    private Class<T> numericTypeClass;

    public FuzzyNumber(Class<T> numericTypeClass) {
        this.numericTypeClass = Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");
    }

    public FuzzyNumber(String name, Class<T> numericTypeClass) {
        this.name = name;
        this(numericTypeClass);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Class<T> getNumericTypeClass() {
        return numericTypeClass;
    }

    public abstract T getPeak();

    public abstract T getLowerBound();

    public abstract T getUpperBound();

    @Override
    public abstract double evaluate(T value);

    public abstract double evaluate(double x);

}
