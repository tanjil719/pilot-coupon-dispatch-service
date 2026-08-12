package com.pilotcoupondispatchservice.modules.dashboard.service;

import com.pilotcoupondispatchservice.modules.dashboard.dto.AdminDashboardResponse;
import com.pilotcoupondispatchservice.modules.dashboard.dto.OwnerDashboardResponse;

public interface DashboardService {

    OwnerDashboardResponse getOwnerDashboard();

    AdminDashboardResponse getAdminDashboard();
}
