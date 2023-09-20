//사용자 정의 타입가드

type Dog = {
    name: string;
    isBark: boolean;
};

type Cat = {
    name: string;
    isScratch: boolean;
};

type Animal = Dog | Cat;

//만약 animal 타입 객체 A를 전달했을 때, 해당 함수의 결과값이 참이라면 A는 Dog 타입이다.
function isDog(animal: Animal): animal is Dog {
    return (animal as Dog).isBark !== undefined;
}

//만약 animal 타입 객체 A를 전달했을 때, 해당 함수의 결과값이 참이라면 A는 Dog 타입이다.
function isCat(animal: Animal): animal is Cat {
    return (animal as Cat).isScratch !== undefined;
}

function warning(animal: Animal) {
    if (isDog(animal)) {
        animal; //Dog로 추론됨
    } else if (isCat(animal)) {
        animal; //Cat으로 추론됨
    }
}