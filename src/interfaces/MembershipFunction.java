package interfaces;

/**
 * Functional interface representing a fuzzy membership function $\mu(x)$.
 * Maps a real-valued domain scalar $x$ to its corresponding degree of
 * membership within the interval $[0, 1]$.
 */
@FunctionalInterface
public interface MembershipFunction {
    /**
     * Calculates the membership degree $\mu(x)$ for a given scalar domain value.
     *
     * @param value the domain value $x$ to evaluate
     * @return the resulting membership degree, typically in the range $[0.0, 1.0]$
     */
    double calculate(double value);
}
