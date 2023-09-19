//Unknown 타입
//Unknown 타입은 모든 타입의 슈퍼 타입이기 때문에, 모든 타입에 대해 업캐스팅 가능
function unknownExam() {
    //업캐스팅
    let a: unknown = 1;
    let b: unknown = "hello";
    let c: unknown = true;
    let d: unknown = null;
    let e: unknown = () => {};
    let f: unknown = undefined;

    let unknownVar: unknown;
    //다운캐스팅 (불가능)
    //let num: number = unknownVar;
    //let str: string = unknownVar;
    //let bool: boolean = unknownVar;
}

//never 타입
function neverExam() {
    function neverFunc(): never {
        while (true) {}
    }

    let num: number = neverFunc(); //업캐스팅
    let str: string = neverFunc(); //업캐스팅
    let bool: boolean = neverFunc(); //업캐스팅

    //다운캐스팅 (불가능)
    // let never1: never = 10;
    // let never2: never = "hello";
    // let never3: never = true;
    // let never4: never = null;
}

// void 타입
function voidExam() {
    function voidFunc(): void {
        console.log("hi");
    }

    //업캐스팅
    let voidVar: void = undefined;
}

// any 타입
function anyExam() {
    let unknownVar: unknown;
    let anyVar: any;
    let undefinedVar: undefined;
    let neverVar: never;

    //다운캐스팅 -> any 타입이라면 가능 (any를 다운캐스팅하거나, any로 다운캐스팅하는 것 모두 가능)
    anyVar = unknownVar;
    undefinedVar = anyVar;

    //단 never는 불가능
    // neverVar = anyVar;
}