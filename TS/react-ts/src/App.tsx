import { useRef, useState, useEffect } from "react";
import "./App.css";
import Editor from "./components/Editor";

interface Todo {
  id: number;
  content: string;
}

function App() {
  const [todos, setTodos] = useState<Todo[]>([]); //현재 컴포넌트 내부에서 사용되는 상태값 (리렌더링)
  const idRef = useRef(0); //현재 컴포넌트 전체에서 범용적으로 사용되는 값

  //이벤트 핸들러 콜백 함수 정의
  const onClickAdd = (text: string) => {
    setTodos([
      ...todos,
      {
        id: idRef.current++,
        content: text,
      },
    ]);
  };

  //이벤트 핸들러 콜백 함수 정의
  const onChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setText(e.target.value);
  };

  //두번째 인자로 전달된 값이 변경될 때마다, 첫번째 콜백 함수 실행
  useEffect(() => {
    console.log(todos);
  }, [todos]);

  return (
    <div className="App">
      <h1>Todo</h1>
      <Editor onClickAdd={onClickAdd} />
    </div>
  );
}

export default App;
