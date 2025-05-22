//package org.example.Security;
//
//import com.lab_2.demo.Security.UserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Optional;
//
//public interface UserRepository extends JpaRepository<UserEntity, Integer> {
//    Optional<UserEntity> findByUsername(String username);
//    Boolean existsByUsername(String username);
//}
