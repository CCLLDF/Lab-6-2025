package threads;

import functions.Function;
import functions.basic.Log;

/**
 * Класс для генерации заданий на интегрирование с использованием семафора.
 * Расширяет класс Thread.
 */
public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    /**
     * Конструктор класса Generator.
     * @param task объект задания
     * @param semaphore объект семафора для синхронизации
     */
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount() && !Thread.currentThread().isInterrupted(); i++) {
                // Захватываем семафор для записи
                semaphore.beginWrite();

                try {
                    // Создание логарифмической функции с случайным основанием от 1 до 10
                    // Основание не должно быть равно 1, поэтому используем диапазон (1, 10]
                    double base = 1.0 + Math.random() * 9.0; // от 1+eps до 10
                    Function logFunction = new Log(base);
                    task.setFunction(logFunction);

                    // Левая граница: случайно от 0.01 до 100 (чтобы избежать x=0)
                    double leftBound = 0.01 + Math.random() * 99.99;
                    task.setLeftBound(leftBound);

                    // Правая граница: случайно от 100 до 200
                    double rightBound = 100.0 + Math.random() * 100.0;
                    task.setRightBound(rightBound);

                    // Шаг дискретизации: случайно от 0 до 1
                    double step = Math.random(); // от 0.0 до 1.0
                    task.setStep(step);

                    // Вывод сообщения Source
                    System.out.printf("Source %.2f %.2f %.5f%n", leftBound, rightBound, step);
                } finally {
                    // Освобождаем семафор записи
                    semaphore.endWrite();
                }

                // Небольшая задержка для демонстрации работы потоков
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Generator thread was interrupted");
        }
    }
}
