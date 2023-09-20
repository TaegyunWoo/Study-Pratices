//함수 타입 호환성

//기준 1 : 반환값이 호환되는가
type A = () => number;
type B = () => 10;

let a: A = () => 10;
let b: B = () => 10;

a = b; //가능 (B의 return 타입인 number literal 을 number 타입으로 업캐스팅)
// b = a; //불가능 (A의 return 타입인 number을 number literal 타입으로 다운캐스팅)

//기준 2 : 매개변수가 호환되는가
//2-1 : 매개변수의 개수가 같을 때
type C = (value:number) => void;
type D = (value:10) => void;

let c: C = (value) => {};
let d: D = (value) => {};

// c = d; //불가능 (D의 매개변수 타입인 number literal을 number 타입으로 업캐스팅)
d = c; //가능 (C의 매개변수 타입인 number를 number literal 타입으로 다운캐스팅)

//매개변수 타입은 업캐스팅이 아니라, 다운캐스팅만 가능한 이유
type Animal = { //슈퍼 타입
    name: string;
};
type Dog = { //서브 타입
    name: string;
    color: string;
};
let animalFunc = (animal: Animal) => {
    console.log(animal.name);
};
let dogFunc = (dog: Dog) => {
    console.log(dog.name);
    console.log(dog.color);
}
// animalFunc = dogFunc; //불가능 (매개변수 타입이 업캐스팅)
dogFunc = animalFunc; //가능 (매개변수 타입이 다운캐스팅)

//만약 매개변수끼리 업캐스팅이 가능하다면, 아래도 가능해야함.
let testFunc = (dog: Animal) => { //Dog -> Animal 로 업캐스팅
    console.log(dog.name);
    // console.log(dog.color);
}

//따라서 아래와 같은 매개변수 타입의 다운캐스팅만 가능
let testFunc2 = (animal: Dog) => { //Animal -> Dog
    console.log(animal.name);
}

//정리하자면, Dog는 Animal 타입의 서브타입이기에, Dog 타입은 Animal 타입의 모든 프로퍼티를 갖는다.
//따라서, Animal 타입 Argument를 기반으로 작성된 로직이 Dog 타입을 매개변수 타입으로 갖는 변수에 저장되어도 문제 X

//2-2 : 매개변수의 개수가 다를 때 (타입이 같다는 전재)
type Func1 = (a:number, b:number) => void;
type Func2 = (a:number) => void;

let func1: Func1 = (a,b) => {};
let func2: Func2 = (a) => {};

func1 = func2; //가능 (매개변수 1개를 제거해서, 총 매개변수가 1개가 될 수 있기에)
// func2 = func1; //불가능 (매개변수 1개를 2개를 늘릴 수는 없기에)