// ==========================================
// MANIKANTA SALES
// VERIFY OTP
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    const email = document.getElementById("email");
    const otp = document.getElementById("otp");

    const verifyBtn = document.getElementById("verifyBtn");
    const resendBtn = document.getElementById("resendBtn");

    const otpMessage = document.getElementById("otpMessage");
    const timer = document.getElementById("timer");

    // ======================================
    // LOAD EMAIL
    // ======================================

    const verifyEmail = sessionStorage.getItem("verifyEmail");

    if (!verifyEmail) {

        alert("Please register first.");

        window.location.href = "register.html";

        return;

    }

    email.value = verifyEmail;

    // ======================================
    // TIMER
    // ======================================

    let seconds = 60;

    const interval = setInterval(() => {

        seconds--;

        timer.textContent = seconds;

        if (seconds <= 0) {

            clearInterval(interval);

            timer.textContent = "0";

            resendBtn.disabled = false;

        }

    }, 1000);

    // ======================================
    // VERIFY OTP
    // ======================================

    document.getElementById("verifyOtpForm")

            .addEventListener("submit", async (e) => {

        e.preventDefault();

        otpMessage.textContent = "";
        otpMessage.style.color = "";

        verifyBtn.disabled = true;
        verifyBtn.textContent = "Verifying...";

        try {

            const response = await fetch(VERIFY_OTP_API, {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    email: email.value,

                    otp: otp.value.trim()

                })

            });

            const message = await response.text();

            if (!response.ok) {

                otpMessage.style.color = "red";
                otpMessage.textContent = message;

                return;

            }

            otpMessage.style.color = "green";
            otpMessage.textContent = message;

            sessionStorage.removeItem("verifyEmail");

            setTimeout(() => {

                window.location.href = "login.html";

            }, 1500);

        }

        catch (error) {

            console.error(error);

            otpMessage.style.color = "red";
            otpMessage.textContent = "Unable to connect to server.";

        }

        finally {

            verifyBtn.disabled = false;
            verifyBtn.textContent = "Verify OTP";

        }

    });

    // ======================================
    // RESEND OTP
    // ======================================

    resendBtn.addEventListener("click", async () => {

        otpMessage.textContent = "";

        resendBtn.disabled = true;
        resendBtn.textContent = "Sending...";

        try {

            // Requires backend endpoint:
            // POST /api/auth/resend-otp

            const response = await fetch(AUTH_API + "/resend-otp", {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify({

                    email: email.value

                })

            });

            const message = await response.text();

            if (!response.ok) {

                otpMessage.style.color = "red";
                otpMessage.textContent = message;

                resendBtn.disabled = false;
                resendBtn.textContent = "Resend OTP";

                return;

            }

            otpMessage.style.color = "green";
            otpMessage.textContent = message;

            // Restart Timer

            let seconds = 60;

            timer.textContent = seconds;

            const newInterval = setInterval(() => {

                seconds--;

                timer.textContent = seconds;

                if (seconds <= 0) {

                    clearInterval(newInterval);

                    resendBtn.disabled = false;

                    resendBtn.textContent = "Resend OTP";

                }

            }, 1000);

        }

        catch (error) {

            console.error(error);

            otpMessage.style.color = "red";
            otpMessage.textContent = "Unable to connect to server.";

            resendBtn.disabled = false;
            resendBtn.textContent = "Resend OTP";

        }

    });

});