// ==========================================
// MANIKANTA SALES
// REGISTER
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    const registerForm = document.getElementById("registerForm");

    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const email = document.getElementById("email");
    const mobile = document.getElementById("mobile");
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirmPassword");

    const registerBtn = document.getElementById("registerBtn");
    const registerMessage = document.getElementById("registerMessage");

    const togglePassword = document.getElementById("togglePassword");
    const toggleConfirmPassword = document.getElementById("toggleConfirmPassword");

    // ==========================================
    // SHOW PASSWORD
    // ==========================================

    togglePassword.addEventListener("click", () => {

        if (password.type === "password") {

            password.type = "text";
            togglePassword.textContent = "visibility_off";

        } else {

            password.type = "password";
            togglePassword.textContent = "visibility";

        }

    });

    // ==========================================
    // SHOW CONFIRM PASSWORD
    // ==========================================

    toggleConfirmPassword.addEventListener("click", () => {

        if (confirmPassword.type === "password") {

            confirmPassword.type = "text";
            toggleConfirmPassword.textContent = "visibility_off";

        } else {

            confirmPassword.type = "password";
            toggleConfirmPassword.textContent = "visibility";

        }

    });

    // ==========================================
    // REGISTER
    // ==========================================

    registerForm.addEventListener("submit", async (e) => {

        e.preventDefault();

        registerMessage.textContent = "";
        registerMessage.style.color = "";

        // First Name

        if (firstName.value.trim().length < 3) {

            registerMessage.style.color = "red";
            registerMessage.textContent = "First Name must be at least 3 characters.";

            return;

        }

        // Last Name

        if (lastName.value.trim().length < 1) {

            registerMessage.style.color = "red";
            registerMessage.textContent = "Last Name is required.";

            return;

        }

        // Mobile

        if (!/^[6-9]\d{9}$/.test(mobile.value.trim())) {

            registerMessage.style.color = "red";
            registerMessage.textContent = "Enter valid Mobile Number.";

            return;

        }

        // Password Match

        if (password.value !== confirmPassword.value) {

            registerMessage.style.color = "red";
            registerMessage.textContent = "Passwords do not match.";

            return;

        }

        registerBtn.disabled = true;
        registerBtn.textContent = "Creating Account...";

        try {

            const response = await fetch(REGISTER_API, {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    firstName: firstName.value.trim(),

                    lastName: lastName.value.trim(),

                    email: email.value.trim(),

                    mobile: mobile.value.trim(),

                    password: password.value

                })

            });

            const message = await response.text();

            if (!response.ok) {

                registerMessage.style.color = "red";
                registerMessage.textContent = message;

                return;

            }

            registerMessage.style.color = "green";
            registerMessage.textContent = message;

            // Save email for OTP verification

            sessionStorage.setItem(

                    "verifyEmail",

                    email.value.trim()

            );

            setTimeout(() => {

                window.location.href = "verify-otp.html";

            }, 1500);

        }

        catch (error) {

            console.error(error);

            registerMessage.style.color = "red";
            registerMessage.textContent = "Unable to connect to server.";

        }

        finally {

            registerBtn.disabled = false;
            registerBtn.textContent = "Create Account";

        }

    });

});