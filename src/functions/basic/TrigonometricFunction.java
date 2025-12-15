package functions.basic;

import functions.Function;

/**
 * Базовый класс для тригонометрических функций.
 * Реализует интерфейс Function и описывает общие методы
 * получения границ области определения для тригонометрических функций.
 */
public abstract class TrigonometricFunction implements Function {
    @Override
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
}



