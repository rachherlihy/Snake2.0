package SnakeAgar;
import java.util.Iterator;
import java.util.NoSuchElementException;

// uses created underlying doubly linked list. Create and store deque of
// food objects and the snake's body objects
public class Deque<E extends Comparable<E>> implements DequeADT<E> {
    public E element;
    protected Node<E> firstNode;
    protected Node<E> lastNode;
    protected int numElements;
    
    // default constructor initialises empty
    public Deque() {
    }
    
    @Override
    public int size() {
        return numElements;
    }

    @Override
    public E first() throws NoSuchElementException {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return firstNode.element;
    }

    @Override
    public E last() throws NoSuchElementException {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return lastNode.element;
    }
    
    @Override
    public void enqueueRear(E element) {
        Node<E> newNode = new Node<E>();
        if(isEmpty()) {
            
            firstNode = newNode;
            firstNode.element = element;
            lastNode.previous = firstNode;
            lastNode = newNode;
            numElements++;
        }
        else {
            lastNode.next = newNode;
            newNode.previous = lastNode;
            newNode.element = element;
            lastNode = newNode;
            numElements++;
        }
    }

    @Override
    public void enqueueFront(E element) {
        Node<E> newNode = new Node<E>();
        if(isEmpty()) {
            firstNode = newNode;
            firstNode.element = element;
            firstNode.next = lastNode;
            lastNode = newNode;
            numElements++;
        }
        else {
            firstNode.previous = newNode;
            newNode.next = firstNode;
            newNode.element = element;
            firstNode = newNode;
            numElements++;
        }
    }

    @Override
    public E dequeueRear() throws NoSuchElementException {
        if(numElements > 0) {
            E remove = lastNode.element;
            lastNode = lastNode.previous;
            lastNode.next = null;
            numElements--;
            return remove;
        }
        else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public E dequeueFront() throws NoSuchElementException {
        if(numElements > 0) {
            E remove = firstNode.element;
            firstNode = firstNode.next;
            numElements--;
            return remove;
        }
        else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean isEmpty() throws NoSuchElementException {
        return firstNode == null;
    }

    @Override
    public void clear() {
        firstNode = null;
        numElements = 0;
    }

    // returns an iterator for traversing the list in either direction
    @Override
    public Iterator<E> iterator(boolean fromFront) {
        if(fromFront) {
            return new ForwardIterator<E>(firstNode);
        }
        return new ReverseIterator<E>(lastNode);
    }
    
    // inner class for iterator traversing from front to rear
    public class ForwardIterator<E> implements Iterator<E> {
        private Node<E> forward;
        // constructor uses first node in deque to iterate over collection
        public ForwardIterator(Node<E> firstNode) {
            forward = firstNode;
        }
        @Override
        public boolean hasNext() {
            return(forward!=null);
        }
        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = forward.element;
            forward = forward.next;
            return element;
        }  
    }
    // inner class for iterator traversing from rear to front
    public class ReverseIterator<E> implements Iterator<E> {
        private Node<E> reverse;
        public ReverseIterator(Node<E> lastNode) {
            reverse = lastNode;
        }
        @Override
        public boolean hasNext() {
            return(reverse!=null);
        }
        @Override
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException();
            }
            E element = reverse.element;
            reverse = reverse.previous;
            return element;
        }
    }
    
    // inner class for the Nodes in the deque
    protected class Node<E> {
        public Node<E> previous;
        public Node<E> next;
        public E element;
    }
}
