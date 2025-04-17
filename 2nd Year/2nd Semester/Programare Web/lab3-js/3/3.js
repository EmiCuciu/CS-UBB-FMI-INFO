document.addEventListener("DOMContentLoaded", () => {
    const rows = 4;
    const cols = 4;

    let numbers = [];
    for (let i = 1; i <= (rows * cols) / 2; i++) {
        numbers.push(i);
        numbers.push(i);
    }

    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }
    shuffle(numbers);

    const gameBoard = document.getElementById("game-board");
    const table = document.createElement("table");
    let index = 0;

    for (let i = 0; i < rows; i++) {
        const tr = document.createElement("tr");
        for (let j = 0; j < cols; j++) {
            const td = document.createElement("td");
            td.dataset.value = numbers[index];
            td.textContent = '';
            td.addEventListener("click", handleCellClick);
            tr.appendChild(td);
            index++;
        }
        table.appendChild(tr);
    }
    gameBoard.appendChild(table);

    let firstCard = null;
    let secondCard = null;
    let lockBoard = false;

    function handleCellClick(event) {
        const cell = event.target;

        if (cell.classList.contains('revealed') || lockBoard) {
            return;
        }

        cell.textContent = cell.dataset.value;
        cell.classList.add('revealed');

        if (!firstCard) {
            firstCard = cell;
        } else if (!secondCard && cell !== firstCard) {
            secondCard = cell;

            if (firstCard.dataset.value === secondCard.dataset.value) {
                firstCard = null;
                secondCard = null;
                checkWin();
            } else {
                lockBoard = true;
                setTimeout(() => {
                    firstCard.textContent = '';
                    secondCard.textContent = '';
                    firstCard.classList.remove('revealed');
                    secondCard.classList.remove('revealed');
                    firstCard = null;
                    secondCard = null;
                    lockBoard = false;
                }, 500);
            }
        }
    }

    function checkWin() {
        const allCells = document.querySelectorAll("td");
        const allRevealed = Array.from(allCells).every(cell => cell.classList.contains('revealed'));

        if (allRevealed) {
            setTimeout(() => {
                alert("Congratulations! You've matched all pairs!");
                resetGame();
            }, 1000);
        }
    }
});