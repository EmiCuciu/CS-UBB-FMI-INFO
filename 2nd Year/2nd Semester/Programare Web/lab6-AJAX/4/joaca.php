<?php
session_start();

if (!isset($_SESSION['tabla']) || isset($_POST['newGame'])) {
    $_SESSION['tabla'] = array_fill(0, 9, '');
    $_SESSION['computerFirst'] = rand(0, 1) == 1;

    if ($_SESSION['computerFirst']) {
        $randomPos = rand(0, 8);
        $_SESSION['tabla'][$randomPos] = '0';
    }
}

if (isset($_POST['pozitie']) && !isset($_POST['newGame'])) {
    $poz = (int)$_POST['pozitie'];

    if ($poz >= 0 && $poz < 9 && $_SESSION['tabla'][$poz] === '') {
        $_SESSION['tabla'][$poz] = 'X';

        $winner = checkWinner($_SESSION['tabla']);

        if (!$winner && !isBoardFull($_SESSION['tabla'])) {
            makeComputerMove();
            $winner = checkWinner($_SESSION['tabla']);
        }

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

echo json_encode([
    'tabla' => $_SESSION['tabla'],
    'castigator' => checkWinner($_SESSION['tabla']),
    'remiza' => isBoardFull($_SESSION['tabla']),
    'computerFirst' => $_SESSION['computerFirst']
]);

function checkWinner($board) {
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
    $winningMove = findWinningMove($_SESSION['tabla'], '0');
    if ($winningMove !== false) {
        $_SESSION['tabla'][$winningMove] = '0';
        return;
    }

    $blockingMove = findWinningMove($_SESSION['tabla'], 'X');
    if ($blockingMove !== false) {
        $_SESSION['tabla'][$blockingMove] = '0';
        return;
    }

    if ($_SESSION['tabla'][4] === '') {
        $_SESSION['tabla'][4] = '0';
        return;
    }

    $corners = [0, 2, 6, 8];
    shuffle($corners);
    foreach ($corners as $corner) {
        if ($_SESSION['tabla'][$corner] === '') {
            $_SESSION['tabla'][$corner] = '0';
            return;
        }
    }

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
