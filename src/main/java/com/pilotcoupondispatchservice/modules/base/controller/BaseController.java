package com.pilotcoupondispatchservice.modules.base.controller;

import com.pilotcoupondispatchservice.modules.base.dto.PingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("${url.base}/public/ping")
    public ResponseEntity<?> publicPing() {
        return ResponseEntity.ok(new PingResponse("pong"));
    }

    @GetMapping("${url.secured}/ping")
    public ResponseEntity<?> securePing() {
        return ResponseEntity.ok(new PingResponse("pong"));
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<?> favicon() {
        return ResponseEntity.noContent().build();
    }
}
