package com.example.manikantasales.controller;


import com.example.manikantasales.dto.AddressRequest;
import com.example.manikantasales.dto.AddressResponse;
import com.example.manikantasales.service.AddressService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/api/address")
@CrossOrigin("*")
public class AddressController {



    private final AddressService addressService;



    public AddressController(
            AddressService addressService
    ){

        this.addressService = addressService;

    }






    // =====================================
    // ADD ADDRESS
    // POST /api/address/add
    // =====================================

    @PostMapping("/add")
    public ResponseEntity<AddressResponse> addAddress(

            @RequestBody AddressRequest request

    ){


        return ResponseEntity.ok(

                addressService.addAddress(request)

        );

    }







    // =====================================
    // GET MY ADDRESSES
    // GET /api/address/my
    // =====================================

    @GetMapping("/my")
    public ResponseEntity<List<AddressResponse>> getMyAddresses(){


        return ResponseEntity.ok(

                addressService.getMyAddresses()

        );

    }







    // =====================================
    // GET ADDRESS BY ID
    // GET /api/address/{id}
    // =====================================

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddress(

            @PathVariable Long id

    ){


        return ResponseEntity.ok(

                addressService.getAddressById(id)

        );

    }







    // =====================================
    // UPDATE ADDRESS
    // PUT /api/address/update/{id}
    // =====================================

    @PutMapping("/update/{id}")
    public ResponseEntity<AddressResponse> updateAddress(

            @PathVariable Long id,

            @RequestBody AddressRequest request

    ){


        return ResponseEntity.ok(

                addressService.updateAddress(
                        id,
                        request
                )

        );

    }







    // =====================================
    // DELETE ADDRESS
    // DELETE /api/address/delete/{id}
    // =====================================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAddress(

            @PathVariable Long id

    ){


        addressService.deleteAddress(id);



        return ResponseEntity.ok(

                "Address deleted successfully"

        );

    }







    // =====================================
    // SET DEFAULT ADDRESS
    // PUT /api/address/default/{id}
    // =====================================

    @PutMapping("/default/{id}")
    public ResponseEntity<AddressResponse> setDefault(

            @PathVariable Long id

    ){


        return ResponseEntity.ok(

                addressService.setDefaultAddress(id)

        );

    }


}