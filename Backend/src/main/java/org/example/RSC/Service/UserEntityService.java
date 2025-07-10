package org.example.RSC.Service;

import org.example.RSC.Entity.UserEntity;
import org.example.RSC.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserEntityService implements UserDetailsService {

    private UserRepository userRepository;
    @Autowired
    public UserEntityService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public UserEntity getByUsername(String username)
    {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void save(UserEntity user)
    {
        userRepository.save(user);
    }

}
