// 서로소 유니온 타입

type Admin = {
    tag: "ADMIN"; //string literal
    name: string;
    kickCount: number;
};
type Member = {
    tag: "MEMBER"; //string literal
    name: string;
    point: number;
};
type Guest = {
    tag: "GUEST"; //string literal
    name: string;
    visitCount: number;
};

type User = Admin | Member | Guest; //유니온 타입

// Admin -> {name}님 현재까지 {kickCount}명 강퇴했습니다.
// Member -> {name}님 현재까지 {point} 모았습니다.
// Guest -> {name}님 현재까지 {visitCount}번 오셨습니다.
function login1(user: User) {
    if ('kickCount' in user) { //Admin 타입 가드 (직관적이지 않음)
        console.log(`${user.name}님 현재까지 ${user.kickCount}명 강퇴했습니다.`);

    } else if ('point' in user) { //Member 타입 가드 (직관적이지 않음)
        console.log(`${user.name}님 현재까지 ${user.point} 모았습니다.`);

    } else { //Guest 타입 가드 (직관적이지 않음)
        console.log(`${user.name}님 현재까지 ${user.visitCount}번 방문했습니다.`);
    }
}

//개선 1
function login2(user: User) {
    if (user.tag === "ADMIN") { //Admin 타입 가드 (직관적)
        console.log(`${user.name}님 현재까지 ${user.kickCount}명 강퇴했습니다.`);
        
    } else if (user.tag === "MEMBER") { //Member 타입 가드 (직관적)
        console.log(`${user.name}님 현재까지 ${user.point} 모았습니다.`);

    } else { //Guest 타입 가드 (직관적)
        console.log(`${user.name}님 현재까지 ${user.visitCount}번 방문했습니다.`);
    }
}

//개선 2
function login3(user: User) {
    switch (user.tag) {
        case "ADMIN": {
            console.log(`${user.name}님 현재까지 ${user.kickCount}명 강퇴했습니다.`);
            break;
        }
        case "MEMBER": {
            console.log(`${user.name}님 현재까지 ${user.point} 모았습니다.`);
            break;
        }
        case "GUEST": {
            console.log(`${user.name}님 현재까지 ${user.visitCount}번 방문했습니다.`);
            break;
        }

    }
}