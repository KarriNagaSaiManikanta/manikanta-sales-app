// =====================================
// PAGE LOAD
// =====================================

document.addEventListener("DOMContentLoaded",()=>{

    loadUserProfile();

    loadProducts();
	loadWishlist();
	
	loadCart();

});




// =====================================
// LOAD USER PROFILE
// =====================================

function loadUserProfile(){


const token = localStorage.getItem("token");


if(!token){

    setDefaultUser();

    return;

}



fetch("/api/users/profile",{

    method:"GET",

    headers:{

        "Authorization":"Bearer "+token,

        "Content-Type":"application/json"

    }

})


.then(response=>{


    if(!response.ok){

        throw new Error(
            "Profile loading failed"
        );

    }


    return response.json();


})


.then(user=>{


const profile =

user.user ||

user.data ||

user;



const firstName =

profile.firstName || "";



const lastName =

profile.lastName || "";



const fullName =

(firstName+" "+lastName).trim();



const displayName =

fullName ||

"User";





// NAME

let welcome =
document.getElementById(
"welcomeUserName"
);


if(welcome){

welcome.innerText=displayName;

}




let name =
document.getElementById(
"userName"
);


if(name){

name.innerText=displayName;

}




// EMAIL

let email =
document.getElementById(
"userEmail"
);


if(email){

email.innerText =
profile.email || "No email";

}




// IMAGE

let image =
document.getElementById(
"userProfileImage"
);



if(image && profile.profileImage){


image.src =

profile.profileImage.startsWith("http")

?

profile.profileImage

:

profile.profileImage;



}




})


.catch(error=>{


console.log(
"Profile Error:",
error
);


setDefaultUser();


});


}






// =====================================
// DEFAULT USER
// =====================================

function setDefaultUser(){


let welcome =
document.getElementById(
"welcomeUserName"
);


let name =
document.getElementById(
"userName"
);


let email =
document.getElementById(
"userEmail"
);



if(welcome)
welcome.innerText="User";


if(name)
name.innerText="User";


if(email)
email.innerText="No email";


}








// =====================================
// LOAD PRODUCTS
// =====================================

function loadProducts(){


fetch("/api/products")


.then(response=>{


if(!response.ok){

throw new Error(
"Products loading failed"
);

}


return response.json();


})


.then(response=>{


let products=[];



if(Array.isArray(response)){


products=response;


}


else if(Array.isArray(response.content)){


products=response.content;


}


else if(Array.isArray(response.products)){


products=response.products;


}



displayProducts(products);



})


.catch(error=>{


console.log(
"Product Error:",
error
);


});


}









// =====================================
// DISPLAY PRODUCTS
// =====================================

function displayProducts(products){


const container =
document.getElementById(
"product-container"
);



if(!container){

return;

}



container.innerHTML="";




products.forEach(product=>{


let image="/images/product.png";



if(product.image){


image =

product.image.startsWith("http")

?

product.image

:

product.image;


}




container.innerHTML +=`


<div class="product-card">


<div class="product-image">

<img src="${image}">

</div>



<h3>

${product.name || ""}

</h3>



<p class="category">

${product.category?.name || ""}

</p>



<h2 class="price">

₹ ${product.price || 0}

</h2>




<div class="product-actions">


<button

class="details-btn"

onclick="viewProduct(${product.id})">

View Details

</button>




<button

class="cart-btn"

onclick="addToCart(${product.id})">

Add Cart

</button>




<button

class="wishlist-btn"

onclick="addWishlist(${product.id})">

❤️

</button>



</div>


</div>


`;



});


}




// =====================================
// ADD TO CART
// =====================================

function addToCart(productId){

    const token = localStorage.getItem("token");

    if(!token){
        alert("Please login first");
        window.location.href = "login.html";
        return;
    }

    fetch(`/api/cart/add?productId=${productId}&quantity=1`,{

        method:"POST",

        headers:{
            "Authorization":"Bearer " + token
        }

    })

    .then(async response=>{

        const message = await response.text();

        console.log("Cart Response :", message);

        if(!response.ok){
            throw new Error(message);
        }

        return message;

    })

    .then(data=>{

        alert("Product added to Cart 🛒");

        loadCart();

        setTimeout(() => {

            location.reload();

        }, 500);

    })

    .catch(error=>{

        console.log(error.message);

        if(error.message.includes("Already")){

            alert("Product already in Cart 🛒");

        }else{

            alert("Product out of Stock");

        }

    });

}
function loadCart(){

    const token = localStorage.getItem("token");

    if(!token){
        return Promise.resolve();
    }

    return fetch("/api/cart",{

        method:"GET",

        headers:{
            "Authorization":"Bearer " + token
        }

    })
    .then(response=>{

        if(!response.ok){
            throw new Error("Cart loading failed");
        }

        return response.json();

    })
    .then(data=>{

        console.log("MY CART :", data);

        const count = document.getElementById("cartCount");

        if(count){
            count.innerText = data.length;
        }

    });

}
// =====================================
// ADD TO WISHLIST
// =====================================

function addWishlist(productId){

    const token = localStorage.getItem("token");


    if(!token){

        alert("Please login first");

        window.location.href="login.html";

        return;

    }


    fetch(`/api/wishlist/add?productId=${productId}`,{

        method:"POST",

        headers:{

            "Authorization":"Bearer "+token

        }

    })


    .then(async response=>{


        let message = await response.text();


        console.log("Wishlist Response:",message);



        if(!response.ok){

            throw new Error(message);

        }


        return message;


    })

	.then(data=>{

	    alert("Added to Wishlist ❤️");

	    loadWishlist();

	    setTimeout(()=>{

	        location.reload();

	    },500);

	})

    .catch(error=>{


        console.log(error.message);



        if(error.message.includes("Already in wishlist")){


            alert("Already added in Wishlist ❤️");


        }

        else{


            alert("Wishlist add failed");


        }


    });


}

// =====================================
// LOAD MY WISHLIST
// =====================================

function loadWishlist(){


    const token = localStorage.getItem("token");


    if(!token){

        return;

    }



    fetch("/api/wishlist/my",{


        method:"GET",


        headers:{


            "Authorization":"Bearer "+token


        }


    })


    .then(response=>{


        if(!response.ok){

            throw new Error("Wishlist loading failed");

        }


        return response.json();


    })


    .then(data=>{


        console.log("MY WISHLIST :",data);



        const count =
        document.getElementById("wishlistCount");



        if(count){

            count.innerText=data.length;

        }



    })


    .catch(error=>{


        console.log("Wishlist Error :",error);


    });


}
// =====================================
// VIEW PRODUCT
// =====================================

function viewProduct(id){


window.location.href =

"/uproduct-details.html?id="+id;


}









// =====================================
// CART PAGE NAVIGATION
// =====================================

function openCart(){


window.location.href="/cart.html";


}







// =====================================
// WISHLIST PAGE NAVIGATION
// =====================================

function openWishlist(){


window.location.href="/wishlist.html";


}







// =====================================
// NAVBAR SUPPORT
// =====================================

function goCart(){


openCart();


}



function goWishlist(){


openWishlist();


}









// =====================================
// LOGOUT
// =====================================

function logout(){


localStorage.removeItem("token");


localStorage.removeItem("user");



window.location.href="/login.html";


}

