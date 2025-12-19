import functions.Function;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.Functions;
import functions.basic.Sin;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;
import threads.Task;
import threads.SimpleGenerator;
import threads.SimpleIntegrator;
import threads.Generator;
import threads.Integrator;
import threads.ReadWriteSemaphore;

import java.io.*;
import java.util.Random;

public class    Main {
    private static final double PI = Math.PI;

    public static void main(String[] args) {
        try {
            // Тест 1: Sin и Cos на отрезке от 0 до π
            testSinAndCos();

            // Тест 2: Табулированные аналоги Sin и Cos
            testTabulatedSinAndCos();

            // Тест 3: Сумма квадратов табулированных синуса и косинуса
            testSumOfSquares();

            // Тест 4: Экспонента с записью/чтением через символьный поток
            testExpWithTextFile();

            // Тест 5: Логарифм с записью/чтением через байтовый поток
            testLogWithBinaryFile();

            // Тест 6: Изучение содержимого файлов
            analyzeFiles();

            // Тест 7: Интегрирование экспоненты
            testExpIntegration();

            // Тест 8: Последовательное выполнение заданий
            nonThread();

            // Тест 9: Многопоточное выполнение заданий
            simpleThreads();

            // Тест 10: Многопоточное выполнение с семафором
            complicatedThreads();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== ВСЕ ТЕСТЫ ЗАВЕРШЕНЫ ===");
        System.out.println("Программа завершает работу.\n");
    }

    /**
     * Тест 1: Создание объектов Sin и Cos, вывод значений на отрезке от 0 до π с шагом 0.1
     */
    private static void testSinAndCos() {
        System.out.println("=== ТЕСТ 1: SIN И COS ===\n");

        Function sin = new Sin();
        Function cos = new Cos();

        System.out.println("Значения sin(x) и cos(x) на отрезке [0, π] с шагом 0.1:");
        System.out.printf("%-8s %-15s %-15s%n", "x", "sin(x)", "cos(x)");
        System.out.println("---------------------------------------------------");

        for (double x = 0; x <= PI + 0.05; x += 0.1) {
            double sinValue = sin.getFunctionValue(x);
            double cosValue = cos.getFunctionValue(x);
            System.out.printf("%-8.2f %-15.6f %-15.6f%n", x, sinValue, cosValue);
        }
        System.out.println();
    }

    /**
     * Тест 2: Табулированные аналоги Sin и Cos, сравнение с исходными функциями
     */
    private static void testTabulatedSinAndCos() {
        System.out.println("=== ТЕСТ 2: ТАБУЛИРОВАННЫЕ АНАЛОГИ SIN И COS ===\n");

        Function sin = new Sin();
        Function cos = new Cos();

        // Создание табулированных аналогов с 10 точками
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, PI, 10);

        System.out.println("Сравнение исходных и табулированных функций на отрезке [0, π] с шагом 0.1:");
        System.out.printf("%-8s %-15s %-15s %-15s %-15s%n", 
            "x", "sin(x)", "tab_sin(x)", "cos(x)", "tab_cos(x)");
        System.out.println("--------------------------------------------------------------------------------");

        for (double x = 0; x <= PI + 0.05; x += 0.1) {
            double sinValue = sin.getFunctionValue(x);
            double tabSinValue = tabulatedSin.getFunctionValue(x);
            double cosValue = cos.getFunctionValue(x);
            double tabCosValue = tabulatedCos.getFunctionValue(x);

            System.out.printf("%-8.2f %-15.6f %-15.6f %-15.6f %-15.6f%n",
                x, sinValue, tabSinValue, cosValue, tabCosValue);
        }

        // Вычисление максимальной погрешности
        double maxSinError = 0;
        double maxCosError = 0;
        for (double x = 0; x <= PI + 0.05; x += 0.1) {
            double sinError = Math.abs(sin.getFunctionValue(x) - tabulatedSin.getFunctionValue(x));
            double cosError = Math.abs(cos.getFunctionValue(x) - tabulatedCos.getFunctionValue(x));
            if (sinError > maxSinError) maxSinError = sinError;
            if (cosError > maxCosError) maxCosError = cosError;
        }

        System.out.printf("\nМаксимальная погрешность для sin: %.6f%n", maxSinError);
        System.out.printf("Максимальная погрешность для cos: %.6f%n", maxCosError);
        System.out.println();
    }

