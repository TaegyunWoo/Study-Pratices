// 제네릭

function func<T>(value: T): T {
  return value;
}

let num = func(10); //typeof num === "number"
let bool = func(true); //typeof bool === "boolean"
let arr1 = func([1, 2, 3]); //typeof arr1 === "number[]"
let arr2 = func<[number, number, number]>([1, 2, 3]); //typeof arr2 === "[number, number, number]"
