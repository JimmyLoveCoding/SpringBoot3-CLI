-- 课程管理系统数据库脚本

USE `course_system`;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `username`    VARCHAR(50)      NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100)     NOT NULL COMMENT '密码',
    `real_name`   VARCHAR(50)      DEFAULT NULL COMMENT '真实姓名',
    `phone`       VARCHAR(20)      DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100)     DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(500)     DEFAULT NULL COMMENT '头像URL',
    `role`        VARCHAR(20)      DEFAULT 'student' COMMENT '角色：admin-管理员, teacher-教师, student-学生',
    `status`      TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-禁用, 1-启用',
    `creator_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除'
) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;

-- 课程表
DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_code`  VARCHAR(50)      NOT NULL COMMENT '课程编号',
    `course_name`  VARCHAR(100)     NOT NULL COMMENT '课程名称',
    `credit`       DECIMAL(3, 1)    DEFAULT 2.0 COMMENT '学分',
    `teacher_id`   BIGINT UNSIGNED  NOT NULL COMMENT '教师ID',
    `classroom`    VARCHAR(100)     DEFAULT NULL COMMENT '教室',
    `schedule`     VARCHAR(200)     DEFAULT NULL COMMENT '上课时间，如：周一 8:00-10:00',
    `semester`     VARCHAR(50)      DEFAULT NULL COMMENT '学期，如：2024-2025-1',
    `description`  TEXT             DEFAULT NULL COMMENT '课程描述',
    `max_students` INT              DEFAULT 50 COMMENT '最大学生数',
    `status`       TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-未开课, 1-进行中, 2-已结课',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除'
) COMMENT '课程表' COLLATE = utf8mb4_unicode_ci;

-- 学生选课表
DROP TABLE IF EXISTS `course_student`;
CREATE TABLE IF NOT EXISTS `course_student`
(
    `id`          BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`   BIGINT UNSIGNED NOT NULL COMMENT '课程ID',
    `student_id`  BIGINT UNSIGNED NOT NULL COMMENT '学生ID',
    `score`       DECIMAL(5, 2)   DEFAULT NULL COMMENT '成绩',
    `creator_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '创建者ID',
    `create_time` DATETIME        DEFAULT NULL COMMENT '创建时间',
    `updater_id`  BIGINT UNSIGNED DEFAULT NULL COMMENT '更新者ID',
    `update_time` DATETIME        DEFAULT NULL COMMENT '更新时间',
    `version`     TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`  TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除',
    UNIQUE KEY `uk_course_student` (`course_id`, `student_id`)
) COMMENT '学生选课表' COLLATE = utf8mb4_unicode_ci;

-- 考勤记录表
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE IF NOT EXISTS `attendance`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `student_id`   BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `attend_date`  DATE             NOT NULL COMMENT '考勤日期',
    `status`       TINYINT UNSIGNED NOT NULL COMMENT '状态：0-缺勤, 1-出勤, 2-迟到, 3-请假, 4-早退',
    `check_in_time` DATETIME        DEFAULT NULL COMMENT '签到时间',
    `check_out_time` DATETIME       DEFAULT NULL COMMENT '签退时间',
    `location`     VARCHAR(200)     DEFAULT NULL COMMENT '签到位置',
    `remark`       VARCHAR(500)     DEFAULT NULL COMMENT '备注',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除',
    UNIQUE KEY `uk_course_student_date` (`course_id`, `student_id`, `attend_date`)
) COMMENT '考勤记录表' COLLATE = utf8mb4_unicode_ci;

-- 请假申请表
DROP TABLE IF EXISTS `leave_request`;
CREATE TABLE IF NOT EXISTS `leave_request`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `student_id`   BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `leave_type`   TINYINT UNSIGNED NOT NULL COMMENT '请假类型：1-病假, 2-事假, 3-其他',
    `start_date`   DATE             NOT NULL COMMENT '开始日期',
    `end_date`     DATE             NOT NULL COMMENT '结束日期',
    `reason`       TEXT             NOT NULL COMMENT '请假原因',
    `attachment`   VARCHAR(500)     DEFAULT NULL COMMENT '附件URL',
    `status`       TINYINT UNSIGNED DEFAULT 0 COMMENT '审批状态：0-待审批, 1-已通过, 2-已拒绝',
    `approver_id`  BIGINT UNSIGNED  DEFAULT NULL COMMENT '审批人ID',
    `approve_time` DATETIME         DEFAULT NULL COMMENT '审批时间',
    `approve_remark` VARCHAR(500)   DEFAULT NULL COMMENT '审批备注',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除'
) COMMENT '请假申请表' COLLATE = utf8mb4_unicode_ci;

-- 课程讨论区表
DROP TABLE IF EXISTS `course_discussion`;
CREATE TABLE IF NOT EXISTS `course_discussion`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `user_id`      BIGINT UNSIGNED  NOT NULL COMMENT '发布者ID',
    `parent_id`    BIGINT UNSIGNED  DEFAULT NULL COMMENT '父评论ID，用于回复',
    `content`      TEXT             NOT NULL COMMENT '评论内容',
    `attachment`   VARCHAR(500)     DEFAULT NULL COMMENT '附件URL',
    `like_count`   INT              DEFAULT 0 COMMENT '点赞数',
    `is_top`       TINYINT UNSIGNED DEFAULT 0 COMMENT '是否置顶：0-否, 1-是',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除'
) COMMENT '课程讨论区表' COLLATE = utf8mb4_unicode_ci;

-- 作业表
DROP TABLE IF EXISTS `assignment`;
CREATE TABLE IF NOT EXISTS `assignment`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`      BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `title`          VARCHAR(200)     NOT NULL COMMENT '作业标题',
    `description`    TEXT             DEFAULT NULL COMMENT '作业描述',
    `attachment`     VARCHAR(500)     DEFAULT NULL COMMENT '附件URL',
    `deadline`       DATETIME         NOT NULL COMMENT '截止时间',
    `total_score`    DECIMAL(5, 2)    DEFAULT 100.00 COMMENT '总分',
    `status`         TINYINT UNSIGNED DEFAULT 1 COMMENT '状态：0-草稿, 1-已发布, 2-已截止',
    `creator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`    DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`    DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除'
) COMMENT '作业表' COLLATE = utf8mb4_unicode_ci;

-- 作业提交表
DROP TABLE IF EXISTS `assignment_submission`;
CREATE TABLE IF NOT EXISTS `assignment_submission`
(
    `id`             BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `assignment_id`  BIGINT UNSIGNED  NOT NULL COMMENT '作业ID',
    `student_id`     BIGINT UNSIGNED  NOT NULL COMMENT '学生ID',
    `content`        TEXT             DEFAULT NULL COMMENT '提交内容',
    `attachment`     VARCHAR(500)     DEFAULT NULL COMMENT '附件URL',
    `score`          DECIMAL(5, 2)    DEFAULT NULL COMMENT '得分',
    `feedback`       TEXT             DEFAULT NULL COMMENT '教师反馈',
    `submit_time`    DATETIME         DEFAULT NULL COMMENT '提交时间',
    `status`         TINYINT UNSIGNED DEFAULT 0 COMMENT '状态：0-未提交, 1-已提交, 2-已批改',
    `creator_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`    DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`     BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`    DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`        TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`     TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除',
    UNIQUE KEY `uk_assignment_student` (`assignment_id`, `student_id`)
) COMMENT '作业提交表' COLLATE = utf8mb4_unicode_ci;

-- 课程资料表
DROP TABLE IF EXISTS `course_material`;
CREATE TABLE IF NOT EXISTS `course_material`
(
    `id`           BIGINT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
    `course_id`    BIGINT UNSIGNED  NOT NULL COMMENT '课程ID',
    `title`        VARCHAR(200)     NOT NULL COMMENT '资料标题',
    `description`  VARCHAR(500)     DEFAULT NULL COMMENT '资料描述',
    `file_url`     VARCHAR(500)     NOT NULL COMMENT '文件URL',
    `file_type`    VARCHAR(50)      DEFAULT NULL COMMENT '文件类型',
    `file_size`    BIGINT           DEFAULT NULL COMMENT '文件大小（字节）',
    `download_count` INT            DEFAULT 0 COMMENT '下载次数',
    `creator_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '创建者ID',
    `create_time`  DATETIME         DEFAULT NULL COMMENT '创建时间',
    `updater_id`   BIGINT UNSIGNED  DEFAULT NULL COMMENT '更新者ID',
    `update_time`  DATETIME         DEFAULT NULL COMMENT '更新时间',
    `version`      TINYINT UNSIGNED DEFAULT NULL COMMENT '乐观锁版本',
    `is_deleted`   TINYINT UNSIGNED DEFAULT 0 COMMENT '逻辑删除：0-未删除, 1-已删除'
) COMMENT '课程资料表' COLLATE = utf8mb4_unicode_ci;

