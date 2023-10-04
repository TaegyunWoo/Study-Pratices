// 맵드 타입

interface User {
  id: number;
  name: string;
  age: number;
}

// 한명의 유저 정보를 불러오는 기능
function fetchUser(): User {
  //... 기능
  return {
    id: 1,
    name: "이정환",
    age: 27,
  };
}

// 한명의 유저 정보를 수정하는 기능
function updateUser(user: PartialUser1) {
  //...수정하는 기능
}

//문제점: age 프로퍼티의 값만 바꿀건데, 모든 프로퍼티를 다 전달해줘야함.
updateUser({
  age: 25, //새값
});

//아래와 같이 수정 가능
type PartialUser1 = {
  [key in "id" | "name" | "age"]?: User[key];
};

type PartialUser2 = {
  [key in keyof User]?: User[key];
};
