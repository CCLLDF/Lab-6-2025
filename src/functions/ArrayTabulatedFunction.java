package functions;

import java.io.*;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private static final double EPSILON = 1e-10;
    private FunctionPoint[] funct;
    private int len;

    private static boolean isEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount)
    {
        if (leftX >= rightX - EPSILON){
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (pointsCount<2){
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        this.funct = new FunctionPoint[pointsCount];
        this.len = pointsCount;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; ++i) {
            funct[i] = new FunctionPoint((leftX + i * step), 0);
        }
}

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values)
    {
        if (leftX >= rightX - EPSILON){
            throw new IllegalArgumentException("The left boundary of the domain must be less than the right one");
        }
        if (values.length<2){
            throw new IllegalArgumentException("The number of points must be more than 2");
        }

        this.funct = new FunctionPoint[values.length];
        this.len=values.length;
        double step = (rightX - leftX) / (len - 1);
        for (int i = 0; i < len; ++i) {
            funct[i] = new FunctionPoint((leftX + i * step), values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points)
    {
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

        // Создание копий для обеспечения инкапсуляции
        this.funct = new FunctionPoint[points.length];
        this.len = points.length;
        for (int i = 0; i < len; i++) {
            funct[i] = new FunctionPoint(points[i]);
        }
    }

    public double getLeftDomainBorder()
    {
        return funct[0].getX();
    }

    public double getRightDomainBorder()
    {
        return funct[len-1].getX();
    }

    public double getFunctionValue(double x)
    {
        if (x<getLeftDomainBorder() || x>getRightDomainBorder()) {
            return Double.NaN;
        }
        else {
            int i = 0;
            while(i < len && funct[i].getX() < x - EPSILON) {
                i++;
            }
            if (isEqual(funct[i].getX(), x)) {
                return funct[i].getY();
            }
        else {
            double x1=funct[i-1].getX();
            double y1=funct[i-1].getY();
            double x2=funct[i].getX();
            double y2=funct[i].getY();
            double y = (y1 + (x - x1) * (y2 - y1) / (x2 - x1));
            return y;
        }

        }
    }
    public int getPointsCount()
    {
        return len;
    }
    public FunctionPoint getPoint(int index)
    {
        if(index > len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        return new FunctionPoint(funct[index]);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < len - 1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        if(index < len - 1 && funct[index+1].getX() <= point.getX() + EPSILON){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        if(index > 0 && funct[index-1].getX() >= point.getX() - EPSILON){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        funct[index]=new FunctionPoint(point);
    }
    public double getPointX(int index)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        return funct[index].getX();
    }
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < len - 1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        if(index < len - 1 && funct[index+1].getX() <= x + EPSILON){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        if(index > 0 && funct[index-1].getX() >= x - EPSILON){
            throw new InappropriateFunctionPointException("The point disrupts the order");
        }
        funct[index] = new FunctionPoint (x, funct[index].getY());
    }
    public double getPointY(int index)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        return funct[index].getY();
    }
    public void setPointY(int index, double y)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("Going beyond the set of points");
        }
        funct[index] = new FunctionPoint (funct[index].getX(),y);
    }
    public void deletePoint(int index)
    {
        if(index>len-1){
            throw new FunctionPointIndexOutOfBoundsException("This point is out of bounds");
        }
        if(len<3){
            throw new IllegalStateException("Incorrect number of points");
        }
        if (index<len){
            System.arraycopy(funct,index+1, funct,index,len-index-1);
            funct[len-1]=null;
            len--;
        }
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        for(int i =0;i<len;++i) {
            if (isEqual(funct[i].getX(), point.getX())) {
                throw new InappropriateFunctionPointException("This Point already exists");
            }
        }
        int i=0;
        while((i<len && funct[i].getX() < point.getX() - EPSILON)){
            ++i;
        }
        if ((i<len && !isEqual(funct[i].getX(), point.getX())) || (i==len && !isEqual(funct[i-1].getX(), point.getX()))){
            FunctionPoint[] array = new FunctionPoint[len+1];
            System.arraycopy(funct, 0, array, 0, i);
            array[i] = new FunctionPoint(point);
            System.arraycopy(funct, i, array, i + 1, len - i);
            len++;
            funct = array;
        }
        else {
            System.arraycopy(funct, i, funct, i + 1, len - i);
            funct[i] = new FunctionPoint(point);
            len++;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(funct[i].toString());
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TabulatedFunction)) return false;
        
        TabulatedFunction that = (TabulatedFunction) o;
        
        if (len != that.getPointsCount()) return false;
        
        // Оптимизация для ArrayTabulatedFunction
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction arrThat = (ArrayTabulatedFunction) o;
            for (int i = 0; i < len; i++) {
                if (!funct[i].equals(arrThat.funct[i])) {
                    return false;
                }
            }
        } else {
            // Общий случай для других реализаций TabulatedFunction
            for (int i = 0; i < len; i++) {
                FunctionPoint thatPoint = that.getPoint(i);
                if (!funct[i].equals(thatPoint)) {
                    return false;
                }
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = len;
        for (int i = 0; i < len; i++) {
            result ^= funct[i].hashCode();
        }
        return result;
    }

    @Override
    public Object clone() {
        FunctionPoint[] clonedPoints = new FunctionPoint[len];
        for (int i = 0; i < len; i++) {
            clonedPoints[i] = (FunctionPoint) funct[i].clone();
        }
        return new ArrayTabulatedFunction(clonedPoints);
    }

}

