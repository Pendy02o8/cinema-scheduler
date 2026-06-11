-- Generated from the existing MySQL cinema_scheduler database.
-- The seed runs only when the core SQLite tables are empty.

CREATE TEMP TABLE IF NOT EXISTS seed_control (should_seed integer not null);
DELETE FROM seed_control;
INSERT INTO seed_control (should_seed)
SELECT CASE WHEN
    NOT EXISTS (SELECT 1 FROM positions)
    AND NOT EXISTS (SELECT 1 FROM employees)
    AND NOT EXISTS (SELECT 1 FROM weekly_schedules)
    AND NOT EXISTS (SELECT 1 FROM position_requirements)
    AND NOT EXISTS (SELECT 1 FROM availability)
    AND NOT EXISTS (SELECT 1 FROM schedule_assignments)
    THEN 1 ELSE 0 END;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 1, '1F+公關', 1, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 2, '3F', 1, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 3, '票房', 1, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 4, '販賣', 1, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 5, '開機+輪休', 1, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 6, '關機+輪休', 1, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 7, '票+販', 0, 1781032516000, 1781032516000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (id, name, is_required, created_at, updated_at)
SELECT 8, '試片', 0, 1781033880000, 1781033880000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO positions (name, is_required, created_at, updated_at)
SELECT '休', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM positions WHERE name = '休');

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 1, '劉信宏', '副理', 1, '', 1781032777000, 1781086019000, 'FULL_TIME', NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 2, '鄭佩綺', '會計', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 3, '蕭俊琪', '主任', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 4, '李連峻', '組長', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 5, '林宜庭', '總務', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 6, '章勝勛', '放映師', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 7, '盧冠綸', '早班正職', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 8, '林美瑜', '早班正職', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 9, '劉怡君', '早班正職', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 10, '吳宓霏', '早班正職', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 11, '陳宛芸', '晚班正職', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 12, '陳聖傑', '晚班正職', 1, '', 1781032777000, 1781078163000, 'FULL_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 13, '賴錦標', '正職清潔', 1, '', 1781032777000, 1781078163000, 'CLEANER', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 14, '洪凌祥', '晚班清潔', 1, '', 1781032777000, 1781078163000, 'CLEANER', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 15, '黃于真', '晚班清潔', 1, '', 1781032777000, 1781078163000, 'CLEANER', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 16, '林芷瑜', '早班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 17, '王心寧', '早班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 18, '沈俞宜', '早班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 19, '沈旻萱', '早班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'MORNING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 20, '曾煒智', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 21, '陳性衡', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 22, '蔡承祐', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 23, '林信偉', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 24, '陳柏皓', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 25, '李怡佳', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 26, '黃若筑', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO employees (id, name, job_title, is_active, note, created_at, updated_at, employee_type, fixed_shift_type)
SELECT 27, '李安琪', '晚班工讀生', 1, '', 1781032777000, 1781078163000, 'PART_TIME', 'EVENING'
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO weekly_schedules (id, week_start_date, week_end_date, status, created_at, updated_at)
SELECT 9, 1777219200000, 1777737600000, 'DRAFT', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 1, 1, 12000000, 54000000, 1, 1781033622000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 2, 2, 6600000, 54000000, 1, 1781033622000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 3, 3, 6600000, 54000000, 1, 1781033622000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 4, 4, 4800000, 54000000, 1, 1781033622000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 5, 5, 3000000, 25200000, 1, 1781033622000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 6, 6, 33600000, 54000000, 1, 1781033622000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO position_requirements (id, position_id, start_time, end_time, required_count, created_at)
SELECT 7, 7, 4800000, 54000000, 1, 1781088854000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 141, 10, 9, 1777219200000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 142, 10, 9, 1777305600000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 143, 10, 9, 1777392000000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 144, 10, 9, 1777478400000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 145, 10, 9, 1777564800000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 146, 10, 9, 1777651200000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 147, 10, 9, 1777737600000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 148, 19, 9, 1777219200000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 149, 19, 9, 1777305600000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 150, 19, 9, 1777392000000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 151, 19, 9, 1777478400000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 152, 19, 9, 1777564800000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 153, 19, 9, 1777651200000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 154, 19, 9, 1777737600000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 155, 22, 9, 1777219200000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 156, 22, 9, 1777305600000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 157, 22, 9, 1777392000000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 158, 22, 9, 1777478400000, 'AFTER', 28800000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 159, 22, 9, 1777564800000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 160, 22, 9, 1777651200000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 161, 22, 9, 1777737600000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 162, 16, 9, 1777219200000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 163, 16, 9, 1777305600000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 164, 16, 9, 1777392000000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 165, 16, 9, 1777478400000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 166, 16, 9, 1777564800000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 167, 16, 9, 1777651200000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 168, 16, 9, 1777737600000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 169, 25, 9, 1777219200000, 'AFTER', 35400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 170, 25, 9, 1777305600000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 171, 25, 9, 1777392000000, 'AFTER', 33600000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 172, 25, 9, 1777478400000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 173, 25, 9, 1777564800000, 'AFTER', 33600000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 174, 25, 9, 1777651200000, 'AFTER', 33600000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 175, 25, 9, 1777737600000, 'AFTER', 33600000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 176, 26, 9, 1777219200000, 'AFTER', 37200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 177, 26, 9, 1777305600000, 'AFTER', 37200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 178, 26, 9, 1777392000000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 179, 26, 9, 1777478400000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 180, 26, 9, 1777564800000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 181, 26, 9, 1777651200000, 'AFTER', 35400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 182, 26, 9, 1777737600000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 183, 21, 9, 1777219200000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 184, 21, 9, 1777305600000, 'AFTER', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 185, 21, 9, 1777392000000, 'AFTER', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 186, 21, 9, 1777478400000, 'AFTER', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 187, 21, 9, 1777564800000, 'AFTER', 35400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 188, 21, 9, 1777651200000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 189, 21, 9, 1777737600000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 190, 17, 9, 1777219200000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 191, 17, 9, 1777305600000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 192, 17, 9, 1777392000000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 193, 17, 9, 1777478400000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 194, 17, 9, 1777564800000, 'BEFORE', 32400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 195, 17, 9, 1777651200000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 196, 17, 9, 1777737600000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 197, 23, 9, 1777219200000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 198, 23, 9, 1777305600000, 'AFTER', 35400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 199, 23, 9, 1777392000000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 200, 23, 9, 1777478400000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 201, 23, 9, 1777564800000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 202, 23, 9, 1777651200000, 'AFTER', 35400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 203, 23, 9, 1777737600000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 204, 27, 9, 1777219200000, 'OFF', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 205, 27, 9, 1777305600000, 'AFTER', 37200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 206, 27, 9, 1777392000000, 'AFTER', 37200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 207, 27, 9, 1777478400000, 'AFTER', 28800000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 208, 27, 9, 1777564800000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 209, 27, 9, 1777651200000, 'AFTER', 37200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO availability (id, employee_id, weekly_schedule_id, date, availability_type, boundary_time, note, created_at, updated_at)
SELECT 210, 27, 9, 1777737600000, 'ALL_DAY', NULL, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 1, 1, 1781452800000, '月休', 1781078994000, 1781078994000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 2, 1, 1777219200000, '月休', 1781079519000, 1781079519000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 14, 7, 1780675200000, '月休', 1781084434000, 1781084434000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 15, 8, 1780675200000, '月休', 1781084434000, 1781084434000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 16, 10, 1780675200000, '月休', 1781084435000, 1781084435000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 17, 4, 1782489600000, '月休', 1781084437000, 1781084437000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 18, 5, 1782489600000, '月休', 1781084437000, 1781084437000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 19, 6, 1782489600000, '月休', 1781084438000, 1781084438000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 25, 2, 1782057600000, '月休', 1781084469000, 1781084469000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 26, 3, 1782057600000, '月休', 1781084470000, 1781084470000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 27, 4, 1782144000000, '月休', 1781084471000, 1781084471000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 28, 5, 1782144000000, '月休', 1781084471000, 1781084471000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 29, 6, 1782230400000, '月休', 1781084472000, 1781084472000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 30, 11, 1782230400000, '月休', 1781084473000, 1781084473000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 31, 2, 1782316800000, '月休', 1781084474000, 1781084474000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 32, 6, 1782316800000, '月休', 1781084474000, 1781084474000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 33, 11, 1782316800000, '月休', 1781084475000, 1781084475000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 35, 6, 1782403200000, '月休', 1781084477000, 1781084477000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 36, 4, 1782403200000, '月休', 1781084478000, 1781084478000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 37, 10, 1782403200000, '月休', 1781084478000, 1781084478000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 38, 4, 1782057600000, '月休', 1781084521000, 1781084521000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 39, 5, 1782057600000, '月休', 1781084521000, 1781084521000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 41, 3, 1777305600000, '月休', 1781085858000, 1781085858000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 42, 7, 1777305600000, '月休', 1781085859000, 1781085859000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 43, 8, 1777305600000, '月休', 1781085859000, 1781085859000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 44, 4, 1777392000000, '月休', 1781085861000, 1781085861000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 45, 10, 1777392000000, '月休', 1781085861000, 1781085861000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 46, 5, 1777392000000, '月休', 1781085862000, 1781085862000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 47, 4, 1777478400000, '月休', 1781085863000, 1781085863000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 48, 10, 1777478400000, '月休', 1781085863000, 1781085863000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 49, 9, 1777478400000, '月休', 1781085864000, 1781085864000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 50, 9, 1777564800000, '月休', 1781085867000, 1781085867000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 51, 4, 1777564800000, '月休', 1781085868000, 1781085868000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 52, 12, 1777564800000, '月休', 1781085868000, 1781085868000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 53, 12, 1777651200000, '月休', 1781085869000, 1781085869000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 54, 11, 1777651200000, '月休', 1781085869000, 1781085869000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 55, 13, 1777651200000, '月休', 1781085870000, 1781085870000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 56, 12, 1777737600000, '月休', 1781085871000, 1781085871000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 57, 13, 1777737600000, '月休', 1781085871000, 1781085871000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 58, 11, 1777737600000, '月休', 1781085873000, 1781085873000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 75, 7, 1780588800000, '月休', 1781094951000, 1781094951000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 76, 3, 1780588800000, '月休', 1781094952000, 1781094952000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 77, 2, 1780588800000, '月休', 1781094953000, 1781094953000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 79, 5, 1780588800000, '月休', 1781094969000, 1781094969000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 80, 9, 1780588800000, '月休', 1781095255000, 1781095255000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 82, 6, 1780588800000, '月休', 1781095259000, 1781095259000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 83, 4, 1780588800000, '月休', 1781095260000, 1781095260000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO monthly_leaves (id, employee_id, leave_date, note, created_at, updated_at)
SELECT 84, 10, 1780588800000, '月休', 1781095261000, 1781095261000
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 586, 9, 2, NULL, 1777219200000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 588, 9, 4, 6, 1777219200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 589, 9, 5, 2, 1777219200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 590, 9, 6, 1, 1777219200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 592, 9, 8, 4, 1777219200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 593, 9, 9, 2, 1777219200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 594, 9, 10, 1, 1777219200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 595, 9, 11, 3, 1777219200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 596, 9, 12, 4, 1777219200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 597, 9, 13, NULL, 1777219200000, 17400000, 50400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 598, 9, 2, NULL, 1777305600000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 599, 9, 4, 6, 1777305600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 600, 9, 5, 3, 1777305600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 601, 9, 6, 8, 1777305600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 602, 9, 9, 5, 1777305600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 603, 9, 10, 3, 1777305600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 604, 9, 11, 1, 1777305600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 605, 9, 12, 2, 1777305600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 606, 9, 13, NULL, 1777305600000, 17400000, 50400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 607, 9, 2, NULL, 1777392000000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 609, 9, 6, 1, 1777392000000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 610, 9, 7, 4, 1777392000000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 611, 9, 8, 3, 1777392000000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 612, 9, 9, 2, 1777392000000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 613, 9, 11, 3, 1777392000000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 614, 9, 12, 6, 1777392000000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 615, 9, 13, NULL, 1777392000000, 17400000, 50400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 616, 9, 2, NULL, 1777478400000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 618, 9, 5, 6, 1777478400000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 619, 9, 6, 1, 1777478400000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 620, 9, 7, 2, 1777478400000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 621, 9, 8, 3, 1777478400000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 622, 9, 11, 2, 1777478400000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 623, 9, 12, 4, 1777478400000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 624, 9, 13, NULL, 1777478400000, 17400000, 50400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 625, 9, 2, NULL, 1777564800000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 626, 9, 3, 5, 1777564800000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 627, 9, 5, 6, 1777564800000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 628, 9, 6, 4, 1777564800000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 629, 9, 7, 2, 1777564800000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 630, 9, 8, 3, 1777564800000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 631, 9, 10, 4, 1777564800000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 632, 9, 11, 1, 1777564800000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 633, 9, 13, NULL, 1777564800000, 17400000, 50400000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 634, 9, 2, NULL, 1777651200000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 635, 9, 3, 5, 1777651200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 636, 9, 4, 6, 1777651200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 637, 9, 5, 3, 1777651200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 638, 9, 6, 4, 1777651200000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 639, 9, 7, 2, 1777651200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 640, 9, 8, 3, 1777651200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 641, 9, 9, 4, 1777651200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 642, 9, 10, 1, 1777651200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 643, 9, 2, NULL, 1777737600000, 3000000, 34200000, NULL, NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 644, 9, 3, 5, 1777737600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 645, 9, 4, 6, 1777737600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 646, 9, 5, 4, 1777737600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 647, 9, 6, 3, 1777737600000, 31800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 648, 9, 7, 2, 1777737600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 649, 9, 8, 1, 1777737600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 650, 9, 9, 3, 1777737600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 651, 9, 10, 7, 1777737600000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 662, 9, 16, 2, 1777305600000, 6600000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 664, 9, 17, 1, 1777305600000, 12000000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 665, 9, 19, 4, 1777305600000, 4800000, 30600000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 666, 9, 16, 1, 1777392000000, 12000000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 667, 9, 21, 2, 1777392000000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 668, 9, 23, 4, 1777392000000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 669, 9, 17, 4, 1777478400000, 4800000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 670, 9, 27, 3, 1777478400000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 671, 9, 23, 1, 1777478400000, 12000000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 672, 9, 17, 1, 1777564800000, 12000000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 673, 9, 22, 2, 1777564800000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 674, 9, 25, 3, 1777564800000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 675, 9, 19, 1, 1777651200000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 676, 9, 25, 2, 1777651200000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 677, 9, 26, 4, 1777737600000, 4800000, 32400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 678, 9, 21, 2, 1777737600000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 679, 9, 25, 1, 1777737600000, 33600000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 681, 9, 3, 5, 1777478400000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 682, 9, 3, 5, 1777392000000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 683, 9, 3, 5, 1777219200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 686, 9, 21, 4, 1777305600000, 30000000, 54000000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 687, 9, 7, 3, 1777219200000, 3000000, 34200000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 696, 9, 14, NULL, 1777219200000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 697, 9, 15, NULL, 1777305600000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 698, 9, 14, NULL, 1777392000000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 699, 9, 14, NULL, 1777564800000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 700, 9, 14, NULL, 1777478400000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 701, 9, 15, NULL, 1777651200000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

INSERT INTO schedule_assignments (id, weekly_schedule_id, employee_id, position_id, date, start_time, end_time, note, created_at, updated_at)
SELECT 702, 9, 15, NULL, 1777737600000, 49800000, -23400000, '', NULL, NULL
WHERE (SELECT should_seed FROM seed_control) = 1;

DROP TABLE seed_control;
