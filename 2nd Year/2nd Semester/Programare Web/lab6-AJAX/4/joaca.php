<?php
session_start();

// Initialize game if not set or a new game is requested
if (!isset($_SESSION['tabla']) || isset($_POST['newGame'])) {
    $_SESSION['tabla'] = array_fill(0, 9, '');
    $_SESSION['computerFirst'] = rand(0, 1) == 1; // 50% chance computer starts

    // If computer starts, make first move
    if ($_SESSION['computerFirst']) {
        $randomPos = rand(0, 8);
        $_SESSION['tabla'][$randomPos] = '0';
    }
}

// Process player move if position was sent and valid
if (isset($_POST['pozitie']) && !isset($_POST['newGame'])) {
    $poz = (int)$_POST['pozitie'];

    // Check if the position is valid and empty
    if ($poz >= 0 && $poz < 9 && $_SESSION['tabla'][$poz] === '') {
        $_SESSION['tabla'][$poz] = 'X';

        // Check for winner after player move
        $winner = checkWinner($_SESSION['tabla']);

        // Computer makes a move if no winner and board not full
        if (!$winner && !isBoardFull($_SESSION['tabla'])) {
            makeComputerMove();
            $winner = checkWinner($_SESSION['tabla']);
        }

        // Check for draw
        $isDraw = !$winner && isBoardFull($_SESSION['tabla']);

        echo json_encode([
            'tabla' => $_SESSION['tabla'],
            'castigator' => $winner,
            'remiza' => $isDraw,
            'computerFirst' => $_SESSION['computerFirst']
        ]);
        exit;
    }
}

// Return current game state
echo json_encode([
    'tabla' => $_SESSION['tabla'],
    'castigator' => checkWinner($_SESSION['tabla']),
    'remiza' => isBoardFull($_SESSION['tabla']),
    'computerFirst' => $_SESSION['computerFirst']
]);

// Functions
function checkWinner($board) {
    // Winning combinations: rows, columns, and diagonals
    $lines = [
        [0, 1, 2], [3, 4, 5], [6, 7, 8], // rows
        [0, 3, 6], [1, 4, 7], [2, 5, 8], // columns
        [0, 4, 8], [2, 4, 6]             // diagonals
    ];

    foreach ($lines as $line) {
        [$a, $b, $c] = $line;
        if ($board[$a] !== '' && $board[$a] === $board[$b] && $board[$a] === $board[$c]) {
            return $board[$a];
        }
    }

    return null;
}

function isBoardFull($board) {
    foreach ($board as $cell) {
        if ($cell === '') return false;
    }
    return true;
}

function makeComputerMove() {
    // Try to win
    $winningMove = findWinningMove($_SESSION['tabla'], '0');
    if ($winningMove !== false) {
        $_SESSION['tabla'][$winningMove] = '0';
        return;
    }

    // Block player from winning
    $blockingMove = findWinningMove($_SESSION['tabla'], 'X');
    if ($blockingMove !== false) {
        $_SESSION['tabla'][$blockingMove] = '0';
        return;
    }

    // Take center if available
    if ($_SESSION['tabla'][4] === '') {
        $_SESSION['tabla'][4] = '0';
        return;
    }

    // Take a corner if available
    $corners = [0, 2, 6, 8];
    shuffle($corners);
    foreach ($corners as $corner) {
        if ($_SESSION['tabla'][$corner] === '') {
            $_SESSION['tabla'][$corner] = '0';
            return;
        }
    }

    // Take any available spot
    for ($i = 0; $i < 9; $i++) {
        if ($_SESSION['tabla'][$i] === '') {
            $_SESSION['tabla'][$i] = '0';
            return;
        }
    }
}

function findWinningMove($board, $player) {
    $lines = [
        [0, 1, 2], [3, 4, 5], [6, 7, 8],
        [0, 3, 6], [1, 4, 7], [2, 5, 8],
        [0, 4, 8], [2, 4, 6]
    ];

    foreach ($lines as $line) {
        [$a, $b, $c] = $line;
        if ($board[$a] === $player && $board[$b] === $player && $board[$c] === '') return $c;
        if ($board[$a] === $player && $board[$c] === $player && $board[$b] === '') return $b;
        if ($board[$b] === $player && $board[$c] === $player && $board[$a] === '') return $a;
    }

    return false;
}
