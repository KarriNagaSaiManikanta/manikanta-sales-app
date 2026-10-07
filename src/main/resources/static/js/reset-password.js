// ==========================================
// MANIKANTA SALES
// RESET PASSWORD
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    const resetPasswordForm = document.getElementById("resetPasswordForm");

    const email = document.getElementById("email");
    const otp = document.getElementById("otp");

    const newPassword = document.getElementById("newPassword");
    const confirmPassword = document.getElementById("confirmPassword");

    const toggleNewPassword =
            document.getElementById("toggleNewPassword");

    const toggleConfirmPassword =
            document.getElementById("toggleConfirmPassword");

    const resetBtn = document.getElementById("resetBtn");

    const resetMessage =
            document.getElementById("resetMessage");

    // ======================================
    // LOAD EMAIL
    // ======================================

    const resetEmail =
            sessionStorage.getItem("resetEmail");

    if (!resetEmail) {

        alert("Please request OTP first.");

        window.location.href = "forgot-password.html";

        return;

    }

    email.value = resetEmail;

    // ======================================
    // SHOW / HIDE PASSWORD
    // ======================================

    function togglePassword(input, icon) {

        if (input.type === "password") {

            input.type = "text";
            icon.textContent = "visibility_off";

        } else {

            input.type = "password";
            icon.textContent = "visibility";

        }

    }

    toggleNewPassword.addEventListener("click", () => {

        togglePassword(newPassword, toggleNewPassword);

    });

    toggleConfirmPassword.addEventListener("click", () => {

        togglePassword(confirmPassword, toggleConfirmPassword);

    });

    // ======================================
    // RESET PASSWORD
    // ======================================

    resetPasswordForm.addEventListener("submit", async (e) => {

        e.preventDefault();

        resetMessage.textContent = "";
        resetMessage.style.color = "";

        if (otp.value.trim() === "") {

            resetMessage.style.color = "red";
            resetMessage.textContent = "OTP is required.";

            return;

        }

        if (newPassword.value.length < 6) {

            resetMessage.style.color = "red";
            resetMessage.textContent =
                    "Password must be at least 6 characters.";

            return;

        }

        if (newPassword.value !== confirmPassword.value) {

            resetMessage.style.color = "red";
            resetMessage.textContent =
                    "Passwords do not match.";

            return;

        }

        resetBtn.disabled = true;
        resetBtn.textContent = "Updating...";

        try {

            const response = await fetch(RESET_PASSWORD_API, {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    email: email.value,

                    otp: otp.value.trim(),

                    newPassword: newPassword.value

                })

            });

            const message = await response.text();

            if (!response.ok) {

                resetMessage.style.color = "red";
                resetMessage.textContent = message;

                return;

            }

            resetMessage.style.color = "green";
            resetMessage.textContent = message;

            sessionStorage.removeItem("resetEmail");

            setTimeout(() => {

                window.location.href = "login.html";

            }, 1500);

        }

        catch (error) {

            console.error(error);

            resetMessage.style.color = "red";
            resetMessage.textContent =
                    "Unable to connect to server.";

        }

        finally {

            resetBtn.disabled = false;
            resetBtn.textContent = "Update Password";

        }

    });

});