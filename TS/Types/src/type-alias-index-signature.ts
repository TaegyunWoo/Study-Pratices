//타입 별칭이 없다면...
let user1: {
    id: number;
    name: string;
    nickname: string;
    birth: string;
    bio: string;
    location: string;
} = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
};

let user2: { //동일한 타입이 중복되어, 매우 코드가 길어짐
    id: number;
    name: string;
    nickname: string;
    birth: string;
    bio: string;
    location: string;
} = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
};

//타입 별칭 적용
type User = { //타입 별칭 정의
    id: number;
    name: string;
    nickname: string;
    birth: string;
    bio: string;
    location: string;
}
let user3: User = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
}
let user4: User = {
    id: 1,
    name: "이정환",
    nickname: "winterlood",
    birth: "1997.01.07",
    bio: "안녕하세요",
    location: "부천시"
}

//인덱스 시그니처가 없다면...
type CountryCodes = {
    Korea: string;
    UnitedState: string;
    UnitedKingdom: string;
    //나라가 추가됨에 따라, 무수히 많은 프로퍼티를 정의해야함.
}
let countryCodes: CountryCodes = {
    Korea: 'ko',
    UnitedState: 'us',
    UnitedKingdom: 'uk'
}

//인덱스 시그니처 적용
type IndexedCountryCodes = {
    [key : string] : string; //key가 string이고, value도 string이다.
};
let countryCodes2: IndexedCountryCodes = {
    Korea: 'ko',
    UnitedState: 'us',
    UnitedKingdom: 'uk'
};

//인덱스 시그니처 사용시 주의점 1
let countryCodes3: IndexedCountryCodes = {
    //프로퍼티가 비어있음에도 오류 발생 X (인덱스 시그니처는 존재하는 프로퍼티가 해당 규칙을 만족하기만 하면 정상처리됨)
};

//따라서 필수적인 프로퍼티는 아래처럼 정의
type HaveKorCountryCode = {
    [key : string] : string;
    Korea: string; //반드시 Korea 프로퍼티와 string형 값이 존재해야함
}
let countryCodes4: HaveKorCountryCode = {
    Korea: "kor"
}

//인덱스 시그니처 사용시 주의점 2
type NotMatchedType = {
    [key : string] : number;
    //Korea: string; 이렇게 인덱스 시그니처와 매치되지 않는 경우는 불가능
}