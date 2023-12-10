import logo from './logo.svg';
import './App.css';
import MyComponent from './MyComponent';

function App() {
  const myComponent = <MyComponent text="old" />;
  console.log(myComponent.props.text);
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
      {myComponent}
    </div>
  );
}

export default App;
