"user strict";

/* [Promise의 상태 변화]
    - pending 상태: 우리가 지정한 작업을 수행 중인 경우
    - fulfilled 상태: 지정된 작업을 성공한 경우
    - refected 상태: 지정된 작업을 실패한 경우
*/

/* [Promise 구성 요소]
    - Producer: 데이터 생산자
    - Consumer: 데이터 소비자
*/

//1. Producer
//새로운 Promise 객체 생성 시, 전달한 콜백함수가 바로 실행된다.
const promise = new Promise((resolve, reject) => {
  //Promise로 실행할 콜백함수
  console.log("doing something...");
  setTimeout(() => {
    resolve("ellie"); //then()으로 넘겨받은 콜백함수 실행
    // reject(new Error("no network")); //catch()로 넘겨받은 콜백함수 실행
  }, 2000);
});

//2. Consumer: then, catch, finally
promise
  .then((value) => `${value} 1, `)
  .then((value) => `${value} 2, `)
  .then((value) => `${value} 3, `)
  .then(console.log)
  .catch((err) => {
    console.log(err);
  })
  .finally(() => console.log("the end"));
