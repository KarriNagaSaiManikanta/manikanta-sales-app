// =====================================================
// MANIKANTA SALES
// HERO SLIDER
// =====================================================

document.addEventListener("DOMContentLoaded", () => {

    const heroImage = document.getElementById("heroSlider");

    if (!heroImage) return;

    // ==========================================
    // HERO BANNERS
    // ==========================================

    const banners = [

        "images/banner1.png",

        "images/banner2.png",

        "images/banner3.png",

        "images/banner4.png"

    ];

    let current = 0;

    // ==========================================
    // CHANGE SLIDE
    // ==========================================

    function showSlide(index) {

        heroImage.style.opacity = "0";

        setTimeout(() => {

            heroImage.src = banners[index];

            heroImage.style.opacity = "1";

        }, 300);

    }

    // ==========================================
    // NEXT
    // ==========================================

    function nextSlide() {

        current++;

        if (current >= banners.length) {

            current = 0;

        }

        showSlide(current);

    }

    // ==========================================
    // PREVIOUS
    // ==========================================

    function previousSlide() {

        current--;

        if (current < 0) {

            current = banners.length - 1;

        }

        showSlide(current);

    }

    // ==========================================
    // AUTO SLIDE
    // ==========================================

    setInterval(nextSlide, 5000);

    // ==========================================
    // BUTTONS
    // ==========================================

    const nextBtn = document.querySelector(".hero-next");
    const prevBtn = document.querySelector(".hero-prev");

    if (nextBtn) {

        nextBtn.addEventListener("click", nextSlide);

    }

    if (prevBtn) {

        prevBtn.addEventListener("click", previousSlide);

    }

});