package com.gomson.gomsonServer.user;

import com.gomson.gomsonServer.domain.User;
import com.gomson.gomsonServer.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String joinUser(UserEntity userEntity) {
        if (userEntity.getPassword() != null && !userEntity.getPassword().isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        return userRepository.save(userEntity).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//        Optional<UserEntity> userWrapper = userRepository.findById(id);
//        UserEntity userEntity = userWrapper.get();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserEntity userEntity = new UserEntity(id, "anonymous", passwordEncoder.encode("password"));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("Admin"));
        authorityList.add(new SimpleGrantedAuthority("User"));

        return new User(userEntity, authorityList);
    }
}
