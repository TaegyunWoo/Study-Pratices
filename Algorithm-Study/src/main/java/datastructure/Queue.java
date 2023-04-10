package datastructure;

import java.util.ArrayDeque;

public class Queue {
  java.util.Queue queue = new ArrayDeque();

  public void enqueue(Object o) {
    queue.add(o);
  }

  public Object dequeue() {
    return queue.poll();
  }
}
