var modificat = false;
var idCurent = '';

function loadIds() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'get_ids.php', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.error) {
                document.getElementById('mesaj').textContent = 'Eroare: ' + response.error;
                return;
            }
            const lista = document.getElementById('lista');
            response.ids.forEach(id => {
                const option = document.createElement('option');
                option.value = id;
                option.textContent = id;
                lista.appendChild(option);
            });
        } else {
            document.getElementById('mesaj').textContent = 'Eroare HTTP: ' + xhr.status;
        }
    };
    xhr.onerror = function() {
        document.getElementById('mesaj').textContent = 'Eroare de rețea';
    };
    xhr.send();
}

function loadPerson(id) {
    if (!id) return;
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `get_person.php?id=${id}`, true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.error) {
                document.getElementById('mesaj').textContent = 'Eroare: ' + response.error;
                return;
            }
            document.getElementById('nume').value = response.nume;
            document.getElementById('prenume').value = response.prenume;
            document.getElementById('telefon').value = response.telefon;
            document.getElementById('email').value = response.email;
            document.getElementById('save').disabled = true;
            modificat = false;
            idCurent = id;
        } else {
            document.getElementById('mesaj').textContent = 'Eroare HTTP: ' + xhr.status;
        }
    };
    xhr.onerror = function() {
        document.getElementById('mesaj').textContent = 'Eroare de rețea';
    };
    xhr.send();
}

function savePerson() {
    const data = {
        id: idCurent,
        nume: document.getElementById('nume').value,
        prenume: document.getElementById('prenume').value,
        telefon: document.getElementById('telefon').value,
        email: document.getElementById('email').value
    };
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'save_person.php', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.error) {
                document.getElementById('mesaj').textContent = 'Eroare: ' + response.error;
                return;
            }
            document.getElementById('mesaj').textContent = 'Date salvate cu succes';
            document.getElementById('save').disabled = true;
            modificat = false;
        } else {
            document.getElementById('mesaj').textContent = 'Eroare HTTP: ' + xhr.status;
        }
    };
    xhr.onerror = function() {
        document.getElementById('mesaj').textContent = 'Eroare de rețea';
    };
    xhr.send(JSON.stringify(data));
}

document.getElementById('lista').addEventListener('change', function() {
    const newId = this.value;

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

document.getElementById('formular').addEventListener('input', function() {
    modificat = true;
    document.getElementById('save').disabled = false;
});

document.getElementById('save').addEventListener('click', savePerson);

loadIds();