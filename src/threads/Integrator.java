package threads;

import functions.Functions;

/**
 * Класс для решения заданий на интегрирование с использованием семафора.
 * Расширяет класс Thread.
 */
public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    /**
     * Конструктор класса Integrator.
     * @param task объект задания
     * @param semaphore объект семафора для синхронизации
     */
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount() && !Thread.currentThread().isInterrupted(); i++) {
                // Захватываем семафор для чтения
                semaphore.beginRead();

                try {
                    // Вычисление интеграла
                    double result = Functions.integrate(task.getFunction(), task.getLeftBound(),
                            task.getRightBound(), task.getStep());

                    // Вывод сообщения Result
                    System.out.printf("Result %.2f %.2f %.5f %.10f%n",
                            task.getLeftBound(), task.getRightBound(), task.getStep(), result);
                } catch (Exception e) {
                    System.out.printf("Result %.2f %.2f %.5f ERROR: %s%n",
                            task.getLeftBound(), task.getRightBound(), task.getStep(), e.getMessage());
                } finally {
                    // Освобождаем семафор чтения
                    semaphore.endRead();
                    // Сбрасываем флаг готовности данных для следующего цикла
                    semaphore.resetDataReady();
                }

                // Небольшая задержка для демонстрации работы потоков
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Integrator thread was interrupted");
        }
    }
}
