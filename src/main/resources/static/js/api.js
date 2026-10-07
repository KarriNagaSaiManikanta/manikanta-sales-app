// ==========================================
// MANIKANTA SALES
// API CONFIG=========

// Base URL
const API_BASE_URL = "http://localhost:8080/api";

// ==========================================
// AUTH APIs
// ==========================================

const AUTH_API = API_BASE_URL + "/auth";

const REGISTER_API = AUTH_API + "/register";
const VERIFY_OTP_API = AUTH_API + "/verify-otp";
const RESEND_OTP_API = AUTH_API + "/resend-otp";
const LOGIN_API = AUTH_API + "/login";
const FORGOT_PASSWORD_API = AUTH_API + "/forgot-password";
const RESET_PASSWORD_API = AUTH_API + "/reset-password";
const CHANGE_PASSWORD_API = AUTH_API + "/change-password";

// ==========================================
// PRODUCT APIs
// ==========================================

const PRODUCT_API = API_BASE_URL + "/products";

// ==========================================
// CATEGORY APIs
// ==========================================

const CATEGORY_API = API_BASE_URL + "/categories";

// ==========================================
// CART APIs
// ==========================================

const CART_API = API_BASE_URL + "/cart";

// ==========================================
// WISHLIST APIs
// ==========================================

const WISHLIST_API = API_BASE_URL + "/wishlist";

// ==========================================
// ORDER APIs
// ==========================================

const ORDER_API = API_BASE_URL + "/orders";

// ==========================================
// ADMIN APIs
// ==========================================

const ADMIN_API = API_BASE_URL + "/admin";

// ==========================================
// TOKEN
// ==========================================

function getToken() {
    return localStorage.getItem("token");
}

// ==========================================
// JSON HEADERS
// ==========================================

function getHeaders() {
    const token = getToken();

    return {
        "Content-Type": "application/json",
        "Authorization": token ? "Bearer " + token : ""
    };
}

// ==========================================
// FORM DATA HEADERS
// ==========================================

function getFormHeaders() {
    const token = getToken();

    return {
        "Authorization": token ? "Bearer " + token : ""
    };
}

// ==========================================
// COMMON API REQUEST
// ==========================================

async function apiRequest(url, method = "GET", body = null, formData = false) {

    const options = {
        method: method,
        headers: formData ? getFormHeaders() : getHeaders()
    };

    if (body) {
        options.body = formData ? body : JSON.stringify(body);
    }

    const response = await fetch(url, options);

    if (response.status === 401) {

        localStorage.removeItem("token");
        localStorage.removeItem("loggedInUser");

        window.location.href = "/login.html";
        return null;
    }

    return response;
}

// ==========================================
// LOGIN CHECK
// ==========================================

function isLoggedIn() {
    return getToken() !== null;
}

// ==========================================
// LOGOUT
// ==========================================

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("loggedInUser");
    window.location.href = "/login.html";
}

// ==========================================
// USER EMAIL
// ==========================================

function getLoggedInUser() {
    return localStorage.getItem("loggedInUser");
}