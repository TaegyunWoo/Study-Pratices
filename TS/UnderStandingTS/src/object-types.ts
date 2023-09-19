type Animal = {
    name: string;
    color: string;
};

type Dog = { //Animal 타입의 모든 프로퍼티가 있음
    name: string; //Animal 타입의 프로퍼티
    color: string; //Animal 타입의 프로퍼티
    breed: string;
}

let animal: Animal = {
    name: "기린",
    color: "yellow"
}
let dog: Dog = {
    name: "돌돌이",
    color: "yellow",
    breed: "진도"
}

animal = dog; //가능 (업캐스팅)
// dog = animal; //불가능 (다운캐스팅)

//연습
type Book = {
    name: string;
    price: number;
}
type ProgrammingBook = {
    name: string;
    price: number;
    skill: string;
}

let book: Book;
let programmingBook: ProgrammingBook = {
    name: "프로그래밍 책",
    price: 33000,
    skill: "TS"
}
book = programmingBook; //업캐스팅
//programmingBook = book; //다운캐스팅

//초과 프로퍼티 검사 기능
book = {
    name: "프로그래밍 책",
    price: 33000,
    // skill: "TS", //불가능
}