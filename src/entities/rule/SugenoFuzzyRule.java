package entities.rule;

import java.util.Objects;

import entities.FuzzyNumber;

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
        return super.getAntecedent().isPresent() ? super.getAntecedent().get().evaluate(crisp)
                : super.getAntecedentExpr().get().evaluate(crisp);
    }

    @Override
    public RuleType getRuleType() {
        return RuleType.SUGENO;
    }

}
