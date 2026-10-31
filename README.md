# Java Fuzzy Arithmetic & Inference Library

A lightweight, robust, and extensible Java library for continuous and discrete **Fuzzy Arithmetic**, **Fuzzy Logic Operations**, and **Fuzzy Inference Systems** (supporting both **Mamdani** and **Takagi-Sugeno** architectures).

## 🌟 Key Features

* **Multiple Fuzzy Number Types:**
  * Triangular (`TriangularFuzzyNumber`)
  * Trapezoidal (`TrapezoidalFuzzyNumber`)
  * Gaussian (`GaussianFuzzyNumber`)
  * Generalized Bell Curve (`GeneralizedBellCurveFuzzyNumber`)
  * Generic / Custom Membership Functions (`GenericFuzzyNumber`)
* **Interval Arithmetic via Alpha-Cuts:** Compute fuzzy addition, subtraction, multiplication, and division using resolution principle alpha-cut decomposition (`AlphaCutFuzzyArithmetic`).
* **Composable Logical Expressions:** Chain fuzzy membership expressions fluently using `and`, `or`, and `not` operators.
* **Inference Systems (`FuzzyRuleSystem`):**
  * **Mamdani Systems** with customizable defuzzification strategies (*Center of Gravity*, *Mean of Maxima*, *First/Last of Maxima*, *Bisector of Area*).
  * **Takagi-Sugeno Systems** using weighted average output calculation.
* **Strong Generic Typing:** Fully typed using Java generics (`<T extends Number>`).

## 🚀 Getting Started

### Prerequisites

* **Java 17** or higher.

## 💡 Usage Examples

### 1. Creating Fuzzy Numbers and Evaluating Membership

```java
import entities.number.*;

// Create a Triangular Fuzzy Number (a=10, b=20, c=30)
TriangularFuzzyNumber<Double> tempLow = new TriangularFuzzyNumber<>(
    "Low Temperature", 10.0, 20.0, 30.0, Double.class
);

// Evaluate membership degree at a specific value
double degree = tempLow.evaluate(15.0); // Returns 0.5

// Extract an alpha-cut interval at alpha = 0.5
var alphaCut = tempLow.getAlphaCut(0.5);
System.out.println("Alpha Cut [0.5]: " + alphaCut.min() + " to " + alphaCut.max());
```

### 2. Performing Alpha-Cut Fuzzy Arithmetic

```java
import entities.number.*;
import utilities.AlphaCutFuzzyArithmetic;
import utilities.AlphaCutFuzzyArithmetic.Operation;

TriangularFuzzyNumber<Double> numA = new TriangularFuzzyNumber<>("A", 10.0, 20.0, 30.0, Double.class);
TriangularFuzzyNumber<Double> numB = new TriangularFuzzyNumber<>("B", 20.0, 30.0, 40.0, Double.class);

// Perform Fuzzy Addition
GenericFuzzyNumber<Double> sum = AlphaCutFuzzyArithmetic.execute(
    numA, numB, Operation.ADD, 100, Double.class
);

System.out.println("Sum Peak: " + sum.getPeak()); // ~ 50.0

// Perform Fuzzy Multiplication
GenericFuzzyNumber<Double> product = AlphaCutFuzzyArithmetic.execute(
    numA, numB, Operation.MULTIPLY, 100, Double.class
);

System.out.println("Product Peak: " + product.getPeak()); // ~ 600.0
```

### 3. Composing Advanced `FuzzyExpression` Logic

You can build complex antecedent condition logic using standard t-norms (`and`), t-conorms (`or`), and negation (`not`).

```java
import entities.number.TriangularFuzzyNumber;
import entities.rule.FuzzyExpression;

TriangularFuzzyNumber<Double> tempCold = new TriangularFuzzyNumber<>(0.0, 10.0, 20.0, Double.class);
TriangularFuzzyNumber<Double> humidityHigh = new TriangularFuzzyNumber<>(60.0, 80.0, 100.0, Double.class);
TriangularFuzzyNumber<Double> pressureLow = new TriangularFuzzyNumber<>(980.0, 995.0, 1010.0, Double.class);

// Option A: Fluent method chaining -> (tempCold AND humidityHigh) OR NOT(pressureLow)
FuzzyExpression<Double> combinedExpression = tempCold.and(humidityHigh).or(pressureLow.not());

// Option B: Static varargs helpers -> AND(expr1, expr2, expr3...)
FuzzyExpression<Double> multiCondition = FuzzyExpression.and(tempCold, humidityHigh, pressureLow);

// Evaluate compound expression at a specific domain input
double evaluation = combinedExpression.evaluate(15.0);
```

### 4. Complete `FuzzyRuleSystem` Examples

