package threads;

/**
 * Одноместный семафор, различающий операции чтения и записи.
 * Гарантирует, что в каждый момент времени только один поток может
 * выполнять операцию чтения или записи.
 */
public class ReadWriteSemaphore {
    private int readers = 0;      // Количество активных читателей
    private int writers = 0;      // Количество активных писателей (0 или 1)
    private int waitingWriters = 0; // Количество ожидающих писателей

    /**
     * Захватывает семафор для операции записи.
     * Блокирует выполнение, если есть активные читатели или писатели.
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public synchronized void startWrite() throws InterruptedException {
        waitingWriters++;
        try {
            // Ждем, пока не освободятся все читатели и писатели
            while (readers > 0 || writers > 0) {
                // Проверяем прерывание перед ожиданием
                if (Thread.currentThread().isInterrupted()) {
                    notifyAll(); // Разбудим другие потоки
                    throw new InterruptedException("Поток прерван во время ожидания записи");
                }
                try {
                    wait(100); // Таймаут 100 мс для wait(), чтобы периодически проверять прерывание
                } catch (InterruptedException e) {
                    waitingWriters--; // Уменьшаем счетчик в finally не сработает, если исключение
                    notifyAll(); // Разбудим другие потоки при прерывании
                    throw e;
                }
            }
            // Проверяем прерывание после выхода из wait()
            if (Thread.currentThread().isInterrupted()) {
                notifyAll(); // Разбудим другие потоки
                throw new InterruptedException("Поток прерван после ожидания записи");
            }
            writers = 1;
        } finally {
            waitingWriters--;
        }
    }

    /**
     * Освобождает семафор после операции записи.
     */
    public synchronized void endWrite() {
        writers = 0;
        notifyAll(); // Уведомляем всех ожидающих потоков
    }

    /**
     * Захватывает семафор для операции чтения.
     * Блокирует выполнение, если есть активные писатели или ожидающие писатели.
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public synchronized void startRead() throws InterruptedException {
        // Ждем, пока не освободятся все писатели и не закончатся ожидающие писатели
        while (writers > 0 || waitingWriters > 0) {
            // Проверяем прерывание перед ожиданием
            if (Thread.currentThread().isInterrupted()) {
                notifyAll(); // Разбудим другие потоки
                throw new InterruptedException("Поток прерван во время ожидания чтения");
            }
            try {
                wait();
            } catch (InterruptedException e) {
                notifyAll(); // Разбудим другие потоки при прерывании
                throw e;
            }
        }
        // Проверяем прерывание после выхода из wait()
        if (Thread.currentThread().isInterrupted()) {
            notifyAll(); // Разбудим другие потоки
            throw new InterruptedException("Поток прерван после ожидания чтения");
        }
        readers++;
    }

    /**
     * Освобождает семафор после операции чтения.
     */
    public synchronized void endRead() {
        readers--;
        if (readers == 0) {
            notifyAll(); // Уведомляем всех ожидающих потоков
        }
    }
}

