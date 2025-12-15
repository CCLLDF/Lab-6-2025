package functions.basic;

/**
 * Класс для вычисления косинуса.
 * Наследуется от TrigonometricFunction.
 */
public class Cos extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        return Math.cos(x);
    }
}



