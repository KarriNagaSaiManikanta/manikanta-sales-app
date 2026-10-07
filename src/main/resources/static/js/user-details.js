const USER_API = "http://localhost:8080/api/users";

const token = localStorage.getItem("token");

const params = new URLSearchParams(window.location.search);
const id = params.get("id");

// =====================================
// PAGE LOAD
// =====================================
document.addEventListener("DOMContentLoaded", () => {
    loadUser();
});

// =====================================
// LOAD USER DETAILS
// =====================================
function loadUser() {

    fetch(USER_API + "/" + id, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {

        if (!res.ok) {
            throw new Error("Failed to load user");
        }

        return res.json();

    })
    .then(user => {

        document.getElementById("userId").innerText = user.id;
        document.getElementById("firstName").innerText = user.firstName;
        document.getElementById("lastName").innerText = user.lastName;
        document.getElementById("name").innerText =
            user.firstName + " " + user.lastName;

        document.getElementById("email").innerText = user.email;
        document.getElementById("userEmail").innerText = user.email;
        document.getElementById("mobile").innerText =
            user.mobile || "-";

        document.getElementById("role").innerText = user.role;

        document.getElementById("verified").innerText =
            user.enabled ? "Verified" : "Not Verified";

        document.getElementById("accountStatus").innerText =
            user.accountLocked ? "Blocked" : "Active";

        document.getElementById("created").innerText =
            user.createdAt || "-";

        // ==========================
        // PROFILE IMAGE
        // ==========================
        const img = document.getElementById("profileImage");

        img.src = user.profileImage && user.profileImage.trim() !== ""
            ? "http://localhost:8080" + user.profileImage
            : "../images/user.png";

        img.onerror = function () {
            this.src = "../images/user.png";
        };

        // ==========================
        // STATUS
        // ==========================
        const status = document.getElementById("status");

        const blockBtn = document.getElementById("blockBtn");
        const unblockBtn = document.getElementById("unblockBtn");

        if (user.accountLocked) {

            status.innerText = "Blocked";
            status.style.background = "#f44336";

            blockBtn.style.display = "none";
            unblockBtn.style.display = "inline-block";

        } else {

            status.innerText = "Active";
            status.style.background = "#4CAF50";

            blockBtn.style.display = "inline-block";
            unblockBtn.style.display = "none";
        }

    })
    .catch(error => {

        console.log("Load User Error :", error);

    });

}

// =====================================
// BLOCK USER
// =====================================
document.getElementById("blockBtn").onclick = function () {

    if (!confirm("Block this user?")) {
        return;
    }

    fetch(USER_API + "/block/" + id, {

        method: "PUT",

        headers: {
            "Authorization": "Bearer " + token
        }

    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Block failed");
        }

        loadUser();

    })
    .catch(error => {

        console.log("Block Error :", error);

    });

};

// =====================================
// UNBLOCK USER
// =====================================
document.getElementById("unblockBtn").onclick = function () {

    if (!confirm("Unblock this user?")) {
        return;
    }

    fetch(USER_API + "/unblock/" + id, {

        method: "PUT",

        headers: {
            "Authorization": "Bearer " + token
        }

    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Unblock failed");
        }

        loadUser();

    })
    .catch(error => {

        console.log("Unblock Error :", error);

    });

};

// =====================================
// DELETE USER
// =====================================
document.getElementById("deleteBtn").onclick = function () {

    if (!confirm("Delete this user permanently?")) {
        return;
    }

    fetch(USER_API + "/" + id, {

        method: "DELETE",

        headers: {
            "Authorization": "Bearer " + token
        }

    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Delete failed");
        }

        alert("User deleted successfully.");

        window.location.href = "users.html";

    })
    .catch(error => {

        console.log("Delete Error :", error);

    });

};