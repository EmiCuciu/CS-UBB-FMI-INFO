$('#plecare').change(function () {
    var staiePlecare = $(this).val();
    $.ajax({
        url: 'get_sosiri.php',
        type: 'GET',
        data: {plecare: staiePlecare},
        dataType: 'json',
        success: function (sosiri) {
            var listaSosire = $('#sosire');
            listaSosire.empty();
            $.each(sosiri, function (sosire) {
                listaSosire.append($('<option>').val(sosire).text(sosire));
            });
        }
    });
});