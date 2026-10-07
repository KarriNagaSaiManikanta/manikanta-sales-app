const USER_API = "http://localhost:8080/api/users";

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

    fetch(USER_API + "/all", {
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

        const image =
            user.profileImage && user.profileImage.trim() !== ""
                ? "http://localhost:8080" + user.profileImage
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
// =====================================
// ADD USER
// =====================================
