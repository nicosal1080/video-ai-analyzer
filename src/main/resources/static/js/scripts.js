
document.body.addEventListener('htmx:beforeRequest', function(evt) {
    const targetId = evt.target.closest('form').getAttribute('data-loading-target');

    if (targetId) {
        const submitButton = evt.target.closest('form').querySelector('button[type="submit"]');
        if (submitButton) {
            submitButton.classList.add('hidden');
        }
        // Mostrar el indicador de carga
        document.getElementById(targetId).classList.remove('hidden');
    }
});
document.body.addEventListener('htmx:afterRequest', function(evt) {
    const loginmodal = document.getElementById('login-modal');

    window.addEventListener('click', (event) => {
        if (event.target === loginmodal) {
            loginmodal.classList.add('hidden');
        }
    });
});
function copyToClipboard(element) {
    const textToCopy = element.querySelector('span').innerText;
    navigator.clipboard.writeText(textToCopy).then(() => {
        const feedback = element.querySelector('.copy-feedback');
        feedback.classList.remove('opacity-0');
        feedback.classList.add('opacity-100');

        setTimeout(() => {
            feedback.classList.remove('opacity-100');
            feedback.classList.add('opacity-0');
        }, 500);
    }).catch(err => {
        console.error('Error al copiar: ', err);
    });
}

document.body.addEventListener('htmx:afterRequest', function(evt) {
    const elements = document.querySelectorAll('.word-by-word');
    let totalElements = elements.length;
    let completed = 0;

    elements.forEach(element => {
        wordByWordAnimation(element, 100, function() {
            completed++;
            if (completed === totalElements) {
                document.getElementById('restartButtonContainer').classList.remove('hidden');
            }
        });
    });
});
function wordByWordAnimation(element, delay = 100, callback = null) {
    const text = element.innerText.trim();
    const words = text.split(' ');
    element.innerHTML = '';

    words.forEach((word, index) => {
        const span = document.createElement('span');
        span.textContent = word + ' ';
        span.style.visibility = 'hidden';
        element.appendChild(span);

        setTimeout(() => {
            span.style.visibility = 'visible';
            span.style.opacity = '1';

            if (index === words.length - 1 && typeof callback === 'function') {
                callback();
            }
        }, index * delay);
    });
}
function closeErrorMessage() {
    document.getElementById('error-message').classList.add('hidden');
}
