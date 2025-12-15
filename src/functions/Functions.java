package functions;

import functions.meta.*;

/**
 * Утилитарный класс, содержащий вспомогательные статические методы
 * для работы с функциями. Нельзя создать объект этого класса.
 */
public class Functions {

    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private Functions() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Возвращает объект функции, полученной из исходной сдвигом вдоль осей.
     * @param f исходная функция
     * @param shiftX величина сдвига вдоль оси абсцисс
     * @param shiftY величина сдвига вдоль оси ординат
     * @return функция, полученная сдвигом исходной функции
     */
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    /**
     * Возвращает объект функции, полученной из исходной масштабированием вдоль осей.
     * @param f исходная функция
     * @param scaleX коэффициент масштабирования вдоль оси абсцисс
     * @param scaleY коэффициент масштабирования вдоль оси ординат
     * @return функция, полученная масштабированием исходной функции
     */
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    /**
     * Возвращает объект функции, являющейся заданной степенью исходной.
     * @param f исходная функция
     * @param power степень, в которую возводятся значения функции
     * @return функция, являющаяся степенью исходной функции
     */
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    /**
     * Возвращает объект функции, являющейся суммой двух исходных.
     * @param f1 первая функция
     * @param f2 вторая функция
     * @return функция, являющаяся суммой двух исходных функций
     */
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    /**
     * Возвращает объект функции, являющейся произведением двух исходных.
     * @param f1 первая функция
     * @param f2 вторая функция
     * @return функция, являющаяся произведением двух исходных функций
     */
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    /**
     * Возвращает объект функции, являющейся композицией двух исходных.
     * @param f1 первая функция (внутренняя)
     * @param f2 вторая функция (внешняя)
     * @return функция, являющаяся композицией f2(f1(x))
     */
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    /**
     * Вычисляет определенный интеграл функции на заданном интервале методом трапеций.
     * @param function функция для интегрирования
     * @param leftBound левая граница интегрирования
     * @param rightBound правая граница интегрирования
     * @param step шаг дискретизации
     * @return значение интеграла
     * @throws IllegalArgumentException если интервал выходит за границы области определения функции
     */
    public static double integrate(Function function, double leftBound, double rightBound, double step) {
        if (leftBound >= rightBound) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг дискретизации должен быть положительным");
        }

        // Проверка границ области определения
        if (leftBound < function.getLeftDomainBorder() || rightBound > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования [" + leftBound + ", " + rightBound +
                    "] выходит за границы области определения функции [" +
                    function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
        }

        double integral = 0.0;
        double x = leftBound;

        while (x < rightBound) {
            double xNext = Math.min(x + step, rightBound);
            double h = xNext - x; // длина текущего участка

            // Площадь трапеции: h * (f(x) + f(xNext)) / 2
            double f_x = function.getFunctionValue(x);
            double f_xNext = function.getFunctionValue(xNext);
            integral += h * (f_x + f_xNext) / 2.0;

            x = xNext;
        }

        return integral;
    }
}



