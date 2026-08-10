package com.pilotcoupondispatchservice.modules.base.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=Vwis1u0gfFH5G3uf3R9nHWR3SQxBn+2VqGXkPV1HaSg=",
        "jwt.access-token-expiration=PT1H",
        "jwt.refresh-token-expiration=P7D",
        "url.base=/api/1.0.0",
        "url.secured=/api/1.0.0/secured"
})
class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicPing_noAuth_returns200() throws Exception {
        mockMvc.perform(get("/api/1.0.0/public/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("pong"));
    }

    @Test
    void securePing_noAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/1.0.0/secured/ping"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void securePing_authenticated_returns200() throws Exception {
        mockMvc.perform(get("/api/1.0.0/secured/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("pong"));
    }
}
