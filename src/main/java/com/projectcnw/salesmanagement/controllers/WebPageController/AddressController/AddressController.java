package com.projectcnw.salesmanagement.controllers.WebPageController.AddressController;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.customer.address.AddressRequest;
import com.projectcnw.salesmanagement.services.AddressService.AddressService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/address")
@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;


    @GetMapping()
    public ResponseEntity<ResponseObject> getAddress() {
        return addressService.getAllAddress();
    }

    @PostMapping()
    public ResponseEntity<ResponseObject> addAddress(@RequestBody AddressRequest addressRequest) {
        return addressService.addAddress(addressRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateAddress(@PathVariable int id, @RequestBody AddressRequest addressRequest) {
        return addressService.updateAddress(id, addressRequest);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteAddress(@RequestBody Integer id) {
        return addressService.deleteAddress(id);
    }
}
