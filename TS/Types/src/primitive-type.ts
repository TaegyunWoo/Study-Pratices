//---[number]---
let num1: number = 123;
let num2: number = -123;
let num3: number = 0.123;
let num4: number = -0.123;
let num5: number = Infinity;
let num6: number = -Infinity;
let num7: number = NaN; //Not a number까지 숫자로 포함됨.

/* 아래는 오류 발생 (숫자 타입에서 불가능)
    num1 = "hello";
    num1.toUpperCase();
*/

// 아래는 가능
num1.toFixed();

//---[string]---
let str1: string = "hello";
let str2: string = 'hello';
let str3: string = `hello`;
let str4: string = `hello ${num1}`;

/* 아래는 오류 발생 (문자열 타입에서 불가능)
    str1 = 10;
    str1.toFixed();
*/

// 아래는 가능
str1.toUpperCase();

//---[boolean]---
let bool1: boolean = true;
let bool2: boolean = false;

//---[null]---
let null1: null = null;

//---[undefined]---
let unde1: undefined = undefined;

//---[리터럴 타입]---
let numA: 10 = 10;
let strA: "my" = "my";
let boolA: true = true;

/* 아래는 오류 발생 (타입으로 지정된 값 이외는 불가능)
    numA = 12;
    strA = "you";
    boolA = false;
*/