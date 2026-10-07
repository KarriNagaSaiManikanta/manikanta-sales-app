document.addEventListener(
"DOMContentLoaded",
()=>{

    loadNavbar();

});




// ===============================
// LOAD NAVBAR
// ===============================

function loadNavbar(){


fetch("../components/navbar.html")


.then(res=>res.text())


.then(data=>{


document.getElementById(
"navbar-container"
).innerHTML=data;



setNavbarPage();


loadCartCount();

loadWishlistCount();



});


}





// ===============================
// PAGE BASED NAVBAR
// ===============================

function setNavbarPage(){


let page =
document.body.dataset.page;


let searchBox =
document.getElementById("navbarSearch");


let title =
document.getElementById("pageTitle");



if(page==="products"){


    searchBox.style.display="flex";

    title.style.display="none";


}



else if(page==="product-details"){


    searchBox.style.display="none";

    title.style.display="block";

    title.innerText =
    "Product Details";


}

else if(page==="wish"){


    searchBox.style.display="none";

    title.style.display="block";

    title.innerText =
    "Wishlist ❤️ Details";


}



else if(page==="myorder"){


    searchBox.style.display="none";

    title.style.display="block";

    title.innerText =
    "Orders  Details";


}
else if(page==="chechout"){


    searchBox.style.display="none";

    title.style.display="block";

    title.innerText =
    "Chechout  Details";


}

else if(page==="cart"){


    searchBox.style.display="none";

    title.style.display="block";

    title.innerText =
    "Cart  Details";


}
else{


    searchBox.style.display="none";

    title.style.display="none";


}


}


// ===============================
// SEARCH
// ===============================

function searchProducts(){


let value =
document.getElementById(
"searchInput"
).value;



if(value.trim()){


window.location.href =
"products.html?search="+value;


}



}




// ===============================
// CART COUNT
// ===============================

function loadCartCount(){


let cart =
JSON.parse(
localStorage.getItem("cart")
) || [];



let count =
document.getElementById(
"cartCount"
);



if(count){

count.innerText =
cart.length;

}


}





// ===============================
// WISHLIST COUNT
// ===============================

function loadWishlistCount(){


let wishlist =
JSON.parse(
localStorage.getItem("wishlist")
) || [];



let count =
document.getElementById(
"wishlistCount"
);



if(count){

count.innerText =
wishlist.length;

}


}