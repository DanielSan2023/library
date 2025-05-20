function showAddBookForm() {
    document.getElementById('addBookForm').style.display = 'block';
}

function hideAddBookForm() {
    document.getElementById('addBookForm').style.display = 'none';
    document.getElementById('formError').textContent = '';
    document.getElementById('bookForm').reset();
}

function submitBookForm(event) {
    event.preventDefault();

    const book = {
        title: document.getElementById('title').value.trim(),
        author: document.getElementById('author').value.trim(),
        isbn: document.getElementById('isbn').value.trim(),
        publishedYear: parseInt(document.getElementById('publishedYear').value)
    };

    if (!book.title || !book.author || !book.isbn || isNaN(book.publishedYear)) {
        document.getElementById('formError').textContent = "Please fill in all required fields correctly.";
        return;
    }

    fetch('/api/books', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(book)
    }).then(res => {
        if (res.ok) {
            alert("Book added successfully!");
            hideAddBookForm();
            location.reload();
        } else {
            res.json().then(data => {
                document.getElementById('formError').textContent = data.message || "Error adding book.";
            });
        }
    }).catch(() => {
        document.getElementById('formError').textContent = "Network error.";
    });
}

function addCopy(bookId) {
    fetch(`/api/books/${bookId}/copies`, {method: 'POST'})
        .then(res => {
            if (res.ok) {
                alert("Copy added!");
                updateCopyInfo(bookId);
            } else {
                alert("Failed to add copy.");
            }
        });
}

function updateCopyInfo(bookId) {
    const copyInfoDiv = document.getElementById(`copy-info-${bookId}`);
    if (!copyInfoDiv) return;

    fetch(`/api/books/${bookId}/copies`)
        .then(res => res.json())
        .then(copies => {
            const available = copies.filter(c => c.available === true).length;
            const borrowed = copies.filter(c => c.available === false).length;
            const unknown = copies.filter(c => c.available == null).length;
            const total = copies.length;

            let text = `Copies: ${total}`;
            if (available > 0) text += ` | Available: ${available}`;
            if (borrowed > 0) text += ` | Borrowed: ${borrowed}`;
            if (unknown > 0) text += ` | Unknown: ${unknown}`;

            copyInfoDiv.textContent = text;
        })
        .catch(() => {
            copyInfoDiv.textContent = "Error loading copies.";
        });
}

function deleteBook(bookId) {
    if (!confirm("Are you sure you want to delete this book?")) return;

    fetch(`/api/books/${bookId}`, {method: 'DELETE'})
        .then(res => {
            if (res.ok) {
                alert("Book deleted.");
                location.reload();
            } else {
                alert("Error deleting book.");
            }
        });
}

function borrowCopy(bookId) {
    fetch(`/api/books/${bookId}/copies/borrow`, {method: 'PUT'})
        .then(res => {
            if (res.status === 204) {
                alert("Book copy borrowed!");
                updateCopyInfo(bookId);
            } else if (res.status === 400) {
                alert("No available copies to borrow.");
            } else {
                alert("Error borrowing copy.");
            }
        })
        .catch(() => {
            alert("Network error.");
        });
}

function returnCopy(bookId) {
    fetch(`/api/books/${bookId}/copies/return`, {method: 'PUT'})
        .then(res => {
            if (res.status === 204) {
                alert("Book copy returned!");
                updateCopyInfo(bookId);
            } else {
                alert("Error returning copy.");
            }
        })
        .catch(() => {
            alert("Network error.");
        });
}

document.addEventListener("DOMContentLoaded", () => {
    const bookCards = document.querySelectorAll(".book-card");
    bookCards.forEach(card => {
        const copyInfoDiv = card.querySelector(".copy-info");
        if (!copyInfoDiv) return;

        const bookId = copyInfoDiv.id.replace("copy-info-", "");
        updateCopyInfo(bookId);
    });
});
