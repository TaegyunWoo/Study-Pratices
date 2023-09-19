let myVar = 10;
// myVar = "hello"; 불가능 -> 초기화된 값 10 때문에 myVar는 number 타입으로 설정됨

let anyVar: any = 10; //any 타입
anyVar = "hello"; //가능
anyVar = () => {}; //가능
anyVar.toUpperCase();

//unknown
let normalVar: number;
let unknownVar: unknown;
unknownVar = "hello"; //가능
unknownVar = 1; //가능
unknownVar = () => {}; //가능
// normalVar = unknownVar; -> 불가능 (unknown 타입 변수에 담긴 값은 다른 변수에 담을 수 없다)
if (typeof unknownVar === "number") { //타입을 확실히 밝힌 뒤에, 해당 타입으로 사용 가능
    normalVar = unknownVar;
}