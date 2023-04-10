package datastructure;

public class Stack {
  public java.util.Stack stack = new java.util.Stack();

  public Object pop() {
    Object pop = stack.pop();
    return pop;
  }

  public void push(Object o) {
    stack.push(o);
  }
}