-- 插入测试数据
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '管理员', '13800138000', 'admin@example.com', 'admin', 1, NOW(), NOW()),
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '张老师', '13800138001', 'teacher1@example.com', 'teacher', 1, NOW(), NOW()),
('teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '李老师', '13800138002', 'teacher2@example.com', 'teacher', 1, NOW(), NOW()),
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '王同学', '13800138003', 'student1@example.com', 'student', 1, NOW(), NOW()),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '刘同学', '13800138004', 'student2@example.com', 'student', 1, NOW(), NOW()),
('student3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '陈同学', '13800138005', 'student3@example.com', 'student', 1, NOW(), NOW());

INSERT INTO `course` (`course_code`, `course_name`, `credit`, `teacher_id`, `classroom`, `schedule`, `semester`, `description`, `max_students`, `status`, `create_time`, `update_time`) VALUES
('CS101', 'Java程序设计', 4.0, 2, 'A101', '周一 8:00-10:00, 周三 8:00-10:00', '2024-2025-1', 'Java编程基础课程', 60, 1, NOW(), NOW()),
('CS102', '数据结构与算法', 3.5, 2, 'A102', '周二 14:00-16:00, 周四 14:00-16:00', '2024-2025-1', '数据结构与算法分析', 50, 1, NOW(), NOW()),
('CS201', '数据库原理', 3.0, 3, 'B201', '周一 14:00-16:00, 周五 8:00-10:00', '2024-2025-1', '关系数据库原理与应用', 55, 1, NOW(), NOW()),
('CS202', '计算机网络', 3.5, 3, 'B202', '周三 14:00-16:00, 周五 14:00-16:00', '2024-2025-1', '计算机网络基础', 50, 1, NOW(), NOW());

INSERT INTO `course_student` (`course_id`, `student_id`, `create_time`, `update_time`) VALUES
(1, 4, NOW(), NOW()),
(1, 5, NOW(), NOW()),
(1, 6, NOW(), NOW()),
(2, 4, NOW(), NOW()),
(2, 5, NOW(), NOW()),
(3, 4, NOW(), NOW()),
(3, 6, NOW(), NOW()),
(4, 5, NOW(), NOW()),
(4, 6, NOW(), NOW());
