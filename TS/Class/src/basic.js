// 클래스

let studentA = {
  name: "이정환",
  grage: "A+",
  age: 27,
  study() {
    console.log("열심히 공부 함");
  },
  introduce() {
    console.log("안녕하세요");
  },
};

class Student {
  // field
  name;
  grade;
  age;

  // constructor
  constructor(name, grade, age) {
    this.name = name;
    this.grade = grade;
    this.age = age;
  }

  // method
  study() {
    console.log("열심히 공부 함");
  }

  introduce() {
    console.log(`안녕하세요. ${this.name}입니다.`);
  }
}

let studentB = new Student("홍길동", "B+", 25);
console.log(studentB);
studentB.study();
studentB.introduce();

class StudentDeveloper extends Student {
  // field
  favoriteSkill;

  // constructor
  constructor(name, grade, age, favoriteSkill) {
    super(name, grade, age);
    this.favoriteSkill = favoriteSkill;
  }

  // method
  programming() {
    console.log(`${this.favoriteSkill}로 개발함.`);
  }

  get nameAge() {
    return `${this.name} + ${this.age}`;
  }
}

const studentDeveloper = new StudentDeveloper("이정환", "B+", 26, "TypeScript");
console.log(studentDeveloper);
studentDeveloper.programming();
