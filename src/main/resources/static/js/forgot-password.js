// ==========================================
// MANIKANTA SALES
// FORGOT PASSWORD
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    const forgotPasswordForm = document.getElementById("forgotPasswordForm");

    const email = document.getElementById("email");

    const forgotBtn = document.getElementById("forgotBtn");

    const forgotMessage = document.getElementById("forgotMessage");

    // ==========================================
    // SEND OTP
    // ==========================================

    forgotPasswordForm.addEventListener("submit", async (e) => {

        e.preventDefault();

        forgotMessage.textContent = "";
        forgotMessage.style.color = "";

        const emailValue = email.value.trim();

        // Email Validation

        if (emailValue === "") {

            forgotMessage.style.color = "red";
            forgotMessage.textContent = "Email is required.";

            return;

        }

        const emailRegex =
                /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        if (!emailRegex.test(emailValue)) {

            forgotMessage.style.color = "red";
            forgotMessage.textContent = "Enter a valid email address.";

            return;

        }

        forgotBtn.disabled = true;
        forgotBtn.textContent = "Sending OTP...";

        try {

            const response = await fetch(FORGOT_PASSWORD_API, {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    email: emailValue

                })

            });

            const message = await response.text();

            if (!response.ok) {

                forgotMessage.style.color = "red";
                forgotMessage.textContent = message;

                return;

            }

            forgotMessage.style.color = "green";
            forgotMessage.textContent = message;

            // Save Email for Reset Password

            sessionStorage.setItem(

                    "resetEmail",

                    emailValue

            );

            // Redirect

            setTimeout(() => {

                window.location.href = "reset-password.html";

            }, 1500);

        }

        catch (error) {

            console.error(error);

            forgotMessage.style.color = "red";
            forgotMessage.textContent =
                    "Unable to connect to server.";

        }

        finally {

            forgotBtn.disabled = false;
            forgotBtn.textContent = "Send OTP";

        }

    });

});