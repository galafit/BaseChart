package data;

/**
 * http://sky2high.net/2010/04/java-%D0%B1%D0%B8%D0%BD%D0%B0%D1%80%D0%BD%D1%8B%D0%B9-%D0%BF%D0%BE%D0%B8%D1%81%D0%BA/
 * https://www.youtube.com/watch?v=OE7wUUpJw6I
 * http://www.geeksforgeeks.org/find-first-last-occurrences-element-sorted-array/
 * http://www.ffbit.com/blog/2013/02/26/first-occurrence-binary-search/
 * http://info.javarush.ru/tag/%D0%91%D0%B8%D0%BD%D0%B0%D1%80%D0%BD%D1%8B%D0%B9%20%D0%BF%D0%BE%D0%B8%D1%81%D0%BA/
 */
public class Utils {

    // jdk realization from java.util.arrays
    public static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1; // the same as (low + high) / 2
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    public static int binarySearch(double[] a, int fromIndex, int toIndex, double key) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a[mid];

            if (midVal < key)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal > key)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits)     // Values are equal
                    return mid;             // Key found
                else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        return -(low + 1);  // key not found.
    }

    public static int firstOrLastOccurrenceBinarySearch(double[] a, double key, boolean first) {
        int low = 0;
        int high = a.length - 1;
        int index = -1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a[mid];

            if (midVal < key)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal > key)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits) { // Values are equal
                    index = mid;
                    if (first)
                        high = mid - 1;
                    else
                        low = low + 1;
                } else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        return index;
    }

    public static int prevOrNextBinarySearch(double[] a, double key, boolean previouse) {
        if (key < a[0] || key > a[a.length - 1]) {
            return -1; // key not found
        }
        int low = 0;
        int high = a.length - 1;
        int index = -1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            double midVal = a[mid];

            if (midVal < key)
                low = mid + 1;  // Neither val is NaN, thisVal is smaller
            else if (midVal > key)
                high = mid - 1; // Neither val is NaN, thisVal is larger
            else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(key);
                if (midBits == keyBits) { // Values are equal
                    index = mid;
                    if (previouse)
                        high = mid - 1;
                    else
                        low = low + 1;
                } else if (midBits < keyBits) // (-0.0, 0.0) or (!NaN, NaN)
                    low = mid + 1;
                else                        // (0.0, -0.0) or (NaN, !NaN)
                    high = mid - 1;
            }
        }
        if(index < 0) {
            if (previouse)
                return high;
            else
                return low;
        }
        return index;
    }


    public static void main(String args[]) {
        double[] a = {1, 1, 1, 4, 4, 4, 4, 4, 5, 5.1, 5.6, 5.8};
        int prev = Utils.prevOrNextBinarySearch(a, 1.00, true);
        int next = Utils.prevOrNextBinarySearch(a, 5.05, false);
        System.out.println("prev: "+prev + " next: "+next);
    }

}
