package entities.rule;

import java.util.Objects;
import java.util.Optional;

import entities.number.FuzzyNumber;

/**
 * Abstract representation of a fuzzy rule containing an antecedent (as a single
 * FuzzyNumber or compound FuzzyExpression).
 *
 * @param <T> input domain numeric type
 * @param <U> output domain numeric type
 */
public abstract class FuzzyRule<T extends Number, U extends Number> {

    private String ruleName;
    protected FuzzyNumber<T> antecedent = null;
    protected FuzzyExpression<T> antecedentExpr = null;

    /**
     * Base constructor initializing rule name.
     *
     * @param ruleName unique rule name identifier
     */
    public FuzzyRule(String ruleName) {
        this.ruleName = Objects.requireNonNull(ruleName, "ruleName can't be null");
        if (ruleName.isBlank())
            throw new IllegalArgumentException("ruleName can't be empty");
    }

    /**
     * Constructs a rule with a single fuzzy number antecedent.
     *
     * @param ruleName   rule name
     * @param antecedent antecedent fuzzy number
     */
    public FuzzyRule(String ruleName, FuzzyNumber<T> antecedent) {
        this(ruleName);
        this.antecedent = Objects.requireNonNull(antecedent, "antecedent can't be null");
    }

    /**
     * Constructs a rule with a compound fuzzy expression antecedent.
     *
     * @param ruleName       rule name
     * @param antecedentExpr compound antecedent expression
     */
    public FuzzyRule(String ruleName, FuzzyExpression<T> antecedentExpr) {
        this(ruleName);
        this.antecedentExpr = Objects.requireNonNull(antecedentExpr, "antecedentExpr can't be null");
    }

    /**
     * Returns the name of the rule.
     *
     * @return rule name
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Sets a new rule name.
     *
     * @param ruleName rule name
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * Returns an Optional containing the single fuzzy number antecedent if
     * configured.
     *
     * @return optional antecedent fuzzy number
     */
    public Optional<FuzzyNumber<T>> getAntecedent() {
        return Optional.ofNullable(antecedent);
    }

    /**
     * Returns an Optional containing the compound antecedent expression if
     * configured.
     *
     * @return optional antecedent expression
     */
    public Optional<FuzzyExpression<T>> getAntecedentExpr() {
        return Optional.ofNullable(antecedentExpr);
    }

    /**
     * Evaluates activation strength (alpha value) of rule antecedent for crisp
     * input.
     *
     * @param crisp input crisp scalar value
     * @return activation level in $[0, 1]$
     */
    public abstract double evaluateAlpha(T crisp);

    /**
     * Returns the target system type (Mamdani or Sugeno) of this rule.
     *
     * @return rule system type
     */
    public abstract RuleType getRuleType();

}
