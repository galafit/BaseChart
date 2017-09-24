package data;

import java.util.List;

/**
 * Created by galafit on 18/9/17.
 */
public class BaseNumberSet implements NumberSet {
    NumberSet innerSet;

    public BaseNumberSet(int[] data) {
        innerSet = new IntArraySet(data);
    }

    public BaseNumberSet(double[] data) {
        innerSet = new DoubleArraySet(data);
    }

    public BaseNumberSet(List<? extends Number> data) {
        innerSet = new ListSet(data);
    }


    @Override
    public int size() {
        return innerSet.size();
    }

    @Override
    public double get(int index) {
        return innerSet.get(index);
    }

    class IntArraySet implements NumberSet {
        private int[] array;

        public IntArraySet(int[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public double get(int index) {
            return array[index];
        }

    }

    class DoubleArraySet implements NumberSet{
        private double[] array;

        public DoubleArraySet(double[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public double get(int index) {
            return array[index];
        }
    }


    class ListSet implements NumberSet{
        private List<? extends Number> list;

        public ListSet(List<? extends Number> list) {
            this.list = list;
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public double get(int index) {
            return list.get(index).doubleValue();
        }
    }





}
