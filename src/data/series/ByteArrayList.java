package data.series;

import java.util.Arrays;

/**
 * Created by galafit on 1/1/18.
 */
public class ByteArrayList implements ByteSeries {
    private byte[] data;
    private int size;

    public ByteArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
        }
        data = new byte[initialCapacity];
    }

    public ByteArrayList() {
        this(10);
    }

    public ByteArrayList(byte[] source) {
        size = source.length;
        data = new byte[size];
        System.arraycopy(source, 0, data, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public byte get(int index) {
        if (index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return data[index];
    }

    /**
     * Remove an element from the specified index
     */
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        int entry = data[index];
//    int[] outgoing = new int[count - 1];
//    System.arraycopy(data, 0, outgoing, 0, index);
//    count--;
//    System.arraycopy(data, index + 1, outgoing, 0, count - index);
//    data = outgoing;
        // For most cases, this actually appears to be faster
        // than arraycopy() on an array copying into itself.
        for (int i = index; i < size-1; i++) {
            data[i] = data[i+1];
        }
        size--;
        return entry;
    }

    /**
     * Add a new element to the list.
     */
    public void add(byte value) {
        ensureCapacity(size + 1);  // Increments modCount!!
        data[size] = value;
        size++;
    }


    public void add(byte... values) {
        int numNew = values.length;
        ensureCapacity(size + numNew);  // Increments modCount
        System.arraycopy(values, 0, data, size, numNew);
        size += numNew;
    }

    public void ensureCapacity(int minCapacity) {
        int oldCapacity = data.length;
        if (minCapacity > oldCapacity) {
            // int[] oldData = data;
            int newCapacity = (oldCapacity * 3)/2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            // minCapacity is usually close to size, so this is a win:
            data = Arrays.copyOf(data, newCapacity);
        }
    }
}