    /**
     * Тест 3: Сумма квадратов табулированных синуса и косинуса
     */
    private static void testSumOfSquares() {
        System.out.println("=== ТЕСТ 3: СУММА КВАДРАТОВ ТАБУЛИРОВАННЫХ SIN И COS ===\n");

        Function sin = new Sin();
        Function cos = new Cos();

        // Тестирование с разным количеством точек
        int[] pointCounts = {10, 20, 50};

        for (int pointsCount : pointCounts) {
            System.out.println("Количество точек табуляции: " + pointsCount);

            TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, PI, pointsCount);
            TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, PI, pointsCount);

            // Создание квадратов функций
            Function sinSquared = Functions.power(tabulatedSin, 2);
            Function cosSquared = Functions.power(tabulatedCos, 2);

            // Создание суммы квадратов
            Function sumOfSquares = Functions.sum(sinSquared, cosSquared);

            System.out.printf("%-8s %-20s %-20s%n", "x", "sin²(x)+cos²(x)", "Ожидаемое (≈1.0)");
            System.out.println("------------------------------------------------------------");

            for (double x = 0; x <= PI + 0.05; x += 0.1) {
                double value = sumOfSquares.getFunctionValue(x);
                double expected = Math.sin(x) * Math.sin(x) + Math.cos(x) * Math.cos(x);
                System.out.printf("%-8.2f %-20.10f %-20.10f%n", x, value, expected);
            }

