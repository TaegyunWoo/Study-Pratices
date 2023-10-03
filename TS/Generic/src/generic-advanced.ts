// 제네릭 활용 사례 1
function swap<T1, T2>(a: T1, b: T2) {
  return [b, a];
}
const [a, b] = swap("1", 2);

// 제네릭 활용 사례 2
function returnFirstVal1<T>(data: T[]) {
  return data[0];
}
let num1 = returnFirstVal1([0, 1, 2]);
let str1 = returnFirstVal1([1, "hello", "name"]); //str1은 number | string 유니온 타입으로 추론됨.

// 제네릭 활용 사례 3
function returnFirstVal2<T>(data: [T, ...unknown[]]) {
  return data[0];
}
let num2 = returnFirstVal2([0, 1, 2]);
let str2 = returnFirstVal2([1, "hello", "name"]);

// 제네릭 활용 사례 4
function getLength<T extends { length: number }>(data: T) {
  return data.length;
}
let var1 = getLength([1, 2, 3]);
let var2 = getLength("12345");
let var3 = getLength({ length: 10 });
// let var4 = getLength(100); number 타입의 값 10에는 length라는 프로퍼티가 없기 때문에 오류