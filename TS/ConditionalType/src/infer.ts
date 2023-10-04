// infer

type FuncA = () => string;
type FuncB = () => number;
type ReturnType<T> = T extends () => infer R ? R : never;
//infer를 사용하면, T extends () => R 조건이 true가 되도록 R을 추론함.
type A = ReturnType<FuncA>;
type B = ReturnType<FuncB>;

// 예제 (Promise의 제네릭타입만 가져오기)
type PromiseUnpack<T> = T extends Promise<infer R> ? R : never;
type PromiseA = PromiseUnpack<Promise<number>>;
