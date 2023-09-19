//타입 단언

type Person = {
    name: string;
    age: number;
};

/* 아래 코드는 불가능 (나중에 프로퍼티 값을 초기화하고 싶은 경우)
let person: Person = {}; //Person 타입이지만, 빈 객체로 초기화됨 -> 에러
person.name = "우태균";
person.age = 12;
*/

/* 아래 코드도 불가능
let person = {}; //빈 객체 타입으로 선언됨.
person.name = "우태균"; //빈 객체 타입이기 때문에 에러 발생
person.age = 12; //빈 객체 타입이기 때문에 에러 발생
*/

//해결방법 -> 타입 단언
let person = {} as Person; //TSC에게 person 변수를 Person 타입으로 간주하라고 알려줌
person.name = "우태균";
person.age = 12;

type Dog = {
    name: string;
    color: string;
};

let dog1 : Dog = {
    name : "돌돌이",
    color : "brown",
    // breed : "진도" //초과 프로퍼티 검사 발동 -> 오류
};
let dog2 = {
    name : "돌돌이",
    color : "brown",
    breed : "진도" //초과 프로퍼티 검사 발동 X -> 오류
} as Dog; //Dog 타입으로 단언

//타입 단언 규칙
let num1 = 10 as never; //num1을 never 타입으로 단언 가능 (number는 never의 슈퍼타입이기에)
let num2 = 10 as unknown; //num1을 unknown 타입으로 단언 가능 (number는 unknown의 서브타입이기에)
// let num3 = 10 as string; //num1을 string 타입으로 단언 불가능 (number와 string은 관계 X)

//const 단언
let num4 = 10 as const; //number literal 타입으로 단언됨
let cat = {
    name: "야옹이",
    color: "yellow"
} as const; //모든 프로퍼티가 readonly로 추론됨
// cat.color = "black"; //불가능

//Non Null 단언
type Post = {
    title: string;
    author?: string; //익명인 경우, undefined 가능
};

let post: Post = {
    title: "게시글1",
    author: "우태균"
}

// const leng: number = post.author?.length; //number 타입에 undefined가 올수있어서 오류 발생
// post.author?.length => 옵셔널 체이닝 : 만약 post.author가 null이거나 undefined라면, post.author.length 전체가 null 혹은 undefined
//해결방법
const leng: number = post.author!.length; //post.author가 null이나, undefined가 아니라는 것이라고 확신시킴