package threads;

import functions.Function;
import functions.basic.Log;

/**
 * Класс для генерации заданий на интегрирование.
 * Реализует интерфейс Runnable.
 */
public class SimpleGenerator implements Runnable {
    private final Task task;

    /**
     * Конструктор класса SimpleGenerator.
     * @param task объект задания
     */
    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            synchronized (task) {
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
            }

            // Небольшая задержка для демонстрации работы потоков
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
