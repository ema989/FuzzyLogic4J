package entities.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utilities.Defuzzifier;
import utilities.Defuzzifier.MamdaniDefuzzificationMethod;

/**
 * Container and inference manager for a set of fuzzy rules (Mamdani or Sugeno).
 *
 * @param <T> input domain numeric type
 * @param <U> output domain numeric type
 * @param <R> specialized rule type
 */
public class FuzzyRuleSystem<T extends Number, U extends Number, R extends FuzzyRule<T, U>> {

    private final RuleType ruleType;
    private final List<R> ruleList = new ArrayList<>();

    /**
     * Constructs an empty rule system of specified type.
     *
     * @param ruleType rule system model type
     */
    public FuzzyRuleSystem(RuleType ruleType) {
        this.ruleType = Objects.requireNonNull(ruleType, "ruleType can't be null");
    }

    /**
     * Constructs a rule system loaded with rules.
     *
     * @param rules    initial list of rules
     * @param ruleType system type
     */
    public FuzzyRuleSystem(List<R> rules, RuleType ruleType) {
        this(ruleType);
        if (rules != null)
            rules.forEach(this::add);
    }

    /**
     * Returns an unmodifiable copy of rule list.
     *
     * @return rule collection list
     */
    public List<FuzzyRule<T, U>> getRuleList() {
        return List.copyOf(ruleList);
    }

    /**
     * Adds a new rule to system.
     *
     * @param rule rule object
     * @return true if successfully added
     */
    public boolean add(R rule) {
        Objects.requireNonNull(rule, "rule can't be null");

        if (ruleType == RuleType.MAMDANI && !(rule instanceof MamdaniFuzzyRule))
            throw new IllegalArgumentException("Cannot add a non-Mamdani rule to a MAMDANI FuzzyRuleSystem.");
        if (ruleType == RuleType.SUGENO && !(rule instanceof SugenoFuzzyRule))
            throw new IllegalArgumentException("Cannot add a non-Sugeno rule to a SUGENO FuzzyRuleSystem.");

        return ruleList.add(rule);
    }

    /**
     * Removes rule by index.
     *
     * @param index element index
     * @return removed rule
     */
    public FuzzyRule<T, U> remove(int index) {
        return ruleList.remove(index);
    }

    /**
     * Removes specified rule instance.
     *
     * @param obj target object
     * @return true if removed
     */
    public boolean remove(Object obj) {
        return ruleList.remove(obj);
    }

    /**
     * Removes rule matching rule name.
     *
     * @param ruleName rule name
     * @return true if rule removed
     */
    public boolean remove(String ruleName) {
        return ruleList.removeIf(rule -> rule.getRuleName().equals(ruleName));
    }

    /**
     * Filters active rules having $\alpha > 0$ mapped by name for input.
     *
     * @param crisp input crisp value
     * @return active rule map
     */
    public Map<String, R> activeOutputMap(T crisp) {
        Map<String, R> resultMap;

        if (ruleList.isEmpty())
            return Map.of();

        resultMap = new HashMap<>();
        ruleList.stream()
                .filter(rule -> rule.evaluateAlpha(crisp) > 0d)
                .forEach(fRule -> resultMap.put(fRule.getRuleName(), fRule));

        return resultMap.isEmpty() ? Map.of() : resultMap;
    }

    /**
     * Returns list of active rules for input.
     *
     * @param crisp input value
     * @return list of rules triggering $\alpha > 0$
     */
    public List<R> activeOutputList(T crisp) {
        return ruleList.stream()
                .filter(rule -> rule.evaluateAlpha(crisp) > 0d)
                .toList();
    }

    /**
     * Evaluates inference engine pipeline returning defuzzified crisp output value.
     *
     * @param crisp         input crisp value
     * @param min           minimum integration bound for Mamdani defuzzification
     * @param max           maximum integration bound for Mamdani defuzzification
     * @param step          sampling step size
     * @param mamdaniMethod defuzzification strategy used when type is Mamdani
     * @return crisp scalar output value of type {@code U}
     */
    @SuppressWarnings("unchecked")
    public U defuzzy(T crisp, U min, U max, double step, MamdaniDefuzzificationMethod mamdaniMethod) {
        Class<U> outputClass = (Class<U>) min.getClass();

        return switch (ruleType) {
            case MAMDANI -> {
                List<entities.number.FuzzyNumber<U>> activeOutputs = ruleList.stream()
                        .filter(rule -> rule.evaluateAlpha(crisp) > 0d)
                        .map(rule -> ((MamdaniFuzzyRule<T, U>) rule).getConsequent())
                        .toList();

                yield switch (mamdaniMethod) {
                    case CENTER_OF_GRAVITY -> Defuzzifier.centerOfGravity(activeOutputs, min, max, step, outputClass);
                    case MEAN_OF_MAXIMA -> Defuzzifier.meanOfMaxima(activeOutputs, min, max, step, outputClass);
                    case FIRST_OF_MAXIMA -> Defuzzifier.firstOfMaxima(activeOutputs, min, max, step, outputClass);
                    case LAST_OF_MAXIMA -> Defuzzifier.lastOfMaxima(activeOutputs, min, max, step, outputClass);
                    case BISECTOR_OF_AREA -> Defuzzifier.bisectorOfArea(activeOutputs, min, max, step, outputClass);
                };
            }
            case SUGENO -> {
                List<Double> alphas = new ArrayList<>();
                List<Double> zValues = new ArrayList<>();

                for (R rule : ruleList) {
                    double alpha = rule.evaluateAlpha(crisp);
                    if (alpha > 0d) {
                        alphas.add(alpha);
                        SugenoFuzzyRule<T, U> sugenoRule = (SugenoFuzzyRule<T, U>) rule;
                        zValues.add(sugenoRule.getConsequent().doubleValue());
                    }
                }

                yield Defuzzifier.sugeno(alphas, zValues, outputClass);
            }
            default -> throw new IllegalArgumentException("RuleType is not valid");
        };
    }

}
