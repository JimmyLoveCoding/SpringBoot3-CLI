-- 课程管理系统数据库表

-- 1. 教师表
CREATE TABLE IF NOT EXISTS `teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `teacher_no` varchar(50) NOT NULL COMMENT '教师编号',
  `name` varchar(50) NOT NULL COMMENT '教师姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别：0-女，1-男',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `department` varchar(100) DEFAULT NULL COMMENT '所属院系',
  `title` varchar(50) DEFAULT NULL COMMENT '职称',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-离职，1-在职',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_no` (`teacher_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师表';

-- 2. 学生表
CREATE TABLE IF NOT EXISTS `student` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_no` varchar(50) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '学生姓名',
  `gender` tinyint DEFAULT NULL COMMENT '性别：0-女，1-男',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  `class_name` varchar(50) DEFAULT NULL COMMENT '班级',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-休学，1-在读',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_no` (`student_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

-- 3. 课程表
CREATE TABLE IF NOT EXISTS `course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_code` varchar(50) NOT NULL COMMENT '课程编码',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `credits` decimal(3,1) NOT NULL COMMENT '学分',
  `hours` int DEFAULT NULL COMMENT '总学时',
  `teacher_id` bigint NOT NULL COMMENT '授课教师ID',
  `classroom` varchar(50) DEFAULT NULL COMMENT '教室',
  `semester` varchar(50) DEFAULT NULL COMMENT '学期',
  `weekday` tinyint DEFAULT NULL COMMENT '上课日：1-周一，2-周二...7-周日',
  `start_time` time DEFAULT NULL COMMENT '开始时间',
  `end_time` time DEFAULT NULL COMMENT '结束时间',
  `max_students` int DEFAULT '50' COMMENT '最大选课人数',
  `description` text COMMENT '课程描述',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-关闭，1-开启',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_code` (`course_code`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

-- 4. 选课表
CREATE TABLE IF NOT EXISTS `course_enrollment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `enroll_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-已退课，1-已选，2-已完成',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_student` (`course_id`, `student_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='选课表';

-- 5. 考勤表
CREATE TABLE IF NOT EXISTS `attendance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `attend_date` date NOT NULL COMMENT '考勤日期',
  `status` tinyint DEFAULT '0' COMMENT '状态：0-未签到，1-已签到，2-迟到，3-早退，4-缺勤，5-请假',
  `sign_time` datetime DEFAULT NULL COMMENT '签到时间',
  `sign_type` tinyint DEFAULT NULL COMMENT '签到方式：1-扫码，2-定位，3-教师记录',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_student_date` (`course_id`, `student_id`, `attend_date`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_attend_date` (`attend_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤表';

-- 6. 请假表
CREATE TABLE IF NOT EXISTS `leave_request` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `leave_type` tinyint NOT NULL COMMENT '请假类型：1-事假，2-病假，3-公假，4-其他',
  `start_date` date NOT NULL COMMENT '开始日期',
  `end_date` date NOT NULL COMMENT '结束日期',
  `reason` varchar(500) NOT NULL COMMENT '请假原因',
  `attachment` varchar(255) DEFAULT NULL COMMENT '附件路径',
  `status` tinyint DEFAULT '0' COMMENT '状态：0-待审批，1-已通过，2-已拒绝',
  `approve_user` bigint DEFAULT NULL COMMENT '审批人ID',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_comment` varchar(255) DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='请假表';

-- 7. 课程评论表
CREATE TABLE IF NOT EXISTS `course_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `user_id` bigint NOT NULL COMMENT '评论人ID',
  `user_type` tinyint NOT NULL COMMENT '用户类型：1-学生，2-教师',
  `parent_id` bigint DEFAULT '0' COMMENT '父评论ID，0表示主题评论',
  `content` text NOT NULL COMMENT '评论内容',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-隐藏，1-显示',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程评论表';

-- 8. 作业表
CREATE TABLE IF NOT EXISTS `homework` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `title` varchar(100) NOT NULL COMMENT '作业标题',
  `description` text COMMENT '作业描述',
  `attachment` varchar(255) DEFAULT NULL COMMENT '附件路径',
  `deadline` datetime DEFAULT NULL COMMENT '截止时间',
  `full_score` int DEFAULT '100' COMMENT '满分',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-关闭，1-开启',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业表';

-- 9. 作业提交表
CREATE TABLE IF NOT EXISTS `homework_submission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `homework_id` bigint NOT NULL COMMENT '作业ID',
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `content` text COMMENT '提交内容',
  `attachment` varchar(255) DEFAULT NULL COMMENT '附件路径',
  `score` int DEFAULT NULL COMMENT '得分',
  `comment` varchar(500) DEFAULT NULL COMMENT '评语',
  `submit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `grade_time` datetime DEFAULT NULL COMMENT '批改时间',
  `status` tinyint DEFAULT '0' COMMENT '状态：0-未批改，1-已批改',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_homework_student` (`homework_id`, `student_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='作业提交表';

-- 10. 课程资料表
CREATE TABLE IF NOT EXISTS `course_material` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `title` varchar(100) NOT NULL COMMENT '资料标题',
  `description` varchar(255) DEFAULT NULL COMMENT '资料描述',
  `file_type` varchar(20) DEFAULT NULL COMMENT '文件类型',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小（字节）',
  `download_count` int DEFAULT '0' COMMENT '下载次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint DEFAULT '0' COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_course_id` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程资料表';
