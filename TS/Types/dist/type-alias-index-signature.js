//타입 별칭이 없다면...
let user1 = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
};
let user2 = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
};
let user3 = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
};
let user4 = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
};
let countryCodes = {
    Korea: 'ko',
    UnitedState: 'us',
    UnitedKingdom: 'uk'
};
let countryCodes2 = {
    Korea: 'ko',
    UnitedState: 'us',
    UnitedKingdom: 'uk'
};
//인덱스 시그니처 사용시 주의점 1
let countryCodes3 = {
//프로퍼티가 비어있음에도 오류 발생 X (인덱스 시그니처는 존재하는 프로퍼티가 해당 규칙을 만족하기만 하면 정상처리됨)
};
let countryCodes4 = {
    Korea: "kor"
};
export {};
