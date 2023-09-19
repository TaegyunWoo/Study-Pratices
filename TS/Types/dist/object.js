//object
let user = {
    id: 1,
    name: "이정환",
};
// user.id; -> TS는 object 타입이라는 것 이외의 정보를 몰라서, id 프로퍼티를 못찾는다고 오류 발생
//object literal
let user2 = {
    id: 1,
    name: "이정환"
};
//선택적으로 프로퍼티 사용 (Optional Property)
let user3 = {
    // id: 3, -> 해당 프로퍼티는 존재하지 않아도 된다.
    // id: "hello" -> 해당 프로퍼티가 존재한다면, 반드시 number 타입이어야 한다.
    name: "이정환"
};
//불변 프로퍼티
let config = {
    apiKey: "my api key",
};
export {};
// config.apiKey = "my fixed api key"; -> 불가능
