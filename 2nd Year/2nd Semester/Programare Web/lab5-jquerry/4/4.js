$(function() {
    initializeVertical("tableid");
    initializeVertical("tableid-cars");

    initializeHorizontal("tableid-horizontal");
    initializeHorizontal("tableid-cars-horizontal");
});

function initializeVertical(tableId) {
    const $table = $(`#${tableId}`);
    if (!$table.length) return;

    const $headers = $table.find('.header');

    $headers.on('click', function() {
        const rowIndex = parseInt($(this).data('row'));
        const currentSortDirection = $(this).hasClass('sort-asc') ? 'asc' :
            $(this).hasClass('sort-desc') ? 'desc' : 'none';

        $headers.removeClass('sort-asc sort-desc');

        let sortDirection;
        if (currentSortDirection === 'none' || currentSortDirection === 'desc') {
            sortDirection = 'asc';
            $(this).addClass('sort-asc');
        } else {
            sortDirection = 'desc';
            $(this).addClass('sort-desc');
        }

        sortRow($table, rowIndex, sortDirection);
    });
}

function sortRow($table, rowIndex, direction) {
    const $rows = $table.find('tr');
    const values = [];
    const $rowData = $($rows[rowIndex]);

    for (let i = 1; i < $rowData.find('td, th').length; i++) {
        const colValues = {};
        $rows.each(function(j) {
            colValues[j] = $(this).find('td, th').eq(i).text();
        });
        values.push({ index: i, values: colValues });
    }

    values.sort(function(a, b) {
        const aVal = isNaN(a.values[rowIndex]) ? a.values[rowIndex] : parseFloat(a.values[rowIndex]);
        const bVal = isNaN(b.values[rowIndex]) ? b.values[rowIndex] : parseFloat(b.values[rowIndex]);

        if (direction === 'asc') {
            return aVal > bVal ? 1 : aVal < bVal ? -1 : 0;
        } else {
            return aVal < bVal ? 1 : aVal > bVal ? -1 : 0;
        }
    });

    const $temporaryTable = $('<table>');

    $rows.each(function(i) {
        const $newRow = $('<tr>').appendTo($temporaryTable);
        const $headerCell = $(this).find('td, th').eq(0).clone(true);
        if ($headerCell.is('th')) {
            $newRow.append($headerCell);
        }
    });

    values.forEach(function(item) {
        $rows.each(function(i) {
            const $cell = $(this).find('td, th').eq(item.index);
            const $newCell = $('<td>').text($cell.text());
            $temporaryTable.find('tr').eq(i).append($newCell);
        });
    });

    $rows.each(function(i) {
        const $currentRow = $(this);
        const $tempRow = $temporaryTable.find('tr').eq(i);

        const $firstCell = $currentRow.find('td, th').eq(0);
        $currentRow.find('td, th').not($firstCell).remove();

        $tempRow.find('td, th').slice(1).each(function() {
            $(this).clone().appendTo($currentRow);
        });
    });
}

function initializeHorizontal(tableId) {
    const $table = $(`#${tableId}`);
    if (!$table.length) return;

    const $headers = $table.find('.header');

    $headers.on('click', function() {
        const colIndex = parseInt($(this).data('col'));
        const currentSortDirection = $(this).hasClass('sort-asc') ? 'asc' :
            $(this).hasClass('sort-desc') ? 'desc' : 'none';

        $headers.removeClass('sort-asc sort-desc');

        let sortDirection;
        if (currentSortDirection === 'none' || currentSortDirection === 'desc') {
            sortDirection = 'asc';
            $(this).addClass('sort-asc');
        } else {
            sortDirection = 'desc';
            $(this).addClass('sort-desc');
        }

        sortCol($table, colIndex, sortDirection);
    });
}

function sortCol($table, colIndex, direction) {
    const $tbody = $table.find('tbody');
    const $rows = $tbody.find('tr').get();

    $rows.sort(function(a, b) {
        const aVal = $(a).find('td').eq(colIndex).text().trim();
        const bVal = $(b).find('td').eq(colIndex).text().trim();

        const aNum = isNaN(aVal) ? aVal : parseFloat(aVal);
        const bNum = isNaN(bVal) ? bVal : parseFloat(bVal);

        if (direction === 'asc') {
            return aNum > bNum ? 1 : aNum < bNum ? -1 : 0;
        } else {
            return aNum < bNum ? 1 : aNum > bNum ? -1 : 0;
        }
    });

    $.each($rows, function(index, row) {
        $tbody.append(row);
    });
}