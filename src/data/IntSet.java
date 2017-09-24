package data;

/**
 * Created by galafit on 22/9/17.
 */
public interface IntSet {
    public int size();
    public int get(int index);

    public default Range minMaxRange() {
        if(size() == 0){
            return null;
        }
        int min = get(0);
        int max = get(0);
        for (int i = 1; i < size() ; i++) {
            min = Math.min(min, get(i));
            max = Math.max(max, get(i));
        }
        return new Range(min, max);
    }

    /**
     * Lower bound search algorithm.<br>
     * Lower bound is kind of binary search algorithm but:<br>
     * -If searched element doesn't exist function returns index of first element which is less than searched value.<br>
     * -If there are many values equals searched value function returns FIRST occurrence.<br>
     * Behaviour for unsorted arrays is unspecified.
     * <p>
     * Complexity O(log n).
     */
    public default int lowerBound(int fromIndex, int length, double doubleValue) {
        int value = new Double(Math.floor(doubleValue)).intValue();
        int low = fromIndex;
        int high = fromIndex + length -1;
        int index = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            if (value > get(mid)) {
                low = mid + 1;
            } else if (value < get(mid)) {
                high = mid - 1;
            } else { //  Values are equal
                index = mid;
                if(value == doubleValue) {
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
    public default int upperBound(int fromIndex, int length, double doubleValue) {
        int value = new Double(Math.ceil(doubleValue)).intValue();
        int low = fromIndex;
        int high = fromIndex + length -1;
        int index = -1;
        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            if (value > get(mid)) {
                low = mid + 1;
            } else if (value < get(mid)) {
                high = mid - 1;
            } else { //  Values are equal
                index = mid;
                if(value == doubleValue) {
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




    public static void main(String args[]) {
        int[] a = {-2, -1, 4, 4, 5, 5, 5, 6,  8};
        for (int i = 0; i < a.length -1; i++) {
            System.out.print(a[i] + ", ");
        }
        System.out.println(a[a.length - 1] + "   size = "+a.length);

        IntSet set = new IntSet() {
            @Override
            public int size() {
                return a.length;
            }

            @Override
            public int get(int index) {
                return a[index];
            }
        };

        int lower = set.lowerBound(0, set.size(), 5.3);
        int upper = set.upperBound(0, set.size(), 5.3);

        System.out.println("lower(5.3)= "+lower + ",  upper(5.3) = "+ upper);

        lower = set.lowerBound(0, set.size(), -1.2);
        upper = set.upperBound(0, set.size(), -1.2);

        System.out.println("lower(-1.2) = "+lower + ",  upper(-1.2) = "+ upper);

        lower = set.lowerBound(0, set.size(), 1);
        upper = set.upperBound(0, set.size(), 9);

        System.out.println("lower(1) = "+lower + ",  upper(9) = "+ upper);
    }

}
