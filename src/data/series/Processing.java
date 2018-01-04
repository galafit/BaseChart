package data.series;

import base.Range;

/**
 * Created by galafit on 24/9/17.
 */
public class Processing {

    /******************************************************************
     *                         MIN-MAX
     ******************************************************************/
    public static Range minMaxRange(IntSeries data, int from, int length) {
        if(data.size() == 0){
            return null;
        }
        int min = data.get(from);
        int max = data.get(from);
        for (int i = from + 1; i < from + length ; i++) {
            min = Math.min(min, data.get(i));
            max = Math.max(max, data.get(i));
        }
        return new Range(min, max);
    }

    public static Range minMaxRange(FloatSeries data, int from, int length) {
        if(data.size() == 0){
            return null;
        }
        float min = data.get(from);
        float max = data.get(from);
        for (int i = from + 1; i < from + length ; i++) {
            min = Math.min(min, data.get(i));
            max = Math.max(max, data.get(i));
        }
        return new Range(min, max);
    }

    /******************************************************************
     *                         BINARY SEARCH
     ******************************************************************/

    /**
     * Lower bound search algorithm.<br>
     * Lower bound is kind of binary search algorithm but:<br>
     * -If searched element doesn't exist function returns index of first element which is less than searched value.<br>
     * -If there are many values equals searched value function returns FIRST occurrence.<br>
     * Behaviour for unsorted arrays is unspecified.
     * <p>
     * Complexity O(log n).
     */
    public static int lowerBound(IntSeries data, double value, int fromIndex, int length) {
        int intValue = new Double(Math.floor(value)).intValue();
        int low = fromIndex;
        int high = fromIndex + length -1;
        int index = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            if (intValue > data.get(mid)) {
                low = mid + 1;
            } else if (intValue < data.get(mid)) {
                high = mid - 1;
            } else { //  Values are equal
                index = mid;
                if(intValue == value) {
                    high = mid - 1; // если doubleValue на самом деле int и есть равные ему элементы то бежим по ним вниз
                } else {
                    low = low + 1; // в случае настоящено дробного, если есть элементы равные округленному value то бежим по ним вверх
                }

            }
        }
        if(index < 0) {
            return high;
        }
        return index;

    }

    /**
     * Upper bound search algorithm.<br>
     * Upper bound is kind of binary search algorithm but:<br>
     * -If searched element doesn't exist function returns index of first element which is bigger than searched value.<br>
     * -If there are many values equals searched value function returns LAST occurrence.<br>
     * Behaviour for unsorted arrays is unspecified.
     * <p>
     * Complexity O(log n).
     */
    public static int upperBound(IntSeries data, double value, int fromIndex, int length) {
        int intValue = new Double(Math.ceil(value)).intValue();
        int low = fromIndex;
        int high = fromIndex + length -1;
        int index = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            if (intValue > data.get(mid)) {
                low = mid + 1;
            } else if (intValue < data.get(mid)) {
                high = mid - 1;
            } else { //  Values are equal
                index = mid;
                if(intValue == value) {
                    low = low + 1; // если doubleValue на самом деле int и есть равные ему элементы то бежим по ним вверх
                } else {
                    high = mid - 1; // в случае настоящено дробного, если есть элементы равные округленному value то бежим по ним вниз
                }
            }
        }
        if(index < 0) {
            return low;
        }
        return index;

    }

    public static int lowerBound(FloatSeries data, float value, int fromIndex, int length) {
        int low = fromIndex;
        int high = fromIndex + length -1;
        int index = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            if (value > data.get(mid)) {
                low = mid + 1;
            } else if (value < data.get(mid)) {
                high = mid - 1;
            } else { //  Values are equal
                long midBits = Double.doubleToLongBits(data.get(mid));
                long valueBits = Double.doubleToLongBits(value);
                if (midBits == valueBits) { // Values are equal
                    index = mid;
                    high = mid - 1;
                } else if (midBits < valueBits) { // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                }
                else {  // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
                }
            }
        }
        if(index < 0) {
            return high;
        }
        return index;
    }

    public static int upperBound(FloatSeries data, float value, int fromIndex, int length) {
        int low = fromIndex;
        int high = fromIndex + length -1;
        int index = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            if (value > data.get(mid)) {
                low = mid + 1;
            } else if (value < data.get(mid)) {
                high = mid - 1;
            } else { //  Values are equal
                long midBits = Double.doubleToLongBits(data.get(mid));
                long valueBits = Double.doubleToLongBits(value);
                if (midBits == valueBits) { // Values are equal
                    index = mid;
                    low = low + 1;
                } else if (midBits < valueBits) { // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                }
                else {  // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
                }
            }
        }
        if(index < 0) {
            return low;
        }
        return index;

    }

    /**
     * Test method
     */
    public static void main(String args[]) {
        /******************************
         *   BINARY SEARCH INT TEST
         *****************************/
        System.out.print("INT ARRAY: [");
        int[] a = {-2, -1, 4, 5, 5, 5, 6,  8};
        for (int i = 0; i < a.length -1; i++) {
            System.out.print(a[i] + ", ");
        }
        System.out.println(a[a.length - 1] + "]   size = "+a.length);

        IntSeries data = new IntSeries() {
            @Override
            public int size() {
                return a.length;
            }

            @Override
            public int get(int index) {
                return a[index];
            }
        };

        int lower = Processing.lowerBound(data, 5.3, 0, data.size());
        int upper = Processing.upperBound(data, 5.3, 0, data.size());

        System.out.println("lower(5.3)= "+lower + ",  upper(5.3) = "+ upper);

        lower = Processing.lowerBound(data, 5.0, 0, data.size());
        upper = Processing.upperBound(data, 5.0, 0, data.size());

        System.out.println("lower(5.0)= "+lower + ",  upper(5.0) = "+ upper);

        lower = Processing.lowerBound(data, -1.2, 0, data.size());
        upper = Processing.upperBound(data, -1.2, 0, data.size());

        System.out.println("lower(-1.2) = "+lower + ",  upper(-1.2) = "+ upper);

        lower = Processing.lowerBound(data, 4.3, 0, data.size());
        upper = Processing.upperBound(data, 4.3, 0, data.size());

        System.out.println("lower(4.3) = "+lower + ",  upper(4.3) = "+ upper);

        lower = Processing.lowerBound(data, -3, 0, data.size());
        upper = Processing.upperBound(data, 9, 0, data.size());

        System.out.println("lower(-3) = "+lower + ",  upper(9) = "+ upper);

        System.out.println();

        /************************************
         *   BINARY SEARCH DOUBLE TEST
         ************************************/
        System.out.print("FLOAT ARRAY: [");
        float[] b = {-2.5f, -1.1f, 4.5f,  5, 5, 5, 6,  8};
        for (int i = 0; i < b.length -1; i++) {
            System.out.print(b[i] + ", ");
        }
        System.out.println(b[b.length - 1] + "]   size = "+b.length);

        FloatSeries data1 = new FloatSeries() {
            @Override
            public int size() {
                return b.length;
            }

            @Override
            public float get(int index) {
                return b[index];
            }
        };

        lower = Processing.lowerBound(data1, 5.3f, 0, data1.size());
        upper = Processing.upperBound(data1, 5.3f, 0, data1.size());

        System.out.println("lower(5.3)= "+lower + ",  upper(5.3) = "+ upper);

        lower = Processing.lowerBound(data1, 5.0f, 0, data1.size());
        upper = Processing.upperBound(data1, 5.0f, 0, data1.size());

        System.out.println("lower(5.0)= "+lower + ",  upper(5.0) = "+ upper);

        lower = Processing.lowerBound(data1, -1.2f, 0, data1.size());
        upper = Processing.upperBound(data1, -1.2f, 0, data1.size());

        System.out.println("lower(-1.2) = "+lower + ",  upper(-1.2) = "+ upper);

        lower = Processing.lowerBound(data1, -3, 0, data1.size());
        upper = Processing.upperBound(data1, 9, 0, data1.size());

        System.out.println("lower(-3) = "+lower + ",  upper(9) = "+ upper);
    }

}
