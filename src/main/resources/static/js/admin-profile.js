// =======================================
// ADMIN PROFILE JS
// Manikanta Sales Admin
// =======================================

const API = "/api/users/profile";
const token = localStorage.getItem("token");

document.addEventListener("DOMContentLoaded", loadProfile);

async function loadProfile() {

    try {

        const response = await fetch(API, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Failed to load profile");
        }

        const user = await response.json();

        document.getElementById("id").innerText = user.id;
        document.getElementById("name").innerText = user.fullName;
        document.getElementById("email").innerText = user.email;
        document.getElementById("role").innerText = user.role;
        document.getElementById("phone").innerText = user.mobile;
        document.getElementById("status").innerText =
            user.enabled ? "Active" : "Inactive";

        const image = document.getElementById("profileImage");

        if (user.profileImage) {
            image.src = user.profileImage; // Fixed: Removed localhost reference
        } else {
            image.src = "/images/admin.png";
        }

    } catch (e) {
        console.error(e);
    }
}