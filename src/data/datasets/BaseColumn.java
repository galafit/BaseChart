package data.datasets;

import java.util.List;

/**
 * Created by galafit on 18/9/17.
 */
public class BaseColumn implements NumberColumn {
    NumberColumn innerSet;

    public BaseColumn(int[] data) {
        innerSet = new IntArrayColumn(data);
    }

    public BaseColumn(double[] data) {
        innerSet = new DoubleArrayColumn(data);
    }

    public BaseColumn(List<? extends Number> data) {
        innerSet = new ListColumn(data);
    }


    @Override
    public int size() {
        return innerSet.size();
    }

    @Override
    public double getValue(int index) {
        return innerSet.getValue(index);
    }

    class IntArrayColumn implements NumberColumn {
        private int[] array;

        public IntArrayColumn(int[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public double getValue(int index) {
            return array[index];
        }

    }

    class DoubleArrayColumn implements NumberColumn {
        private double[] array;

        public DoubleArrayColumn(double[] array) {
            this.array = array;
        }

        @Override
        public int size() {
            return array.length;
        }

        @Override
        public double getValue(int index) {
            return array[index];
        }
    }


    class ListColumn implements NumberColumn {
        private List<? extends Number> list;

        public ListColumn(List<? extends Number> list) {
            this.list = list;
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public double getValue(int index) {
            return list.get(index).doubleValue();
        }
    }





}
