function incarcaNod(nod, cale) {
    $.ajax({
        url: 'get_director.php',
        type: 'GET',
        data: { cale: cale },
        dataType: 'json',
        success: function(continut) {
            if (continut.error) {
                $('#continut').text('Eroare: ' + continut.error);
                return;
            }
            const $ul = $('<ul>');
            $.each(continut, function(i, item) {
                const $li = $('<li>').text(item.nume).addClass(item.tip);
                if (item.tip === 'director') {
                    $li.click(function(e) {
                        e.stopPropagation();
                        incarcaNod($li, cale === '/' ? `/${item.nume}` : `${cale}/${item.nume}`);
                    });
                } else {
                    $li.click(function(e) {
                        e.stopPropagation();
                        incarcaFisier(cale === '/' ? `/${item.nume}` : `${cale}/${item.nume}`);
                    });
                }
                $ul.append($li);
            });
            nod.append($ul);
        },
        error: function(xhr, status, error) {
            $('#continut').text('Eroare AJAX: ' + status);
        }
    });
}

function incarcaFisier(cale) {
    $.ajax({
        url: 'get_file.php',
        type: 'GET',
        data: { cale: cale },
        dataType: 'json',
        success: function(response) {
            if (response.error) {
                $('#continut').text('Eroare: ' + response.error);
                return;
            }
            $('#continut').text(response.content);
        },
        error: function(xhr, status, error) {
            $('#continut').text('Eroare AJAX: ' + status);
        }
    });
}

incarcaNod($('#arbore'), '/');