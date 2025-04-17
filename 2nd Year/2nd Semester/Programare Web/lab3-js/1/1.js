document.addEventListener('DOMContentLoaded', function() {
    const list1 = document.getElementById('list1');
    const list2 = document.getElementById('list2');

    // Move from list1 to list2
    list1.addEventListener('dblclick', function(event) {
        const selectedOption = event.target;
        if (selectedOption.tagName === 'OPTION') {
            const newOption = document.createElement('option');
            newOption.value = selectedOption.value;
            newOption.textContent = selectedOption.textContent;
            list2.appendChild(newOption);
            list1.removeChild(selectedOption);
        }
    });

    // Move from list2 to list1
    list2.addEventListener('dblclick', function(event) {
        console.log(event.target);
        add(list2);
    });

    function add(list){
        console.log(list);
        const selectedOption = event.target;
        if (selectedOption.tagName === 'OPTION') {
            const newOption = document.createElement('option');
            newOption.value = selectedOption.value;
            newOption.textContent = selectedOption.textContent;
            if (list === list1) {
                list2.appendChild(newOption);
                list1.removeChild(selectedOption);
            }
            else if (list === list2){
                list1.appendChild(newOption);
                list2.removeChild(selectedOption);
            }
        }
    }
});