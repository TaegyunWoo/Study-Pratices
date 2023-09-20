// 인터페이스의 확장
interface Animal {
    name: string;
    age: number;
}

interface Dog extends Animal { //확장 (상속)
    isBark: boolean;
}

interface Cat extends Animal { //확장 (상속)
    isScratch: boolean;
}

interface Chicken extends Animal { //확장 (상속)
    name: "literal name"; //타입 재정의
    isFly: boolean;
}

let dog: Dog = {
    name: "발발이",
    age: 4,
    isBark: true
}

let chicken: Chicken = {
    name: "literal name", //재정의된 타입 사용
    age: 4,
    isFly: true
}

//다중 상속
interface DogCat extends Dog, Cat { //다중 상속
}
let dogCat: DogCat = {
    name: "개냥이",
    age: 5,
    isBark: true,
    isScratch: true
}