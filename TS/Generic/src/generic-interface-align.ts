// 제네릭 인터페이스
interface KeyPair<K, V> {
  key: K;
  value: V;
}

let keyPair: KeyPair<string, number> = {
  key: "a",
  value: 1,
};

let keyPair2: KeyPair<boolean, string[]> = {
  key: true,
  value: ["apple", "banana"],
};

//인덱스 시그니처
interface Map<V> {
  [key: string]: V;
}
let stringMap: Map<string> = {
  item1: "apple",
  item2: "banana",
};
let booleanMap: Map<boolean> = {
  item1: true,
  item2: false,
};

//제네릭 타입 별칭 (인터페이스와 유사)
type Map2<V> = {
  [key: string]: V;
};
let stringMap2: Map2<string> = {
  item1: "apple",
  item2: "banana",
};

//제네릭 인터페이스의 활용 예시
interface Student {
  type: "student";
  school: string;
}
interface Developer {
  type: "developer";
  skill: string;
}
interface User<T> {
  name: string;
  profile: T;
}
function goToSchool(user: User<Student>) {
  const school = user.profile.school;
  console.log(`${school}로 등교 완료`);
}

const developerUser: User<Developer> = {
  name: "우태균",
  profile: {
    type: "developer",
    skill: "TS",
  },
};
const studentUser: User<Student> = {
  name: "홍길동",
  profile: {
    type: "student",
    school: "가톨릭대학교",
  },
};
