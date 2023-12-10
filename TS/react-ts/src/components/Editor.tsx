import { useState } from "react";

export default function Editor() {
  const [text, setText] = useState(""); //현재 컴포넌트 전체에서 사용되는 상태값 (리렌더링)
  //이벤트 핸들러 콜백 함수 정의
  const onChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setText(e.target.value);
  };
  const onClickButton = () => {};

  return (
    <div>
      <input value={text} onChange={onChangeInput} />
      <button onClick={onClickButton}>추가</button>
    </div>
  );
}
