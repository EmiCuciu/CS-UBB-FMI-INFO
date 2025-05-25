var offset = 0;
const limit = 3;

function loadData() {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `fetch_data.php?offset=${offset}`, true);

    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            if (response.error) {
                console.error('Eroare server:', response.error);
                return;
            }
            const data = response.data;
            const tbody = document.getElementById("results");
            tbody.innerHTML = "";

            data.forEach(persoana => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${persoana.nume}</td>
                    <td>${persoana.prenume}</td>
                    <td>${persoana.telefon}</td>
                    <td>${persoana.email}</td>
                `;
                tbody.appendChild(tr);
            });

            document.getElementById("prev").disabled = offset === 0;
            document.getElementById("next").disabled = !response.are_urmatoare;
        } else {
            console.error('Eroare la încărcare:', xhr.statusText);
        }
    };
    xhr.send();
}

document.getElementById("prev").addEventListener("click", () => {
    if (offset >= limit) {
        offset -= limit;
        loadData();
    }
});

document.getElementById("next").addEventListener("click", () => {
    offset += limit;
    loadData();
});

loadData();