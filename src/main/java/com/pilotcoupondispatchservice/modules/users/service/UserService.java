package com.pilotcoupondispatchservice.modules.users.service;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.enums.UserType;
import com.pilotcoupondispatchservice.exceptions.ResourceAlreadyExistException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import com.pilotcoupondispatchservice.modules.users.dto.UserDTO;
import com.pilotcoupondispatchservice.modules.users.dto.UserProfileResponse;
import com.pilotcoupondispatchservice.modules.users.dto.UserResponse;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.mapper.UserMapper;
import com.pilotcoupondispatchservice.modules.users.repository.UserRepository;
import com.pilotcoupondispatchservice.utils.SecurityUtil;
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
    public Page<UserResponse> searchUser(String name, Pageable pageable) {
        return userRepository.findAllByNameContainingIgnoreCaseAndUserType(name, UserType.OWNER, pageable)
                .map(UserMapper::toResponse);
    }

    @HasPermission(permission = PermissionConstant.READ_USER)
    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(findByIdOrThrow(id));
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

    @HasPermission(permission = PermissionConstant.MODIFY_USER)
    public boolean activeDeactiveUser(Long id, Boolean active) {

        User user = findByIdOrThrow(id);
        user.setIsActive(active);
        userRepository.save(user);

        return true;
    }

    @HasPermission(permission = PermissionConstant.UPDATE_USER_PROFILE_PASSWORD)
    public void updateUserPassword(Long id, String newPassword) {
        User user = findByIdOrThrow(id);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @HasPermission(permission = PermissionConstant.READ_USER_PROFILE)
    public UserProfileResponse getUserProfile() {
        return UserMapper.toProfileResponse(findByIdOrThrow(SecurityUtil.getLoggedInUserId()));
    }

    private User findByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @HasPermission(permission = PermissionConstant.MODIFY_USER_PROFILE)
    public User updateUserProfile(UserDTO userDTO, Long id) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDTO.getName());

            if (!user.getEmail().equals(userDTO.getEmail()) &&
                    userRepository.existsByEmail(userDTO.getEmail())) {
                throw new ResourceAlreadyExistException("User", "email", userDTO.getEmail());
            }
            user.setEmail(userDTO.getEmail());

            if (!user.getPhone().equals(userDTO.getPhone()) &&
                    userRepository.existsByPhone(userDTO.getPhone())) {
                throw new ResourceAlreadyExistException("User", "phone", userDTO.getPhone());
            }
            user.setPhone(userDTO.getPhone());

            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phone", phone));
    }

    public java.util.Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUserDirect(User user) {
        return userRepository.save(user);
    }
}