#### A. Mamdani Fuzzy Inference System

Mamdani systems map fuzzy antecedents to fuzzy set consequents and defuzzify the result into a crisp output.

```java
import entities.number.TriangularFuzzyNumber;
import entities.rule.*;
import utilities.Defuzzifier.MamdaniDefuzzificationMethod;

// 1. Define Antecedents and Consequents
TriangularFuzzyNumber<Double> tempCold = new TriangularFuzzyNumber<>("Cold", 0.0, 10.0, 20.0, Double.class);
TriangularFuzzyNumber<Double> tempHot = new TriangularFuzzyNumber<>("Hot", 25.0, 35.0, 45.0, Double.class);

TriangularFuzzyNumber<Double> fanSlow = new TriangularFuzzyNumber<>("FanSlow", 0.0, 20.0, 50.0, Double.class);
TriangularFuzzyNumber<Double> fanFast = new TriangularFuzzyNumber<>("FanFast", 50.0, 80.0, 100.0, Double.class);

// 2. Define Rules using single FuzzyNumbers or compound FuzzyExpressions
MamdaniFuzzyRule<Double, Double> rule1 = new MamdaniFuzzyRule<>(
    "Rule_Cold_SlowFan", tempCold, fanSlow
);
MamdaniFuzzyRule<Double, Double> rule2 = new MamdaniFuzzyRule<>(
    "Rule_Hot_FastFan", tempHot, fanFast
);

// 3. Initialize FuzzyRuleSystem
FuzzyRuleSystem<Double, Double, MamdaniFuzzyRule<Double, Double>> mamdaniSystem = 
    new FuzzyRuleSystem<>(RuleType.MAMDANI);

mamdaniSystem.add(rule1);
mamdaniSystem.add(rule2);

// 4. Query active rules for crisp input
System.out.println("Active Rules count: " + mamdaniSystem.activeOutputList(12.0).size());

// 5. Defuzzify output crisp value using Center of Gravity (CoG)
Double crispInput = 12.0;
Double output = mamdaniSystem.defuzzy(
    crispInput, 0.0, 100.0, 0.5, MamdaniDefuzzificationMethod.CENTER_OF_GRAVITY
);

System.out.println("Defuzzified Fan Speed: " + output);
```

#### B. Takagi-Sugeno Fuzzy Inference System

Takagi-Sugeno systems map fuzzy antecedents to scalar crisp values for fast weighted-average defuzzification.

```java
import entities.number.TriangularFuzzyNumber;
import entities.rule.*;

TriangularFuzzyNumber<Double> tempCold = new TriangularFuzzyNumber<>("Cold", 0.0, 10.0, 20.0, Double.class);
TriangularFuzzyNumber<Double> tempHot = new TriangularFuzzyNumber<>("Hot", 25.0, 35.0, 45.0, Double.class);

// Sugeno rules have scalar consequents (e.g., 20.0 RPM, 90.0 RPM)
SugenoFuzzyRule<Double, Double> rule1 = new SugenoFuzzyRule<>("Rule1", tempCold, 20.0);
SugenoFuzzyRule<Double, Double> rule2 = new SugenoFuzzyRule<>("Rule2", tempHot, 90.0);

FuzzyRuleSystem<Double, Double, SugenoFuzzyRule<Double, Double>> sugenoSystem = 
    new FuzzyRuleSystem<>(RuleType.SUGENO);

sugenoSystem.add(rule1);
sugenoSystem.add(rule2);

// Evaluate Sugeno Defuzzification (Weighted Average)
Double output = sugenoSystem.defuzzy(12.0, 0.0, 100.0, 1.0, null);
System.out.println("Sugeno Crisp Output: " + output);
```

## 🛠 Project Structure

```
src/
├── entities/
│   ├── arithmetic/
│   │   └── Interval.java
│   ├── number/
│   │   ├── FuzzyNumber.java
│   │   ├── TriangularFuzzyNumber.java
│   │   ├── TrapezoidalFuzzyNumber.java
│   │   ├── GaussianFuzzyNumber.java
│   │   ├── GeneralizedBellCurveFuzzyNumber.java
│   │   └── GenericFuzzyNumber.java
│   └── rule/
│       ├── FuzzyExpression.java
│       ├── FuzzyRule.java
│       ├── MamdaniFuzzyRule.java
│       ├── SugenoFuzzyRule.java
│       ├── FuzzyRuleSystem.java
│       └── RuleType.java
├── interfaces/
│   └── MembershipFunction.java
└── utilities/
    ├── AlphaCutFuzzyArithmetic.java
    ├── CastUtilities.java
    ├── Defuzzifier.java
    └── FuzzyOperators.java
```

## 📄 License

This library is open-source and available under the [MIT License](LICENSE).