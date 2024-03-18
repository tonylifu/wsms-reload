package com.lifu.wsms.reload.service;

import com.lifu.wsms.reload.api.SequenceService;
import com.lifu.wsms.reload.dto.response.SequenceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SequenceServiceGenerator implements SequenceService {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public Optional<SequenceResponse> getNextSequenceValue(String sequenceName) {
        var nextValue = getNextVal(sequenceName)
                .orElseThrow();
        var currval = getCurrentVal(sequenceName)
                .orElseThrow();

        return Optional.of(SequenceResponse.builder()
                        .nextVal(nextValue)
                        .currentVal(currval)
                .build());
    }

    private Optional<Long> getNextVal(String sequenceName) {
        String sql = "SELECT nextval(?)";
        Long nextValue = null;

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nextValue = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("Get nextval sequence failed => {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(nextValue);
    }

    private Optional<Long> getCurrentVal(String sequenceName) {
        String sql = "SELECT currval(?)";
        Long nextValue = null;

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sequenceName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nextValue = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("Get currval sequence failed => {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(nextValue);
    }
}
