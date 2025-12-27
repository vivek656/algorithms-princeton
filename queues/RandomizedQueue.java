/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int DEFAULT_CAPACITY = 1;
    private static final double SIZE_FACTOR_TO_SHRINK = 0.25;

    private Item[] items;
    private int n = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        setArrayToSize(DEFAULT_CAPACITY);
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        validateAddition(item);
        checkAndResizeAddition();
        items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        validateRemoval();
        int d = (int) StdRandom.uniformDouble(0, n);
        Item itemAtD = items[d];
        Item lastItem = items[n - 1];
        items[d] = lastItem;
        items[n - 1] = null;
        n--;
        checkAndResizeRemoval();
        return itemAtD;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        validateRemoval();
        int d = (int) StdRandom.uniformDouble(0, n);
        return items[d];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new IteratorImpl();
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

    private void checkAndResizeAddition() {
        if (n == items.length) {
            upSize();
        }
    }

    private void checkAndResizeRemoval() {
        int newSize = size();
        if (newSize < items.length * SIZE_FACTOR_TO_SHRINK || newSize < 0) {
            downSize();
        }
    }

    private void upSize() {
        Item[] currentItems = items;
        int newSize = (currentItems.length * 2);
        setArrayToSize(newSize);
        Item[] newItems = items;
        for (int i = 0; i < n; i++) {
            newItems[i] = currentItems[i];
        }
    }

    private void downSize() {
        if (items.length == 1) return;
        Item[] currentItems = items;
        int newSize = (currentItems.length / 2);
        setArrayToSize(newSize);
        Item[] newItems = items;
        for (int i = 0; i < n; i++) {
            newItems[i] = currentItems[i];
        }
    }

    private void validateAddition(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("argument must not be null");
        }
    }

    private void validateRemoval() {
        if (size() == 0) {
            throw new NoSuchElementException("queue is empty");
        }
    }

    private class IteratorImpl implements Iterator<Item> {
        private Item[] copy = (Item[]) new Object[n];
        private int curr = 0;

        {
            for (int i = 0; i < n; i++) {
                copy[i] = items[i];
            }
            StdRandom.shuffle(copy);
        }

        public boolean hasNext() {
            return curr < n;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("iterator does not have more elements");
            }
            return copy[curr++];
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }

    private void setArrayToSize(int size) {
        items = (Item[]) new Object[size];
    }
}
