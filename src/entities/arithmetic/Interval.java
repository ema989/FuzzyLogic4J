package entities.arithmetic;

public record Interval(
        double min,
        double max) {

    public Interval {
        if (min > max)
            throw new IllegalArgumentException("min can't be greater than max");
    }

    public Interval add(Interval other) {
        return new Interval(this.min + other.min, this.max + other.max);
    }

    public Interval subtract(Interval other) {
        return new Interval(this.min - other.max, this.max - other.min);
    }

    public Interval multiply(Interval other) {
        double p1 = this.min * other.min;
        double p2 = this.min * other.max;
        double p3 = this.max * other.min;
        double p4 = this.max * other.max;

        double min = Math.min(Math.min(p1, p2), Math.min(p3, p4));
        double max = Math.max(Math.max(p1, p2), Math.max(p3, p4));

        return new Interval(min, max);
    }

    public Interval divide(Interval other) {
        if (other.min <= 0 && other.max >= 0) {
            throw new ArithmeticException("Divisione per un intervallo che contiene lo zero.");
        }
        double p1 = this.min / other.min;
        double p2 = this.min / other.max;
        double p3 = this.max / other.min;
        double p4 = this.max / other.max;

        double min = Math.min(Math.min(p1, p2), Math.min(p3, p4));
        double max = Math.max(Math.max(p1, p2), Math.max(p3, p4));

        return new Interval(min, max);
    }

}
