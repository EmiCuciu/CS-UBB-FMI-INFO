var modificat = false;
var idCurent = '';

function loadIds() {
    $.ajax({
        url: 'get_ids.php',
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            console.log("Response received:", response);

            if (response.error) {
                $('#mesaj').text('Eroare: ' + response.error);
                return;
            }
            const $lista = $('#lista');
            $.each(response.ids, function(index, id) {
                $lista.append(`<option value="${id}">${id}</option>`);
            });
        },
        error: function(xhr, status, error) {
            console.error("AJAX Error:", status, error);
            console.log("Response:", xhr.responseText);
            $('#mesaj').text('Eroare AJAX: ' + status);
        }
    });
}

function loadPerson(id) {
    if (!id) return;
    $.ajax({
        url: 'get_person.php',
        type: 'GET',
        data: { id: id },
        dataType: 'json',
        success: function(response) {
            if (response.error) {
                $('#mesaj').text('Eroare: ' + response.error);
                return;
            }
            $('#nume').val(response.nume);
            $('#prenume').val(response.prenume);
            $('#telefon').val(response.telefon);
            $('#email').val(response.email);
            $('#save').prop('disabled', true);
            modificat = false;
            idCurent = id;
        },
        error: function(xhr, status, error) {
            $('#mesaj').text('Eroare AJAX: ' + status);
        }
    });
}

function savePerson() {
    const data = {
        id: idCurent,
        nume: $('#nume').val(),
        prenume: $('#prenume').val(),
        telefon: $('#telefon').val(),
        email: $('#email').val()
    };
    $.ajax({
        url: 'save_person.php',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function(response) {
            if (response.error) {
                $('#mesaj').text('Eroare: ' + response.error);
                return;
            }
            $('#mesaj').text('Date salvate cu succes');
            $('#save').prop('disabled', true);
            modificat = false;
        },
        error: function(xhr, status, error) {
            $('#mesaj').text('Eroare AJAX: ' + status);
        }
    });
}

$('#lista').change(function() {
    const newId = $(this).val();

    if (modificat) {
        if (confirm('Datele au fost modificate. Doriți să le salvați?')) {
            savePerson();
            setTimeout(function() {
                loadPerson(newId);
            }, 300);

            return;
        }
    }

    loadPerson(newId);
});

$('#formular').on('input', function() {
    modificat = true;
    $('#save').prop('disabled', false);
});

$('#save').click(savePerson);

loadIds();