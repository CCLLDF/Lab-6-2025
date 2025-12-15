package functions;

import java.io.*;

/**
 * Табулированная функция на основе двусвязного циклического списка
 * с выделенной головой (sentinel).
 */
public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable, Cloneable {

    private static final double EPSILON = 1e-10;

    private static boolean isEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Узел списка. Инкапсулирован как private static class:
     * внешний код не может получить ссылки на узлы, взаимодействие
     * идет через методы внешнего класса.
     */
    private static class FunctionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        private FunctionPoint value;
        private FunctionNode next;
        private FunctionNode prev;

        FunctionNode(FunctionPoint value) {
            this.value = value == null ? null : new FunctionPoint(value);
        }
    }

    // Голова списка (не хранит данных, всегда присутствует)
    private FunctionNode head;
    // Текущее количество значащих элементов (без головы)
    private int size;

    // Кэш последнего обращения для оптимизации getNodeByIndex
    private FunctionNode cachedNode;
    private int cachedIndex;

    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;
    }

    /* ==========================
     * Конструкторы табулированной функции
     * ========================== */

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        // Инициализация полей (то, что делает дефолтный конструктор)
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;

        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            addNodeToTailWithValue(new FunctionPoint(leftX + i * step, 0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        // Инициализация полей (то, что делает дефолтный конструктор)
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;

        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; ++i) {
            addNodeToTailWithValue(new FunctionPoint(leftX + i * step, values[i]));
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        // Инициализация полей (то, что делает дефолтный конструктор)
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;

        if (points == null) {
            throw new IllegalArgumentException("Points array must not be null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("The number of points must be at least 2");
        }

        // Проверка упорядоченности по абсциссе
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Points array must not contain null elements");
            }
            if (points[i].getX() >= points[i + 1].getX() - EPSILON) {
                throw new IllegalArgumentException("Points must be ordered by x-coordinate in ascending order");
            }
        }
        if (points[points.length - 1] == null) {
            throw new IllegalArgumentException("Points array must not contain null elements");
        }

        // Добавление точек с созданием копий для обеспечения инкапсуляции
        for (int i = 0; i < points.length; i++) {
            addNodeToTailWithValue(points[i]);
        }
    }

    /* ==========================
     * Методы работы с табулированной функцией
     * ========================== */

    public double getLeftDomainBorder() {
        if (size == 0) {
            throw new IllegalStateException("Function has no points");
        }
        return head.next.value.getX();
    }

    public double getRightDomainBorder() {
        if (size == 0) {
            throw new IllegalStateException("Function has no points");
        }
        return head.prev.value.getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        FunctionNode current = head.next;
        while (current != head && current.value.getX() < x - EPSILON) {
            current = current.next;
        }

        if (current != head && isEqual(current.value.getX(), x)) {
            return current.value.getY();
        } else {
            FunctionNode prevNode = current.prev;
            double x1 = prevNode.value.getX();
            double y1 = prevNode.value.getY();
            double x2 = current.value.getX();
            double y2 = current.value.getY();
            return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
        }
    }

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.value);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null");
        }
        FunctionNode node = getNodeByIndex(index);

        // Проверка порядка
        if (index > 0) {
            FunctionNode prev = node.prev;
            if (prev.value.getX() >= point.getX() - EPSILON) {
                throw new InappropriateFunctionPointException("The point disrupts the order");
            }
        }
        if (index < size - 1) {
            FunctionNode next = node.next;
            if (next.value.getX() <= point.getX() + EPSILON) {
                throw new InappropriateFunctionPointException("The point disrupts the order");
            }
        }
        node.value = new FunctionPoint(point);
    }

    public double getPointX(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.value.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if (index > 0 && node.prev.value.getX() >= x - EPSILON) {
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        if (index < size - 1 && node.next.value.getX() <= x + EPSILON) {
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }

        node.value = new FunctionPoint(x, node.value.getY());
    }

    public double getPointY(int index) {
        FunctionNode node = getNodeByIndex(index);
        return node.value.getY();
    }

    public void setPointY(int index, double y) {
        FunctionNode node = getNodeByIndex(index);
        node.value = new FunctionPoint(node.value.getX(), y);
    }

    public void deletePoint(int index) {
        if (size < 3) {
            throw new IllegalStateException("Incorrect number of points");
        }
        FunctionNode node = getNodeByIndex(index);
        deleteNodeAndUpdateCache(node, index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (point == null) {
            throw new IllegalArgumentException("Point must not be null");
        }

        FunctionNode current = head.next;
        int index = 0;
        while (current != head && current.value.getX() < point.getX() - EPSILON) {
            current = current.next;
            index++;
        }

        if (current != head && isEqual(current.value.getX(), point.getX())) {
            throw new InappropriateFunctionPointException("Point with this X already exists");
        }

        FunctionNode node = new FunctionNode(point);
        insertBefore(current, node);

        if (cachedIndex >= 0 && index <= cachedIndex) {
            cachedIndex++;
        }
    }

    /**
     * Возвращает узел по индексу (0..size-1) с оптимизацией за счет кэша.
     */
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }

        // Выбор стартовой точки обхода: голова слева, голова справа или кэш
        FunctionNode startNode;
        int startIndex;

        boolean cacheValid = cachedNode != null && cachedIndex >= 0 && cachedIndex < size;
        int fromCache = cacheValid ? Math.abs(index - cachedIndex) : Integer.MAX_VALUE;
        int fromHead = index;               // расстояние от head.next
        int fromTail = size - 1 - index;    // расстояние от head.prev

        if (fromCache <= fromHead && fromCache <= fromTail) {
            startNode = cachedNode;
            startIndex = cachedIndex;
        } else if (fromHead <= fromTail) {
            startNode = head.next;
            startIndex = 0;
        } else {
            startNode = head.prev;
            startIndex = size - 1;
        }

        FunctionNode current = startNode;
        if (startIndex < index) {
            for (int i = startIndex; i < index; i++) {
                current = current.next;
            }
        } else if (startIndex > index) {
            for (int i = startIndex; i > index; i--) {
                current = current.prev;
            }
        }

        cachedNode = current;
        cachedIndex = index;
        return current;
    }

    /**
     * Добавляет узел в конец (перед головой) и возвращает его.
     */
    private FunctionNode addNodeToTail() {
        FunctionNode node = new FunctionNode(null);
        insertBefore(head, node);
        return node;
    }

    private void deleteNodeAndUpdateCache(FunctionNode node, int index) {
        unlink(node);
        if (cachedNode == node) {
            cachedNode = null;
            cachedIndex = -1;
        } else if (cachedIndex > index) {
            cachedIndex--;
        }
    }

    private FunctionNode addNodeToTailWithValue(FunctionPoint point) {
        FunctionNode node = new FunctionNode(point);
        insertBefore(head, node);
        return node;
    }

    /**
     * Добавляет узел по индексу (0..size) и возвращает его.
     * Если index == size — вставка в хвост.
     */
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (index == size) {
            return addNodeToTail();
        }
        FunctionNode target = getNodeByIndex(index);
        FunctionNode node = new FunctionNode(null);
        insertBefore(target, node);
        // Сдвигаем кэш, если вставили раньше него
        if (cachedIndex >= index) {
            cachedIndex++;
        }
        return node;
    }

    /**
     * Удаляет узел по индексу и возвращает его (узел отсоединён).
     */
    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode node = getNodeByIndex(index);
        unlink(node);
        // Инвалидация/коррекция кэша
        if (cachedNode == node) {
            cachedNode = null;
            cachedIndex = -1;
        } else if (cachedIndex > index) {
            cachedIndex--;
        }
        return node;
    }

    private void insertBefore(FunctionNode anchor, FunctionNode node) {
        node.prev = anchor.prev;
        node.next = anchor;
        anchor.prev.next = node;
        anchor.prev = node;
        size++;
    }

    private void unlink(FunctionNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        node.next = null;
        node.prev = null;
    }

    // Реализация Externalizable для более эффективной сериализации
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        FunctionNode current = head.next;
        while (current != head) {
            out.writeDouble(current.value.getX());
            out.writeDouble(current.value.getY());
            current = current.next;
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // Инициализация структуры списка
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        cachedNode = null;
        cachedIndex = -1;

        // Чтение точек и восстановление списка
        int pointsCount = in.readInt();
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTailWithValue(new FunctionPoint(x, y));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        FunctionNode current = head.next;
        boolean first = true;
        while (current != head) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(current.value.toString());
            current = current.next;
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction that = (TabulatedFunction) o;
        
        if (size != that.getPointsCount()) return false;
        
        // Оптимизация для LinkedListTabulatedFunction
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction listThat = (LinkedListTabulatedFunction) o;
            FunctionNode currentThis = head.next;
            FunctionNode currentThat = listThat.head.next;
            
            while (currentThis != head && currentThat != listThat.head) {
                if (!currentThis.value.equals(currentThat.value)) {
                    return false;
                }
                currentThis = currentThis.next;
                currentThat = currentThat.next;
            }
        } else {
            // Общий случай для других реализаций TabulatedFunction
            FunctionNode current = head.next;
            int index = 0;
            while (current != head) {
                FunctionPoint thatPoint = that.getPoint(index);
                if (!current.value.equals(thatPoint)) {
                    return false;
                }
                current = current.next;
                index++;
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = size;
        FunctionNode current = head.next;
        while (current != head) {
            result ^= current.value.hashCode();
            current = current.next;
        }
        return result;
    }

    @Override
    public Object clone() {
        LinkedListTabulatedFunction cloned = new LinkedListTabulatedFunction();
        
        // Пересборка списка без использования методов добавления
        FunctionNode current = head.next;
        FunctionNode prevNode = cloned.head;
        
        while (current != head) {
            FunctionNode newNode = new FunctionNode((FunctionPoint) current.value.clone());
            newNode.prev = prevNode;
            newNode.next = cloned.head;
            prevNode.next = newNode;
            cloned.head.prev = newNode;
            prevNode = newNode;
            cloned.size++;
            current = current.next;
        }
        
        return cloned;
    }
}
