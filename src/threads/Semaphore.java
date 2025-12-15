package threads;

/**
 * Одноместный семафор, различающий операции чтения и записи.
 * Реализует паттерн "turnstile" для синхронизации доступа к разделяемому ресурсу.
 */
public class Semaphore {
    private boolean isWriting = false;
    private int readersCount = 0;
    private boolean isDataReady = false;

    /**
     * Начинает операцию чтения.
     * Поток блокируется, пока не завершится запись или не будут готовы данные.
     */
    public synchronized void beginRead() throws InterruptedException {
        while (isWriting || !isDataReady) {
            wait();
        }
        readersCount++;
    }

    /**
     * Завершает операцию чтения.
     */
    public synchronized void endRead() {
        readersCount--;
        if (readersCount == 0) {
            notifyAll();
        }
    }

    /**
     * Начинает операцию записи.
     * Поток блокируется, пока есть активные читатели.
     */
    public synchronized void beginWrite() throws InterruptedException {
        while (readersCount > 0 || isWriting) {
            wait();
        }
        isWriting = true;
    }

    /**
     * Завершает операцию записи и помечает данные как готовые.
     */
    public synchronized void endWrite() {
        isWriting = false;
        isDataReady = true;
        notifyAll();
    }

    /**
     * Сбрасывает флаг готовности данных.
     * Используется для подготовки к новой порции данных.
     */
    public synchronized void resetDataReady() {
        isDataReady = false;
    }
}
