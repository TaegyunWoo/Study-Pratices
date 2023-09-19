let a = 10; // -> number 타입으로 자동 추론
let b = "hello" // -> string 타입으로 자동 추론
let c = {
    id: 1,
    name: "우태균",
    profile: {
        nickname: "taeja",
    },
    urls: ["https://taegyunwoo.github.io"]
}; /*
    {
        id: number;
        name: string;
        profile: {
            nickname: string;
        };
        urls: string[];
    } 으로 자동 추론
*/

//객체 구조 분해에도 추론
let {id: var1, name: var2, profile: var3} = c;
/*
    let var1: number
    let var2: string
    let var3: {
        nickname: string;
    }
    으로 자동 추론
*/

//배열 구조 분해에도 추론
let [var4, var5, var6] = [1, "hello~!", null];
/*
    let var4: number;
    let var5: string;
    let var6: null;
*/

//함수 반환값에도 추론
function func1() {
    return "hello";
}
/*
    function func1(): string
    으로 추론
*/

//함수 기본값 설정된 매개변수에도 추론
function func2(message="default message") {
    return "hello";
}
/*
    function func2(message?: string): string
    으로 추론
*/

//암묵적인 any 타입
let d; // 이건 any 타입으로 추론 (암묵적인 any 타입)

d = 1; // 이제 number 타입으로 다시 추론
d.toFixed(); //number 이기에 가능
// d.toUpperCase(); //number 이기에 불가능

d = "hello"; // 이제 string 타입으로 다시 추론
d.toUpperCase(); //number 이기에 가능
// d.toFixed(); //number 이기에 불가능

//const
const num = 10; //number literal 타입으로 추론 (const는 상수이기에)
const str = "hello"; //string literal 타입으로 추론 (const는 상수이기에)

//배열
let arr = [1, "string"]; //(number | string)[] 으로 추론