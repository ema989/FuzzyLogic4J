package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import entities.number.FuzzyNumber;

public final class Defuzzifier {

    // --- MAMDANI: CENTER OF GRAVITY (CoG) ---
    public static <T extends Number> T centerOfGravity(List<FuzzyNumber<T>> activeOutputs, T min, T max,
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

    // --- MAMDANI: MEAN OF MAXIMA (MoM) ---
    public static <T extends Number> T meanOfMaxima(List<FuzzyNumber<T>> activeOutputs, T min, T max,
            double step, Class<T> numericTypeClass) {
        Objects.requireNonNull(activeOutputs, "activeOutputs list can't be null");
        Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");

        double maxMembership = 0.0;
        List<Double> maxPoints = new ArrayList<>();

        for (double y = min.doubleValue(); y <= max.doubleValue(); y += step) {
            double currentY = y;
            double membership = activeOutputs.stream()
                    .mapToDouble(fn -> fn.evaluate(currentY))
                    .max()
                    .orElse(0.0);

            if (membership > maxMembership + 1e-9) {
                maxMembership = membership;
                maxPoints.clear();
                maxPoints.add(y);
            } else if (Math.abs(membership - maxMembership) < 1e-9 && maxMembership > 0) {
                maxPoints.add(y);
            }
        }

        if (maxPoints.isEmpty()) {
            return CastUtilities.convertToType((min.doubleValue() + max.doubleValue()) / 2.0, numericTypeClass);
        }

        double sum = 0.0;
        for (double p : maxPoints)
            sum += p;
        return CastUtilities.convertToType(sum / maxPoints.size(), numericTypeClass);
    }

    // --- MAMDANI: FIRST OF MAXIMA (FoM) ---
    public static <T extends Number> T firstOfMaxima(List<FuzzyNumber<T>> activeOutputs, T min, T max,
            double step, Class<T> numericTypeClass) {
        Objects.requireNonNull(activeOutputs, "activeOutputs list can't be null");
        Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");

        double maxMembership = 0.0;
        double firstMaxY = (min.doubleValue() + max.doubleValue()) / 2.0;

        for (double y = min.doubleValue(); y <= max.doubleValue(); y += step) {
            double currentY = y;
            double membership = activeOutputs.stream()
                    .mapToDouble(fn -> fn.evaluate(currentY))
                    .max()
                    .orElse(0.0);

            if (membership > maxMembership + 1e-9) {
                maxMembership = membership;
                firstMaxY = y;
            }
        }

        return CastUtilities.convertToType(firstMaxY, numericTypeClass);
    }

    // --- MAMDANI: LAST OF MAXIMA (LoM) ---
    public static <T extends Number> T lastOfMaxima(List<FuzzyNumber<T>> activeOutputs, T min, T max,
            double step, Class<T> numericTypeClass) {
        Objects.requireNonNull(activeOutputs, "activeOutputs list can't be null");
        Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");

        double maxMembership = 0.0;
        double lastMaxY = (min.doubleValue() + max.doubleValue()) / 2.0;

        for (double y = min.doubleValue(); y <= max.doubleValue(); y += step) {
            double currentY = y;
            double membership = activeOutputs.stream()
                    .mapToDouble(fn -> fn.evaluate(currentY))
                    .max()
                    .orElse(0.0);

            if (membership >= maxMembership - 1e-9 && membership > 0) {
                maxMembership = membership;
                lastMaxY = y;
            }
        }

        return CastUtilities.convertToType(lastMaxY, numericTypeClass);
    }

    // --- MAMDANI: BISECTOR OF AREA (BoA) ---
    public static <T extends Number> T bisectorOfArea(List<FuzzyNumber<T>> activeOutputs, T min, T max,
            double step, Class<T> numericTypeClass) {
        Objects.requireNonNull(activeOutputs, "activeOutputs list can't be null");
        Objects.requireNonNull(numericTypeClass, "numericTypeClass class can't be null");

        List<Double> yValues = new ArrayList<>();
        List<Double> muValues = new ArrayList<>();

        double totalArea = 0.0;

        for (double y = min.doubleValue(); y <= max.doubleValue(); y += step) {
            double currentY = y;
            double maxMu = activeOutputs.stream()
                    .mapToDouble(fn -> fn.evaluate(currentY))
                    .max()
                    .orElse(0.0);

            yValues.add(y);
            muValues.add(maxMu);
            totalArea += maxMu;
        }

        if (totalArea == 0.0) {
            return CastUtilities.convertToType((min.doubleValue() + max.doubleValue()) / 2.0, numericTypeClass);
        }

        double halfArea = totalArea / 2.0;
        double accumulatedArea = 0.0;

        for (int i = 0; i < yValues.size(); i++) {
            accumulatedArea += muValues.get(i);
            if (accumulatedArea >= halfArea) {
                return CastUtilities.convertToType(yValues.get(i), numericTypeClass);
            }
        }

        return CastUtilities.convertToType((min.doubleValue() + max.doubleValue()) / 2.0, numericTypeClass);
    }

    // --- SUGENO: WEIGHTED AVERAGE ---
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

    public enum MamdaniDefuzzificationMethod {
        CENTER_OF_GRAVITY,
        MEAN_OF_MAXIMA,
        FIRST_OF_MAXIMA,
        LAST_OF_MAXIMA,
        BISECTOR_OF_AREA
    }

}
