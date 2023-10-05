// 맵드 타입 기반
// 1. Parital<T>
// -> 특정 객체 타입의 모든 프로퍼티를 선택적 프로퍼티로 바꿔주는 타입
interface Post {
  title: string;
  tags: string[];
  content: string;
  thumbnailURL?: string;
}
const draft: Partial<Post> = {
  title: "제목 나중에 짓자",
  content: "초안...",
};
//Partial<T> 구현
type Partial<T> = {
  [key in keyof T]?: T[key];
};

// 2. Required<T>
// 특정 객체 타입의 모든 프로퍼티를 필수 프로퍼티로 바꿔주는 타입
const withThumbnailPost: Required<Post> = {
  title: "한입 타스 후기",
  tags: ["ts"],
  content: "",
  thumbnailURL: "https://...",
};
//Required<T> 구현
type Required<T> = {
  [key in keyof T]-?: T[key];
};

// 3. Readonly<T>
// 특정 객체 타입의 모든 프로퍼티를 readonly로 바꿔주는 타입
const readonlyPost: Readonly<Post> = {
  title: "보호된 게시글입니다.",
  tags: [],
  content: "",
};
// readonlyPost.title = "새로운 제목"; 불가능
//Readonly<T> 구현
type Readonly<T> = {
  readonly [key in keyof T]: T[key];
};

// 4. Pick<T, K>
// 객체 타입으로부터 특정 프로퍼티만 골라내는 타입
const legacyPost: Pick<Post, "title" | "content"> = {
  title: "옛날 글",
  content: "옛날 컨텐츠",
};
//Pick<T, K> 구현
type Pick<T, K extends keyof T> = {
  [key in K]: T[key];
};

// 5. Omit<T, K>
// 객체 타입으로부터 특정 프로퍼티만 제거하는 타입
const noTitlePost: Omit<Post, "title"> = {
  content: "",
  tags: [],
  thumbnailURL: "",
};
//Omit<T, K> 구현
type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;
// T = Post, K = "title"
// Pick<Post, Exclude<keyof Post, "title">>
// Pick<Post, Exclude<'title' | 'content' | 'tags' | 'thumbnailURL', 'title'>>

// 6. Record<K, V>
// K로 들어온 타입을 프로퍼티, V는 모든 프로퍼티의 타입으로 갖는 객체 타입을 정의
type ThumbnailLegacy = {
  large: {
    url: string;
  };
  medium: {
    url: string;
  };
  small: {
    url: string;
  };
};
type Thumbnail = Record<"large" | "medium" | "smill", { url: string }>;
//Record<K, V> 구현
type Record<K extends keyof any, V> = {
    [key in K]: V;
}