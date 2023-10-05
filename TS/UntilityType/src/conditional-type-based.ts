// 조건부 타입 기반의 유틸리티 타입
// 1. Exclude<T, U>
//T 유니온 타입에서 U 타입을 제거하는 타입
type A = Exclude<string | boolean, boolean>;
//Exclude<T, U> 구현
type Exclude<T, U> = T extends U ? never : T;

// 2. Extract<T, U>
//T 유니온 타입에서 U 타입을 추출하는 타입
type B = Extract<string | boolean, boolean>;
//Extract<T, U> 구현
type Extract<T, U> = T extends U ? T : never;

// 3. ReturnType<T>
//함수형 타입 T의 반환 타입을 추출하는 타입
type C = ReturnType<() => string>;
//ReturnType<T> 구현
type ReturnType<T extends (...args: any) => any> = T extends (
  ...args: any
) => infer R
  ? R
  : never;
