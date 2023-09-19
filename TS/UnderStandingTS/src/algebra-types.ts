//대수 타입: 합집합, 교집합 타입

//합집합 타입 (Union 타입)
let a: string | number | boolean;
a = 1;
a = "hello";
a = true;

let arr: (number | string | boolean)[] = ["hello", 123, false];

type Dog = {
    name: string;
    color: string;
}
type Person = {
    name: string;
    language: string;
}
type Union1 = Dog | Person; //타입 별칭을 사용해서, Union 타입 만들기

let union1: Union1 = { //이건 Dog 타입에 해당
    name: "",
    color: ""
}
let union2: Union1 = { //이건 Person 타입에 해당
    name: "",
    language: ""
}
let union3: Union1 = { //이건 교집합 타입에 해당
    name: "",
    color: "",
    language: ""
}
// let union4: Union1 = { //불가능 (Dog, Person 어디에도 속하지 않음)
//     name: "",
// }


//교집합 타입 (Intersaction 타입)
let variable: number & string; //number와 string은 교집합이 없다 -> 따라서 never 타입 (공집합)

type Intersaction = Dog & Person;
let intersaction1: Intersaction = { //모든 프로퍼티를 다 가져야만, 교집합에 해당됨
    name: "",
    color: "",
    language: ""
};
// let intersaction2: Intersaction = { //모든 프로퍼티를 다 가져야만, 교집합에 해당됨
//     name: "",
//     color: ""
// };
// let intersaction3: Intersaction = { //모든 프로퍼티를 다 가져야만, 교집합에 해당됨
//     name: "",
//     language: ""
// };