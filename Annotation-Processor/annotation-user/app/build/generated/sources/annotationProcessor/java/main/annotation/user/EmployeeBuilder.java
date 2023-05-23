package annotation.user;

public class EmployeeBuilder {

    private java.lang.String name;
    private int age;

    public EmployeeBuilder name(java.lang.String value) {
        name = value;
        return this;
    }

    public EmployeeBuilder age(int value) {
        age = value;
        return this;
    }

    public Employee build() {
        return new Employee(name, age);
    }

}
