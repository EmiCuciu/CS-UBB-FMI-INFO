import React, { useState } from "react";

function UpdateConfigurationForm() {
    const [configId, setConfigId] = useState("");
    const [words, setWords] = useState("");
    const [result, setResult] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const wordList = words.split(",").map(w => w.trim());
        try {
            // Updated URL to include port 8080 where Spring Boot is running
            const response = await fetch(`http://localhost:8080/api/configuratii/${configId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ id: Number(configId), words: wordList })
            });
            if (response.ok) {
                const data = await response.json();
                setResult(data);
            } else {
                setResult("Update failed");
            }
        } catch (err) {
            setResult("Error: " + err.message);
        }
    };

    // Rest of component remains the same
    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Configuration ID: </label>
                <input
                    type="number"
                    value={configId}
                    onChange={e => setConfigId(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Words (comma separated): </label>
                <input
                    type="text"
                    value={words}
                    onChange={e => setWords(e.target.value)}
                    required
                />
            </div>
            <button type="submit">Update Configuration</button>
            {result && (
                <div>
                    <strong>Result:</strong> {typeof result === "string" ? result : JSON.stringify(result)}
                </div>
            )}
        </form>
    );
}

export default UpdateConfigurationForm;