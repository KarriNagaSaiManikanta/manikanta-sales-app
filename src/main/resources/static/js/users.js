const USERS_API = "/api/users";

let allUsers = [];

// =====================================
// PAGE LOAD
// =====================================
document.addEventListener("DOMContentLoaded", () => {
    loadUsers();
});

// =====================================
// TOKEN
// =====================================
function getToken() {
    return localStorage.getItem("token");
}

// =====================================
// LOAD USERS
// =====================================
function loadUsers() {

    // Fixed: Changed USER_API to USERS_API to match the declaration above
    fetch(USERS_API + "/all", {
        headers: {
            "Authorization": "Bearer " + getToken()
        }
    })
    .then(response => {

        if (!response.ok) {
            throw new Error("Users loading failed");
        }

        return response.json();

    })
    .then(data => {

        allUsers = data;
        displayUsers(data);

    })
    .catch(error => {

        console.log("User Error :", error);

    });

}

// =====================================
// DISPLAY USERS
// =====================================
function displayUsers(users) {

    const table = document.getElementById("userTable");

    table.innerHTML = "";

    users.forEach(user => {

        let profileImg = user.profileImage;

        // Fixed: Clean up old localhost reference from database records for user profile images
        if (profileImg && profileImg.includes("localhost:8080")) {
            profileImg = profileImg.replace("http://localhost:8080", "").replace("https://localhost:8080", "");
        }

        const image =
            profileImg && profileImg.trim() !== ""
                ? profileImg
                : "../images/user.png";

        const status = user.accountLocked
            ? '<span class="blocked">Blocked</span>'
            : '<span class="active">Active</span>';

        table.innerHTML += `

        <tr>

            <td>

                <div class="user-info">

                    <img class="user-img"
                         src="${image}"
                         onerror="this.src='../images/user.png'">

                    <div>
                        <b>${user.firstName} ${user.lastName}</b>
                    </div>

                </div>

            </td>

            <td>${user.email}</td>

            <td>${user.mobile || "-"}</td>

            <td>
                <span class="role">${user.role}</span>
            </td>

            <td>${status}</td>

            <td class="action">

                <button class="view"
                        onclick="viewUser(${user.id})">
                    View
                </button>

            </td>

        </tr>

        `;

    });

}

// =====================================
// SEARCH USERS
// =====================================
function searchUsers() {

    const keyword = document
        .getElementById("searchUser")
        .value
        .toLowerCase();

    const filtered = allUsers.filter(user => {

        return (
            user.firstName.toLowerCase().includes(keyword) ||
            user.lastName.toLowerCase().includes(keyword) ||
            user.email.toLowerCase().includes(keyword)
        );

    });

    displayUsers(filtered);

}

// =====================================
// VIEW USER DETAILS PAGE
// =====================================
function viewUser(id) {

    window.location.href =
        "user-details.html?id=" + id;

}
