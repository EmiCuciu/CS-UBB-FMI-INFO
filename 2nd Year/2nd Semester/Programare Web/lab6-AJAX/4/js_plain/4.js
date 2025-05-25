document.addEventListener('DOMContentLoaded', function() {
    var celule = document.querySelectorAll('td');
    var gameStatus = document.createElement('div');
    gameStatus.id = 'status';
    document.body.appendChild(gameStatus);

    var resetButton = document.createElement('button');
    resetButton.textContent = 'Joc nou';
    resetButton.style.marginTop = '10px';
    document.body.appendChild(resetButton);

    // Initialize the game state
    initializeGame();

    function initializeGame() {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'joaca.php', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                var stare = JSON.parse(xhr.responseText);
                updateBoard(stare);

                if (stare.computerFirst) {
                    gameStatus.textContent = 'Calculatorul începe primul!';
                } else {
                    gameStatus.textContent = 'Tu începi primul! Fă o mutare.';
                }
            }
        };
        xhr.send('newGame=1');
    }

    celule.forEach(function(celula) {
        celula.addEventListener('click', function() {
            if (this.textContent !== '') return; // Cell already taken

            var pozitie = this.dataset.poz;
            var xhr = new XMLHttpRequest();
            xhr.open('POST', 'joaca.php', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var stare = JSON.parse(xhr.responseText);
                    updateBoard(stare);

                    if (stare.castigator) {
                        gameStatus.textContent = 'Câștigător: ' + stare.castigator + '!';
                    } else if (stare.remiza) {
                        gameStatus.textContent = 'Joc terminat: Remiză!';
                    }
                }
            };
            xhr.send('pozitie=' + pozitie);
        });
    });

    resetButton.addEventListener('click', initializeGame);

    function updateBoard(stare) {
        celule.forEach(function(c, i) {
            c.textContent = stare.tabla[i];
        });
    }
});