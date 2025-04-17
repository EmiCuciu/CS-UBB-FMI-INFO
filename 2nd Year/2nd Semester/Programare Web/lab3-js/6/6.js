const DEFAULT_SIZE = 4; // 4x4 puzzle by default
        let size = DEFAULT_SIZE;
        let board = [];
        let emptyCell = { row: 0, col: 0 };
        let moves = 0;
        let gameCompleted = false;
        
        // Initialize the game
        function initGame() {
            const puzzleContainer = document.getElementById('puzzle-table');
            puzzleContainer.innerHTML = '';
            
            // Create table
            const table = document.createElement('table');
            puzzleContainer.appendChild(table);
            
            // Initialize board with solved state first
            board = [];
            let counter = 1;
            
            for (let i = 0; i < size; i++) {
                board[i] = [];
                const tr = document.createElement('tr');
                table.appendChild(tr);
                
                for (let j = 0; j < size; j++) {
                    const td = document.createElement('td');
                    tr.appendChild(td);
                    
                    // Last cell is empty in solved state
                    if (i === size - 1 && j === size - 1) {
                        board[i][j] = 0; // 0 represents empty cell
                        td.classList.add('empty');
                        td.textContent = '';
                        emptyCell = { row: i, col: j };
                    } else {
                        board[i][j] = counter++;
                        td.textContent = board[i][j];
                    }
                    
                    // Add click event to move tile
                    td.addEventListener('click', () => {
                        if (!gameCompleted) {
                            const cellPosition = findCellPosition(td);
                            if (isAdjacentToEmpty(cellPosition.row, cellPosition.col)) {
                                moveCell(cellPosition.row, cellPosition.col);
                            }
                        }
                    });
                }
            }
            
            // Shuffle the board
            shuffleBoard();
            updateBoard();
            
            moves = 0;
            gameCompleted = false;
            document.getElementById('message').textContent = '';
        }
        
        // Find position of a cell in the table
        function findCellPosition(cell) {
            const row = cell.parentNode.rowIndex;
            const col = cell.cellIndex;
            return { row, col };
        }
        
        // Check if cell is adjacent to empty cell
        function isAdjacentToEmpty(row, col) {
            return (
                (row === emptyCell.row && Math.abs(col - emptyCell.col) === 1) ||
                (col === emptyCell.col && Math.abs(row - emptyCell.row) === 1)
            );
        }
        
        // Move a cell into the empty position
        function moveCell(row, col) {
            // Swap values in board
            board[emptyCell.row][emptyCell.col] = board[row][col];
            board[row][col] = 0;
            
            // Update empty cell position
            emptyCell = { row, col };
            
            // Update the board display
            updateBoard();
            
            // Increment moves counter
            moves++;
            
            // Check if puzzle is solved
            if (checkSolution()) {
                gameCompleted = true;
                document.getElementById('message').textContent = `Puzzle solved in ${moves} moves!`;
            }
        }
        
        // Update the visual representation of the board
        function updateBoard() {
            const table = document.querySelector('table');
            
            for (let i = 0; i < size; i++) {
                for (let j = 0; j < size; j++) {
                    const cell = table.rows[i].cells[j];
                    
                    if (board[i][j] === 0) {
                        cell.textContent = '';
                        cell.classList.add('empty');
                    } else {
                        cell.textContent = board[i][j];
                        cell.classList.remove('empty');
                    }
                }
            }
        }
        
        // Shuffle the board for a new game
        function shuffleBoard() {
            // Initialize with given example
            const initialBoard = [
                [3, 8, 9, 5],
                [7, 13, 6, 15],
                [10, 0, 14, 4],  // 0 represents the empty space
                [2, 11, 1, 12]
            ];
            
            board = initialBoard;
            
            // Find the empty cell position
            for (let i = 0; i < size; i++) {
                for (let j = 0; j < size; j++) {
                    if (board[i][j] === 0) {
                        emptyCell = { row: i, col: j };
                        break;
                    }
                }
            }
        }
        
        // Solve the puzzle (set to target solution)
        function solveBoard() {
            // The target solved state
            const solvedBoard = [
                [1, 2, 3, 4],
                [5, 6, 7, 8],
                [9, 10, 11, 12],
                [13, 14, 15, 0]  // 0 represents the empty space
            ];
            
            board = solvedBoard;
            emptyCell = { row: size - 1, col: size - 1 };
            
            updateBoard();
            gameCompleted = true;
            document.getElementById('message').textContent = 'Puzzle solved!';
        }
        
        // Check if the puzzle is solved
        function checkSolution() {
            let counter = 1;
            
            for (let i = 0; i < size; i++) {
                for (let j = 0; j < size; j++) {
                    // Skip the last cell which should be empty
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
        
        // Handle keyboard controls
        document.addEventListener('keydown', (event) => {
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
            
            // Prevent default scrolling behavior when using arrow keys
            if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(event.key)) {
                event.preventDefault();
            }
        });
        
        // Button event listeners
        document.getElementById('new-game').addEventListener('click', initGame);
        document.getElementById('solve').addEventListener('click', solveBoard);
        
        // Initialize game on page load
        window.addEventListener('load', initGame);