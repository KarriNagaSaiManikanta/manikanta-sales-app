// =======================================
// EDIT CATEGORY
// CATEGORY_API comes from api.js
// =======================================

// =======================================
// ELEMENTS
// =======================================

const form = document.getElementById("editCategoryForm");
const nameInput = document.getElementById("name");
const descriptionInput = document.getElementById("description");
const imageInput = document.getElementById("image");
const preview = document.getElementById("preview");
const activeInput = document.getElementById("active");
const loader = document.getElementById("loader");
const toast = document.getElementById("toast");

// =======================================
// CATEGORY ID
// =======================================

const params = new URLSearchParams(window.location.search);
const categoryId = params.get("id");

// =======================================
// LOADER
// =======================================

function showLoader() {
    if (loader) loader.style.display = "flex";
}

function hideLoader() {
    if (loader) loader.style.display = "none";
}

// =======================================
// TOAST
// =======================================

function showToast(message) {

    if (!toast) {
        alert(message);
        return;
    }

    toast.innerHTML = message;
    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

// =======================================
// LOAD CATEGORY
// =======================================

async function loadCategory() {

    if (!categoryId) {
        showToast("Category ID Missing");
        return;
    }

    showLoader();

    try {

        const response = await fetch(
            CATEGORY_API + "/" + categoryId,
            {
                headers: {
                    Authorization: "Bearer " + getToken()
                }
            }
        );

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "Category not found");
        }

        nameInput.value = data.name;
        descriptionInput.value = data.description || "";

        // FIXED
        activeInput.checked = data.active;

        if (data.image) {
            preview.src = window.location.origin + data.image;
            preview.style.display = "block";
        }

    } catch (error) {

        showToast(error.message);

    } finally {

        hideLoader();

    }

}

// =======================================
// IMAGE PREVIEW
// =======================================

function previewImage(event) {

    const file = event.target.files[0];

    if (!file) return;

    if (!file.type.startsWith("image/")) {
        showToast("Only image files allowed");
        imageInput.value = "";
        return;
    }

    if (file.size > 5 * 1024 * 1024) {
        showToast("Image size should be below 5MB");
        imageInput.value = "";
        return;
    }

    preview.src = URL.createObjectURL(file);
    preview.style.display = "block";
}

if (imageInput) {
    imageInput.addEventListener("change", previewImage);
}

// =======================================
// UPDATE CATEGORY
// =======================================

if (form) {

    form.addEventListener("submit", async function (e) {

        e.preventDefault();

        const formData = new FormData();

        formData.append("name", nameInput.value.trim());
        formData.append("description", descriptionInput.value.trim());

        // FIXED
        formData.append("active", activeInput.checked);

        if (imageInput.files.length > 0) {
            formData.append("image", imageInput.files[0]);
        }

        showLoader();

        try {

            const response = await fetch(
                CATEGORY_API + "/" + categoryId,
                {
                    method: "PUT",
                    headers: {
                        Authorization: "Bearer " + getToken()
                    },
                    body: formData
                }
            );

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || "Update Failed");
            }

            showToast("Category Updated Successfully");

            setTimeout(() => {
                window.location.href = "categories.html";
            }, 1000);

        } catch (error) {

            showToast(error.message);

        } finally {

            hideLoader();

        }

    });

}

// =======================================
// START
// =======================================

loadCategory();