function initialize(tableId) {
    const table = document.getElementById(tableId);
    if (!table) return;

    const headers = table.querySelectorAll('.header');

    headers.forEach(header => {
        header.addEventListener('click', function () {
            const rowIndex = parseInt(this.getAttribute('data-row'));
            const currentSortDirection = this.classList.contains('sort-asc') ? 'asc' :
                this.classList.contains('sort-desc') ? 'desc' : 'none';

            // Resetăm toate antetele
            headers.forEach(h => {
                h.classList.remove('sort-asc', 'sort-desc');
            });

            // Determinăm direcția de sortare
            let sortDirection;
            if (currentSortDirection === 'none' || currentSortDirection === 'desc') {
                sortDirection = 'asc';
                this.classList.add('sort-asc');
            } else {
                sortDirection = 'desc';
                this.classList.add('sort-desc');
            }

            sortRow(table, rowIndex, sortDirection);
        });
    });
}

function sortRow(table, rowIndex, direction) {
    const rows = Array.from(table.rows);
    const values = [];
    const rowData = rows[rowIndex];

    for (let i = 1; i < rowData.cells.length; i++) {
        const colValues = {};
        for (let j = 0; j < rows.length; j++) {
            colValues[j] = rows[j].cells[i].textContent;
        }
        values.push({ index: i, values: colValues });
    }

    values.sort((a, b) => {
        const aVal = isNaN(a.values[rowIndex]) ? a.values[rowIndex] : parseFloat(a.values[rowIndex]);
        const bVal = isNaN(b.values[rowIndex]) ? b.values[rowIndex] : parseFloat(b.values[rowIndex]);

        if (direction === 'asc') {
            return aVal > bVal ? 1 : aVal < bVal ? -1 : 0;
        } else {
            return aVal < bVal ? 1 : aVal > bVal ? -1 : 0;
        }
    });

    // Reordonăm coloanele în tabel
    const tempTable = document.createElement('table');

    // Creăm prima coloană (antetul)
    for (let i = 0; i < rows.length; i++) {
        const newRow = tempTable.insertRow();
        const headerCell = newRow.insertCell();
        headerCell.innerHTML = rows[i].cells[0].innerHTML;
        headerCell.className = rows[i].cells[0].className;
        if (rows[i].cells[0].hasAttribute('data-row')) {
            headerCell.setAttribute('data-row', rows[i].cells[0].getAttribute('data-row'));
        }
    }

    // Adăugăm coloanele sortate
    for (const item of values) {
        for (let i = 0; i < rows.length; i++) {
            const cell = tempTable.rows[i].insertCell();
            cell.textContent = rows[i].cells[item.index].textContent;
        }
    }

    // Înlocuim conținutul tabelului original
    for (let i = 0; i < rows.length; i++) {
        for (let j = 0; j < tempTable.rows[i].cells.length; j++) {
            if (j < rows[i].cells.length) {
                rows[i].cells[j].innerHTML = tempTable.rows[i].cells[j].innerHTML;
                rows[i].cells[j].className = tempTable.rows[i].cells[j].className;
                if (tempTable.rows[i].cells[j].hasAttribute('data-row')) {
                    rows[i].cells[j].setAttribute('data-row', tempTable.rows[i].cells[j].getAttribute('data-row'));
                }
            } else {
                const cell = rows[i].insertCell();
                cell.innerHTML = tempTable.rows[i].cells[j].innerHTML;
            }
        }
    }
}


// Sortare orizontală
function initialize_horizontal(tabelid) {
    const table = document.getElementById(tabelid);
    if (!table) return;

    const headers = table.querySelectorAll('.header');

    headers.forEach(header => {
        header.addEventListener('click', function () {
            const colIndex = parseInt(this.getAttribute('data-col'));
            const currentSortDirection = this.classList.contains('sort-asc') ? 'asc' :
                this.classList.contains('sort-desc') ? 'desc' : 'none';

            // Resetăm toate antetele
            headers.forEach(h => {
                h.classList.remove('sort-asc', 'sort-desc');
            });

            // Determinăm direcția de sortare
            let sortDirection;
            if (currentSortDirection === 'none' || currentSortDirection === 'desc') {
                sortDirection = 'asc';
                this.classList.add('sort-asc');
            } else {
                sortDirection = 'desc';
                this.classList.add('sort-desc');
            }

            sortCol(table, colIndex, sortDirection);
        });
    });
}

function sortCol(table, colIndex, direction) {
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.rows);

    rows.sort((a, b) => {
        const aVal = a.cells[colIndex].textContent.trim();
        const bVal = b.cells[colIndex].textContent.trim();

        const aNum = isNaN(aVal) ? aVal : parseFloat(aVal);
        const bNum = isNaN(bVal) ? bVal : parseFloat(bVal);

        if (direction === 'asc') {
            return aNum > bNum ? 1 : aNum < bNum ? -1 : 0;
        } else {
            return aNum < bNum ? 1 : aNum > bNum ? -1 : 0;
        }
    })

    // Reordonăm rândurile în tabel
    rows.forEach(row => {
        tbody.appendChild(row);
    });
}


document.addEventListener('DOMContentLoaded', function () {
    initialize('tableid');
    initialize('tableid-cars');
    initialize_horizontal('tableid-horizontal');
    initialize_horizontal('tableid-cars-horizontal');
});
