package entities.rule;

/**
 * Enumeration specifying supported fuzzy inference rule architectures.
 */
public enum RuleType {
    /**
     * Mamdani-style inference model (Fuzzy input $\rightarrow$ Fuzzy output).
     */
    MAMDANI,

    /**
     * Takagi-Sugeno inference model (Fuzzy input $\rightarrow$ Crisp
     * scalar/functional output).
     */
    SUGENO
}
