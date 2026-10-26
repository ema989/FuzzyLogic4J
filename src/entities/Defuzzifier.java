package entities;

import java.util.List;
import java.util.Objects;

import utilities.CastUtilities;

public class Defuzzifier {

    // Mamdani: center of gravity
    public static <T extends Number> T mamdani(List<FuzzyNumber<T>> activeOutputs, T min, T max,
            double step, Class<T> numericTypeClass) {
        Objects.requireNonNull(activeOutputs, "activeOutputs list can't be null");
        Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");

        double numerator = 0.0;
        double denominator = 0.0;

        for (double y = min.doubleValue(); y <= max.doubleValue(); y += step) {
            double currentY = y;
            double maxMembershipAtY = activeOutputs.stream()
                    .mapToDouble(fn -> fn.evaluate(currentY))
                    .max()
                    .orElse(0.0);

            numerator += maxMembershipAtY * y;
            denominator += maxMembershipAtY;
        }

        if (denominator == 0.0)
            return CastUtilities.convertToType(((min.doubleValue() + max.doubleValue()) / 2.0), numericTypeClass);

        return CastUtilities.convertToType(numerator / denominator, numericTypeClass);
    }

    public static <T extends Number> T sugeno(List<Double> alphas, List<Double> zValues, Class<T> numericTypeClass) {
        Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");

        if (alphas == null || zValues == null || alphas.size() != zValues.size()) {
            throw new IllegalArgumentException("The lists must be non-null and of the same size");
        }

        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < alphas.size(); i++) {
            double alpha = alphas.get(i);
            double z = zValues.get(i);

            numerator += alpha * z;
            denominator += alpha;
        }

        if (denominator == 0d) {
            return CastUtilities.convertToType(0d, numericTypeClass);
        }

        return CastUtilities.convertToType(numerator / denominator, numericTypeClass);
    }

}
