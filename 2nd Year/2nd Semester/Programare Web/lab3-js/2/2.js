document.addEventListener('DOMContentLoaded', function() {
    const numeInput = document.getElementById('nume');
    const dataNasteriiInput = document.getElementById('dataNasterii');
    const varstaInput = document.getElementById('varsta');
    const emailInput = document.getElementById('email');
    const submitBtn = document.getElementById('submitBtn');
    const resultMessage = document.getElementById('result-message');
    
    submitBtn.addEventListener('click', validateForm);
    
    dataNasteriiInput.addEventListener('change', function() {
        if (dataNasteriiInput.value) {
            const calculatedAge = calculateAge(new Date(dataNasteriiInput.value));
            varstaInput.value = calculatedAge;
        }
    });

    function calculateAge(birthDate) {
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }
        
        return age;
    }
    
    function validateForm() {
        clearErrors();
        
        let isValid = true;
        let invalidFields = [];
        
        if (!numeInput.value.trim() || !/^[A-Za-zĂăÂâÎîȘșȚț\s-]+$/.test(numeInput.value)) {
            markAsInvalid(numeInput, 'Numele trebuie să conțină doar litere, spații și cratimă');
            invalidFields.push('numele');
            isValid = false;
        }
        
        if (!dataNasteriiInput.value) {
            markAsInvalid(dataNasteriiInput, 'Data nașterii este obligatorie');
            invalidFields.push('data nașterii');
            isValid = false;
        } else {
            const birthDate = new Date(dataNasteriiInput.value);
            const today = new Date();
            if (birthDate > today) {
                markAsInvalid(dataNasteriiInput, 'Data nașterii nu poate fi în viitor');
                invalidFields.push('data nașterii');
                isValid = false;
            }
        }
        
        if (!varstaInput.value || isNaN(varstaInput.value) || varstaInput.value < 1 || varstaInput.value > 120) {
            markAsInvalid(varstaInput, 'Vârsta trebuie să fie între 1 și 120 ani');
            invalidFields.push('vârsta');
            isValid = false;
        } else {
            if (dataNasteriiInput.value) {
                const calculatedAge = calculateAge(new Date(dataNasteriiInput.value));
                if (parseInt(varstaInput.value) !== calculatedAge) {
                    markAsInvalid(varstaInput, 'Vârsta nu corespunde cu data nașterii');
                    invalidFields.push('vârsta');
                    isValid = false;
                }
            }
        }
        
        if (!emailInput.value.trim() || !/@/.test(emailInput.value)) {
            markAsInvalid(emailInput, 'Adresa de email nu este validă');
            invalidFields.push('email-ul');
            isValid = false;
        }
        
        if (isValid) {
            showSuccessMessage();
        } else {
            showErrorMessage(invalidFields);
        }
    }
    
    function markAsInvalid(inputElement, errorMessage) {
        inputElement.classList.add('invalid');  // Add invalid class for styling
        const errorElement = document.getElementById(inputElement.id + '-error');
        if (errorElement) {
            errorElement.textContent = errorMessage;
        }
    }
    
    function clearErrors() {
        const inputs = [numeInput, dataNasteriiInput, varstaInput, emailInput];
        inputs.forEach(input => {
            input.classList.remove('invalid');
            const errorElement = document.getElementById(input.id + '-error');
            if (errorElement) {
                errorElement.textContent = '';
            }
        });
        
        resultMessage.classList.add('hidden');
        resultMessage.classList.remove('error');
    }
    
    function showSuccessMessage() {
        resultMessage.textContent = 'Datele sunt completate corect';
        resultMessage.classList.remove('hidden', 'error');
        resultMessage.classList.add('success');
    }
    
    function showErrorMessage(invalidFields) {
        const fieldsText = invalidFields.join(' și ');
        resultMessage.textContent = `Câmpurile ${fieldsText} nu sunt completate corect`;
        resultMessage.classList.remove('hidden', 'success');
        resultMessage.classList.add('error');
    }
});