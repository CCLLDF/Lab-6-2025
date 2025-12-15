package threads;

import functions.Function;

/**
 * Класс для хранения параметров задания на интегрирование.
 * Содержит функцию, границы интегрирования, шаг дискретизации и количество заданий.
 */
public class Task {
    private Function function;
    private double leftBound;
    private double rightBound;
    private double step;
    private int taskCount;

    /**
     * Конструктор по умолчанию.
     */
    public Task() {
    }

    /**
     * Конструктор с параметрами.
     * @param function функция для интегрирования
     * @param leftBound левая граница интегрирования
     * @param rightBound правая граница интегрирования
     * @param step шаг дискретизации
     * @param taskCount количество выполняемых заданий
     */
    public Task(Function function, double leftBound, double rightBound, double step, int taskCount) {
        this.function = function;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.step = step;
        this.taskCount = taskCount;
    }

    /**
     * Возвращает функцию для интегрирования.
     * @return функция
     */
    public Function getFunction() {
        return function;
    }

    /**
     * Устанавливает функцию для интегрирования.
     * @param function функция
     */
    public void setFunction(Function function) {
        this.function = function;
    }

    /**
     * Возвращает левую границу интегрирования.
     * @return левая граница
     */
    public double getLeftBound() {
        return leftBound;
    }

    /**
     * Устанавливает левую границу интегрирования.
     * @param leftBound левая граница
     */
    public void setLeftBound(double leftBound) {
        this.leftBound = leftBound;
    }

    /**
     * Возвращает правую границу интегрирования.
     * @return правая граница
     */
    public double getRightBound() {
        return rightBound;
    }

    /**
     * Устанавливает правую границу интегрирования.
     * @param rightBound правая граница
     */
    public void setRightBound(double rightBound) {
        this.rightBound = rightBound;
    }

    /**
     * Возвращает шаг дискретизации.
     * @return шаг дискретизации
     */
    public double getStep() {
        return step;
    }

    /**
     * Устанавливает шаг дискретизации.
     * @param step шаг дискретизации
     */
    public void setStep(double step) {
        this.step = step;
    }

    /**
     * Возвращает количество выполняемых заданий.
     * @return количество заданий
     */
    public int getTaskCount() {
        return taskCount;
    }

    /**
     * Устанавливает количество выполняемых заданий.
     * @param taskCount количество заданий
     */
    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}
