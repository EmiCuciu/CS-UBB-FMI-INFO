$(document).ready(function() {
    var $status = $('<div id="status"></div>').appendTo('body');
    var $resetButton = $('<button>Joc nou</button>')
        .css('marginTop', '10px')
        .appendTo('body');

    initializeGame();

    function initializeGame() {
        $.ajax({
            url: 'joaca.php',
            type: 'POST',
            data: { newGame: 1 },
            dataType: 'json',
            success: function(stare) {
                updateBoard(stare);

                if (stare.computerFirst) {
                    $status.text('Calculatorul începe primul!');
                } else {
                    $status.text('Tu începi primul! Fă o mutare.');
                }
            }
        });
    }

    $('td').click(function() {
        if ($(this).text() !== '') return; // Cell already taken

        var pozitie = $(this).data('poz');
        $.ajax({
            url: 'joaca.php',
            type: 'POST',
            data: { pozitie: pozitie },
            dataType: 'json',
            success: function(stare) {
                updateBoard(stare);

                if (stare.castigator) {
                    $status.text('Câștigător: ' + stare.castigator + '!');
                } else if (stare.remiza) {
                    $status.text('Joc terminat: Remiză!');
                }
            }
        });
    });

    $resetButton.click(initializeGame);

    function updateBoard(stare) {
        $('td').each(function(i) {
            $(this).text(stare.tabla[i]);
        });
    }
});