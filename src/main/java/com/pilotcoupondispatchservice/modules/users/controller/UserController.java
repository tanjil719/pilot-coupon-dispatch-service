package com.pilotcoupondispatchservice.modules.users.controller;

import com.pilotcoupondispatchservice.annotations.HasPermission;
import com.pilotcoupondispatchservice.constants.PermissionConstant;
import com.pilotcoupondispatchservice.modules.users.dto.UserDTO;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    @HasPermission(permission = PermissionConstant.READ_USER)
    public ResponseEntity<?> searchUsers(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.searchUser(name, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @HasPermission(permission = PermissionConstant.READ_USER)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

//    @PostMapping
//    @HasPermission(permission = PermissionConstant.CREATE_USER)
//    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
//        User user = userService.createUser(userDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(user);
//    }

//    @PutMapping("/{id}")
//    @HasPermission(permission = PermissionConstant.MODIFY_USER)
//    public ResponseEntity<?> updateUser(
//            @PathVariable Long id,
//            @RequestBody UserDTO userDTO) {
//        User user = userService.updateUser(userDTO, id);
//        return ResponseEntity.ok(user);
//    }

    @DeleteMapping("/{id}")
    @HasPermission(permission = PermissionConstant.DELETE_USER)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/profile")
    @HasPermission(permission = PermissionConstant.READ_USER_PROFILE)
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        User user = userService.getUserProfile(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/profile")
    @HasPermission(permission = PermissionConstant.MODIFY_USER_PROFILE)
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        User user = userService.updateUserProfile(userDTO, id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/change-password")
    @HasPermission(permission = PermissionConstant.UPDATE_USER_PROFILE_PASSWORD)
    public ResponseEntity<?> updateUserPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        userService.updateUserPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
}
