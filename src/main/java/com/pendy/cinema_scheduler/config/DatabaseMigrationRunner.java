package com.pendy.cinema_scheduler.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DatabaseMigrationRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        addSortOrderColumnIfMissing();
        addRequiresPositionAssignmentColumnIfMissing();
        addRequiresMonthlyLeaveColumnIfMissing();
        addMonthlyLeaveTypeColumnIfMissing();

        // initializeRequiresPositionAssignment();
        //initializeRequiresMonthlyLeave();
    }

    private void addSortOrderColumnIfMissing() {
        if (!columnExists("employees", "sort_order")) {
            jdbcTemplate.execute("ALTER TABLE employees ADD COLUMN sort_order INTEGER DEFAULT 9999");
            System.out.println("Added sort_order column to employees table.");
        }
    }

    private void addRequiresPositionAssignmentColumnIfMissing() {
        if (!columnExists("employees", "requires_position_assignment")) {
            jdbcTemplate.execute("ALTER TABLE employees ADD COLUMN requires_position_assignment INTEGER DEFAULT 1");
            System.out.println("Added requires_position_assignment column to employees table.");
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        List<Map<String, Object>> columns = jdbcTemplate.queryForList("PRAGMA table_info(" + tableName + ")");

        return columns.stream()
                .anyMatch(column -> columnName.equalsIgnoreCase(String.valueOf(column.get("name"))));
    }

    private void initializeRequiresPositionAssignment() {
        jdbcTemplate.update("UPDATE employees SET requires_position_assignment = 0 WHERE job_title = '副理'");
        jdbcTemplate.update("UPDATE employees SET requires_position_assignment = 0 WHERE job_title = '會計'");
        jdbcTemplate.update("UPDATE employees SET requires_position_assignment = 0 WHERE job_title = '正職清潔'");
        jdbcTemplate.update("UPDATE employees SET requires_position_assignment = 0 WHERE job_title = '晚班清潔'");


        System.out.println("Initialized requires_position_assignment for management/admin employees.");
    }

    private void addRequiresMonthlyLeaveColumnIfMissing() {
        if (!columnExists("employees", "requires_monthly_leave")) {
            jdbcTemplate.execute("ALTER TABLE employees ADD COLUMN requires_monthly_leave INTEGER DEFAULT 0");
            System.out.println("Added requires_monthly_leave column to employees table.");
        }
    }

    private void addMonthlyLeaveTypeColumnIfMissing() {
        if (!columnExists("monthly_leaves", "leave_type")) {
            jdbcTemplate.execute("ALTER TABLE monthly_leaves ADD COLUMN leave_type TEXT DEFAULT 'REGULAR_LEAVE'");
            System.out.println("Added leave_type column to monthly_leaves table.");
        }

        jdbcTemplate.update(
                "UPDATE monthly_leaves SET leave_type = 'REGULAR_LEAVE' " +
                        "WHERE leave_type IS NULL OR TRIM(leave_type) = ''"
        );
    }

    private void initializeRequiresMonthlyLeave() {
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '會計'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '主任'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '早班正職'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '組長'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '總務'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '放映師'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '晚班正職'");
        jdbcTemplate.update("UPDATE employees SET requires_monthly_leave = 1 WHERE job_title = '正職清潔'");


        System.out.println("Initialized requires_position_assignment for management/admin employees.");
    }
}
