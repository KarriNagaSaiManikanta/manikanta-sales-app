// ======================================================
// MANIKANTA SALES - PROFESSIONAL NAVBAR
// Part 1 - Navbar UI
// ======================================================

document.addEventListener("DOMContentLoaded", () => {
    loadNavbar();
});

// ======================================================
// LOAD NAVBAR
// ======================================================

function loadNavbar() {

    const navbar = document.getElementById("navbar");

    if (!navbar) return;

    const token = localStorage.getItem("token");
    const email = localStorage.getItem("loggedInUser");

    let accountSection = "";

    if (token && email) {

        const username = email.split("@")[0];

        accountSection = `
            <div class="profile-dropdown">

                <button class="profile-btn" id="profileBtn">

                    <span class="material-icons">
                        account_circle
                    </span>

                    <span>${username}</span>

                    <span class="material-icons">
                        expand_more
                    </span>

                </button>

                <div class="profile-menu" id="profileMenu">

                    <a href="profile.html">
                        My Profile
                    </a>

                    <a href="orders.html">
                        My Orders
                    </a>

                    <a href="wishlist.html">
                        Wishlist
                    </a>

                    <a href="change-password.html">
                        Change Password
                    </a>

                    <hr>

                    <a href="#" id="logoutBtn">
                        Logout
                    </a>

                </div>

            </div>
        `;

    } else {

        accountSection = `

            <a href="login.html" class="login-btn">

                <span class="material-icons">
                    person
                </span>

                Login

            </a>

            <a href="register.html">
                Register
            </a>

        `;
    }

    navbar.innerHTML = `

<header>

    <!-- ========================= -->
    <!-- TOP NAVBAR -->
    <!-- ========================= -->

    <nav class="navbar">

        <div class="container navbar-container">

            <!-- Logo -->

            <div class="logo">

                <a href="index.html">

                    🛒 <span>Manikanta Sales</span>

                </a>

            </div>


            <!-- Search -->

            <div class="search-box">

                <input
                    type="text"
                    id="searchInput"
                    placeholder="Search Products...">

                <button id="searchBtn">

                    <span class="material-icons">

                        search

                    </span>

                </button>

            </div>


            <!-- Mobile Menu -->

            <button class="mobile-menu-btn">

                <span class="material-icons">

                    menu

                </span>

            </button>


            <!-- Right Menu -->

            <div class="right-menu">

                ${accountSection}

                <a href="wishlist.html" class="icon-link">

                    <span class="material-icons">

                        favorite_border

                    </span>

                    <span
                        class="badge"
                        id="wishlistBadge">

                        0

                    </span>

                </a>

                <a href="cart.html" class="icon-link">

                    <span class="material-icons">

                        shopping_cart

                    </span>

                    <span
                        class="badge"
                        id="cartBadge">

                        0

                    </span>

                </a>

            </div>

        </div>

    </nav>



    <!-- ========================= -->
    <!-- CATEGORY MENU -->
    <!-- ========================= -->

    <div class="menu-bar">

        <div class="container">

            <ul class="menu-list">

                <li>

                    <a href="index.html">

                        Home

                    </a>

                </li>

                <li>

                    <a href="products.html">

                        Products

                    </a>

                </li>

                <li>

                    <a href="products.html?category=Mobiles">

                        Mobiles

                    </a>

                </li>

                <li>

                    <a href="products.html?category=Laptops">

                        Laptops

                    </a>

                </li>

                <li>

                    <a href="products.html?category=Electronics">

                        Electronics

                    </a>

                </li>

                <li>

                    <a href="products.html?category=Fashion">

                        Fashion

                    </a>

                </li>

                <li>

                    <a href="products.html?category=Home Appliances">

                        Home Appliances

                    </a>

                </li>

                <li>

                    <a href="contact.html">

                        Contact

                    </a>

                </li>

            </ul>

        </div>

    </div>

</header>

`;

    initializeNavbar();
}
// ======================================================
// INITIALIZE NAVBAR
// ======================================================

function initializeNavbar() {

    setupSearch();

    updateBadges();

    setupMobileMenu();

    setupStickyNavbar();

    setupProfileMenu();

    highlightActiveMenu();

}


// ======================================================
// SEARCH
// ======================================================

function setupSearch() {

    const input = document.getElementById("searchInput");

    const button = document.getElementById("searchBtn");

    if (!input || !button) return;

    function searchProducts() {

        const keyword = input.value.trim();

        if (keyword === "") {

            input.focus();

            return;

        }

        window.location.href =
            "products.html?search=" +
            encodeURIComponent(keyword);

    }

    button.addEventListener("click", searchProducts);

    input.addEventListener("keypress", function (e) {

        if (e.key === "Enter") {

            searchProducts();

        }

    });

}


