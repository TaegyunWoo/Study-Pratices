package datastructure;

public class MyQueue {
  private static final int size = 10;
  private int head = 0;
  private int tail = 0;
  Object[] queue = new Object[size];

  public void enqueue(Object o) {
    if (isFull()) {
      throw new RuntimeException("Queue Overflow");
    }
    tail = (tail + 1) % size;
    queue[tail] = o;
  }

  public Object dequeue() {
    if (isEmpty()) {
      throw new RuntimeException("Queue Underflow");
    }
    head = (head + 1) % size;
    Object o = queue[head];
    return o;
  }

  public boolean isEmpty() {
    if (head == tail) {
      return true;
    }
    return false;
  }

  public boolean isFull() {
    if (head == (tail + 1) % size) {
      return true;
    }
    return false;
  }
}
