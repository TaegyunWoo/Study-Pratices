// 인덱스드 엑세스 타입
type PostList = {
  title: string;
  content: string;
  author: {
    id: number;
    name: string;
    age: number;
  };
}[]; //배열타입

function printAuthorInfo(author: PostList[number]["author"]) {
  //인덱스드 엑세스 타입
  console.log(`${author.name} - ${author.id}`);
}

const post: PostList[number] = {
  title: "게시글 제목",
  content: "게시글 본문",
  author: {
    id: 1,
    name: "이정환",
    age: 27,
  },
};

printAuthorInfo(post.author);

type Tup = [number, string, boolean];
type Tup0 = Tup[0];
type Tup1 = Tup[1];
type Tup2 = Tup[2];
type TupNum = Tup[number]; //모든 타입의 유니온 타입
