import React, { useState } from "react";

function UpdateQuestionForm() {
    const [questionId, setQuestionId] = useState("");
    const [questionText, setQuestionText] = useState("");
    const [result, setResult] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/intrebari/${questionId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ text: questionText })
            });

            if (response.ok) {
                const data = await response.json();
                setResult({success: true, data: data});
                setQuestionText("");
            } else {
                const errorText = await response.text();
                setResult({success: false, message: errorText});
            }
        } catch (err) {
            setResult({success: false, message: err.message});
        }
    };

    return (
        <div className="form-container">
            <h2>Update Question Text</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Question ID: </label>
                    <input
                        type="number"
                        value={questionId}
                        onChange={e => setQuestionId(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>New Question Text: </label>
                    <input
                        type="text"
                        value={questionText}
                        onChange={e => setQuestionText(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Update Question</button>

                {result && result.success && (
                    <div className="success-message">
                        <p>Question updated successfully!</p>
                    </div>
                )}

                {result && !result.success && (
                    <div className="error-message">
                        <p>Error: {result.message}</p>
                    </div>
                )}
            </form>
        </div>
    );
}

export default UpdateQuestionForm;