// =======================================
// ADMIN PROFILE JS
// Manikanta Sales Admin
// =======================================
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

        // Safe helper function to avoid null property errors
        const setText = (id, value) => {
            const el = document.getElementById(id);
            if (el) {
                el.innerText = value || "-";
            }
        };

        setText("id", user.id);
        setText("name", user.fullName);
        setText("email", user.email);
        setText("role", user.role);
        setText("phone", user.mobile);
        setText("status", user.enabled ? "Active" : "Inactive");

        const image = document.getElementById("profileImage");
        if (image) {
            if (user.profileImage) {
                image.src = user.profileImage;
            } else {
                image.src = "/images/admin.png";
            }
        }

    } catch (e) {
        console.error("Error loading profile:", e);
    }
}