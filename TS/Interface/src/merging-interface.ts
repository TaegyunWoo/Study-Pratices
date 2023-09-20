// 인터페이스 선언 합침
interface Person {
    name: string;
}
interface Person { //중복된 인터페이스 (위 인터페이스와 합쳐짐)
    // name: number; //이건 충돌이라 안됨
    name: string;
    age: number;
}
const person: Person = {
    name: "이정환",
    age: 23
}