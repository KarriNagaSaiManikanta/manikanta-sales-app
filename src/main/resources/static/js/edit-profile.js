const PROFILE_API =
"http://localhost:8080/api/users/profile";


const IMAGE_API =
"http://localhost:8080/api/users/profile/image";


const token = localStorage.getItem("token");


let selectedImage = null;



document.addEventListener(
"DOMContentLoaded",
loadEditProfile
);





async function loadEditProfile(){


    try{


        const response = await fetch(PROFILE_API,{

            headers:{

                "Authorization":
                "Bearer " + token

            }

        });


        const user = await response.json();



        let nameParts = user.fullName.split(" ");



        document.getElementById("firstName").value =
        nameParts[0];


        document.getElementById("lastName").value =
        nameParts.slice(1).join("");


        document.getElementById("email").value =
        user.email;


        document.getElementById("mobile").value =
        user.mobile;


        document.getElementById("role").value =
        user.role;



        if(user.profileImage){


            document.getElementById("profilePreview").src =
            "http://localhost:8080"
            + user.profileImage;


        }


    }
    catch(error){

        console.log(error);

        alert("Profile loading failed");

    }


}






// IMAGE PREVIEW

function previewImage(event){


    selectedImage =
    event.target.files[0];



    if(selectedImage){


        const reader = new FileReader();



        reader.onload=function(){


            document.getElementById("profilePreview").src =
            reader.result;


        }



        reader.readAsDataURL(selectedImage);

    }


}









// UPDATE PROFILE

async function updateProfile(){



    const data = {


        firstName:
        document.getElementById("firstName").value,


        lastName:
        document.getElementById("lastName").value,


        mobile:
        document.getElementById("mobile").value


    };




    try{


        // 1. UPDATE NAME MOBILE

        const response = await fetch(PROFILE_API,{

            method:"PUT",


            headers:{


                "Content-Type":
                "application/json",


                "Authorization":
                "Bearer " + token

            },


            body:
            JSON.stringify(data)


        });




        if(!response.ok){

            throw new Error();

        }






        // 2. UPLOAD IMAGE

        if(selectedImage){


            const formData = new FormData();


            formData.append(
                "file",
                selectedImage
            );



            const imageResponse =
            await fetch(IMAGE_API,{


                method:"POST",


                headers:{


                    "Authorization":
                    "Bearer " + token


                },


                body:formData


            });



            if(!imageResponse.ok){

                alert(
                "Image upload failed"
                );

                return;

            }


        }





        alert(
        "Profile Updated Successfully"
        );



        window.location.href =
        "profile.html";



    }

    catch(error){


        console.log(error);


        alert(
        "Update Failed"
        );


    }


}