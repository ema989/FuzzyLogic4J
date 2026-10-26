package entities;

import java.util.Objects;

public abstract class FuzzyNumber<T extends Number> {

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

    public abstract double evaluate(T value);

}
