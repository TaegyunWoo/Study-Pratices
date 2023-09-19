function func1(): string {
    return "hello";
}

// void 타입
function func2(): void {
    console.log("hello");
    //return 없음
}

let a: void;
// a = 1; 불가능
// a = "hello"; 불가능
// a = {}; 불가능
a = undefined;
// a = null; strictNullChecks 옵션을 끄면 가능

// never 타입
function func3(): never {
    while (true) {} //무한루프에 의해, 반환될 값이 존재할 수 없음
}

function func4(): never {
    throw new Error(); //의도적으로 반드시 예외가 발생하도록 처리되어, 반환될 값이 존재할 수 없음
}

let b: never;
// b = 1; 불가능
// b = "hello"; 불가능
// b = {}; 불가능
// b = undefined; 불가능 (void 타입인 경우, undefined 가능)
// b = null; strictNullChecks 옵션을 꺼도 불가능