"use strict";
class UserStorage {
  loginUser(id, password, onSuccess, onError) {
    setTimeout(() => {
      if (
        (id === "ellie" && password === "dream") ||
        (id === "coder" && password === "academy")
      ) {
        onSuccess(id);
      } else {
        onError(new Error("not found"));
      }
    }, 2000);
  }

  getRoles(user, onSuccess, onError) {
    setTimeout(() => {
      if (user === "ellie") {
        onSuccess({ name: "ellie", role: "admin" });
      } else {
        onError(new Error("no access"));
      }
    }, 1000);
  }
}

const userStorage = new UserStorage();
const id = "ellie";
const pw = "dream";
userStorage.loginUser(
  id,
  pw,
  (id) => {
    //로그인 성공시 (1차 콜백)
    userStorage.getRoles(
      id,
      (userWithRole) => {
        //role 성공시 (2차 콜백)
        console.log(`${userWithRole.name} your role is ${userWithRole.role}`);
      },
      (err) => {
        //role 실패시 (2차 콜백)
        console.log(err);
      }
    );
  },
  (err) => {
    //로그인 실패시 (1차 콜백)
    console.log(err);
  }
);

//위 콜백 지옥을 Promise로 해결 가능
class BetterUserStorage {
  loginUser(id, password) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (
          (id === "ellie" && password === "dream") ||
          (id === "coder" && password === "academy")
        ) {
          resolve(id);
        } else {
          reject(new Error("not found"));
        }
      }, 2000);
    });
  }

  getRoles(user) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (user === "ellie") {
          resolve({ name: "ellie", role: "admin" });
        } else {
          reject(new Error("no access"));
        }
      }, 1000);
    });
  }
}

const betterUserStorage = new BetterUserStorage();
betterUserStorage
  .loginUser(id, pw)
  .then((id) => betterUserStorage.getRoles(id)) //loginUser 함수가 성공한 경우, 실행할 콜백함수 전달
  .then((loginUser) => console.log(loginUser)) //위 콜백함수가 성공한 경우, 실행할 콜백함수
  .catch((err) => console.log(err));
