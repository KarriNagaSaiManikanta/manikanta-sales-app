// ==========================================
// MANIKANTA SALES ADMIN NAVBAR
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    const navbar = `
    
    <nav class="admin-navbar">

        <div class="nav-left">

            <a href="dashboard.html" class="logo">
                Manikanta Sales
            </a>

        </div>

        <div class="nav-right">

            <a href="dashboard.html">Home</a>

            <a href="products.html">Products</a>

            <a href="orders.html">Orders</a>

            <a href="categories.html">Categories</a>

            <a href="users.html">Users</a>

            <span class="admin-email">
                ${localStorage.getItem("loggedInUser") || "Admin"}
            </span>

            <button onclick="logout()">
                Logout
            </button>

        </div>

    </nav>

    `;

    document.body.insertAdjacentHTML("afterbegin", navbar);

});


// ==========================================
// LOGOUT
// ==========================================

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("loggedInUser");

    window.location.href = "../login.html";

}