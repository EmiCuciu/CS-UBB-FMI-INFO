function loadFilters() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'get_filtre.php', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            const producator = document.getElementById('producator');
            const procesor = document.getElementById('procesor');
            const memorie = document.getElementById('memorie');

            response.producatori.forEach(p => {
                const option = document.createElement('option');
                option.value = p;
                option.textContent = p;
                producator.appendChild(option);
            });
            response.procesoare.forEach(p => {
                const option = document.createElement('option');
                option.value = p;
                option.textContent = p;
                procesor.appendChild(option);
            });
            response.memorii.forEach(m => {
                const option = document.createElement('option');
                option.value = m;
                option.textContent = m;
                memorie.appendChild(option);
            });

            loadArticles();
        } else {
            document.getElementById('rezultate').querySelector('tbody').innerHTML = `<tr><td colspan="5">Eroare HTTP: ${xhr.status}</td></tr>`;
        }
    };
    xhr.onerror = function() {
        document.getElementById('rezultate').querySelector('tbody').innerHTML = `<tr><td colspan="5">Eroare de rețea</td></tr>`;
    };
    xhr.send();
}

function loadArticles() {
    const params = new URLSearchParams({
        producator: document.getElementById('producator').value,
        procesor: document.getElementById('procesor').value,
        memorie: document.getElementById('memorie').value
    });
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `get_articole.php?${params.toString()}`, true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const articole = JSON.parse(xhr.responseText);
            const tbody = document.getElementById('rezultate').querySelector('tbody');
            tbody.innerHTML = '';
            articole.forEach(articol => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${articol.nume}</td>
                    <td>${articol.producator}</td>
                    <td>${articol.procesor}</td>
                    <td>${articol.memorie}</td>
                    <td>${articol.pret}</td>
                `;
                tbody.appendChild(tr);
            });
        } else {
            document.getElementById('rezultate').querySelector('tbody').innerHTML = `<tr><td colspan="5">Eroare HTTP: ${xhr.status}</td></tr>`;
        }
    };
    xhr.onerror = function() {
        document.getElementById('rezultate').querySelector('tbody').innerHTML = `<tr><td colspan="5">Eroare de rețea</td></tr>`;
    };
    xhr.send();
}

document.getElementById('producator').addEventListener('change', loadArticles);
document.getElementById('procesor').addEventListener('change', loadArticles);
document.getElementById('memorie').addEventListener('change', loadArticles);

loadFilters();