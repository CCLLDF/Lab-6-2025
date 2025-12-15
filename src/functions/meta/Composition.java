package functions.meta;

import functions.Function;

/**
 * Класс для представления композиции двух функций.
 * Реализует интерфейс Function.
 */
public class Composition implements Function {
    private final Function f1;
    private final Function f2;

    /**
     * Конструктор класса Composition.
     * @param f1 первая функция (внутренняя)
     * @param f2 вторая функция (внешняя)
     * Композиция: f2(f1(x))
     */
    public Composition(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Functions must not be null");
        }
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public double getLeftDomainBorder() {
        // Область определения совпадает с областью определения первой функции
        return f1.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        // Область определения совпадает с областью определения первой функции
        return f1.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        // Композиция: f2(f1(x))
        double innerValue = f1.getFunctionValue(x);
        return f2.getFunctionValue(innerValue);
    }
}



