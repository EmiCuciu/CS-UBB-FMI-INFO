import React from "react";
import UpdateQuestionForm from "./AppConfigurationForm";

function App() {
  return (
      <div className="app">
        <header className="app-header">
          <h1>Trivia Game Admin</h1>
        </header>
        <main>
          <UpdateQuestionForm />
        </main>
      </div>
  );
}

export default App;