            // Вычисление максимального отклонения от 1.0
            double maxDeviation = 0;
            for (double x = 0; x <= PI + 0.05; x += 0.1) {
                double deviation = Math.abs(sumOfSquares.getFunctionValue(x) - 1.0);
                if (deviation > maxDeviation) maxDeviation = deviation;
            }
            System.out.printf("Максимальное отклонение от 1.0: %.10f%n%n", maxDeviation);
        }
    }

    /**
     * Тест 4: Экспонента с записью/чтением через символьный поток
     */
    private static void testExpWithTextFile() throws IOException {
        System.out.println("=== ТЕСТ 4: ЭКСПОНЕНТА С СИМВОЛЬНЫМ ПОТОКОМ ===\n");

        Function exp = new Exp();
        TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);

        // Запись в файл через символьный поток
        String textFileName = "exp_text.txt";
        try (FileWriter writer = new FileWriter(textFileName)) {
            TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
        }

        // Чтение из файла через символьный поток
        TabulatedFunction readExp;
        try (FileReader reader = new FileReader(textFileName)) {
            readExp = TabulatedFunctions.readTabulatedFunction(reader);
        }

        // Вывод исходного набора точек
        System.out.println("Исходный набор точек:");
        for (int i = 0; i < tabulatedExp.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", tabulatedExp.getPointX(i), tabulatedExp.getPointY(i));
        }

        // Вывод считанного набора точек
        System.out.println("\nСчитанный набор точек:");
        for (int i = 0; i < readExp.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", readExp.getPointX(i), readExp.getPointY(i));
        }

        System.out.println("\nСравнение исходной и считанной функции на отрезке [0, 10] с шагом 1:");
        System.out.printf("%-8s %-20s %-20s %-20s%n", "x", "Исходная exp(x)", "Считанная exp(x)", "Разница");
        System.out.println("--------------------------------------------------------------------------------");

        for (double x = 0; x <= 10; x += 1) {
            double originalValue = tabulatedExp.getFunctionValue(x);
            double readValue = readExp.getFunctionValue(x);
            double difference = Math.abs(originalValue - readValue);
            System.out.printf("%-8.1f %-20.10f %-20.10f %-20.10f%n", x, originalValue, readValue, difference);
        }
        System.out.println();
    }

    /**
     * Тест 5: Логарифм с записью/чтением через байтовый поток
     */
    private static void testLogWithBinaryFile() throws IOException {
        System.out.println("=== ТЕСТ 5: ЛОГАРИФМ С БАЙТОВЫМ ПОТОКОМ ===\n");

        // Создание логарифма по натуральному основанию (e)
        Function ln = new Log(Math.E);
        TabulatedFunction tabulatedLn = TabulatedFunctions.tabulate(ln, 0.1, 10, 11);

        // Запись в файл через байтовый поток
        String binaryFileName = "ln_binary.bin";
        try (FileOutputStream fos = new FileOutputStream(binaryFileName)) {
            TabulatedFunctions.outputTabulatedFunction(tabulatedLn, fos);
        }

        // Чтение из файла через байтовый поток
        TabulatedFunction readLn;
        try (FileInputStream fis = new FileInputStream(binaryFileName)) {
            readLn = TabulatedFunctions.inputTabulatedFunction(fis);
        }

        // Вывод исходного набора точек
        System.out.println("Исходный набор точек:");
        for (int i = 0; i < tabulatedLn.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", tabulatedLn.getPointX(i), tabulatedLn.getPointY(i));
        }

        // Вывод считанного набора точек
        System.out.println("\nСчитанный набор точек:");
        for (int i = 0; i < readLn.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", readLn.getPointX(i), readLn.getPointY(i));
        }

        System.out.println("\nСравнение исходной и считанной функции на отрезке [0.1, 10] с шагом 1:");
        System.out.printf("%-8s %-20s %-20s %-20s%n", "x", "Исходная ln(x)", "Считанная ln(x)", "Разница");
        System.out.println("--------------------------------------------------------------------------------");

        for (double x = 0.1; x <= 10; x += 1) {
            if (x < tabulatedLn.getLeftDomainBorder() || x > tabulatedLn.getRightDomainBorder()) {
                continue;
            }
            double originalValue = tabulatedLn.getFunctionValue(x);
            double readValue = readLn.getFunctionValue(x);
            double difference = Math.abs(originalValue - readValue);
            System.out.printf("%-8.1f %-20.10f %-20.10f %-20.10f%n", x, originalValue, readValue, difference);
        }
        System.out.println();
    }

    /**
     * Тест 6: Изучение содержимого файлов
     */
    private static void analyzeFiles() {
        System.out.println("=== ТЕСТ 6: АНАЛИЗ ФАЙЛОВ ===\n");

        String textFileName = "exp_text.txt";
        String binaryFileName = "ln_binary.bin";

        // Анализ текстового файла
        System.out.println("1. СОДЕРЖИМОЕ ТЕКСТОВОГО ФАЙЛА (exp_text.txt):");
        try (BufferedReader reader = new BufferedReader(new FileReader(textFileName))) {
            String line = reader.readLine();
            System.out.println("Содержимое: " + line);
            System.out.println("Размер файла: " + new File(textFileName).length() + " байт");
        } catch (IOException e) {
            System.err.println("Ошибка при чтении текстового файла: " + e.getMessage());
        }

        System.out.println("\n2. СОДЕРЖИМОЕ БАЙТОВОГО ФАЙЛА (ln_binary.bin):");
        try (FileInputStream fis = new FileInputStream(binaryFileName)) {
            System.out.println("Размер файла: " + new File(binaryFileName).length() + " байт");
            System.out.print("Первые 50 байт (в шестнадцатеричном виде): ");
            byte[] buffer = new byte[50];
            int bytesRead = fis.read(buffer);
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02X ", buffer[i]);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении байтового файла: " + e.getMessage());
        }

        System.out.println("\n3. ВЫВОДЫ О ПРЕИМУЩЕСТВАХ И НЕДОСТАТКАХ:");
        System.out.println("ТЕКСТОВЫЙ ФОРМАТ:");
        System.out.println("  Преимущества:");
        System.out.println("    - Человекочитаемый формат");
        System.out.println("    - Легко отлаживать и проверять");
        System.out.println("    - Можно редактировать вручную");
        System.out.println("    - Кроссплатформенный");
        System.out.println("  Недостатки:");
        System.out.println("    - Больший размер файла");
        System.out.println("    - Медленнее парсинг");
        System.out.println("    - Зависимость от локали (разделитель десятичных чисел)");
        System.out.println();
        System.out.println("БАЙТОВЫЙ ФОРМАТ:");
        System.out.println("  Преимущества:");
        System.out.println("    - Компактный размер файла");
        System.out.println("    - Быстрый ввод/вывод");
        System.out.println("    - Точное представление чисел (без потери точности при парсинге)");
        System.out.println("    - Независимость от локали");
        System.out.println("  Недостатки:");
        System.out.println("    - Нечитаемый формат");
        System.out.println("    - Сложнее отлаживать");
        System.out.println("    - Невозможно редактировать вручную");
        System.out.println("    - Зависимость от порядка байт (endianness)");
    }

    /**
     * Тест 7: Интегрирование экспоненты методом трапеций
     */
    private static void testExpIntegration() {
        System.out.println("=== ТЕСТ 7: ИНТЕГРИРОВАНИЕ ЭКСПОНЕНТЫ ===\n");

        Function exp = new Exp();
        double leftBound = 0.0;
        double rightBound = 1.0;
        
        // Теоретическое значение интеграла: ∫₀¹ e^x dx = e - 1
        double theoreticalValue = Math.E - 1.0;
        System.out.printf("Теоретическое значение интеграла: %.15f%n", theoreticalValue);
        System.out.println("(∫₀¹ e^x dx = e - 1)\n");

        // Тестирование с разными шагами
        double[] steps = {0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001};
        
        System.out.println("Результаты интегрирования с разными шагами:");
        System.out.printf("%-12s %-20s %-20s %-20s%n", "Шаг", "Вычисленное", "Теоретическое", "Погрешность");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (double step : steps) {
            try {
                double computedValue = Functions.integrate(exp, leftBound, rightBound, step);
                double error = Math.abs(computedValue - theoreticalValue);
                System.out.printf("%-12.6f %-20.15f %-20.15f %-20.15f%n", 
                    step, computedValue, theoreticalValue, error);
            } catch (Exception e) {
                System.err.println("Ошибка при шаге " + step + ": " + e.getMessage());
            }
        }

        // Поиск шага для точности в 7 знаке после запятой
        System.out.println("\nПоиск шага для точности в 7 знаке после запятой:");
        System.out.println("(Погрешность должна быть < 0.0000001 = 1e-7)\n");
        
        double targetError = 1e-7;
        
        // Проверка с различными шагами для определения минимального шага
        // Проверяем от большего к меньшему, чтобы найти наибольший шаг, который еще дает нужную точность
        System.out.println("Проверка различных шагов (от большего к меньшему):");
        double[] testSteps = {0.001, 0.0005, 0.0001, 0.00005, 0.00001, 0.000005, 0.000001};
        double foundStep = -1;
        
        for (double step : testSteps) {
            try {
                double computedValue = Functions.integrate(exp, leftBound, rightBound, step);
                double error = Math.abs(computedValue - theoreticalValue);
                boolean meetsTarget = error < targetError;
                String status = meetsTarget ? "✓ (достигнута)" : "✗";
                System.out.printf("Шаг: %.6f, Погрешность: %.15f (%s)%n", 
                    step, error, status);
                
                // Находим первый (наибольший) шаг, который дает нужную точность
                if (meetsTarget && foundStep < 0) {
                    foundStep = step;
                }
            } catch (Exception e) {
                System.err.println("Ошибка при шаге " + step + ": " + e.getMessage());
            }
        }
        
        // Вывод результата
        if (foundStep > 0) {
            System.out.printf("\n✓ Найден шаг для точности в 7 знаке: %.6f%n", foundStep);
            System.out.println("  (Это наибольший шаг, который обеспечивает требуемую точность)");
            try {
                double finalValue = Functions.integrate(exp, leftBound, rightBound, foundStep);
                double finalError = Math.abs(finalValue - theoreticalValue);
                System.out.printf("  Вычисленное значение: %.15f%n", finalValue);
                System.out.printf("  Теоретическое значение: %.15f%n", theoreticalValue);
                System.out.printf("  Погрешность: %.15f%n", finalError);
                System.out.printf("  Требуемая погрешность: %.15f%n", targetError);
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        } else {
            System.out.println("\nНе удалось найти шаг, обеспечивающий требуемую точность.");
            System.out.println("Попробуйте использовать меньший шаг.");
        }
        
        System.out.println();
    }

    /**
     * Последовательная (без применения потоков) версия программы.
     * Создает задания на интегрирование и выполняет их последовательно.
     */
    private static void nonThread() {
        System.out.println("=== ТЕСТ 8: ПОСЛЕДОВАТЕЛЬНОЕ ВЫПОЛНЕНИЕ ЗАДАНИЙ ===\n");

        Random random = new Random();
        Task task = new Task();
        
        // Устанавливаем количество заданий (минимум 100)
        int tasksCount = 100;
        task.setTasksCount(tasksCount);
        
        System.out.println("Количество заданий: " + tasksCount + "\n");
        
        for (int i = 0; i < tasksCount; i++) {
            try {
                // Создаем логарифмическую функцию со случайным основанием от 1 до 10
                // Основание должно быть > 1 и не равно 1, поэтому используем диапазон от 1+epsilon до 10
                double base = 1.0 + 1e-10 + random.nextDouble() * (10.0 - 1.0 - 1e-10); // от чуть больше 1 до 10
                Function logFunction = new Log(base);
                task.setFunction(logFunction);
                
                // Левая граница: случайно от 0 до 100
                double leftBound = random.nextDouble() * 100.0;
                
                // Правая граница: случайно от 100 до 200
                double rightBound = 100.0 + random.nextDouble() * 100.0;
                
                // Убеждаемся, что правая граница больше левой
                if (rightBound <= leftBound) {
                    rightBound = leftBound + 0.1;
                }
                
                task.setLeftBound(leftBound);
                task.setRightBound(rightBound);
                
                // Шаг дискретизации: случайно от 0 до 1
                // Шаг должен быть положительным, поэтому если получили 0, используем минимальное значение
                double step = random.nextDouble();
                if (step == 0.0 || step < 1e-10) {
                    step = 1e-10; // Минимальное положительное значение для избежания проблем
                }
                // Убеждаемся, что шаг не больше длины интервала
                double intervalLength = rightBound - leftBound;
                if (step > intervalLength) {
                    step = intervalLength / 2.0; // Используем половину длины интервала
                }
                task.setStep(step);
                
                // Выводим сообщение Source
                System.out.printf("Source %.6f %.6f %.6f%n", 
                    task.getLeftBound(), task.getRightBound(), task.getStep());
                
                // Вычисляем значение интеграла
                double result = Functions.integrate(
                    task.getFunction(), 
                    task.getLeftBound(), 
                    task.getRightBound(), 
                    task.getStep()
                );
                
                // Выводим сообщение Result
                System.out.printf("Result %.6f %.6f %.6f %.15f%n", 
                    task.getLeftBound(), task.getRightBound(), task.getStep(), result);
                
            } catch (Exception e) {
                System.err.printf("Ошибка при выполнении задания %d: %s%n", i + 1, e.getMessage());
            }
        }
        
        System.out.println("\nВсе задания выполнены.\n");
    }

    /**
     * Многопоточная версия программы с использованием SimpleGenerator и SimpleIntegrator.
     * Создает два потока: один генерирует задания, другой решает их.
     */
    private static void simpleThreads() {
        System.out.println("=== ТЕСТ 9: МНОГОПОТОЧНОЕ ВЫПОЛНЕНИЕ ЗАДАНИЙ ===\n");

        Task task = new Task();
        
        // Устанавливаем количество заданий (минимум 100)
        int tasksCount = 100;
        task.setTasksCount(tasksCount);
        
        System.out.println("Количество заданий: " + tasksCount + "\n");
        
        // Создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        
        // Устанавливаем приоритеты потоков (можно изменять для экспериментов)
        generatorThread.setPriority(Thread.NORM_PRIORITY);
        integratorThread.setPriority(Thread.NORM_PRIORITY);
        
        // Запускаем потоки
        generatorThread.start();
        integratorThread.start();
        
        // Ждем завершения обоих потоков
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потоков: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nВсе задания выполнены.\n");
    }

    /**
     * Многопоточная версия программы с использованием семафора.
     * Создает два потока: Generator и Integrator, которые используют семафор
     * для синхронизации доступа к заданию.
     */
    private static void complicatedThreads() {
        System.out.println("=== ТЕСТ 10: МНОГОПОТОЧНОЕ ВЫПОЛНЕНИЕ С СЕМАФОРОМ ===\n");

        Task task = new Task();
        ReadWriteSemaphore semaphore = new ReadWriteSemaphore();
        
        // Устанавливаем количество заданий (минимум 100)
        int tasksCount = 100;
        task.setTasksCount(tasksCount);
        
        System.out.println("Количество заданий: " + tasksCount + "\n");
        
        // Создаем потоки
        Generator generatorThread = new Generator(task, semaphore);
        Integrator integratorThread = new Integrator(task, semaphore);
        
        // Устанавливаем потоки как daemon, чтобы они автоматически завершались при завершении main
        generatorThread.setDaemon(true);
        integratorThread.setDaemon(true);
        
        // Устанавливаем приоритеты потоков (можно изменять для экспериментов)
        generatorThread.setPriority(Thread.NORM_PRIORITY);
        integratorThread.setPriority(Thread.NORM_PRIORITY);
        
        // Запускаем потоки
        generatorThread.start();
        integratorThread.start();

        // Ждем завершения потоков (интегратор должен обработать все задания)
        try {
            System.out.println("Ожидание завершения обработки всех заданий...");
            // Ждем завершения генератора
            generatorThread.join();
            System.out.println("[Main] Поток Generator завершен");
            // Ждем завершения интегратора (он должен обработать все задания)
            integratorThread.join();
            System.out.println("[Main] Поток Integrator завершен - все задания обработаны\n");
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании завершения потоков: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("\n[Main] Все задания выполнены.");
        System.out.println("Выполнение заданий завершено.\n");
        
        // Принудительно завершаем работу, если потоки все еще активны
        if (generatorThread.isAlive() || integratorThread.isAlive()) {
            System.out.println("[Main] Предупреждение: некоторые потоки все еще активны, но программа завершается.");
        }
    }
}
