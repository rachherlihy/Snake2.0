package SnakeAgar;
import java.util.Iterator;
import java.util.NoSuchElementException;

// Interface for double ended queue, allows insertion and removal from 
// either end. Throws NoSuchElementException if deque is empty and an  
// attempt is made to return or remove an element
public interface DequeADT<E> {
    // returns the number of elements in the deque
    public int size();
    // returns the first element in the deque without removing it
    public E first() throws NoSuchElementException;
    // returns the last element in the queue without removing it
    public E last() throws NoSuchElementException;
    // adds an element to the end of the queue
    public void enqueueRear(E element);
    // adds an element to the front of the queue
    public void enqueueFront(E element);
    // removes an element from the end of the queue
    public E dequeueRear() throws NoSuchElementException;
    // removes an element from the front of the queue
    public E dequeueFront() throws NoSuchElementException;
    // returns true if queue contains no elements
    public boolean isEmpty() throws NoSuchElementException;
    // removes all elements from collection
    public void clear();
    // returns an Iterator over elements in collection from front or rear
    public Iterator<E> iterator(boolean fromFront);
}
