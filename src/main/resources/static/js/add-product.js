const PRODUCT_API =
"http://localhost:8080/api/products";


const CATEGORY_API =
"http://localhost:8080/api/categories";


const token =
localStorage.getItem("token");


let editId = null;



// =================================
// PAGE LOAD
// =================================

document.addEventListener(
"DOMContentLoaded",
()=>{


    loadCategories();


    const params =
    new URLSearchParams(
        window.location.search
    );


    if(params.get("id")){


        editId =
        params.get("id");


        loadProduct(editId);

    }



});




// =================================
// LOAD CATEGORY DROPDOWN
// =================================

async function loadCategories(){


try{


    const response =
    await fetch(
        CATEGORY_API,
        {

        headers:{
            "Authorization":
            "Bearer "+token
        }

        });


    const data =
    await response.json();



    // Pageable response
    const categories =
    data.content || data;



    const select =
    document.getElementById(
        "categoryId"
    );



    categories.forEach(cat=>{


        let option =
        document.createElement(
            "option"
        );


        option.value =
        cat.id;


        option.textContent =
        cat.name;


        select.appendChild(option);


    });



}

catch(error){

    console.log(
        "Category Error:",
        error
    );

}


}






// =================================
// IMAGE PREVIEW
// =================================


const imageInput =
document.getElementById("image");


if(imageInput){


imageInput.addEventListener(
"change",
function(){


    const file =
    this.files[0];


    if(file){


        const reader =
        new FileReader();



        reader.onload=function(e){


            const img =
            document.getElementById(
                "preview"
            );


            img.src =
            e.target.result;


            img.style.display =
            "block";


        }



        reader.readAsDataURL(file);


    }


});


}








// =================================
// LOAD PRODUCT FOR EDIT
// =================================


async function loadProduct(id){


try{


const response =
await fetch(
`${PRODUCT_API}/${id}`,
{

headers:{

"Authorization":
"Bearer "+token

}

});


const product =
await response.json();





document.getElementById("name").value =
product.name || "";



document.getElementById("brand").value =
product.brand || "";



document.getElementById("description").value =
product.description || "";



document.getElementById("price").value =
product.price || "";



document.getElementById("discountPrice").value =
product.discountPrice || "";



document.getElementById("quantity").value =
product.quantity || "";



document.getElementById("sku").value =
product.sku || "";



document.getElementById("active").value =
product.active;



if(product.category){

document.getElementById(
"categoryId"
).value =
product.category.id;

}





if(product.image){


const img =
document.getElementById(
"preview"
);


img.src =
product.image.startsWith("http")
?
product.image
:
"http://localhost:8080"+product.image;



img.style.display =
"block";


}



}

catch(error){

console.log(
"Product Load Error:",
error
);

}


}








// =================================
// SAVE PRODUCT
// =================================


document
.getElementById("productForm")
.addEventListener(
"submit",
async function(e){


e.preventDefault();




let formData =
new FormData();




formData.append(
"name",
document.getElementById("name").value
);



formData.append(
"brand",
document.getElementById("brand").value
);



formData.append(
"description",
document.getElementById("description").value
);



formData.append(
"price",
document.getElementById("price").value
);



formData.append(
"discountPrice",
document.getElementById("discountPrice").value
);



formData.append(
"quantity",
document.getElementById("quantity").value
);



formData.append(
"categoryId",
document.getElementById("categoryId").value
);



formData.append(
"sku",
document.getElementById("sku").value
);



formData.append(
"active",
document.getElementById("active").value
);




const image =
document.getElementById("image")
.files[0];



if(image){


formData.append(
"image",
image
);


}






let url =
PRODUCT_API;



let method =
"POST";




if(editId){


url =
`${PRODUCT_API}/${editId}`;


method =
"PUT";


}







try{


const response =
await fetch(
url,
{


method:method,


headers:{


"Authorization":
"Bearer "+token


},


body:formData


});






if(response.ok){



alert(
editId
?
"Product Updated Successfully"
:
"Product Added Successfully"
);



window.location.href =
"products.html";



}

else{


const msg =
await response.text();


console.log(msg);


alert(
"Save Failed"
);


}



}


catch(error){


console.log(error);


alert(
"Server Error"
);


}



});









// =================================
// BACK
// =================================


function backToProducts(){


window.location.href =
"products.html";


}