import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.FunctionPoint;

public class MethodsTest {
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ МЕТОДОВ toString(), equals(), hashCode() И clone() ===\n");
        
        // Создание тестовых объектов
        FunctionPoint[] points1 = {
            new FunctionPoint(0.0, 1.2),
            new FunctionPoint(1.0, 3.8),
            new FunctionPoint(2.0, 15.2)
        };
        
        FunctionPoint[] points2 = {
            new FunctionPoint(0.0, 1.2),
            new FunctionPoint(1.0, 3.8),
            new FunctionPoint(2.0, 15.2)
        };
        
        FunctionPoint[] points3 = {
            new FunctionPoint(0.0, 1.2),
            new FunctionPoint(1.0, 4.0),  // Отличается от points1
            new FunctionPoint(2.0, 15.2)
        };
        
        ArrayTabulatedFunction arrayFunc1 = new ArrayTabulatedFunction(points1);
        ArrayTabulatedFunction arrayFunc2 = new ArrayTabulatedFunction(points2);
        ArrayTabulatedFunction arrayFunc3 = new ArrayTabulatedFunction(points3);
        
        LinkedListTabulatedFunction listFunc1 = new LinkedListTabulatedFunction(points1);
        LinkedListTabulatedFunction listFunc2 = new LinkedListTabulatedFunction(points2);
        LinkedListTabulatedFunction listFunc3 = new LinkedListTabulatedFunction(points3);
        
        // Тест 1: toString()
        testToString(arrayFunc1, listFunc1);
        
        // Тест 2: equals()
        testEquals(arrayFunc1, arrayFunc2, arrayFunc3, listFunc1, listFunc2, listFunc3);
        
        // Тест 3: hashCode()
        testHashCode(arrayFunc1, arrayFunc2, arrayFunc3, listFunc1, listFunc2, listFunc3);
        
