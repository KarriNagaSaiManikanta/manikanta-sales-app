const token =
localStorage.getItem("token");



document.addEventListener(
"DOMContentLoaded",
()=>{


if(!token){

window.location.href="login.html";

return;

}


loadWishlist();


});


// ===============================
// LOAD WISHLIST
// ===============================

function loadWishlist(){


const token = localStorage.getItem("token");


fetch("/api/wishlist/my",{


method:"GET",


headers:{


"Authorization":
"Bearer " + token


}


})


.then(response=>{


if(!response.ok){

throw new Error(
"Wishlist loading failed"
);

}


return response.json();


})


.then(data=>{


console.log(
"WISHLIST DATA",
data
);


displayWishlist(data);


})


.catch(error=>{


console.error(error);


document.getElementById(
"wishlistContainer"
).innerHTML=`

<h2>
Unable to load wishlist
</h2>

`;


});


}

function displayWishlist(data){


const container =
document.getElementById("wishlistContainer");

if (!container) return;

let html="";

if (!data || data.length === 0) {
    container.innerHTML = `<h2>Your Wishlist is Empty ❤️</h2>`;
    return;
}

data.forEach(item=>{


const product=item.product;

let imgUrl = product.image;

// Clean up old localhost reference from database records
if (imgUrl && imgUrl.includes("localhost:8080")) {
    imgUrl = imgUrl.replace("http://localhost:8080", "").replace("https://localhost:8080", "");
}

const image =
    imgUrl && imgUrl.trim() !== ""
        ? imgUrl
        : "/images/no-image.png";


html += `

<div class="wishlist-card">


<img src="${image}"
onerror="this.onerror=null; this.src='/images/no-image.png'">


<h3>
${product.name}
</h3>


<p>
Brand :
${product.brand || "N/A"}
</p>


<h2>
₹${product.price}
</h2>


<button onclick="addToCart(${product.id})">

Add Cart 🛒

</button>



<button onclick="removeWishlist(${product.id})">

Remove ❌

</button>


</div>

`;

});


container.innerHTML=html;


}

function removeWishlist(productId){


const token =
localStorage.getItem("token");


fetch(
"/api/wishlist/remove?productId="+productId,
{

method:"DELETE",

headers:{
"Authorization":
"Bearer "+token
}

}

)
.then(res=>{


if(res.ok){

alert("Removed ❤️");

loadWishlist();

}

});


}

// =====================================
// ADD TO CART FROM WISHLIST
// =====================================

function addToCart(productId){


    const token = localStorage.getItem("token");


    if(!token){

        alert("Please login first");

        window.location.href="login.html";

        return;

    }



    fetch(
        "/api/cart/add?productId="
        +productId
        +"&quantity=1",
        {

        method:"POST",

        headers:{

            "Authorization":
            "Bearer "+token

        }

    })

    .then(res=>{


        if(!res.ok){

            throw new Error(
                "Cart failed"
            );

        }


        return res.text();


    })


    .then(data=>{


        console.log(data);


        alert(
            "Product added to cart 🛒"
        );


        window.location.href="cart.html";


    })


    .catch(err=>{


        console.error(err);


        alert(
            "Unable to add cart"
        );


    });


}