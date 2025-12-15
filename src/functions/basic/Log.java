package functions.basic;

import functions.Function;

/**
 * Класс для вычисления логарифма по заданному основанию.
 * Реализует интерфейс Function.
 */
public class Log implements Function {
    private static final double EPSILON = 1e-10;
    private final double base;

    /**
     * Конструктор класса Log.
     * @param base основание логарифма (должно быть положительным и не равным 1)
     */
    public Log(double base) {
        if (base <= 0 || Math.abs(base - 1.0) < EPSILON) {
            throw new IllegalArgumentException("Base must be positive and not equal to 1");
        }
        this.base = base;
    }

    @Override
    public double getLeftDomainBorder() {
        return 0;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN;
        }
        // log_base(x) = ln(x) / ln(base)
        return Math.log(x) / Math.log(base);
    }
}

