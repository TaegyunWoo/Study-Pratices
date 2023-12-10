// Promise 사용시
function fetchUser() {
  return new Promise((resolve, reject) => {
    //do network request in 10 secs...
    resolve("ellie");
  });
}

fetchUser().then(console.log);

// 1. async 사용시 Promise를 자동으로 return함
async function fetchUserWithAsync() {
  return "ellie"; //실제 함수 로직은 Promise의 콜백함수로 들어감 ( resolve(return값) )
}

fetchUser().then(console.log);

// 2. await
// await은 async가 적용된 함수 내부에서만 사용 가능
function delay(timeout) {
    //'3초 뒤에 resolve 콜백함수를 실행하는 Promise 반환
    return new Promise((resolve) => setTimeout(resolve, timeout));
}
async function getApple() {
    await delay(3000); //3초 딜레이가 끝날때까지 대기
    return 'apple';
}