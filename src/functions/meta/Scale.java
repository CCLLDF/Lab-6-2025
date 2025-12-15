package functions.meta;

import functions.Function;

/**
 * Класс для представления функции, полученной масштабированием исходной функции
 * вдоль осей координат.
 * Реализует интерфейс Function.
 */
public class Scale implements Function {
    private static final double EPSILON = 1e-10;
    private final Function function;
    private final double scaleX;
    private final double scaleY;

    /**
     * Конструктор класса Scale.
     * @param function исходная функция
     * @param scaleX коэффициент масштабирования вдоль оси абсцисс
     * @param scaleY коэффициент масштабирования вдоль оси ординат
     */
    public Scale(Function function, double scaleX, double scaleY) {
        if (function == null) {
            throw new IllegalArgumentException("Function must not be null");
        }
        if (Math.abs(scaleX) < EPSILON) {
            throw new IllegalArgumentException("ScaleX must not be zero");
        }
        this.function = function;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public double getLeftDomainBorder() {
        // Масштабирование области определения вдоль оси абсцисс
        double left = function.getLeftDomainBorder();
        double right = function.getRightDomainBorder();
        
        if (scaleX > 0) {
            return left / scaleX;
        } else {
            // При отрицательном масштабе границы меняются местами
            return right / scaleX;
        }
    }

    @Override
    public double getRightDomainBorder() {
        // Масштабирование области определения вдоль оси абсцисс
        double left = function.getLeftDomainBorder();
        double right = function.getRightDomainBorder();
        
        if (scaleX > 0) {
            return right / scaleX;
        } else {
            // При отрицательном масштабе границы меняются местами
            return left / scaleX;
        }
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        // Масштабирование: g(x) = scaleY * f(x * scaleX)
        double scaledX = x * scaleX;
        return scaleY * function.getFunctionValue(scaledX);
    }
}