// ======================================================
// UPDATE CART & WISHLIST BADGES
// ======================================================

function updateBadges() {

    let cart = [];

    let wishlist = [];

    try {

        cart = JSON.parse(localStorage.getItem("cart")) || [];

    } catch (e) {

        cart = [];

    }

    try {

        wishlist = JSON.parse(localStorage.getItem("wishlist")) || [];

    } catch (e) {

        wishlist = [];

    }

    const cartBadge =
        document.getElementById("cartBadge");

    const wishlistBadge =
        document.getElementById("wishlistBadge");

    if (cartBadge) {

        cartBadge.textContent = cart.length;

        cartBadge.style.display =
            cart.length > 0 ? "flex" : "none";

    }

    if (wishlistBadge) {

        wishlistBadge.textContent = wishlist.length;

        wishlistBadge.style.display =
            wishlist.length > 0 ? "flex" : "none";

    }

}


// ======================================================
// REFRESH BADGES AFTER ADD/REMOVE
// ======================================================

function refreshNavbarBadges() {

    updateBadges();

}


// ======================================================
// OPTIONAL HELPERS
// ======================================================

function getCartCount() {

    try {

        const cart =
            JSON.parse(localStorage.getItem("cart")) || [];

        return cart.length;

    } catch {

        return 0;

    }

}

function getWishlistCount() {

    try {

        const wishlist =
            JSON.parse(localStorage.getItem("wishlist")) || [];

        return wishlist.length;

    } catch {

        return 0;

    }

}
// ======================================================
// MOBILE MENU
// ======================================================

// ======================================================
// MOBILE MENU FIXED
// ======================================================

function setupMobileMenu() {


    const menuButton = document.querySelector(".mobile-menu-btn");

    const menuBar = document.querySelector(".menu-bar");

    const menuList = document.querySelector(".menu-list");


    if (!menuButton || !menuBar || !menuList) {
        return;
    }



    // Hamburger click

    menuButton.addEventListener("click", function(e){


        e.stopPropagation();


        menuBar.classList.toggle("show");


        menuButton.classList.toggle("active");


        const icon =
        menuButton.querySelector(".material-icons");


        if(menuBar.classList.contains("show")){

            icon.textContent="close";

        }
        else{

            icon.textContent="menu";

        }


    });





    // Menu item click close


    document.querySelectorAll(".menu-list a")
    .forEach(link=>{


        link.addEventListener("click",()=>{


            menuBar.classList.remove("show");


            menuButton.classList.remove("active");


            const icon =
            menuButton.querySelector(".material-icons");


            if(icon){

                icon.textContent="menu";

            }


        });


    });





    // Resize reset


    window.addEventListener("resize",()=>{


        if(window.innerWidth > 768){


            menuBar.classList.remove("show");


            menuButton.classList.remove("active");


            const icon =
            menuButton.querySelector(".material-icons");


            if(icon){

                icon.textContent="menu";

            }


        }


    });





    // Outside click close


    document.addEventListener("click",function(e){


        if(

            !menuBar.contains(e.target)

            &&

            !menuButton.contains(e.target)

        ){


            menuBar.classList.remove("show");


            menuButton.classList.remove("active");


            const icon =
            menuButton.querySelector(".material-icons");


            if(icon){

                icon.textContent="menu";

            }


        }


    });


}

// ======================================================
// STICKY NAVBAR
// ======================================================

function setupStickyNavbar() {

    const navbar = document.querySelector(".navbar");

    if (!navbar) return;

    window.addEventListener("scroll", () => {

        if (window.scrollY > 30) {

            navbar.classList.add("sticky");

        } else {

            navbar.classList.remove("sticky");

        }

    });

}


// ======================================================
// ACTIVE MENU
// ======================================================

function highlightActiveMenu() {

    const currentPage =
        window.location.pathname.split("/").pop() || "index.html";

    document.querySelectorAll(".menu-list a").forEach(link => {

        const href = link.getAttribute("href");

        if (!href) return;

        // Exact page

        if (href === currentPage) {

            link.classList.add("active");

        }

        // Products page with query params

        if (
            currentPage === "products.html" &&
            href.startsWith("products.html")
        ) {

            link.classList.add("active");

        }

    });

}


// ======================================================
// OPTIONAL
// Close Mobile Menu if clicked outside
// ======================================================

