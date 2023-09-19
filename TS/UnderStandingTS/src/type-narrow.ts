// 타입 좁히기
type Person = {
    name: string;
    age: number;
}

function func(value: number | string | Date | null | Person) {
    // value.toUpperCase(); //불가능 (Union 타입이기에)
    // value.toFixed(); //불가능 (Union 타입이기에)

    if (typeof value === 'number') {
        console.log(value.toFixed()); //조건문 덕분에 number 타입으로 추론

    } else if (typeof value === 'string') {
        console.log(value.toUpperCase()); //조건문 덕분에 string 타입으로 추론

    } else if (value instanceof Date) { //클래스 확인 -> instanceof
				console.log(value.getTime()); //조건문 덕분에 Date 객체 타입으로 추론
                
    } else if (value && "age" in value) { //value는 null이 아니고, value 안에 age라는 프로퍼티가 있냐
                console.log(`${value.name}은 ${value.age}살 입니다.`);
    }
}