package com.pilotcoupondispatchservice.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@AllArgsConstructor
@Service
public class DatabaseConnectionServiceImpl implements DatabaseConnectionService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public boolean checkConnection() {

        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2);
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean checkConnectionCustom() {

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
