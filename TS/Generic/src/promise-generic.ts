// 프로미스 기본
const promise = new Promise<number>((resolve, reject) => {
  setTimeout(() => {
    resolve(20);
    reject("~~ 때문에 실패");
  }, 3000);
});
promise.then((response) => {
  console.log(response * 10);
});
promise.catch((err) => {
  if (typeof err === "string") console.log(err); //반드시 명시적 타입 제한 필요
});

//프로미스를 반환하는 함수의 타입 정의 예시
interface Post {
  id: number;
  title: string;
  content: string;
}
function fetchPost(): Promise<Post> { //fetchPost 함수의 리턴타입 정의
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        id: 1,
        title: "게시글 제목",
        content: "게시글 컨텐츠",
      });
    }, 3000);
  });
}
const postRequest = fetchPost();
postRequest.then((post) => {
  post.id;
});
