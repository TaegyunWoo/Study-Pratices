// 타입스크립트의 클래스

const employee = {
  name: "이정환",
  age: 27,
  position: "developer",
  Worker() {
    console.log("일함");
  },
};

class Employee {
  // field
  name: string;
  age: number;
  position: string;

  // constructor
  constructor(name: string, age: number, position: string) {
    this.name = name;
    this.age = age;
    this.position = position;
  }

  // method
  work() {
    console.log("일함");
  }
}

class ExecutiveOfficer extends Employee {
  // field
  officeNumber: number;

  // constructor
  constructor(
    name: string,
    age: number,
    position: string,
    officeNumber: number
  ) {
    super(name, age, position);
    this.officeNumber = officeNumber;
  }
}

const employeeB = new Employee("이정환", 27, "개발자");
console.log(employeeB);

const employeeC: Employee = {
  name: "",
  age: 0,
  position: "",
  work() {},
};
