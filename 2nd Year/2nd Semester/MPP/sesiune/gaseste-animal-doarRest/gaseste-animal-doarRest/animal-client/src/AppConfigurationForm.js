import React, {useState} from "react";

function AppConfigurationForm() {
    const [linie, setLinie] = useState("");
    const [coloana, setColoana] = useState("");
    const [animal, setAnimal] = useState("");
    const [urlAnimal, setUrlAnimal] = useState("");
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch("http://localhost:8080/api/configuratii", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    linie: Number(linie),
                    coloana: Number(coloana),
                    animal,
                    url_animal: urlAnimal
                })
            });

            if (response.ok) {
                setMessage("Configurația a fost adăugată.");
                setLinie("");
                setColoana("");
                setAnimal("");
                setUrlAnimal("");

            }
            else {
                setMessage("Eroare la adăugare.");
            }
        }
        catch (err) {
            setMessage("Serverul nu a răspuns.");
        }
    };

    return React.createElement("div", null,
        React.createElement("h2", null,"Adaugă configurație"),
        React.createElement("form", { onSubmit: handleSubmit },

            React.createElement("input", {
                type: "number",
                placeholder: "Linie (1-3)",
                value: linie,
                onChange: (e) => setLinie(e.target.value),
                required: true
            }),


            React.createElement("input", {
                type: "number",
                placeholder: "Coloană (1-4)",
                value: coloana,
                onChange: (e) => setColoana(e.target.value),
                required: true
            }),

            React.createElement("input", {
                type: "text",
                placeholder: "Animal",
                value: animal,
                onChange: (e) => setAnimal(e.target.value),
                required: true
            }),

            React.createElement("input", {
                type: "text",
                placeholder: "URL Imagine",
                value: urlAnimal,
                onChange: (e) => setUrlAnimal(e.target.value),
                required: true
            }),

            React.createElement("button", {
                type: "submit"
            }, "Adaugă")

            ),

        message && React.createElement("p", null, message)
        );
}

export default AppConfigurationForm;