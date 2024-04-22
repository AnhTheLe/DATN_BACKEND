package com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth;

import com.projectcnw.salesmanagement.exceptions.NotFoundException;
import com.projectcnw.salesmanagement.models.Customer;
import com.projectcnw.salesmanagement.models.Auth.Role;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("customer")
@RequiredArgsConstructor
public class CustomerDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer loadUserByUsername(String phone) throws NotFoundException {

        return customerRepository.findByEmail(phone)
                .orElseThrow(
                        () -> new NotFoundException("User " + phone + " not found"));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }
}
