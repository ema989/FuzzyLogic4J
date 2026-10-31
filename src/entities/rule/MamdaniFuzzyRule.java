package entities.rule;

import java.util.Objects;

import entities.number.FuzzyNumber;

public class MamdaniFuzzyRule<T extends Number, U extends Number> extends FuzzyRule<T, U> {

    private FuzzyNumber<U> consequent;

    public MamdaniFuzzyRule(String ruleName, FuzzyNumber<T> antecedent, FuzzyNumber<U> consequent) {
        super(ruleName, antecedent);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

    public MamdaniFuzzyRule(String ruleName, FuzzyExpression<T> antecedentExpr, FuzzyNumber<U> consequent) {
        super(ruleName, antecedentExpr);
        this.consequent = Objects.requireNonNull(consequent, "consequent can't be null");
    }

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
