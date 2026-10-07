const API = "http://localhost:8080/api/users/profile";

const token = localStorage.getItem("token");


document.addEventListener(
    "DOMContentLoaded",
    loadProfile
);



async function loadProfile(){


    try{


        const response = await fetch(API,{

            headers:{
                "Authorization":
                "Bearer " + token
            }

        });



        if(!response.ok){

            throw new Error("Failed to load profile");

        }



        const user = await response.json();



        document.getElementById("id").innerText =
            user.id;


        document.getElementById("name").innerText =
            user.fullName;


        document.getElementById("email").innerText =
            user.email;


        document.getElementById("role").innerText =
            user.role;


        document.getElementById("phone").innerText =
            user.mobile;


        document.getElementById("status").innerText =
            user.enabled ? "Active" : "Inactive";




        // PROFILE IMAGE

        const image =
        document.getElementById("profileImage");



        if(user.profileImage != null &&
           user.profileImage.trim() !== ""){


            image.src =
            "http://localhost:8080" 
            + user.profileImage;


        }
        else{


            image.src =
            "/images/admin.png";


        }



        image.onerror = function(){

            this.src="/images/admin.png";

        };



    }
    catch(error){

        console.error(
            "Profile Error:",
            error
        );

        alert(
            "Unable to load profile"
        );

    }

}