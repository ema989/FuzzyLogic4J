package entities.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import entities.Defuzzifier;

public class FuzzyRuleSystem<T extends Number, U extends Number, R extends FuzzyRule<T, U>> {

    private final RuleType ruleType;
    private final List<R> ruleList = new ArrayList<>();

    public FuzzyRuleSystem(RuleType ruleType) {
        this.ruleType = Objects.requireNonNull(ruleType, "ruleType can't be null");
    }

    public FuzzyRuleSystem(List<R> rules, RuleType ruleType) {
        this(ruleType);
        if (rules != null)
            ruleList.addAll(rules);
    }

    public List<FuzzyRule<T, U>> getRuleList() {
        return List.copyOf(ruleList);
    }

    public boolean add(R rule) {
        Objects.requireNonNull(rule, "rule can't be null");
        return ruleList.add(rule);
    }

    public FuzzyRule<T, U> remove(int index) {
        return ruleList.remove(index);
    }

    public boolean remove(Object obj) {
        return ruleList.remove(obj);
    }

    public boolean remove(String ruleName) {
        return ruleList.removeIf(rule -> rule.getRuleName().equals(ruleName));
    }

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

    public List<R> activeOutputList(T crisp) {
        return ruleList.stream()
                .filter(rule -> rule.evaluateAlpha(crisp) > 0d)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public U defuzzy(T crisp, U min, U max, double step) {
        Class<U> outputClass = (Class<U>) min.getClass();

        return switch (ruleType) {
            case MAMDANI -> {
                List<entities.FuzzyNumber<U>> activeOutputs = ruleList.stream()
                        .filter(rule -> rule.evaluateAlpha(crisp) > 0d)
                        .map(rule -> ((MamdaniFuzzyRule<T, U>) rule).getConsequent())
                        .toList();

                yield Defuzzifier.mamdani(activeOutputs, min, max, step, outputClass);
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
