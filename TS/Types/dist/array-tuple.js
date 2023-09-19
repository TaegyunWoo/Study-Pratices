//배열 (단일 타입)
let numAry = [1, 2, 3];
let strAry = ["hello", "world", "!"];
let boolAry = [true, false, true]; //제네릭 사용
//배열 (다중 타입)
let multiAry = [1, "hello"];
//다차원 배열
let doubleAry = [
    [1, 2, 3],
    [4, 5]
];
//튜플 (길이와 타입이 고정된 배열)
let tup1 = [1, 2];
let tup2 = [1, "hello", true];
/* 아래는 불가능
    tup1 = [1, 2, 3]; 길이가 2를 넘어섬
    tup1 = ["hello", 2]; 타입 불만족
*/
//튜플 사용처
const users = [
    ["이정환", 1],
    ["이아무개", 2],
    ["김아무개", 3],
    // [5, "최아무개"], 이렇게 형식이 어긋난 경우를 튜플로 방지 가능
    ["박아무개", 4]
];
export {};
