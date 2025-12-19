package threads;

import functions.Function;

/**
 * Класс для хранения параметров задания на интегрирование.
 */
public class Task {
    private Function function;
    private double leftBound;
    private double rightBound;
    private double step;
    private int tasksCount;
    private boolean isReady; // Флаг готовности задания
    private volatile boolean generatorFinished; // Флаг завершения генератора
    private volatile boolean processed; // Флаг обработки задания

    /**
     * Конструктор по умолчанию.
     */
    public Task() {
        this.tasksCount = 0;
        this.isReady = false;
        this.generatorFinished = false;
        this.processed = true; // Изначально задание "обработано" (нет задания)
    }

    /**
     * Возвращает функцию для интегрирования.
     * @return функция для интегрирования
     */
    public synchronized Function getFunction() {
        return function;
    }

    /**
     * Устанавливает функцию для интегрирования.
     * @param function функция для интегрирования
     */
    public synchronized void setFunction(Function function) {
        this.function = function;
    }

    /**
     * Возвращает левую границу области интегрирования.
     * @return левая граница области интегрирования
     */
    public synchronized double getLeftBound() {
        return leftBound;
    }

    /**
     * Устанавливает левую границу области интегрирования.
     * @param leftBound левая граница области интегрирования
     */
    public synchronized void setLeftBound(double leftBound) {
        this.leftBound = leftBound;
    }

    /**
     * Возвращает правую границу области интегрирования.
     * @return правая граница области интегрирования
     */
    public synchronized double getRightBound() {
        return rightBound;
    }

    /**
     * Устанавливает правую границу области интегрирования.
     * @param rightBound правая граница области интегрирования
     */
    public synchronized void setRightBound(double rightBound) {
        this.rightBound = rightBound;
    }

    /**
     * Возвращает шаг дискретизации.
     * @return шаг дискретизации
     */
    public synchronized double getStep() {
        return step;
    }

    /**
     * Устанавливает шаг дискретизации.
     * @param step шаг дискретизации
     */
    public synchronized void setStep(double step) {
        this.step = step;
    }

    /**
     * Возвращает количество выполняемых заданий.
     * @return количество выполняемых заданий
     */
    public int getTasksCount() {
        return tasksCount;
    }

    /**
     * Устанавливает количество выполняемых заданий.
     * @param tasksCount количество выполняемых заданий
     */
    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    /**
     * Проверяет, готово ли задание к обработке.
     * @return true, если задание готово
     */
    public synchronized boolean isReady() {
        return isReady;
    }

    /**
     * Устанавливает готовность задания.
     * @param ready true, если задание готово к обработке
     */
    public synchronized void setReady(boolean ready) {
        this.isReady = ready;
        notifyAll();
    }

    /**
     * Атомарно устанавливает все параметры задания и помечает его как готовое.
     * @param function функция для интегрирования
     * @param leftBound левая граница области интегрирования
     * @param rightBound правая граница области интегрирования
     * @param step шаг дискретизации
     */
    public synchronized void setTask(Function function, double leftBound, double rightBound, double step) {
        this.function = function;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.step = step;
        this.isReady = true;
    }

    /**
     * Атомарно получает все параметры задания и помечает его как обработанное.
     * @return объект TaskData с параметрами или null, если задание не готово
     */
    public synchronized TaskData getTaskData() {
        if (!isReady || function == null) {
            return null;
        }
        // Создаем копию данных
        TaskData data = new TaskData(function, leftBound, rightBound, step);
        // Помечаем задание как обработанное
        isReady = false;
        return data;
    }

    /**
     * Помечает генератор как завершивший работу.
     */
    public synchronized void setGeneratorFinished() {
        this.generatorFinished = true;
        notifyAll(); // Уведомляем ожидающие потоки
    }

    /**
     * Устанавливает флаг обработки задания.
     * @param processed true, если задание обработано
     */
    public synchronized void setProcessed(boolean processed) {
        this.processed = processed;
        notifyAll(); // Уведомляем ожидающие потоки
    }

    /**
     * Проверяет, обработано ли задание.
     * @return true, если задание обработано
     */
    public synchronized boolean isProcessed() {
        return processed;
    }

    /**
     * Проверяет, завершил ли генератор работу.
     * @return true, если генератор завершил работу
     */
    public synchronized boolean isGeneratorFinished() {
        return generatorFinished;
    }

    /**
     * Внутренний класс для хранения данных задания.
     */
    public static class TaskData {
        public final Function function;
        public final double leftBound;
        public final double rightBound;
        public final double step;

        public TaskData(Function function, double leftBound, double rightBound, double step) {
            this.function = function;
            this.leftBound = leftBound;
            this.rightBound = rightBound;
            this.step = step;
        }
    }
}

