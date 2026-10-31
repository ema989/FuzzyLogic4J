package entities.rule;

/**
 * Functional interface representing an evaluable fuzzy expression (membership
 * function).
 *
 * @param <T> numeric domain type
 */
public interface FuzzyExpression<T extends Number> {

    /**
     * Evaluates fuzzy membership value for domain element {@code value}.
     *
     * @param value domain element
     * @return membership degree in $[0, 1]$
     */
    double evaluate(T value);

    /**
     * Combines this expression with another using Fuzzy AND (minimum t-norm).
     *
     * @param other secondary expression
     * @return compound Fuzzy AND expression
     */
    default FuzzyExpression<T> and(FuzzyExpression<T> other) {
        return x -> Math.min(this.evaluate(x), other.evaluate(x));
    }

    /**
     * Combines this expression with another using Fuzzy OR (maximum t-conorm).
     *
     * @param other secondary expression
     * @return compound Fuzzy OR expression
     */
    default FuzzyExpression<T> or(FuzzyExpression<T> other) {
        return x -> Math.max(this.evaluate(x), other.evaluate(x));
    }

    /**
     * Inverts this fuzzy expression using standard Fuzzy NOT ($1 - \mu$).
     *
     * @return inverted Fuzzy NOT expression
     */
    default FuzzyExpression<T> not() {
        return x -> 1d - this.evaluate(x);
    }

    /**
     * Static helper performing Fuzzy AND across multiple expressions.
     *
     * @param <T>   domain type
     * @param exprs array of expressions
     * @return combined AND expression
     */
    @SafeVarargs
    static <T extends Number> FuzzyExpression<T> and(FuzzyExpression<T>... exprs) {
        return x -> {
            double result = exprs[0].evaluate(x);
            for (int i = 1; i < exprs.length; i++)
                result = Math.min(result, exprs[i].evaluate(x));
            return result;
        };
    }

    /**
     * Static helper performing Fuzzy OR across multiple expressions.
     *
     * @param <T>   domain type
     * @param exprs array of expressions
     * @return combined OR expression
     */
    @SafeVarargs
    static <T extends Number> FuzzyExpression<T> or(FuzzyExpression<T>... exprs) {
        return x -> {
            double result = exprs[0].evaluate(x);
            for (int i = 1; i < exprs.length; i++)
                result = Math.max(result, exprs[i].evaluate(x));
            return result;
        };
    }

    /**
     * Static helper wrapping NOT over an expression.
     *
     * @param <T>  domain type
     * @param expr expression to complement
     * @return complemented expression
     */
    static <T extends Number> FuzzyExpression<T> not(FuzzyExpression<T> expr) {
        return expr.not();
    }

}
