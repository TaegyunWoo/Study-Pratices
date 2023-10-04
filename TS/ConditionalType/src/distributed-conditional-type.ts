// 분산적인 조건부 타입
type StringNumberSwitch<T> = T extends number ? string : number;
let a: StringNumberSwitch<number>;
let b: StringNumberSwitch<string>;

let c: StringNumberSwitch<number | string>;
//StringNumberSwitch<number> |
//StringNumberSwitch<string>
//위처럼 분리됨

//실용적인 예제
type Exclude<T, U> = T extends U ? never : T;
type A = Exclude<number | string | boolean, string>;
//1단계
//Exclude<number, string> |
//Exclude<string, string> |
//Exclude<boolean, string>

//2단계
//number |
//never |
//boolean

//결과 (never은 공집합이기에 제거됨)
//number | boolean

//실용적 예제 2
type Extract<T, U> = T extends U ? T : never;
type B = Extract<number | string | boolean, string>;

//분산 방지
type TypeA<T> = [T] extends [number] ? string : number;
let d: TypeA<number | string>;
