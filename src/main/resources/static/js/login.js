// ======================================
// LOGIN
// ======================================

loginForm.addEventListener("submit", async (e) => {

    e.preventDefault();

    loginMessage.textContent = "";
    loginMessage.style.color = "";

    loginBtn.disabled = true;
    loginBtn.textContent = "Logging in...";

    try {

        const response = await fetch(LOGIN_API, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email.value.trim(),
                password: password.value
            })

        });

        // Read Response
        const text = await response.text();

        console.log("Status :", response.status);
        console.log("Response :", text);

        let data = {};

        if (text) {
            data = JSON.parse(text);
        }

        // ==========================
        // LOGIN FAILED
        // ==========================

        if (!response.ok) {

            loginMessage.style.color = "red";
            loginMessage.textContent =
                data.message || "Invalid Email or Password";

            return;
        }

        // ==========================
        // SAVE LOGIN DETAILS
        // ==========================

        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);

        // Decode JWT
        const payload = JSON.parse(atob(data.token.split(".")[1]));

        localStorage.setItem("loggedInUser", payload.sub);

        console.log("Logged In User :", payload.sub);
        console.log("Role :", data.role);

        // ==========================
        // SUCCESS MESSAGE
        // ==========================

        loginMessage.style.color = "green";
        loginMessage.textContent =
            data.message || "Login Successful";

        // ==========================
        // REDIRECT
        // ==========================

        setTimeout(() => {

            if (data.role === "ADMIN") {

                window.location.href = "admin/dashboard.html";

            } else {

                window.location.href = "index.html";

            }

        }, 1000);

    } catch (error) {

        console.error("Login Error :", error);

        loginMessage.style.color = "red";
        loginMessage.textContent =
            "Unable to connect to server.";

    } finally {

        loginBtn.disabled = false;
        loginBtn.textContent = "Login";
    }

});