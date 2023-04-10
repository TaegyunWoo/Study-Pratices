package datastructure;

import java.util.ArrayList;
import java.util.List;

public class MyStack {
  private int head = 0;
  private static final int size = 10;
  Object[] stack = new Object[size];

  public void push(Object o) {
    if (isFull()) {
      throw new RuntimeException("Stack Overflow");
    }
    stack[head++] = o;
  }


  public Object pop() {
    if (isEmpty()) {
      throw new RuntimeException("Stack Underflow");
    }
    return stack[--head];
  }

  private boolean isEmpty() {
    if (head == 0) {
      return true;
    }
    return false;
  }

  private boolean isFull() {
    if (head == size) {
      return true;
    }
    return false;
  }

}
