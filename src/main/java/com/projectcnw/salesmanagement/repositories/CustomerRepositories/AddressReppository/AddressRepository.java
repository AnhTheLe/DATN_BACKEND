package com.projectcnw.salesmanagement.repositories.CustomerRepositories.AddressReppository;

import com.projectcnw.salesmanagement.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address getAddressByIsDefaultTrueAndCustomerId(int customerId);

    List<Address> getAllByCustomer_Id(int customerId);
}
