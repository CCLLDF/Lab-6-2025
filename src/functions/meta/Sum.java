package functions.meta;

import functions.Function;

/**
 * Класс для представления суммы двух функций.
 * Реализует интерфейс Function.
 */
public class Sum implements Function {
    private final Function f1;
    private final Function f2;

    /**
     * Конструктор класса Sum.
     * @param f1 первая функция
     * @param f2 вторая функция
     */
    public Sum(Function f1, Function f2) {
        if (f1 == null || f2 == null) {
            throw new IllegalArgumentException("Functions must not be null");
        }
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public double getLeftDomainBorder() {
        // Пересечение областей определения: максимум левых границ
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }

    @Override
    public double getRightDomainBorder() {
        // Пересечение областей определения: минимум правых границ
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }

    @Override
    public double getFunctionValue(double x) {
        // Проверка, что точка принадлежит пересечению областей определения
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f1.getFunctionValue(x) + f2.getFunctionValue(x);
    }
}



