//map 함수 구현
function map<T, U>(arr: T[], callback: (it: T) => U) {
  let result = [];
  for (let i = 0; i < arr.length; i++) {
    result.push(callback(arr[i]));
  }
  return result;
}
let arr = [1, 2, 3];
map(arr, (i) => i * 2);
map(["hi", "hello"], (i) => parseInt(i));

//foreach 함수 구현
function forEach<T>(arr: T[], callback: (it: T) => void) {
  for (let i = 0; i < arr.length; i++) {
    callback(arr[i]);
  }
}
let arr2 = [1, 2, 3];
forEach(arr2, (it) => {
  console.log(it.toFixed());
});
forEach(["apple", "banana"], (it) => {
  console.log(it.toUpperCase());
});
