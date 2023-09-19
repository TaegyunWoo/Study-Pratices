//숫자형 Enum
var Role;
(function (Role) {
    Role[Role["ADMIN"] = 0] = "ADMIN";
    Role[Role["USER"] = 1] = "USER";
    Role[Role["GUEST"] = 2] = "GUEST";
})(Role || (Role = {}));
const user1 = {
    name: "아무개1",
    role: Role.ADMIN
};
const user2 = {
    name: "아무개2",
    role: Role.USER
};
const user3 = {
    name: "아무개3",
    role: Role.GUEST
};
console.log(user1, user2, user3); // 결과: { name: '아무개1', role: 0 } { name: '아무개2', role: 1 } { name: '아무개3', role: 2 }
//enum 대응값 생략
var Role1;
(function (Role1) {
    Role1[Role1["ADMIN"] = 0] = "ADMIN";
    Role1[Role1["USER"] = 1] = "USER";
    Role1[Role1["GUEST"] = 2] = "GUEST"; //2로 자동 할당됨
})(Role1 || (Role1 = {}));
//enum 대응값 시작점 수정
var Role2;
(function (Role2) {
    Role2[Role2["ADMIN"] = 10] = "ADMIN";
    Role2[Role2["USER"] = 11] = "USER";
    Role2[Role2["GUEST"] = 12] = "GUEST"; //12로 자동 할당됨
})(Role2 || (Role2 = {}));
//문자열형 Enum
var Language;
(function (Language) {
    Language["KOREAN"] = "ko";
    Language["ENGLISH"] = "en";
})(Language || (Language = {}));
const user4 = {
    name: "홍길동",
    lang: Language.KOREAN
};
export {};