        // Тест 4: clone()
        testClone(arrayFunc1, listFunc1);
    }
    
    /**
     * Тест метода toString()
     */
    private static void testToString(ArrayTabulatedFunction arrayFunc, LinkedListTabulatedFunction listFunc) {
        System.out.println("=== ТЕСТ 1: toString() ===\n");
        
        System.out.println("ArrayTabulatedFunction.toString():");
        System.out.println(arrayFunc.toString());
        System.out.println();
        
        System.out.println("LinkedListTabulatedFunction.toString():");
        System.out.println(listFunc.toString());
        System.out.println();
    }
    
    /**
     * Тест метода equals()
     */
    private static void testEquals(
            ArrayTabulatedFunction arrayFunc1, ArrayTabulatedFunction arrayFunc2, ArrayTabulatedFunction arrayFunc3,
            LinkedListTabulatedFunction listFunc1, LinkedListTabulatedFunction listFunc2, LinkedListTabulatedFunction listFunc3) {
        System.out.println("=== ТЕСТ 2: equals() ===\n");
        
        // Сравнение одинаковых объектов одного класса
        System.out.println("1. Сравнение одинаковых ArrayTabulatedFunction:");
        System.out.println("   arrayFunc1.equals(arrayFunc2): " + arrayFunc1.equals(arrayFunc2));
        System.out.println("   arrayFunc2.equals(arrayFunc1): " + arrayFunc2.equals(arrayFunc1));
        System.out.println();
        
        System.out.println("2. Сравнение одинаковых LinkedListTabulatedFunction:");
        System.out.println("   listFunc1.equals(listFunc2): " + listFunc1.equals(listFunc2));
        System.out.println("   listFunc2.equals(listFunc1): " + listFunc2.equals(listFunc1));
        System.out.println();
        
        // Сравнение разных объектов одного класса
        System.out.println("3. Сравнение разных ArrayTabulatedFunction:");
        System.out.println("   arrayFunc1.equals(arrayFunc3): " + arrayFunc1.equals(arrayFunc3));
        System.out.println("   arrayFunc3.equals(arrayFunc1): " + arrayFunc3.equals(arrayFunc1));
        System.out.println();
        
        System.out.println("4. Сравнение разных LinkedListTabulatedFunction:");
        System.out.println("   listFunc1.equals(listFunc3): " + listFunc1.equals(listFunc3));
        System.out.println("   listFunc3.equals(listFunc1): " + listFunc3.equals(listFunc1));
        System.out.println();
        
        // Сравнение объектов разных классов с одинаковыми данными
        System.out.println("5. Сравнение ArrayTabulatedFunction и LinkedListTabulatedFunction (одинаковые данные):");
        System.out.println("   arrayFunc1.equals(listFunc1): " + arrayFunc1.equals(listFunc1));
        System.out.println("   listFunc1.equals(arrayFunc1): " + listFunc1.equals(arrayFunc1));
        System.out.println();
        
        // Сравнение с null
        System.out.println("6. Сравнение с null:");
        System.out.println("   arrayFunc1.equals(null): " + arrayFunc1.equals(null));
        System.out.println("   listFunc1.equals(null): " + listFunc1.equals(null));
        System.out.println();
        
        // Сравнение с объектом другого типа
        System.out.println("7. Сравнение с объектом другого типа:");
        System.out.println("   arrayFunc1.equals(\"строка\"): " + arrayFunc1.equals("строка"));
        System.out.println("   listFunc1.equals(new Object()): " + listFunc1.equals(new Object()));
        System.out.println();
    }
    
    /**
     * Тест метода hashCode()
     */
    private static void testHashCode(
            ArrayTabulatedFunction arrayFunc1, ArrayTabulatedFunction arrayFunc2, ArrayTabulatedFunction arrayFunc3,
            LinkedListTabulatedFunction listFunc1, LinkedListTabulatedFunction listFunc2, LinkedListTabulatedFunction listFunc3) {
        System.out.println("=== ТЕСТ 3: hashCode() ===\n");
        
        System.out.println("1. Хэш-коды одинаковых ArrayTabulatedFunction:");
        int hash1 = arrayFunc1.hashCode();
        int hash2 = arrayFunc2.hashCode();
        System.out.println("   arrayFunc1.hashCode(): " + hash1);
        System.out.println("   arrayFunc2.hashCode(): " + hash2);
        System.out.println("   Совпадают: " + (hash1 == hash2));
        System.out.println();
        
        System.out.println("2. Хэш-коды одинаковых LinkedListTabulatedFunction:");
        int hash3 = listFunc1.hashCode();
        int hash4 = listFunc2.hashCode();
        System.out.println("   listFunc1.hashCode(): " + hash3);
        System.out.println("   listFunc2.hashCode(): " + hash4);
        System.out.println("   Совпадают: " + (hash3 == hash4));
        System.out.println();
        
        System.out.println("3. Хэш-коды разных ArrayTabulatedFunction:");
        int hash5 = arrayFunc3.hashCode();
        System.out.println("   arrayFunc1.hashCode(): " + hash1);
        System.out.println("   arrayFunc3.hashCode(): " + hash5);
        System.out.println("   Совпадают: " + (hash1 == hash5));
        System.out.println();
        
        System.out.println("4. Хэш-коды разных LinkedListTabulatedFunction:");
        int hash6 = listFunc3.hashCode();
        System.out.println("   listFunc1.hashCode(): " + hash3);
        System.out.println("   listFunc3.hashCode(): " + hash6);
        System.out.println("   Совпадают: " + (hash3 == hash6));
        System.out.println();
        
        System.out.println("5. Хэш-коды ArrayTabulatedFunction и LinkedListTabulatedFunction (одинаковые данные):");
        System.out.println("   arrayFunc1.hashCode(): " + hash1);
        System.out.println("   listFunc1.hashCode(): " + hash3);
        System.out.println("   Совпадают: " + (hash1 == hash3));
        System.out.println();
        
        // Проверка согласованности equals() и hashCode()
        System.out.println("6. Проверка согласованности equals() и hashCode():");
        System.out.println("   Если arrayFunc1.equals(arrayFunc2), то hash1 == hash2: " + 
            (arrayFunc1.equals(arrayFunc2) == (hash1 == hash2)));
        System.out.println("   Если arrayFunc1.equals(arrayFunc3), то hash1 == hash5: " + 
            (arrayFunc1.equals(arrayFunc3) == (hash1 == hash5)));
        System.out.println();
        
        // Тест изменения объекта
        System.out.println("7. Изменение объекта и проверка хэш-кода:");
        System.out.println("   Исходный хэш-код arrayFunc1: " + hash1);
        try {
            // Изменяем координату Y первой точки на несколько тысячных
            double originalY = arrayFunc1.getPointY(1);
            arrayFunc1.setPointY(1, originalY + 0.001);
            int newHash = arrayFunc1.hashCode();
            System.out.println("   Новый хэш-код после изменения Y на 0.001: " + newHash);
            System.out.println("   Хэш-код изменился: " + (hash1 != newHash));
            // Возвращаем обратно
            arrayFunc1.setPointY(1, originalY);
            int restoredHash = arrayFunc1.hashCode();
            System.out.println("   Хэш-код после восстановления: " + restoredHash);
            System.out.println("   Хэш-код восстановлен: " + (hash1 == restoredHash));
        } catch (Exception e) {
            System.err.println("   Ошибка при изменении: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Тест метода clone()
     */
    private static void testClone(ArrayTabulatedFunction arrayFunc, LinkedListTabulatedFunction listFunc) {
        System.out.println("=== ТЕСТ 4: clone() ===\n");
        
        // Тест для ArrayTabulatedFunction
        System.out.println("1. Тест clone() для ArrayTabulatedFunction:");
        try {
            ArrayTabulatedFunction clonedArray = (ArrayTabulatedFunction) arrayFunc.clone();
            System.out.println("   Исходный объект: " + arrayFunc.toString());
            System.out.println("   Клонированный объект: " + clonedArray.toString());
            System.out.println("   Объекты равны: " + arrayFunc.equals(clonedArray));
            System.out.println("   Это разные объекты (по ссылке): " + (arrayFunc != clonedArray));
            
            // Изменяем исходный объект
            double originalY = arrayFunc.getPointY(1);
            arrayFunc.setPointY(1, originalY + 10.0);
            System.out.println("\n   После изменения исходного объекта:");
            System.out.println("   Исходный объект: " + arrayFunc.toString());
            System.out.println("   Клонированный объект: " + clonedArray.toString());
            System.out.println("   Клон не изменился: " + 
                (Math.abs(clonedArray.getPointY(1) - originalY) < 0.0001));
            
            // Возвращаем обратно
            arrayFunc.setPointY(1, originalY);
        } catch (Exception e) {
            System.err.println("   Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
        
        // Тест для LinkedListTabulatedFunction
        System.out.println("2. Тест clone() для LinkedListTabulatedFunction:");
        try {
            LinkedListTabulatedFunction clonedList = (LinkedListTabulatedFunction) listFunc.clone();
            System.out.println("   Исходный объект: " + listFunc.toString());
            System.out.println("   Клонированный объект: " + clonedList.toString());
            System.out.println("   Объекты равны: " + listFunc.equals(clonedList));
            System.out.println("   Это разные объекты (по ссылке): " + (listFunc != clonedList));
            
            // Изменяем исходный объект
            double originalY = listFunc.getPointY(1);
            listFunc.setPointY(1, originalY + 10.0);
            System.out.println("\n   После изменения исходного объекта:");
            System.out.println("   Исходный объект: " + listFunc.toString());
            System.out.println("   Клонированный объект: " + clonedList.toString());
            System.out.println("   Клон не изменился: " + 
                (Math.abs(clonedList.getPointY(1) - originalY) < 0.0001));
            
            // Возвращаем обратно
            listFunc.setPointY(1, originalY);
        } catch (Exception e) {
            System.err.println("   Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
        
        // Тест глубокого клонирования точек
        System.out.println("3. Проверка глубокого клонирования точек:");
        try {
            ArrayTabulatedFunction clonedArray = (ArrayTabulatedFunction) arrayFunc.clone();
            LinkedListTabulatedFunction clonedList = (LinkedListTabulatedFunction) listFunc.clone();
            
            // Получаем точки из исходных и клонированных объектов
            FunctionPoint originalPoint1 = arrayFunc.getPoint(0);
            FunctionPoint clonedPoint1 = clonedArray.getPoint(0);
            FunctionPoint originalPoint2 = listFunc.getPoint(0);
            FunctionPoint clonedPoint2 = clonedList.getPoint(0);
            
            System.out.println("   ArrayTabulatedFunction:");
            System.out.println("   Сравниваем точку с индексом 0:");
            System.out.println("   Исходная точка из arrayFunc: " + originalPoint1.toString() + 
                " (x=" + originalPoint1.getX() + ", y=" + originalPoint1.getY() + ")");
            System.out.println("   Клонированная точка из clonedArray: " + clonedPoint1.toString() + 
                " (x=" + clonedPoint1.getX() + ", y=" + clonedPoint1.getY() + ")");
            System.out.println("   Исходная точка (по ссылке): " + System.identityHashCode(originalPoint1));
            System.out.println("   Клонированная точка (по ссылке): " + System.identityHashCode(clonedPoint1));
            System.out.println("   Это разные объекты: " + (originalPoint1 != clonedPoint1));
            System.out.println("   Но они равны по содержимому (координаты одинаковые): " + originalPoint1.equals(clonedPoint1));
            System.out.println("   Координаты совпадают: x одинаковы? " + 
                (Double.compare(originalPoint1.getX(), clonedPoint1.getX()) == 0) + 
                ", y одинаковы? " + (Double.compare(originalPoint1.getY(), clonedPoint1.getY()) == 0));
            
            System.out.println("\n   LinkedListTabulatedFunction:");
            System.out.println("   Сравниваем точку с индексом 0:");
            System.out.println("   Исходная точка из listFunc: " + originalPoint2.toString() + 
                " (x=" + originalPoint2.getX() + ", y=" + originalPoint2.getY() + ")");
            System.out.println("   Клонированная точка из clonedList: " + clonedPoint2.toString() + 
                " (x=" + clonedPoint2.getX() + ", y=" + clonedPoint2.getY() + ")");
            System.out.println("   Исходная точка (по ссылке): " + System.identityHashCode(originalPoint2));
            System.out.println("   Клонированная точка (по ссылке): " + System.identityHashCode(clonedPoint2));
            System.out.println("   Это разные объекты: " + (originalPoint2 != clonedPoint2));
            System.out.println("   Но они равны по содержимому (координаты одинаковые): " + originalPoint2.equals(clonedPoint2));
            System.out.println("   Координаты совпадают: x одинаковы? " + 
                (Double.compare(originalPoint2.getX(), clonedPoint2.getX()) == 0) + 
                ", y одинаковы? " + (Double.compare(originalPoint2.getY(), clonedPoint2.getY()) == 0));
        } catch (Exception e) {
            System.err.println("   Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
}

