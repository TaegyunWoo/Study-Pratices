// 템플릿 리터럴 타입
type Color = "red" | "black" | "green";
type Animal = "dog" | "cat" | "chicken";

//문제점: 각 경우의 수마다 모두 하드코딩해야함
type ColoredAnimal = "red-dog" | "red-cat" | "red-chicken"; //...;

//해결: 템플릿 리터럴
type ColoredAnimal1 = `${Color}-${Animal}`; //모든 조합을 만족
