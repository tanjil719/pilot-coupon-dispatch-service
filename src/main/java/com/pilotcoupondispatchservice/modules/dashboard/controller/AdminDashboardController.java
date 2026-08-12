package com.pilotcoupondispatchservice.modules.dashboard.controller;

import com.pilotcoupondispatchservice.modules.dashboard.dto.AdminDashboardResponse;
import com.pilotcoupondispatchservice.modules.dashboard.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.base}/secured/admin/dashboard")
@AllArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getAdminDashboard() {
        AdminDashboardResponse response = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(response);
    }
}
