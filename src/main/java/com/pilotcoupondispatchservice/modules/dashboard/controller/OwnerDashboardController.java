package com.pilotcoupondispatchservice.modules.dashboard.controller;

import com.pilotcoupondispatchservice.modules.dashboard.dto.OwnerDashboardResponse;
import com.pilotcoupondispatchservice.modules.dashboard.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${url.base}/secured/owner/dashboard")
@AllArgsConstructor
public class OwnerDashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<?> getOwnerDashboard() {
        OwnerDashboardResponse response = dashboardService.getOwnerDashboard();
        return ResponseEntity.ok(response);
    }
}
