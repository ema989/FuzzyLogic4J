package entities.rule;

public interface FuzzyExpression<T extends Number> {

    double evaluate(T value);

    default FuzzyExpression<T> and(FuzzyExpression<T> other) {
        return x -> Math.min(this.evaluate(x), other.evaluate(x));
    }

    default FuzzyExpression<T> or(FuzzyExpression<T> other) {
        return x -> Math.max(this.evaluate(x), other.evaluate(x));
    }

    default FuzzyExpression<T> not() {
        return x -> 1d - this.evaluate(x);
    }

    @SafeVarargs
    static <T extends Number> FuzzyExpression<T> and(FuzzyExpression<T>... exprs) {
        return x -> {
            double result = exprs[0].evaluate(x);
            for (int i = 1; i < exprs.length; i++)
                result = Math.min(result, exprs[i].evaluate(x));
            return result;
        };
    }

    @SafeVarargs
    static <T extends Number> FuzzyExpression<T> or(FuzzyExpression<T>... exprs) {
        return x -> {
            double result = exprs[0].evaluate(x);
            for (int i = 1; i < exprs.length; i++)
                result = Math.max(result, exprs[i].evaluate(x));
            return result;
        };
    }

    static <T extends Number> FuzzyExpression<T> not(FuzzyExpression<T> expr) {
        return expr.not();
    }

}
