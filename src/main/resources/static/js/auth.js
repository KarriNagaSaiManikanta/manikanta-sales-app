// ======================================
// USER LOGIN CHECK
// ======================================

(function () {

    const token = localStorage.getItem("token");

    // Login avvakapothe
    if (!token) {

        window.location.replace("/login.html");

    }

})();