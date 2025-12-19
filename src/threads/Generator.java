package threads;

import functions.Function;
import functions.basic.Log;

import java.util.Random;

/**
 * Класс-генератор заданий для интегрирования.
 * Расширяет класс Thread для работы в отдельном потоке.
 * Использует семафор для синхронизации доступа к заданию.
 */
public class Generator extends Thread {
    private final Task task;
    private final ReadWriteSemaphore semaphore;
    private final Random random;

    /**
     * Конструктор класса Generator.
     * @param task объект задания, в который будут заноситься параметры
     * @param semaphore семафор для синхронизации доступа
     */
    public Generator(Task task, ReadWriteSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
        this.random = new Random();
    }

    @Override
    public void run() {
        int tasksCount = task.getTasksCount();
        
        for (int i = 0; i < tasksCount; i++) {
            // Проверяем, не был ли поток прерван - проверяем в начале каждой итерации
            if (Thread.currentThread().isInterrupted()) {
                System.out.printf("[Generator] Поток прерван на задании %d из %d%n", i + 1, tasksCount);
                break;
            }
            
            try {
                // Проверяем прерывание перед вызовом семафора
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Поток прерван перед записью");
                }
                // Создаем логарифмическую функцию со случайным основанием от 1 до 10
                // Основание должно быть > 1 и не равно 1
                double base = 1.0 + 1e-10 + random.nextDouble() * (10.0 - 1.0 - 1e-10);
                Function logFunction = new Log(base);
                
                // Левая граница: случайно от 0 до 100
                double leftBound = random.nextDouble() * 100.0;
                
                // Правая граница: случайно от 100 до 200
                double rightBound = 100.0 + random.nextDouble() * 100.0;
                
                // Убеждаемся, что правая граница больше левой
                if (rightBound <= leftBound) {
                    rightBound = leftBound + 0.1;
                }
                
                // Шаг дискретизации: случайно от 0 до 1
                double step = random.nextDouble();
                if (step == 0.0 || step < 1e-10) {
                    step = 1e-10;
                }
                // Убеждаемся, что шаг не больше длины интервала
                double intervalLength = rightBound - leftBound;
                if (step > intervalLength) {
                    step = intervalLength / 2.0;
                }
                
                // Используем семафор для записи вместо synchronized блока
                semaphore.startWrite();
                try {
                    // Проверяем прерывание после получения доступа
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Поток прерван во время записи");
                    }
                    task.setTask(logFunction, leftBound, rightBound, step);
                    task.setProcessed(false); // Сбрасываем флаг обработки
                } finally {
                    semaphore.endWrite();
                }

                // Выводим сообщение Source
                System.out.printf("Source %.6f %.6f %.6f%n", leftBound, rightBound, step);

                // Ждем, пока интегратор полностью обработает задание
                while (!Thread.currentThread().isInterrupted()) {
                    if (task.isProcessed()) {
                        break; // Задание обработано, можно создавать следующее
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                
            } catch (InterruptedException e) {
                System.out.printf("[Generator] Поток прерван во время генерации задания %d из %d%n", i + 1, tasksCount);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.printf("Ошибка при генерации задания %d: %s%n", i + 1, e.getMessage());
            }
        }
        System.out.println("[Generator] Выполнение заданий завершено. Сгенерировано заданий: " + tasksCount);
        // Помечаем генератор как завершивший работу
        task.setGeneratorFinished();
        // Небольшая пауза, чтобы другие потоки успели увидеть флаг
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

