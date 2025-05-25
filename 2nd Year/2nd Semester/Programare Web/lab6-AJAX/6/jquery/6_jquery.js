function loadFilters() {
    $.ajax({
        url: 'get_filtre.php',
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            const $producator = $('#producator');
            const $procesor = $('#procesor');
            const $memorie = $('#memorie');

            $.each(response.producatori, function(i, p) {
                $producator.append(`<option value="${p}">${p}</option>`);
            });
            $.each(response.procesoare, function(i, p) {
                $procesor.append(`<option value="${p}">${p}</option>`);
            });
            $.each(response.memorii, function(i, m) {
                $memorie.append(`<option value="${m}">${m}</option>`);
            });

            loadArticles();
        },
        error: function(xhr, status, error) {
            $('#rezultate tbody').html(`<tr><td colspan="5">Eroare AJAX: ${status}</td></tr>`);
        }
    });
}

function loadArticles() {
    const data = {
        producator: $('#producator').val(),
        procesor: $('#procesor').val(),
        memorie: $('#memorie').val()
    };
    $.ajax({
        url: 'get_articole.php',
        type: 'GET',
        data: data,
        dataType: 'json',
        success: function(articole) {
            const $tbody = $('#rezultate tbody');
            $tbody.empty();
            $.each(articole, function(i, articol) {
                $tbody.append(`
                    <tr>
                        <td>${articol.nume}</td>
                        <td>${articol.producator}</td>
                        <td>${articol.procesor}</td>
                        <td>${articol.memorie}</td>
                        <td>${articol.pret}</td>
                    </tr>
                `);
            });
        },
        error: function(xhr, status, error) {
            $('#rezultate tbody').html(`<tr><td colspan="5">Eroare AJAX: ${status}</td></tr>`);
        }
    });
}

$('#producator, #procesor, #memorie').change(loadArticles);

loadFilters();