// ======================================
// MANIKANTA SALES
// ADMIN AUTH
// ======================================

// ===============================
// GET STORAGE DATA
// ===============================

const role = localStorage.getItem("role");
const userData = localStorage.getItem("loggedInUser");

let user = null;

// ===============================
// PARSE USER
// ===============================

try {
    user = JSON.parse(userData);
} catch (error) {
    user = {
        email: userData,
        role: role
    };
}

// ===============================
// DEBUG
// ===============================

console.log("TOKEN :", getToken());
console.log("ROLE :", role);
console.log("USER :", user);

// ===============================
// LOGIN CHECK
// ===============================

if (!getToken()) {
    window.location.href = "/login.html";
}

// ===============================
// ROLE CHECK
// ===============================

if (role !== "ADMIN" && role !== "ROLE_ADMIN") {
    alert("Access Denied");
    window.location.href = "/index.html";
}

// ===============================
// SHOW ADMIN DETAILS
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    const adminEmail = document.getElementById("adminEmail");
    const adminName = document.getElementById("adminName");

    if (adminEmail) {
        adminEmail.textContent = user?.email || "Admin";
    }

    if (adminName) {
        adminName.textContent = user?.name || "Admin";
    }

});

// ===============================
// JWT HEADER
// ===============================

function getAdminHeaders() {

    return {
        "Authorization": "Bearer " + getToken(),
        "Content-Type": "application/json"
    };

}

// ===============================
// LOGOUT
// ===============================

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("loggedInUser");
    localStorage.removeItem("role");

    window.location.href = "/login.html";

}