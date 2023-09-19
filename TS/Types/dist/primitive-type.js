//---[number]---
let num1 = 123;
let num2 = -123;
let num3 = 0.123;
let num4 = -0.123;
let num5 = Infinity;
let num6 = -Infinity;
let num7 = NaN; //Not a number까지 숫자로 포함됨.
/* 아래는 오류 발생 (숫자 타입에서 불가능)
    num1 = "hello";
    num1.toUpperCase();
*/
// 아래는 가능
num1.toFixed();
//---[string]---
let str1 = "hello";
let str2 = 'hello';
let str3 = `hello`;
let str4 = `hello ${num1}`;
/* 아래는 오류 발생 (문자열 타입에서 불가능)
    str1 = 10;
    str1.toFixed();
*/
// 아래는 가능
str1.toUpperCase();
//---[boolean]---
let bool1 = true;
let bool2 = false;
//---[null]---
let null1 = null;
//---[undefined]---
let unde1 = undefined;
//---[리터럴 타입]---
let numA = 10;
let strA = "my";
let boolA = true;
export {};
/* 아래는 오류 발생 (타입으로 지정된 값 이외는 불가능)
    numA = 12;
    strA = "you";
    boolA = false;
*/ 
