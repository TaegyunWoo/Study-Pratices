package annotation.user;

import annotation.processor.MyBuilder;

@MyBuilder
public class Employee {
  private String name;
  private int age;

  public Employee(String name, int age) {
    this.name = name;
    this.age = age;
  }
}
