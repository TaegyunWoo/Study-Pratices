// 접근 제어자
// public private protected
class Employee {
  // field
  public name: string;
  protected age: number;
  private position: string;

  // constructor
  constructor(name: string, age: number, position: string) {
    this.name = name;
    this.age = age;
    this.position = position;
  }

// 아래처럼 생성자 파라미터에 접근제어자를 정의하면, 자동으로 필드를 정의해줌 (+ 필드 초기화까지)
//   constructor(
//     public name: string,
//     protected age: number,
//     private position: string
//   ) {}

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

  func() {
    this.age = 23; //파생 클래스에서 protected 접근 가능
    // this.position = "marketing"; 불가능
  }
}

const employee = new Employee("이정환", 27, "developer");
employee.name = "홍길동";
// employee.age = 30; 불가능
// employee.position = "디자이너"; 불가능
