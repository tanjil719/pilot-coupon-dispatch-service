package com.pilotcoupondispatchservice.modules.users.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.RoleLevel;
import com.pilotcoupondispatchservice.modules.users.dto.UserDTO;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @HasPermission(permission = PermissionConstant.READ_USER)
    public Page<User> searchUser(String name, Pageable pageable) {
        return userRepository.findAllByNameContainingIgnoreCase(name, pageable);
    }

    @HasPermission(permission = PermissionConstant.READ_USER)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

//    @HasPermission(permission = PermissionConstant.CREATE_USER)
//    @Transactional
//    public User createUser(UserDTO userDTO) {
//        if (userRepository.existsByEmail(userDTO.getEmail())) {
//            throw new RuntimeException("User with email already exists: " + userDTO.getEmail());
//        }
//
//        if (userRepository.existsByPhone(userDTO.getPhone())) {
//            throw new RuntimeException("User with phone already exists: " + userDTO.getPhone());
//        }
//
//        User user = new User();
//        user.setName(userDTO.getName());
//        user.setEmail(userDTO.getEmail());
//        user.setPhone(userDTO.getPhone());
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : RoleLevel.OWNER);
//        user.setIsActive(userDTO.getIsActive() != null ? userDTO.getIsActive() : true);
//
//        return userRepository.save(user);
//    }

//    @HasPermission(permission = PermissionConstant.MODIFY_USER)
//    @Transactional
//    public User updateUser(UserDTO userDTO, Long id) {
//        return userRepository.findById(id).map(user -> {
//            user.setName(userDTO.getName());
//
//            if (!user.getEmail().equals(userDTO.getEmail()) &&
//                    userRepository.existsByEmail(userDTO.getEmail())) {
//                throw new RuntimeException("User with email already exists: " + userDTO.getEmail());
//            }
//            user.setEmail(userDTO.getEmail());
//
//            if (!user.getPhone().equals(userDTO.getPhone()) &&
//                    userRepository.existsByPhone(userDTO.getPhone())) {
//                throw new RuntimeException("User with phone already exists: " + userDTO.getPhone());
//            }
//            user.setPhone(userDTO.getPhone());
//
////            user.setRole(userDTO.getRole());
//            user.setIsActive(userDTO.getIsActive());
//
//            return userRepository.save(user);
//        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//    }

    @HasPermission(permission = PermissionConstant.DELETE_USER)
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @HasPermission(permission = PermissionConstant.UPDATE_USER_PROFILE_PASSWORD)
    public void updateUserPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @HasPermission(permission = PermissionConstant.READ_USER_PROFILE)
    public User getUserProfile(Long id) {
        return getUserById(id);
    }

    @HasPermission(permission = PermissionConstant.MODIFY_USER_PROFILE)
    public User updateUserProfile(UserDTO userDTO, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDTO.getName());

            if (!user.getEmail().equals(userDTO.getEmail()) &&
                    userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("User with email already exists: " + userDTO.getEmail());
            }
            user.setEmail(userDTO.getEmail());

            if (!user.getPhone().equals(userDTO.getPhone()) &&
                    userRepository.existsByPhone(userDTO.getPhone())) {
                throw new RuntimeException("User with phone already exists: " + userDTO.getPhone());
            }
            user.setPhone(userDTO.getPhone());

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found with phone: " + phone));
    }

    public java.util.Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUserDirect(User user) {
        return userRepository.save(user);
    }
}
