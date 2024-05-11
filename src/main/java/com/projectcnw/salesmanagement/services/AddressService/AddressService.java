package com.projectcnw.salesmanagement.services.AddressService;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.customer.address.AddressRequest;
import com.projectcnw.salesmanagement.models.Address;
import com.projectcnw.salesmanagement.models.Customer;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.AddressReppository.AddressRepository;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.CustomerRepository;
import com.projectcnw.salesmanagement.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public ResponseEntity<ResponseObject> addAddress(AddressRequest addressRequest) {
        Integer customerId = UserUtil.getCurrentCustomerId();
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Bạn cần đăng nhập để thực hiện chức năng này")
                    .build());
        }
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy khách hàng với ID: " + customerId)
                    .build());
        }

        List<Address> addressList = addressRepository.getAllByCustomer_Id(customerId);

        if (addressRequest.getIsDefault() == null) {
            addressRequest.setIsDefault(addressList.isEmpty());
        } else if (addressRequest.getIsDefault()) {
            Address defaultAddress = addressRepository.getAddressByIsDefaultTrueAndCustomerId(customerId);
            if (defaultAddress != null) {
                defaultAddress.setIsDefault(false);
                addressRepository.save(defaultAddress);
            }
        }

        if (addressRequest.getCustomerName() == null) {
            addressRequest.setCustomerName(customer.get().getName());
        } else if (addressRequest.getCountry() == null) {
            addressRequest.setCountry("Việt Nam");
            addressRequest.setCountryCode("VN");
        }

        Address address = Address.builder()
                .customer(customer.get())
                .address(addressRequest.getAddress())
                .city(addressRequest.getCity())
                .company(addressRequest.getCompany())
                .country(addressRequest.getCountry())
                .countryCode(addressRequest.getCountryCode())
                .district(addressRequest.getDistrict())
                .districtCode(addressRequest.getDistrictCode())
                .isDefault(addressRequest.getIsDefault())
                .phone(addressRequest.getPhone())
                .province(addressRequest.getProvince())
                .provinceCode(addressRequest.getProvinceCode())
                .ward(addressRequest.getWard())
                .wardCode(addressRequest.getWardCode())
                .zip(addressRequest.getZip())
                .customerName(addressRequest.getCustomerName())
                .build();

        addressRepository.save(address);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Thêm địa chỉ thành công")
                .data(address)
                .build());
    }

    public ResponseEntity<ResponseObject> updateAddress(Integer addressId, AddressRequest addressRequest) {
        Integer customerId = UserUtil.getCurrentCustomerId();
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Bạn cần đăng nhập để thực hiện chức năng này")
                    .build());
        }
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy khách hàng với ID: " + customerId)
                    .build());
        }

        Optional<Address> address = addressRepository.findById(addressId);
        if (address.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy địa chỉ với ID: " + addressId)
                    .build());
        }

        if (addressRequest.getIsDefault() == null) {
            addressRequest.setIsDefault(false);
        } else if (addressRequest.getIsDefault()) {
            Address defaultAddress = addressRepository.getAddressByIsDefaultTrueAndCustomerId(customerId);
            if (defaultAddress != null && !defaultAddress.getId().equals(addressId)) {
                defaultAddress.setIsDefault(false);
                addressRepository.save(defaultAddress);
            }
        }

        if (addressRequest.getCustomerName() == null) {
            addressRequest.setCustomerName(customer.get().getName());
        } else if (addressRequest.getCountry() == null) {
            addressRequest.setCountry("Việt Nam");
            addressRequest.setCountryCode("VN");
        }

        Address addressUpdate = getAddress(addressRequest, address);

        addressRepository.save(addressUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Cập nhật địa chỉ thành công")
                .data(addressUpdate)
                .build());
    }

    @NotNull
    private static Address getAddress(AddressRequest addressRequest, Optional<Address> address) {
        Address addressUpdate = address.get();
        addressUpdate.setAddress(addressRequest.getAddress());
        addressUpdate.setCity(addressRequest.getCity());
        addressUpdate.setCompany(addressRequest.getCompany());
        addressUpdate.setCountry(addressRequest.getCountry());
        addressUpdate.setCountryCode(addressRequest.getCountryCode());
        addressUpdate.setDistrict(addressRequest.getDistrict());
        addressUpdate.setDistrictCode(addressRequest.getDistrictCode());
        addressUpdate.setIsDefault(addressRequest.getIsDefault());
        addressUpdate.setPhone(addressRequest.getPhone());
        addressUpdate.setProvince(addressRequest.getProvince());
        addressUpdate.setProvinceCode(addressRequest.getProvinceCode());
        addressUpdate.setWard(addressRequest.getWard());
        addressUpdate.setWardCode(addressRequest.getWardCode());
        addressUpdate.setZip(addressRequest.getZip());
        addressUpdate.setCustomerName(addressRequest.getCustomerName());
        return addressUpdate;
    }

    public ResponseEntity<ResponseObject> deleteAddress( Integer addressId) {
        Integer customerId = UserUtil.getCurrentCustomerId();
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Bạn cần đăng nhập để thực hiện chức năng này")
                    .build());
        }
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy khách hàng với ID: " + customerId)
                    .build());
        }

        Optional<Address> address = addressRepository.findById(addressId);
        if (address.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy địa chỉ với ID: " + addressId)
                    .build());
        }

        addressRepository.deleteById(addressId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Xóa địa chỉ thành công")
                .build());
    }

    public ResponseEntity<ResponseObject> getAllAddress() {
        Integer customerId = UserUtil.getCurrentCustomerId();
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Bạn cần đăng nhập để thực hiện chức năng này")
                    .build());
        }
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy khách hàng với ID: " + customerId)
                    .build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Lấy danh sách địa chỉ thành công")
                .data(addressRepository.getAllByCustomer_Id(customerId))
                .build());
    }
}
