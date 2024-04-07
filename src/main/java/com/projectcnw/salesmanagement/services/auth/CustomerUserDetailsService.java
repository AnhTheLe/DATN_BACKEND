package com.projectcnw.salesmanagement.services.auth;

import com.projectcnw.salesmanagement.exceptions.NotFoundException;
import com.projectcnw.salesmanagement.models.Auth.Role;
import com.projectcnw.salesmanagement.models.Auth.UserEntity;
import com.projectcnw.salesmanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserEntity loadUserByUsername(String phone) throws NotFoundException {
        UserEntity userEntity = userRepository.findByPhone(phone)
                .orElseThrow(
                        () -> new NotFoundException("User " + phone + " not found"));

        return UserEntity //
                .builder() //
                .phone(userEntity.getPhone()) //
                .password(userEntity.getPassword()) //
                .roles(userEntity.getRoles()) //
                .build();
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());
    }
}
