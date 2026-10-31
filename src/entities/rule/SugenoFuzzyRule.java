package entities.rule;

import java.util.Objects;

import entities.number.FuzzyNumber;

public class SugenoFuzzyRule<T extends Number, U extends Number> extends FuzzyRule<T, U> {

    private U consequent;

    public SugenoFuzzyRule(String ruleName, FuzzyNumber<T> antecedent, U consequent) {
        super(ruleName, antecedent);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

    public SugenoFuzzyRule(String ruleName, FuzzyExpression<T> antecedentExpr, U consequent) {
        super(ruleName, antecedentExpr);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

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
