const USER_API = "/api/users";

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

        let profileImg = user.profileImage;

        // Clean up old localhost reference from database records
        if (profileImg && profileImg.includes("localhost:8080")) {
            profileImg = profileImg.replace("http://localhost:8080", "").replace("https://localhost:8080", "");
        }

        img.src = profileImg && profileImg.trim() !== ""
            ? profileImg
            : "data:image/svg+xml;utf8,<svg xmlns=\'http://www.w3.org/2000/svg\' width=\'50\' height=\'50\' viewBox=\'0 0 24 24\' fill=\'%23ccc\'><path d=\'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z\'/></svg>";

        img.onerror = function () {
            this.src = "data:image/svg+xml;utf8,<svg xmlns=\'http://www.w3.org/2000/svg\' width=\'50\' height=\'50\' viewBox=\'0 0 24 24\' fill=\'%23ccc\'><path d=\'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z\'/></svg>";
        };

        // ==========================
        // STATUS
        // ==========================
        const status = document.getElementById("status");

        const blockBtn = document.getElementById("blockBtn");
        const unblockBtn = document.getElementById("unblockBtn");

        if (user.accountLocked) {

            if (status) {
                status.innerText = "Blocked";
                status.style.background = "#f44336";
            }

            if (blockBtn) blockBtn.style.display = "none";
            if (unblockBtn) unblockBtn.style.display = "inline-block";

        } else {

            if (status) {
                status.innerText = "Active";
                status.style.background = "#4CAF50";
            }

            if (blockBtn) blockBtn.style.display = "inline-block";
            if (unblockBtn) unblockBtn.style.display = "none";
        }

    })
    .catch(error => {

        console.log("Load User Error :", error);

    });

}

// =====================================
// BLOCK USER
// =====================================
const blockBtnElement = document.getElementById("blockBtn");
if (blockBtnElement) {
    blockBtnElement.onclick = function () {

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
}

// =====================================
// UNBLOCK USER
// =====================================
const unblockBtnElement = document.getElementById("unblockBtn");
if (unblockBtnElement) {
    unblockBtnElement.onclick = function () {

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
}

// =====================================
// DELETE USER
// =====================================
const deleteBtnElement = document.getElementById("deleteBtn");
if (deleteBtnElement) {
    deleteBtnElement.onclick = function () {

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
}