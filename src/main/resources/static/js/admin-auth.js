// ======================================
// ADMIN AUTH CHECK
// ======================================

(function () {

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    // ==================================
    // 1. NOT LOGGED IN
    // ==================================

    if (!token) {

        window.location.replace("/login.html");

        return;
    }

    // ==================================
    // 2. LOGGED IN BUT NOT ADMIN
    // ==================================

    if (role !== "ADMIN") {

        alert("Access Denied");

        window.location.replace("/index.html");

        return;
    }

})();