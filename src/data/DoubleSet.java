package data;

/**
 * Created by galafit on 23/9/17.
 */
public interface DoubleSet {
    public int size();
    public double get(int index);

    public default Range minMaxRange() {
        if(size() == 0){
            return null;
        }
        double min = get(0);
        double max = get(0);
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
    public default int lowerBound(int fromIndex, int length, double value) {
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
                long midBits = Double.doubleToLongBits(get(mid));
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

    /**
     * Upper bound search algorithm.<br>
     * Upper bound is kind of binary search algorithm but:<br>
     * -If searched element doesn't exist function returns index of first element which is bigger than searched value.<br>
     * -If there are many values equals searched value function returns LAST occurrence.<br>
     * Behaviour for unsorted arrays is unspecified.
     * <p>
     * Complexity O(log n).
     */
    public default int upperBound(int fromIndex, int length, double value) {
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
                long midBits = Double.doubleToLongBits(get(mid));
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

        int lower = set.lowerBound(0, set.size(), 5.0);
        int upper = set.upperBound(0, set.size(), 5.0);

        System.out.println("lower(5.0)= "+lower + ",  upper(5.0) = "+ upper);

        lower = set.lowerBound(0, set.size(), -1.2);
        upper = set.upperBound(0, set.size(), -1.2);

        System.out.println("lower(-1.2) = "+lower + ",  upper(-1.2) = "+ upper);

        lower = set.lowerBound(0, set.size(), 1);
        upper = set.upperBound(0, set.size(), 9);

        System.out.println("lower(1) = "+lower + ",  upper(9) = "+ upper);
    }

}
