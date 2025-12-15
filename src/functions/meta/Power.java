package functions.meta;

import functions.Function;

/**
 * Класс для представления функции, возведённой в степень.
 * Реализует интерфейс Function.
 */
public class Power implements Function {
    private final Function function;
    private final double power;

    /**
     * Конструктор класса Power.
     * @param function базовая функция
     * @param power степень, в которую возводятся значения функции
     */
    public Power(Function function, double power) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        this.function = function;
        this.power = power;
    }

    @Override
    public double getLeftDomainBorder() {
        // Область определения совпадает с областью определения исходной функции
        return function.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        // Область определения совпадает с областью определения исходной функции
        return function.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double value = function.getFunctionValue(x);
        return Math.pow(value, power);
    }
}



