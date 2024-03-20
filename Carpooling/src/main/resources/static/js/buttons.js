function showElement(element) {
    const x = document.getElementById(element);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}
const showFormButton = document.getElementById('showFormButton');
const modalOverlay = document.getElementById('modalOverlay');
const formContainer = document.getElementById('formContainer');

showFormButton.addEventListener('click', function() {
    modalOverlay.style.display = 'flex';
    formContainer.style.display = 'block';
});

const showFormButtonComment = document.getElementById('showCommentFormButton');
const modalOverlayComment = document.getElementById('modalOverlayComment');
const formContainerComment = document.getElementById('formContainerComment');

showFormButtonComment.addEventListener('click', function() {
    modalOverlayComment.style.display = 'flex';
    formContainerComment.style.display = 'block';
});

function closeFormComment() {
    modalOverlayComment.style.display = "none";
    formContainerComment.style.display = "none";
}

function closeForm() {
    modalOverlay.style.display = "none";
    formContainer.style.display = "none";
    const ratingInputs = document.querySelectorAll(".rating input[type='radio']");
    ratingInputs.forEach(input => {
        input.checked = false;
    });
}

function confirmDeleteProfile(id) {
    if (confirm('Are you sure you want to delete your profile? This action is irreversible.')) {
        window.location.href = '/users/' + id  + '/delete';
    } else {
        return false;
    }
}


