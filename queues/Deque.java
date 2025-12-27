/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {

    private int n = 0;
    private Node head = null;
    private Node tail = null;


    // construct an empty deque

    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    //  a (HT)
    //  c(H) -> <- b -> <- a(T)

    public void addFirst(Item item) {
        validateAddition(item);
        if (head == null) {
            head = new Node(item);
            tail = head;
        }
        else {
            Node oldHead = head;
            head = new Node(item);
            head.next = oldHead;
            oldHead.prev = head;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateAddition(item);
        if (tail == null) {
            tail = new Node(item);
            head = tail;
        }
        else {
            Node oldTail = tail;
            tail = new Node(item);
            tail.prev = oldTail;
            oldTail.next = tail;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateRemoval();
        Node oldHead = head;
        head = head.next;
        if (head == null) tail = null;
        else head.prev = null;
        n--;
        return oldHead.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateRemoval();
        Node oldTail = tail;
        tail = tail.prev;
        if (tail == null) head = null;
        else tail.next = null;
        n--;
        return oldTail.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new IteratorImpl();
    }

    // unit testing (required)
    public static void main(String[] args) {

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

    private class Node {
        Item item;
        Node next;
        Node prev;

        Node(Item item) {
            this.item = item;
        }
    }

    private class IteratorImpl implements Iterator<Item> {

        private Node currNode = head;

        public boolean hasNext() {
            return currNode != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("iterator does not have more elements");
            }
            Item item = currNode.item;
            currNode = currNode.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }
}
