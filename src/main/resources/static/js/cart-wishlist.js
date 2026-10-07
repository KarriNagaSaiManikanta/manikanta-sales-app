const CART_API = "/api/cart";
const WISHLIST_API = "/api/wishlist";

document.addEventListener("DOMContentLoaded", () => {
    loadSidebarCounts();
});

// ================================
// LOAD BOTH COUNTS
// ================================
function loadSidebarCounts() {

    const token = localStorage.getItem("token");

    if (!token) {
        return;
    }

    loadCartCount(token);
    loadWishlistCount(token);
}

// ================================
// CART COUNT
// ================================
function loadCartCount(token) {

    fetch(CART_API, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("Cart API Error");
        }
        return res.json();
    })
    .then(data => {

        console.log("Cart Response :", data);

        let count = Array.isArray(data) ? data.length : 0;

        const cartBadge = document.getElementById("sidebarCartCount");

        if (cartBadge) {
            cartBadge.innerText = count;
        }

    })
    .catch(err => console.error("Cart Error :", err));

}

// ================================
// WISHLIST COUNT
// ================================
function loadWishlistCount(token){

    fetch(`${WISHLIST_API}/my`,{
        headers:{
            "Authorization":"Bearer " + token
        }
    })
    .then(res=>{
        if(!res.ok){
            throw new Error("Wishlist API Error");
        }
        return res.json();
    })
    .then(data=>{

        console.log("Wishlist Response:", data);

        let count = data.length;

        const badge = document.getElementById("sidebarWishlistCount");
        if(badge){
            badge.innerText = count;
        }

    })
    .catch(err=>console.error(err));

}