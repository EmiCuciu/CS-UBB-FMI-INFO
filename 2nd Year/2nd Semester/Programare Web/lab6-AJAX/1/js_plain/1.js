document.getElementById('plecare').addEventListener('change', function() {
    var statiePlecare = this.value;
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'get_sosiri.php?plecare=' + statiePlecare, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var sosiri = JSON.parse(xhr.responseText);
            var listaSosire = document.getElementById('sosire');
            listaSosire.innerHTML = '';
            sosiri.forEach(function(sosire) {
                var option = document.createElement('option');
                option.value = sosire;
                option.text = sosire;
                listaSosire.appendChild(option);
            });
        }
    };
    xhr.send();
});