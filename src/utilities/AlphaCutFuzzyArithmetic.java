package utilities;

import java.util.ArrayList;
import java.util.List;

import entities.arithmetic.Interval;
import entities.number.FuzzyNumber;
import entities.number.GenericFuzzyNumber;

public class AlphaCutFuzzyArithmetic {

    public static <T extends Number> GenericFuzzyNumber<T> execute(FuzzyNumber<T> a, FuzzyNumber<T> b, Operation op,
            int steps, Class<T> numericTypeClass) {
        List<Double> alphaLevels = new ArrayList<>();
        List<Interval> resultIntervals = new ArrayList<>();
        double stepSize = 1d / steps;
        double minX;
        double maxX;
        Interval topInterval;
        double center;

        // Sample alpha from stepSize up to 1.0

        for (int i = 1; i <= steps; i++) {
            double alpha = i * stepSize;
            Interval cutA = a.getAlphaCut(alpha);
            Interval cutB = b.getAlphaCut(alpha);
            Interval res = switch (op) {
                case ADD -> cutA.add(cutB);
                case SUBTRACT -> cutA.subtract(cutB);
                case MULTIPLY -> cutA.multiply(cutB);
                case DIVIDE -> cutA.divide(cutB);
            };

            alphaLevels.add(alpha);
            resultIntervals.add(res);
        }

        minX = resultIntervals.get(0).min();
        maxX = resultIntervals.get(0).max();
        topInterval = resultIntervals.getLast();
        center = (topInterval.min() + topInterval.max()) / 2d;

        // Construct the membership function for the resulting fuzzy number.

        return new GenericFuzzyNumber<>(
                CastUtilities.convertToType(minX, numericTypeClass),
                CastUtilities.convertToType(center, numericTypeClass),
                CastUtilities.convertToType(maxX, numericTypeClass),
                x -> evaluateMembershipFromCuts(x, alphaLevels, resultIntervals),
                numericTypeClass);
    }

    // It reconstructs the membership degree μ(x) for a given x by interpolating
    // between alpha-cuts.
    private static double evaluateMembershipFromCuts(double x, List<Double> alphaLevels, List<Interval> intervals) {
        double maxAlpha = 0.0;

        for (int i = 0; i < alphaLevels.size(); i++) {
            Interval interval = intervals.get(i);
            if (x >= interval.min() && x <= interval.max())
                maxAlpha = Math.max(maxAlpha, alphaLevels.get(i));
        }

        return maxAlpha;
    }

    public enum Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

}
