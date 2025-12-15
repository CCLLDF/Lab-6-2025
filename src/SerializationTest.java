import functions.Function;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.Functions;
import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.basic.Exp;
import functions.basic.Log;

import java.io.*;

public class SerializationTest {
    public static void main(String[] args) {
        try {
            // Создание функции ln(exp(x)) = x
            Function exp = new Exp();
            Function ln = new Log(Math.E);
            Function composition = Functions.composition(exp, ln);
            
            System.out.println("=== ТЕСТ СЕРИАЛИЗАЦИИ ===\n");
            
            // Создание табулированного аналога на отрезке [0, 10] с 11 точками для Array (Serializable)
            ArrayTabulatedFunction arrayTabulated = new ArrayTabulatedFunction(0, 10, 11);
            double step = 10.0 / 10;
            for (int i = 0; i < 11; i++) {
                double x = i * step;
                double y = composition.getFunctionValue(x);
                arrayTabulated.setPointY(i, y);
            }
            
            // Создание табулированного аналога на отрезке [0, 10] с 11 точками для LinkedList (Externalizable)
            LinkedListTabulatedFunction linkedListTabulated = new LinkedListTabulatedFunction(0, 10, 11);
            for (int i = 0; i < 11; i++) {
                double x = i * step;
                double y = composition.getFunctionValue(x);
                linkedListTabulated.setPointY(i, y);
            }
            
            System.out.println("Создана табулированная функция ln(exp(x)) на отрезке [0, 10] с 11 точками\n");
            
            // Тест 1: Сериализация через Serializable (Array)
            testSerializable(arrayTabulated);
            
            // Тест 2: Сериализация через Externalizable (LinkedList)
            testExternalizable(linkedListTabulated);
            
            // Тест 3: Анализ файлов
            analyzeSerializationFiles();
            
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Тест сериализации через Serializable
     */
    private static void testSerializable(TabulatedFunction original) throws IOException, ClassNotFoundException {
        System.out.println("=== ТЕСТ 1: SERIALIZABLE ===\n");
        
        String fileName = "function_serializable.ser";
        
        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(original);
        }
        
        // Десериализация
        TabulatedFunction deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            deserialized = (TabulatedFunction) ois.readObject();
        }
        
        // Сравнение значений
        System.out.println("Сравнение исходной и десериализованной функции на отрезке [0, 10] с шагом 1:");
        System.out.printf("%-8s %-20s %-20s %-20s%n", "x", "Исходная", "Десериализованная", "Разница");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (double x = 0; x <= 10; x += 1) {
            double originalValue = original.getFunctionValue(x);
            double deserializedValue = deserialized.getFunctionValue(x);
            double difference = Math.abs(originalValue - deserializedValue);
            System.out.printf("%-8.1f %-20.10f %-20.10f %-20.10f%n", x, originalValue, deserializedValue, difference);
        }
        
        System.out.println("\nРазмер файла: " + new File(fileName).length() + " байт\n");
    }
    
    /**
     * Тест сериализации через Externalizable
     */
    private static void testExternalizable(TabulatedFunction original) throws IOException, ClassNotFoundException {
        System.out.println("=== ТЕСТ 2: EXTERNALIZABLE ===\n");
        
        String fileName = "function_externalizable.ser";
        
        // Сериализация
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(original);
        }
        
        // Десериализация
        TabulatedFunction deserialized;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            deserialized = (TabulatedFunction) ois.readObject();
        }
        
        // Сравнение значений
        System.out.println("Сравнение исходной и десериализованной функции на отрезке [0, 10] с шагом 1:");
        System.out.printf("%-8s %-20s %-20s %-20s%n", "x", "Исходная", "Десериализованная", "Разница");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (double x = 0; x <= 10; x += 1) {
            double originalValue = original.getFunctionValue(x);
            double deserializedValue = deserialized.getFunctionValue(x);
            double difference = Math.abs(originalValue - deserializedValue);
            System.out.printf("%-8.1f %-20.10f %-20.10f %-20.10f%n", x, originalValue, deserializedValue, difference);
        }
        
        System.out.println("\nРазмер файла: " + new File(fileName).length() + " байт\n");
    }
    
    /**
     * Анализ файлов сериализации
     */
    private static void analyzeSerializationFiles() {
        System.out.println("=== ТЕСТ 3: АНАЛИЗ ФАЙЛОВ ===\n");
        
        String serializableFile = "function_serializable.ser";
        String externalizableFile = "function_externalizable.ser";
        
        File serFile = new File(serializableFile);
        File extFile = new File(externalizableFile);
        
        System.out.println("1. РАЗМЕРЫ ФАЙЛОВ:");
        System.out.println("   Serializable: " + serFile.length() + " байт");
        System.out.println("   Externalizable: " + extFile.length() + " байт");
        System.out.println("   Разница: " + Math.abs(serFile.length() - extFile.length()) + " байт\n");
        
        System.out.println("2. СОДЕРЖИМОЕ ФАЙЛОВ (первые 100 байт в hex):");
        
        System.out.println("\n   Serializable:");
        printFileHex(serFile, 100);
        
        System.out.println("\n   Externalizable:");
        printFileHex(extFile, 100);
        
        System.out.println("\n3. ВЫВОДЫ О ПРЕИМУЩЕСТВАХ И НЕДОСТАТКАХ:\n");
        
        System.out.println("SERIALIZABLE:");
        System.out.println("  Преимущества:");
        System.out.println("    - Простая реализация (не требует дополнительных методов)");
        System.out.println("    - Автоматическая сериализация всех полей");
        System.out.println("    - Поддержка версионирования через serialVersionUID");
        System.out.println("    - Обратная совместимость при добавлении полей");
        System.out.println("  Недостатки:");
        System.out.println("    - Больший размер файла (метаданные, ссылки)");
        System.out.println("    - Медленнее (рефлексия, обработка графов объектов)");
        System.out.println("    - Сериализует все поля, даже временные/кэшированные");
        System.out.println("    - Может сериализовать лишние данные (например, кэш)");
        
        System.out.println("\nEXTERNALIZABLE:");
        System.out.println("  Преимущества:");
        System.out.println("    - Полный контроль над процессом сериализации");
        System.out.println("    - Меньший размер файла (только необходимые данные)");
        System.out.println("    - Быстрее (нет рефлексии, прямой ввод/вывод)");
        System.out.println("    - Можно исключить временные поля (кэш, служебные данные)");
        System.out.println("    - Оптимизация формата хранения");
        System.out.println("  Недостатки:");
        System.out.println("    - Требует ручной реализации методов readExternal/writeExternal");
        System.out.println("    - Больше кода для поддержки");
        System.out.println("    - Нужно обновлять методы при изменении структуры класса");
        System.out.println("    - Нет автоматической поддержки версионирования");
        System.out.println("    - Легче допустить ошибку при реализации");
    }
    
    /**
     * Выводит первые N байт файла в шестнадцатеричном формате
     */
    private static void printFileHex(File file, int bytesToRead) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[bytesToRead];
            int bytesRead = fis.read(buffer);
            System.out.print("   ");
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02X ", buffer[i]);
                if ((i + 1) % 16 == 0) {
                    System.out.println();
                    System.out.print("   ");
                }
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}

