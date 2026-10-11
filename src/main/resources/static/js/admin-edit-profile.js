// =======================================
// EDIT PROFILE JS
// Manikanta Sales Admin
// =======================================

const PROFILE_API = "/api/users/profile";
const IMAGE_API = "/api/users/profile/image"; // Fixed: Removed localhost reference

const token = localStorage.getItem("token");
let selectedImage = null;

document.addEventListener("DOMContentLoaded", loadEditProfile);

async function loadEditProfile() {

    try {

        const response = await fetch(PROFILE_API, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load profile");
        }

        const user = await response.json();

        let nameParts = user.fullName.split(" ");

        document.getElementById("firstName").value = nameParts[0];
        document.getElementById("lastName").value = nameParts.slice(1).join(" ");
        document.getElementById("email").value = user.email;
        document.getElementById("mobile").value = user.mobile;
        document.getElementById("role").value = user.role;

        if (user.profileImage) {
            document.getElementById("profilePreview").src = user.profileImage; // Fixed: Removed localhost reference
        }

    } catch (error) {
        console.error(error);
        alert("Profile loading failed");
    }
}

function previewImage(event) {

    selectedImage = event.target.files[0];

    if (selectedImage) {

        const reader = new FileReader();

        reader.onload = function () {
            document.getElementById("profilePreview").src = reader.result;
        };

        reader.readAsDataURL(selectedImage);
    }
}

async function updateProfile() {

    const data = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        mobile: document.getElementById("mobile").value
    };

    try {

        const response = await fetch(PROFILE_API, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error("Profile update failed");
        }

        if (selectedImage) {

            const formData = new FormData();
            formData.append("file", selectedImage);

            const imageResponse = await fetch(IMAGE_API, {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                },
                body: formData
            });

            if (!imageResponse.ok) {
                alert("Image upload failed");
                return;
            }
        }

        alert("Profile Updated Successfully");

        // ✅ Admin Profile Page
        window.location.href = "/admin-profile.html";

    } catch (error) {
        console.error(error);
        alert("Update Failed");
    }
}