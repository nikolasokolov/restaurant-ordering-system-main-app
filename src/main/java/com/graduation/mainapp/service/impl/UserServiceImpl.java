package com.graduation.mainapp.service.impl;

import com.graduation.mainapp.model.Authority;
import com.graduation.mainapp.model.User;
import com.graduation.mainapp.repository.UserRepository;
import com.graduation.mainapp.service.UserService;
import com.graduation.mainapp.web.dto.CompanyDTO;
import com.graduation.mainapp.web.dto.UserAccount;
import com.graduation.mainapp.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        user.setAuthorities(null);
        userRepository.delete(user);
    }

    @Override
    public User createUser(UserAccount userAccount) throws Exception {
        boolean userAccountIsValid = validateUserAccount(userAccount);
        if (userAccountIsValid) {
            Set<Authority> authorities = new HashSet<>();
            authorities.add(new Authority(userAccount.getAuthority()));
            User user = User.builder()
                    .username(userAccount.getUsername())
                    .email(userAccount.getEmail())
                    .password(passwordEncoder.encode(userAccount.getPassword()))
                    .authorities(authorities)
                    .build();
            return save(user);
        } else {
            throw new Exception("Could not create a new user account");
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> createUserDTOs(Collection<User> users) {
        List<UserDTO> userDTOs = new LinkedList<>();
        users.forEach(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setAuthority(user.getAuthorities().stream().findFirst().get().toString());
            userDTO.setCompany(Objects.nonNull(user.getCompany()) ? user.getCompany().getName() : "N/A");
            userDTOs.add(userDTO);
        });
        return userDTOs;
    }

    private boolean validateUserAccount(UserAccount userAccount) throws Exception {
        if (!userAccount.getPassword().equals(userAccount.getConfirmPassword())) {
            throw new Exception("Passwords doesn't match");
        } else {
            return true;
        }
    }
}
