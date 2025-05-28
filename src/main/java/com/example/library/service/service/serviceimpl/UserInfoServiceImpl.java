package com.example.library.service.service.serviceimpl;

import com.example.library.entity.UserInfo;
import com.example.library.exception.ValidationException;
import com.example.library.repository.UserInfoRepository;
import com.example.library.generator.MyUuidGenerator;
import com.example.library.service.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserInfoServiceImpl implements UserInfoService, UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfo saveUser(UserInfo user) {
        String userId = MyUuidGenerator.generateUuid();
        user.setCustomerId(userId);

        if (userInfoRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("User with mail : " + user.getEmail() + " already exists.");
        }
        return userInfoRepository.save(user);
    }

    @Override
    public Optional<UserInfo> findUserByEmail(String email) {
        return userInfoRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo user = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
