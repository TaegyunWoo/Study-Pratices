//함수 오버로딩
//오버로딩될 함수의 타입들 선언 -> 오버로드 시그니처
function func(a: number): void;
function func(a: number, b: number, c: number): void;

//실제 구현부 -> 구현 시그니처
function func(a: number, b?: number, c?: number) { //매개변수가 1개인 오버로드 시그니처가 있기 때문에, 마지막 2개 매개변수는 undefined를 허용해야 한다.
    if (typeof b === "number" && typeof c === "number") {
        console.log(a + b + c);
    } else {
        console.log(a * 20);
    }
};

//오버로드 시그니처가 있다면, 오버로드 시그니처를 따른다.
// func(); //오버로드 시그니처 중, 매개변수가 없는 시그니처는 존재하지 않으므로 오류
func(1); //오버로드 시그니처 중, 매개변수가 1개인 시그니처는 존재하므로 가능
// func(1, 2); //오버로드 시그니처 중, 매개변수가 2개인 시그니처는 존재하지 않으므로 오류
func(1, 2, 3); //오버로드 시그니처 중, 매개변수가 3개인 시그니처는 존재하므로 가능