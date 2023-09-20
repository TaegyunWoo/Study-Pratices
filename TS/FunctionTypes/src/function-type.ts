// 함수에서 타입 정의
function func(a:number, b:number) : number {
    return a + b;
}

// 화살표 함수에서 타입 정의
const add = (a:number, b:number) : number => a + b;

// 함수의 매개변수
function introduce(name = "이정환", age:number, tall?:number) {
    console.log(`name: ${name}`);
    if (typeof tall === "number") //타입 가드
        console.log(`tall: ${tall + 10}`);
}

//아래는 불가능
/*
function introduce(name = "이정환", tall?:number, age:number) {
    console.log(`name: ${name}`);
    if (typeof tall === "number") //타입 가드
        console.log(`tall: ${tall + 10}`);
}
*/

//JS rest 문법 1 (나머지 매개변수를 배열로 받는다)
function getSum1(...rest: number[]) {
    let sum = 0;
    rest.forEach(item => {sum += item});
    return sum;
}
getSum1(1, 2, 3);
getSum1(1, 2, 3, 4, 5, 6);

//JS rest 문법 2 (나머지 매개변수를 배열로 받는다)
function getSum2(...rest: [number, number, number]) {
    let sum = 0;
    rest.forEach(item => {sum += item});
    return sum;
}
getSum2(1, 2, 3);
// getSum2(1, 2, 3, 4, 5, 6); //최대 3개까지만 받을 수 있도록 튜플 타입 지정되어 있기 때문에 불가능