import React from "react";

// export default class MyComponent extends React.Component {
//   render() {
//     this.props.name = "Lee";
//     // TypeError: Cannot assign to read only property 'name' of object '#<Object>'
//     return (
//       <div>
//         <h1>name: {this.props.name}</h1>
//         <h1>age: {this.props.age}</h1>
//       </div>
//     );
//   }
// }
function MyComponent(props) {
  let origin = { num: 1 };
  let updated = Object.assign(
    origin,
    { num: num + 1 },
    { num: num + 1 },
    { num: num + 1 }
  );
  return <button>button</button>;
}

MyComponent.defaultProps = {
  text: "This is default text",
};

export default MyComponent;
