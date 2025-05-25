function incarcaNod(nod, cale) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `get_director.php?cale=${encodeURIComponent(cale)}`, true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const continut = JSON.parse(xhr.responseText);
            if (continut.error) {
                document.getElementById('continut').textContent = 'Eroare: ' + continut.error;
                return;
            }
            const ul = document.createElement('ul');
            continut.forEach(item => {
                const li = document.createElement('li');
                li.textContent = item.nume;
                li.className = item.tip;
                if (item.tip === 'director') {
                    li.addEventListener('click', function(e) {
                        e.stopPropagation();
                        incarcaNod(li, cale === '/' ? `/${item.nume}` : `${cale}/${item.nume}`);
                    });
                } else {
                    li.addEventListener('click', function(e) {
                        e.stopPropagation();
                        incarcaFisier(cale === '/' ? `/${item.nume}` : `${cale}/${item.nume}`);
                    });
                }
                ul.appendChild(li);
            });
            nod.appendChild(ul);
        } else {
            document.getElementById('continut').textContent = 'Eroare HTTP: ' + xhr.status;
        }
    };
    xhr.onerror = function() {
        document.getElementById('continut').textContent = 'Eroare de rețea';
    };
    xhr.send();
}

function incarcaFisier(cale) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `get_file.php?cale=${encodeURIComponent(cale)}`, true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.error) {
                document.getElementById('continut').textContent = 'Eroare: ' + response.error;
                return;
            }
            document.getElementById('continut').textContent = response.content;
        } else {
            document.getElementById('continut').textContent = 'Eroare HTTP: ' + xhr.status;
        }
    };
    xhr.onerror = function() {
        document.getElementById('continut').textContent = 'Eroare de rețea';
    };
    xhr.send();
}

incarcaNod(document.getElementById('arbore'), '/');