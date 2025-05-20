$(function () {
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

    shuffle(numbers)

    const $table = $("<table>");
    let index = 0;

    for (let i = 0; i < rows; i++) {
        const $row = $("<tr>");
        for (let j = 0; j < cols; j++) {
            const $cell = $("<td>")
                .data("value", numbers[index])
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

    function handleCellClick(){
        const $cell = $(this);

        if ($cell.hasClass("revealed") || lockBoard){
            return;
        }

        $cell.text($cell.data("value")).addClass("revealed");

        if (!$firstCard){
            $firstCard = $cell;
        } else if (!$secondCard && $cell[0] !== $firstCard[0]){
            $secondCard = $cell;

            if ($firstCard.data("value") === $secondCard.data("value")){
                $firstCard = null;
                $secondCard = null;
                checkWin();
            } else {
                lockBoard = true;
                setTimeout(() => {
                    $firstCard.text("").removeClass("revealed");
                    $secondCard.text("").removeClass("revealed");
                    $firstCard = null;
                    $secondCard = null;
                    lockBoard = false;
                }, 500);
            }
        }
    }

    function checkWin(){
        if ($("td").not(".revealed").length === 0){
            setTimeout(() => {
                alert("Congratulations! You've matched all pairs!");
            }, 1000);
        }
    }
});