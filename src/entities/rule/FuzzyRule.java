package entities.rule;

import java.util.Objects;
import java.util.Optional;

import entities.number.FuzzyNumber;

public abstract class FuzzyRule<T extends Number, U extends Number> {

    private String ruleName;
    private FuzzyNumber<T> antecedent = null;
    private FuzzyExpression<T> antecedentExpr = null;

    public FuzzyRule(String ruleName) {
        this.ruleName = Objects.requireNonNull(ruleName, "ruleName can't be null");
        if (ruleName.isBlank())
            throw new IllegalArgumentException("ruleName can't be empty");
    }

    public FuzzyRule(String ruleName, FuzzyNumber<T> antecedent) {
        this(ruleName);
        this.antecedent = Objects.requireNonNull(antecedent, "antecedent can't be null");
    }

    public FuzzyRule(String ruleName, FuzzyExpression<T> antecedentExpr) {
        this(ruleName);
        this.antecedentExpr = Objects.requireNonNull(antecedentExpr, "antecedentExpr can't be null");
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Optional<FuzzyNumber<T>> getAntecedent() {
        return Optional.ofNullable(antecedent);
    }

    public Optional<FuzzyExpression<T>> getAntecedentExpr() {
        return Optional.ofNullable(antecedentExpr);
    }

    public abstract double evaluateAlpha(T crisp);

    public abstract RuleType getRuleType();

}
