package functions.basic;

/**
 * Класс для вычисления тангенса.
 * Наследуется от TrigonometricFunction.
 */
public class Tan extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        return Math.tan(x);
    }
}



