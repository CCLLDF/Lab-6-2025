package functions.meta;

import functions.Function;

/**
 * Класс для представления функции, полученной сдвигом исходной функции
 * вдоль осей координат.
 * Реализует интерфейс Function.
 */
public class Shift implements Function {
    private final Function function;
    private final double shiftX;
    private final double shiftY;

    /**
     * Конструктор класса Shift.
     * @param function исходная функция
     * @param shiftX величина сдвига вдоль оси абсцисс
     * @param shiftY величина сдвига вдоль оси ординат
     */
    public Shift(Function function, double shiftX, double shiftY) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        this.function = function;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    @Override
    public double getLeftDomainBorder() {
        // Сдвиг области определения вдоль оси абсцисс
        return function.getLeftDomainBorder() + shiftX;
    }

    @Override
    public double getRightDomainBorder() {
        // Сдвиг области определения вдоль оси абсцисс
        return function.getRightDomainBorder() + shiftX;
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        // Сдвиг: g(x) = shiftY + f(x - shiftX)
        double shiftedX = x - shiftX;
        return shiftY + function.getFunctionValue(shiftedX);
    }
}

