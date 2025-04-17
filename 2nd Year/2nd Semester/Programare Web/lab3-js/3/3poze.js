document.addEventListener("DOMContentLoaded", () => {
    const rows = 4;
    const cols = 4;

    const imagePaths = [
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Boian-Rares-133x100.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Bufnea-Darius.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Suciu-Dan.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Serban-Camelia.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Diana-Halita.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Vancea-Alexandru.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Alexandru-Kiraly.jpg',
        'https://www.cs.ubbcluj.ro/wp-content/uploads/Ioan-Daniel-Pop.jpg'
    ];

    let images = [...imagePaths, ...imagePaths]; // Duplicate the images for pairs

    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }
    shuffle(images);

    const gameBoard = document.getElementById("game-board");
    const table = document.createElement("table");
    let index = 0;

    for (let i = 0; i < rows; i++) {
        const tr = document.createElement("tr");
        for (let j = 0; j < cols; j++) {
            const td = document.createElement("td");
            td.dataset.image = images[index];
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

        cell.innerHTML = `<img src="${cell.dataset.image}" alt="img" width="50" height="50">`;
        cell.classList.add('revealed');

        if (!firstCard) {
            firstCard = cell;
        } else if (!secondCard && cell !== firstCard) {
            secondCard = cell;

            if (firstCard.dataset.image === secondCard.dataset.image) {
                firstCard = null;
                secondCard = null;
                checkWin();
            } else {
                lockBoard = true;
                setTimeout(() => {
                    firstCard.innerHTML = '';
                    secondCard.innerHTML = '';
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