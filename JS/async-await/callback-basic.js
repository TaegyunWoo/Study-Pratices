'use strict';

console.log(1); //sync 동작
setTimeout(() => console.log(2)); //async 동작
console.log(3); //sync 동작

//동기 콜백
function printImmediately(print) {
    print();
}
printImmediately(() => console.log("sync hello"));

//비동기 콜백
function printWithDelay(print, timeout) {
    setTimeout(print, timeout);
}
printWithDelay(() => console.log("async hello"), 100);