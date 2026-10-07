// ==========================================
// ADD CATEGORY JS
// Manikanta Sales Admin
// ==========================================


// ==========================================
// ELEMENTS
// ==========================================


const form =
    document.getElementById("categoryForm");


const nameInput =
    document.getElementById("name");


const descriptionInput =
    document.getElementById("description");


const imageInput =
    document.getElementById("image");


const activeInput =
    document.getElementById("active");


const preview =
    document.getElementById("preview");


const loader =
    document.getElementById("loader");


const toast =
    document.getElementById("toast");





// ==========================================
// SELECTED IMAGE
// ==========================================


let selectedImage = null;







// ==========================================
// LOADER
// ==========================================


function showLoader(){


    if(loader){

        loader.style.display = "flex";

    }

}



function hideLoader(){


    if(loader){

        loader.style.display = "none";

    }

}







// ==========================================
// TOAST
// ==========================================


function showToast(message){


    if(!toast)
        return;



    toast.innerHTML = message;


    toast.classList.add("show");



    setTimeout(()=>{


        toast.classList.remove("show");


    },3000);



}







// ==========================================
// IMAGE PREVIEW
// ==========================================


function previewImage(event){



    const file =
        event.target.files[0];



    if(!file)
        return;





    // TYPE CHECK


    if(!file.type.startsWith("image/")){


        showToast(
            "Only image files allowed"
        );


        imageInput.value="";


        return;

    }






    // SIZE CHECK 5MB


    if(file.size > 5 * 1024 * 1024){



        showToast(
            "Image size should be less than 5MB"
        );


        imageInput.value="";


        return;


    }






    selectedImage = file;





    if(preview){


        preview.src =
            URL.createObjectURL(file);


        preview.style.display =
            "block";


    }



}









// ==========================================
// FORM SUBMIT
// ==========================================


if(form){



form.addEventListener(

"submit",

async function(e){


    e.preventDefault();





    // ==========================
    // VALIDATION
    // ==========================


    const name =
        nameInput.value.trim();



    const description =
        descriptionInput.value.trim();





    if(name === ""){


        showToast(
            "Category name required"
        );


        return;

    }







    if(description === ""){


        showToast(
            "Description required"
        );


        return;

    }








    if(!selectedImage){


        showToast(
            "Category image required"
        );


        return;


    }










    // ==========================
    // FORMDATA
    // ==========================



    const formData =
        new FormData();





    formData.append(

        "name",

        name

    );





    formData.append(

        "description",

        description

    );





    formData.append(

        "active",

        activeInput.checked

    );





    formData.append(

        "image",

        selectedImage

    );









    try{



        showLoader();







        const response =

            await apiRequest(

                CATEGORY_API,

                "POST",

                formData,

                true

            );








        if(!response){


            return;

        }








        const data =

            await response.json();









        if(!response.ok){



            throw new Error(

                data.message ||
                "Category creation failed"

            );


        }










        showToast(

            "Category Added Successfully"

        );









        setTimeout(()=>{



            window.location.href =
                "categories.html";



        },1200);









    }

    catch(error){



        showToast(

            error.message

        );



    }

    finally{


        hideLoader();


    }






});



}