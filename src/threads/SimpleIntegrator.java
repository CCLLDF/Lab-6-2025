package threads;

import functions.Functions;

/**
 * Класс для решения заданий на интегрирование.
 * Реализует интерфейс Runnable.
 */
public class SimpleIntegrator implements Runnable {
    private final Task task;

    /**
     * Конструктор класса SimpleIntegrator.
     * @param task объект задания
     */
    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        // Небольшая задержка для предотвращения NullPointerException
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        for (int i = 0; i < task.getTaskCount(); i++) {
            synchronized (task) {
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
                }
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
