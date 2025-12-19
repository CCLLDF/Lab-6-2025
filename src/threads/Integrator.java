package threads;

import functions.Functions;

/**
 * Класс-интегратор для решения заданий на интегрирование.
 * Расширяет класс Thread для работы в отдельном потоке.
 * Использует семафор для синхронизации доступа к заданию.
 */
public class Integrator extends Thread {
    private final Task task;
    private final ReadWriteSemaphore semaphore;

    /**
     * Конструктор класса Integrator.
     * @param task объект задания, из которого будут браться данные для решения
     * @param semaphore семафор для синхронизации доступа
     */
    public Integrator(Task task, ReadWriteSemaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        int tasksCount = task.getTasksCount();
        int completedTasks = 0;
        int emptyIterations = 0; // Счетчик пустых итераций (когда заданий нет)
        final int MAX_EMPTY_ITERATIONS = 1000; // Максимальное количество пустых итераций (увеличено для надежности)

        while (true) {
            // Проверяем, не был ли поток прерван - проверяем в начале каждой итерации
            if (Thread.currentThread().isInterrupted()) {
                System.out.printf("[Integrator] Поток прерван. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                break;
            }

            // Проверяем условие завершения: генератор завершил работу И все задания обработаны
            if (task.isGeneratorFinished() && completedTasks >= tasksCount) {
                System.out.printf("[Integrator] Генератор завершил работу и все задания обработаны. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                break;
            }


            try {
                // Проверяем прерывание перед вызовом семафора
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException("Поток прерван перед чтением");
                }

                // Используем семафор для чтения вместо synchronized блока
                Task.TaskData taskData = null;
                semaphore.startRead();
                try {
                    // Проверяем прерывание после получения доступа
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException("Поток прерван во время чтения");
                    }
                    taskData = task.getTaskData();
                } finally {
                    semaphore.endRead();
                }

                // Если задание готово, обрабатываем его
                if (taskData != null) {
                    emptyIterations = 0; // Сбрасываем счетчик пустых итераций
                    // Вычисляем значение интеграла (вне синхронизированного блока)
                    double result = Functions.integrate(
                        taskData.function,
                        taskData.leftBound,
                        taskData.rightBound,
                        taskData.step
                    );

                    // Выводим сообщение Result
                    System.out.printf("Result %.6f %.6f %.6f %.15f%n",
                        taskData.leftBound, taskData.rightBound, taskData.step, result);

                    completedTasks++;

                    // Помечаем задание как обработанное
                    task.setProcessed(true);

                    // Проверяем условие завершения после обработки задания
                    if (task.isGeneratorFinished() && completedTasks >= tasksCount) {
                        System.out.printf("[Integrator] Генератор завершил работу и все задания обработаны. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                        return; // Выходим из метода, завершая поток
                    }
                } else {
                    // Проверяем, завершил ли генератор работу и все задания обработаны
                    if (task.isGeneratorFinished() && completedTasks >= tasksCount) {
                        System.out.printf("[Integrator] Генератор завершил работу и все задания обработаны. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                        return; // Выходим из метода, завершая поток
                    }

                    emptyIterations++;
                    // Если генератор еще работает, продолжаем ждать новые задания
                    if (!task.isGeneratorFinished() && emptyIterations > MAX_EMPTY_ITERATIONS) {
                        emptyIterations = 0; // Сбрасываем счетчик, продолжаем ждать
                    }
                    // Если задание еще не готово, делаем небольшую паузу
                    // Проверяем прерывание перед паузой
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.printf("[Integrator] Поток прерван перед паузой. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                        break;
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.printf("[Integrator] Поток прерван во время ожидания. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                        Thread.currentThread().interrupt();
                        break;
                    }
                    // Дополнительная проверка прерывания после паузы
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.printf("[Integrator] Поток прерван после паузы. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                        break;
                    }
                }

            } catch (InterruptedException e) {
                System.out.printf("[Integrator] Поток прерван во время интегрирования. Выполнено заданий: %d из %d%n", completedTasks, tasksCount);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.printf("Ошибка при интегрировании задания %d: %s%n",
                    completedTasks + 1, e.getMessage());
                // Продолжаем выполнение даже при ошибке
                completedTasks++;
            }
        }
        System.out.println("[Integrator] Выполнение заданий завершено. Обработано заданий: " + completedTasks);
    }
}

