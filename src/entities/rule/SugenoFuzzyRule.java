package entities.rule;

import java.util.Objects;

import entities.number.FuzzyNumber;

/**
 * Concrete Takagi-Sugeno-style fuzzy rule where the consequent is a crisp
 * scalar value.
 *
 * @param <T> input domain type
 * @param <U> output domain type
 */
public class SugenoFuzzyRule<T extends Number, U extends Number> extends FuzzyRule<T, U> {

    private U consequent;

    /**
     * Constructs Sugeno rule with single antecedent.
     *
     * @param ruleName   rule identifier
     * @param antecedent antecedent fuzzy number
     * @param consequent crisp scalar consequent value
     */
    public SugenoFuzzyRule(String ruleName, FuzzyNumber<T> antecedent, U consequent) {
        super(ruleName, antecedent);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

    /**
     * Constructs Sugeno rule with expression antecedent.
     *
     * @param ruleName       rule identifier
     * @param antecedentExpr antecedent expression
     * @param consequent     crisp scalar consequent value
     */
    public SugenoFuzzyRule(String ruleName, FuzzyExpression<T> antecedentExpr, U consequent) {
        super(ruleName, antecedentExpr);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

    /**
     * Returns the scalar consequent.
     *
     * @return consequent value
     */
    public U getConsequent() {
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
        return RuleType.SUGENO;
    }

}
