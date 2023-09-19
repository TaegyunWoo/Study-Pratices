//숫자형 Enum
enum Role {
    ADMIN = 0, //Enum이름 = 대응값
    USER = 1,
    GUEST = 2
}

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
enum Role1 {
    ADMIN, //대응되는 값은 여기서부터 0
    USER, //1
    GUEST //2로 자동 할당됨
}

//enum 대응값 시작점 수정
enum Role2 {
    ADMIN = 10,
    USER, //11
    GUEST //12로 자동 할당됨
}

//문자열형 Enum
enum Language {
    KOREAN = "ko",
    ENGLISH = "en",
}
const user4 = {
    name: "홍길동",
    lang: Language.KOREAN
}