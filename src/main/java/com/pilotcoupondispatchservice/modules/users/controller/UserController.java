package com.pilotcoupondispatchservice.modules.users.controller;

import com.pilotcoupondispatchservice.modules.users.dto.UserDTO;
import com.pilotcoupondispatchservice.modules.users.dto.UserProfileResponse;
import com.pilotcoupondispatchservice.modules.users.dto.UserResponse;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${url.base}/secured/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.searchUser(name, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/active-deactive/{id}")
    public ResponseEntity<?> activeDeactiveUser(@PathVariable Long id, @RequestParam Boolean active) {
        userService.activeDeactiveUser(id, active);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        UserProfileResponse profile = userService.getUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO) {
        User user = userService.updateUserProfile(userDTO, id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> updateUserPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        userService.updateUserPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
}
