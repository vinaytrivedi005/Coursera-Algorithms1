import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node<Item> front, rear;

    private class Node<T> {
        T item;
        Node<T> next;
        Node<T> previous;
    }

    private class DequeIterator<T> implements Iterator<Item> {
        Deque<Item>.Node<Item> current;

        public DequeIterator() {
            current = front;
        }

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return current != null;
        }

        @Override
        public Item next() {
            // TODO Auto-generated method stub
            if (!hasNext()) {
                throw new NoSuchElementException("All elements are iterated.");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Deque() { // construct an empty deque
        this.size = 0;
        this.front = null;
        this.rear = null;
    }

    public boolean isEmpty() { // is the deque empty?
        return this.size == 0;
    }

    public int size() { // return the number of items on the deque
        return this.size;
    }

    public void addFirst(Item item) { // add the item to the front
        if (item == null) {
            throw new IllegalArgumentException("null cannot be enqueued.");
        }

        if (isEmpty()) {
            this.front = new Node<>();
            this.rear = this.front;
        }

        else {
            Node<Item> oldFront = this.front;
            this.front = new Node<>();
            this.front.next = oldFront;
            oldFront.previous = this.front;
        }
        this.front.item = item;
        size++;
    }

    public void addLast(Item item) { // add the item to the end
        if (item == null) {
            throw new IllegalArgumentException("null cannot be enqueued.");
        }

        if (isEmpty()) {
            this.front = new Node<>();
            this.rear = this.front;
        }

        else {
            Node<Item> oldRear = this.rear;
            this.rear = new Node<>();
            this.rear.previous = oldRear;
            oldRear.next = this.rear;
        }
        this.rear.item = item;
        size++;
    }

    public Item removeFirst() { // remove and return the item from the front
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        Item answer = this.front.item;
        if (this.size == 1) {
            this.front = null;
            this.rear = null;
        } else {
            this.front = this.front.next;
            this.front.previous = null;
        }
        size--;
        return answer;
    }

    public Item removeLast() { // remove and return the item from the end
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        Item answer = this.rear.item;
        if (this.size == 1) {
            this.front = null;
            this.rear = null;
        } else {
            this.rear = this.rear.previous;
            this.rear.next = null;
        }
        size--;
        return answer;
    }

    public Iterator<Item> iterator() { // return an iterator over items in order from front to end
        return new DequeIterator<Item>();
    }

    public static void main(String[] args) { // unit testing (optional)

    }
}