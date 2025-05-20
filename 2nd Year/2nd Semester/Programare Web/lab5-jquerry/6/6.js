$(function() {
    const DEFAULT_SIZE = 4;
    let size = DEFAULT_SIZE;
    let board = [];
    let emptyCell = { row: 0, col: 0 };
    let moves = 0;
    let gameCompleted = false;

    function initGame() {
        const $puzzleContainer = $('#puzzle-table');
        $puzzleContainer.empty();

        const $table = $('<table>');
        $puzzleContainer.append($table);

        board = [];
        let counter = 1;

        for (let i = 0; i < size; i++) {
            board[i] = [];
            const $tr = $('<tr>');
            $table.append($tr);

            for (let j = 0; j < size; j++) {
                const $td = $('<td>');
                $tr.append($td);

                if (i === size - 1 && j === size - 1) {
                    board[i][j] = 0;
                    $td.addClass('empty');
                    $td.text('');
                    emptyCell = { row: i, col: j };
                } else {
                    board[i][j] = counter++;
                    $td.text(board[i][j]);
                }

                $td.on('click', function() {
                    if (!gameCompleted) {
                        const cellPosition = findCellPosition($(this));
                        if (isAdjacentToEmpty(cellPosition.row, cellPosition.col)) {
                            moveCell(cellPosition.row, cellPosition.col);
                        }
                    }
                });
            }
        }

        shuffleBoard();
        updateBoard();

        moves = 0;
        gameCompleted = false;
        $('#message').text('');
    }

    function findCellPosition($cell) {
        const row = $cell.parent().index();
        const col = $cell.index();
        return { row, col };
    }

    function isAdjacentToEmpty(row, col) {
        return (
            (row === emptyCell.row && Math.abs(col - emptyCell.col) === 1) ||
            (col === emptyCell.col && Math.abs(row - emptyCell.row) === 1)
        );
    }

    function moveCell(row, col) {
        board[emptyCell.row][emptyCell.col] = board[row][col];
        board[row][col] = 0;

        emptyCell = { row, col };

        updateBoard();

        moves++;

        if (checkSolution()) {
            gameCompleted = true;
            $('#message').text(`Puzzle solved in ${moves} moves!`);
        }
    }

    function updateBoard() {
        const $table = $('table');

        for (let i = 0; i < size; i++) {
            for (let j = 0; j < size; j++) {
                const $cell = $table.find('tr').eq(i).find('td').eq(j);

                if (board[i][j] === 0) {
                    $cell.text('');
                    $cell.addClass('empty');
                } else {
                    $cell.text(board[i][j]);
                    $cell.removeClass('empty');
                }
            }
        }
    }

    function shuffleBoard() {
        const initialBoard = [
            [3, 8, 9, 5],
            [7, 13, 6, 15],
            [10, 0, 14, 4],
            [2, 11, 1, 12]
        ];

        board = initialBoard;

        for (let i = 0; i < size; i++) {
            for (let j = 0; j < size; j++) {
                if (board[i][j] === 0) {
                    emptyCell = { row: i, col: j };
                    break;
                }
            }
        }
    }

    function solveBoard() {
        const solvedBoard = [
            [1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12],
            [13, 14, 15, 0]
        ];

        board = solvedBoard;
        emptyCell = { row: size - 1, col: size - 1 };

        updateBoard();
        gameCompleted = true;
        $('#message').text('Puzzle solved!');
    }

    function checkSolution() {
        let counter = 1;

        for (let i = 0; i < size; i++) {
            for (let j = 0; j < size; j++) {
                if (i === size - 1 && j === size - 1) {
                    if (board[i][j] !== 0) {
                        return false;
                    }
                } else if (board[i][j] !== counter++) {
                    return false;
                }
            }
        }

        return true;
    }

    $(document).on('keydown', function(event) {
        if (gameCompleted) return;

        switch (event.key) {
            case 'ArrowUp':
                if (emptyCell.row < size - 1) {
                    moveCell(emptyCell.row + 1, emptyCell.col);
                }
                break;
            case 'ArrowDown':
                if (emptyCell.row > 0) {
                    moveCell(emptyCell.row - 1, emptyCell.col);
                }
                break;
            case 'ArrowLeft':
                if (emptyCell.col < size - 1) {
                    moveCell(emptyCell.row, emptyCell.col + 1);
                }
                break;
            case 'ArrowRight':
                if (emptyCell.col > 0) {
                    moveCell(emptyCell.row, emptyCell.col - 1);
                }
                break;
        }

        if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(event.key)) {
            event.preventDefault();
        }
    });

    $('#new-game').on('click', initGame);
    $('#solve').on('click', solveBoard);

    initGame();
});