document.addEventListener("click", function (e) {

    const menu = document.querySelector(".menu-list");

    const button = document.querySelector(".mobile-menu-btn");

    if (!menu || !button) return;

    if (
        !menu.contains(e.target) &&
        !button.contains(e.target)
    ) {

        menu.classList.remove("show");

        const icon = button.querySelector(".material-icons");

        if (icon) {

            icon.textContent = "menu";

        }

    }

});
// ======================================================
// PROFILE DROPDOWN
// ======================================================

function setupProfileMenu() {

    const profileBtn = document.getElementById("profileBtn");
    const profileMenu = document.getElementById("profileMenu");
    const logoutBtn = document.getElementById("logoutBtn");

    if (!profileBtn || !profileMenu) return;

    profileBtn.addEventListener("click", function (e) {

        e.stopPropagation();

        profileMenu.classList.toggle("show");

    });

    document.addEventListener("click", function () {

        profileMenu.classList.remove("show");

    });

    profileMenu.addEventListener("click", function (e) {

        e.stopPropagation();

    });

    if (logoutBtn) {

        logoutBtn.addEventListener("click", function (e) {

            e.preventDefault();

            logoutUser();

        });

    }

}



// ======================================================
// LOGOUT
// ======================================================

function logoutUser() {

    const confirmLogout =
        confirm("Are you sure you want to logout?");

    if (!confirmLogout) return;

    localStorage.removeItem("token");
    localStorage.removeItem("loggedInUser");
    localStorage.removeItem("user");
    localStorage.removeItem("cart");
    localStorage.removeItem("wishlist");

    alert("Logout Successful.");

    window.location.href = "login.html";

}



// ======================================================
// CHECK LOGIN
// ======================================================

function isLoggedIn() {

    const token = localStorage.getItem("token");

    return token !== null && token !== "";

}



// ======================================================
// GET USER
// ======================================================

function getLoggedInUser() {

    return localStorage.getItem("loggedInUser");

}



// ======================================================
// REFRESH NAVBAR
// ======================================================

function refreshNavbar() {

    loadNavbar();

}



// ======================================================
// PUBLIC METHODS
// ======================================================

window.updateNavbarBadges = updateBadges;
window.refreshNavbar = refreshNavbar;
window.logoutUser = logoutUser;



// ======================================================
// STORAGE LISTENER
// Sync Navbar Between Tabs
// ======================================================

window.addEventListener("storage", function () {

    updateBadges();

});



// ======================================================
// ESC KEY CLOSE PROFILE
// ======================================================

document.addEventListener("keydown", function (e) {

    if (e.key !== "Escape") return;

    const menu = document.getElementById("profileMenu");

    if (menu) {

        menu.classList.remove("show");

    }

});



// ======================================================
// PAGE LOAD
// ======================================================

window.addEventListener("load", function () {

    updateBadges();

});



//-------------------------------------------------
//-----------------------------
//----------

/*user*/
document.addEventListener("DOMContentLoaded",()=>{


    // LOAD NAVBAR

    fetch("/components/navbar.html")


    .then(response=>response.text())


    .then(data=>{


        document.getElementById("navbar")
        .innerHTML = data;



        // LOGIN BUTTON


        const loginBtn =
        document.getElementById("loginBtn");



        if(loginBtn){


            loginBtn.addEventListener(
            "click",
            ()=>{


                window.location.href =
                "/login.html";


            });


        }






        // SEARCH BUTTON


        const searchBtn =
        document.getElementById("searchBtn");



        const searchInput =
        document.getElementById("searchInput");



        if(searchBtn){


            searchBtn.addEventListener(
            "click",
            ()=>{


                let keyword =
                searchInput.value.trim();



                if(keyword){


                    window.location.href =
                    "/products.html?search="
                    + keyword;


                }


            });


        }








        // LOGOUT


        const logoutBtn =
        document.getElementById("logoutBtn");



        if(logoutBtn){


            logoutBtn.addEventListener(
            "click",
            (e)=>{


                e.preventDefault();



                localStorage.removeItem(
                "token"
                );


                localStorage.removeItem(
                "loggedInUser"
                );



                window.location.href =
                "/login.html";


            });


        }






        // CART COUNT


        let cart =
        JSON.parse(
        localStorage.getItem("cart")
        )
        || [];



        let cartCount =
        document.getElementById(
        "cartCount"
        );



        if(cartCount){


            cartCount.innerHTML =
            cart.length;


        }







        // WISHLIST COUNT


        let wishlist =
        JSON.parse(
        localStorage.getItem("wishlist")
        )
        || [];



        let wishlistCount =
        document.getElementById(
        "wishlistCount"
        );



        if(wishlistCount){


            wishlistCount.innerHTML =
            wishlist.length;


        }





    })



    .catch(error=>{


        console.log(
        "Navbar loading error:",
        error
        );


    });



});