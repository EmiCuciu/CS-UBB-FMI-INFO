var offset = 0;
const limit = 3;

function loadData() {
    $.ajax({
        url: 'fetch_data.php',
        type: 'GET',
        data: {offset: offset},
        dataType: 'json',
        success: function (respose) {
            const data = respose.data;
            const $tbody = $('#results');
            $tbody.empty();

            $.each(data, function (index, persoana) {
                $tbody.append(
                    `
                        <tr>
                            <td>${persoana.nume}</td>
                            <td>${persoana.prenume}</td>
                            <td>${persoana.telefon}</td>
                            <td>${persoana.email}</td>
                        </tr>
                    `
                );
            });

            $('#prev').prop('disabled', offset === 0);
            $('#next').prop('disabled', !respose.are_urmatoare);
        },
        error: function (xhr, status, error){
            console.error('Eroare la incarcare: ' , error);
        }
    });
}

$('#prev').click(function () {
    if (offset >= limit) {
        offset -= limit;
        loadData();
    }
});

$('#next').click(function () {
    offset += limit;
    loadData();
});

loadData();