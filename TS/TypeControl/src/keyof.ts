// keyof 연산자
interface Person {
  name: string;
  age: number;
}

function getPropertyKey(person: Person, key: keyof Person) {
  //Person의 프로퍼티 1 | Person의 프로퍼티 2 | ...
  return person[key];
}

const person: Person = {
  name: "이정환",
  age: 27,
};

getPropertyKey(person, "name");

// typeof 연산자로 타입 추출
type Person1 = typeof person;
