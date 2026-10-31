package entities.rule;

import java.util.Objects;

import entities.number.FuzzyNumber;

/**
 * Concrete Mamdani-style fuzzy rule where the consequent is represented by a
 * {@link FuzzyNumber}.
 *
 * @param <T> input type
 * @param <U> output type
 */
public class MamdaniFuzzyRule<T extends Number, U extends Number> extends FuzzyRule<T, U> {

    private FuzzyNumber<U> consequent;

    /**
     * Constructs Mamdani rule with single antecedent.
     *
     * @param ruleName   rule identifier
     * @param antecedent antecedent fuzzy number
     * @param consequent consequent output fuzzy number
     */
    public MamdaniFuzzyRule(String ruleName, FuzzyNumber<T> antecedent, FuzzyNumber<U> consequent) {
        super(ruleName, antecedent);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

    /**
     * Constructs Mamdani rule with expression antecedent.
     *
     * @param ruleName       rule identifier
     * @param antecedentExpr antecedent expression
     * @param consequent     consequent output fuzzy number
     */
    public MamdaniFuzzyRule(String ruleName, FuzzyExpression<T> antecedentExpr, FuzzyNumber<U> consequent) {
        super(ruleName, antecedentExpr);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

    /**
     * Returns the output fuzzy number consequent.
     *
     * @return consequent fuzzy number
     */
    public FuzzyNumber<U> getConsequent() {
        return consequent;
    }

    @Override
    public double evaluateAlpha(T crisp) {
        if (antecedent != null) {
            return antecedent.evaluate(crisp);
        } else if (antecedentExpr != null) {
            return antecedentExpr.evaluate(crisp);
        }
        throw new IllegalStateException("Both antecedent and antecedentExpr are null.");
    }

    @Override
    public RuleType getRuleType() {
        return RuleType.MAMDANI;
    }

}
