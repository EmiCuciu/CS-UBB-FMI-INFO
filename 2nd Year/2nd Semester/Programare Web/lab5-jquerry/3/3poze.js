$(function() {
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

    let images = [...imagePaths, ...imagePaths];

    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
    }
    shuffle(images);

    const $table = $("<table>");
    let index = 0;

    for (let i = 0; i < rows; i++) {
        const $row = $("<tr>");
        for (let j = 0; j < cols; j++) {
            const $cell = $("<td>")
                .data("image", images[index])
                .on("click", handleCellClick);
            $row.append($cell);
            index++;
        }
        $table.append($row);
    }

    $("#game-board").append($table);

    let $firstCard = null;
    let $secondCard = null;
    let lockBoard = false;

    function handleCellClick() {
        const $cell = $(this);

        if ($cell.hasClass("revealed") || lockBoard) {
            return;
        }

        $cell.html(`<img src="${$cell.data("image")}" alt="img" width="50" height="50">`);
        $cell.addClass("revealed");

        if (!$firstCard) {
            $firstCard = $cell;
        } else if (!$secondCard && $cell[0] !== $firstCard[0]) {
            $secondCard = $cell;

            if ($firstCard.data("image") === $secondCard.data("image")) {
                // Match found
                $firstCard = null;
                $secondCard = null;
                checkWin();
            } else {
                // No match
                lockBoard = true;
                setTimeout(() => {
                    $firstCard.html("").removeClass("revealed");
                    $secondCard.html("").removeClass("revealed");
                    $firstCard = null;
                    $secondCard = null;
                    lockBoard = false;
                }, 500);
            }
        }
    }

    function checkWin() {
        if ($("td").not(".revealed").length === 0) {
            setTimeout(() => {
                alert("Congratulations! You've matched all pairs!");
            }, 1000);
        }
    }
});