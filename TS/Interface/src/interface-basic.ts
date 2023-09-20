//인터페이스
interface Person {
    name: string;
    age: number;
    optional?: string;
    readonly tmp: string;
    sayHi1: () => void; //기본 함수 타입 표현식 사용시
    sayHi2(): void; //호출 시그니처 사용시 (화살표 표현식 사용 X)
    sayHiOverLoaded(num: number): void; //오버로딩 (호출 시그니처만 가능)
}

const person: Person = {
    name: "이정환",
    age: 23,
    tmp: "init",
    sayHi1: () => console.log("hello"),
    sayHi2: () => console.log("hello"),
    sayHiOverLoaded: (num) => console.log("hello" + num),
};
// person.tmp = "new value"; //불가능
