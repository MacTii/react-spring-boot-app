package com.ZAIORO.SpringBootApplication.service;

import com.ZAIORO.SpringBootApplication.domain.Authority;
import com.ZAIORO.SpringBootApplication.domain.User;
import com.ZAIORO.SpringBootApplication.dto.UserDto;
import com.ZAIORO.SpringBootApplication.repository.AuthorityRepository;
import com.ZAIORO.SpringBootApplication.repository.UserRepository;
import com.ZAIORO.SpringBootApplication.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void createUser(UserDto userDto) {
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setName(userDto.getName());
        String encodedPassword = customPasswordEncoder.getPasswordEncoder().encode(userDto.getPassword());
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);
        Authority authority = new Authority();
        authority.setAuthority("ROLE_STUDENT");
        authority.setUser(newUser);
        authorityRepository.save(authority);

    }

    @Secured({"ROLE_INSTRUCTOR"})
    public List<User> findNonConfiguredStudents() {
        return userRepository.findAllInactiveBootcampStudents();
    }
}
