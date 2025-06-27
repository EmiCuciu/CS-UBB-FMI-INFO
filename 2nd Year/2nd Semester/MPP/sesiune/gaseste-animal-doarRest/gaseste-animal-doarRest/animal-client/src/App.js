import React from "react";

import AppConfigurationForm from "./AppConfigurationForm";

function App() {

  return React.createElement("div", null,
      React.createElement("h1", null, "Joc - Adaugă Configurație"),
        React.createElement(AppConfigurationForm)
  );
}

export default App;
