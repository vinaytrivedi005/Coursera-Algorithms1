import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] items;

    public RandomizedQueue() { // construct an empty randomized queue
        this.size = 0;
        this.items = (Item[]) new Object[1];
    }

    public boolean isEmpty() { // is the randomized queue empty?
        return this.size == 0;
    }

    public int size() { // return the number of items on the randomized queue
        return this.size;
    }

    public void enqueue(Item item) { // add the item
        if (item == null) {
            throw new IllegalArgumentException("Cannot enque null items.");
        }

        if (isFull()) {
            // System.out.println("capacity inc: "+this.size() * 2);
            resize(this.size() * 2);
        }

        this.items[size++] = item;
    }

    public Item dequeue() { // remove and return a random item
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        int randomIndex = StdRandom.uniform(size);
        Item answer = items[randomIndex];

        if (randomIndex != size - 1) {
            items[randomIndex] = items[size - 1];
        }

        items[size - 1] = null;
        size--;
        if (size < items.length / 4 && size > 0) {
            // System.out.println("capacity dec: "+items.length/2);
            resize(items.length / 2);
        }
        return answer;
    }

    public Item sample() { // return a random item (but do not remove it)
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        int randomIndex = StdRandom.uniform(size);
        Item answer = items[randomIndex];

        return answer;
    }

    public Iterator<Item> iterator() { // return an independent iterator over items in random order
        return new RandomizedQueueIterator<Item>();
    }

    private boolean isFull() {
        // TODO Auto-generated method stub
        return this.size() == items.length;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < this.size(); i++) {
            copy[i] = this.items[i];
        }
        this.items = copy;
    }

    private class RandomizedQueueIterator<T> implements Iterator<Item> {

        Item[] randomizedItems;
        int riSize;

        public RandomizedQueueIterator() {
            // TODO Auto-generated constructor stub
            randomizedItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                randomizedItems[i] = items[i];
            }
            riSize = size;
        }

        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return riSize != 0;
        }

        @Override
        public Item next() {
            // TODO Auto-generated method stub
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int randIndex = StdRandom.uniform(riSize);
            Item next = randomizedItems[randIndex];
            if (randIndex != riSize - 1) {
                randomizedItems[randIndex] = randomizedItems[riSize - 1];
            }
            randomizedItems[riSize - 1] = null;
            riSize--;
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) { // unit testing (optional)
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        System.out.println(rq.isEmpty());
        System.out.println(rq.size());
        System.out.println(rq.size());
        System.out.println(rq.isEmpty());
        System.out.println(rq.isEmpty());
        rq.enqueue(26);
        System.out.println(rq.dequeue());
        System.out.println(rq.size());
        rq.enqueue(12);
        for(int i=0;i<10000;i++) {
        	int call = StdRandom.uniform(1,3);
        	if(call==1) {
        		rq.enqueue(StdRandom.uniform(100));
        	}
        	if(call==2 && rq.size > 0) {
        		System.out.println(rq.dequeue());
        	}
        }
    }
}
