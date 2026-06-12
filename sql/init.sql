CREATE DATABASE IF NOT EXISTS beehive_manager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE beehive_manager;

DROP TABLE IF EXISTS inspection_records;
DROP TABLE IF EXISTS honey_harvests;
DROP TABLE IF EXISTS migration_records;
DROP TABLE IF EXISTS seasonal_nectar_sources;
DROP TABLE IF EXISTS hives;
DROP TABLE IF EXISTS apiaries;

CREATE TABLE apiaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '蜂场名称',
    location VARCHAR(255) NOT NULL COMMENT '蜂场位置',
    latitude DECIMAL(10, 7) COMMENT '纬度',
    longitude DECIMAL(10, 7) COMMENT '经度',
    main_nectar_plant VARCHAR(100) COMMENT '主打蜜源植物',
    area_size DECIMAL(10, 2) COMMENT '面积(亩)',
    description TEXT COMMENT '备注描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '蜂场档案';

CREATE TABLE seasonal_nectar_sources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    apiary_id BIGINT NOT NULL COMMENT '蜂场ID',
    season VARCHAR(20) NOT NULL COMMENT '季节(春/夏/秋/冬)',
    nectar_plant VARCHAR(100) NOT NULL COMMENT '蜜源植物名称',
    bloom_start DATE COMMENT '花期开始',
    bloom_end DATE COMMENT '花期结束',
    description VARCHAR(255) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apiary_id) REFERENCES apiaries(id) ON DELETE CASCADE
) COMMENT '季节性蜜源';

CREATE TABLE hives (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    apiary_id BIGINT NOT NULL COMMENT '所属蜂场ID',
    hive_number VARCHAR(50) NOT NULL COMMENT '蜂箱编号',
    queen_source VARCHAR(20) NOT NULL DEFAULT 'self_bred' COMMENT '蜂王来源: self_bred=自育, purchased=外购',
    queen_year INT COMMENT '蜂王年龄(年份)',
    queen_breed VARCHAR(50) COMMENT '蜂王品种',
    worker_bee_count INT COMMENT '工蜂数量(大致)',
    frame_count INT COMMENT '巢框数量',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active=活跃, weak=弱势, dead=已死亡',
    description TEXT COMMENT '备注描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apiary_id) REFERENCES apiaries(id) ON DELETE CASCADE
) COMMENT '蜂箱(蜂群)档案';

CREATE TABLE inspection_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hive_id BIGINT NOT NULL COMMENT '蜂箱ID',
    inspection_date DATE NOT NULL COMMENT '检查日期',
    queen_present BOOLEAN NOT NULL COMMENT '蜂王是否在',
    has_mites BOOLEAN DEFAULT FALSE COMMENT '是否有蜂螨',
    has_disease BOOLEAN DEFAULT FALSE COMMENT '是否闹病',
    disease_detail VARCHAR(255) COMMENT '病情描述',
    honey_store DECIMAL(10, 2) COMMENT '储蜜量(公斤)',
    brood_condition VARCHAR(20) COMMENT '子脾状况: good=良好, fair=一般, poor=差',
    temper VARCHAR(20) COMMENT '蜂群性情: gentle=温顺, normal=正常, aggressive=凶暴',
    overall_condition VARCHAR(20) COMMENT '整体评估: strong=强群, medium=中等, weak=弱群',
    notes TEXT COMMENT '检查备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hive_id) REFERENCES hives(id) ON DELETE CASCADE
) COMMENT '定期检查记录';

CREATE TABLE honey_harvests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hive_id BIGINT NOT NULL COMMENT '蜂箱ID',
    harvest_date DATE NOT NULL COMMENT '取蜜日期',
    weight DECIMAL(10, 2) NOT NULL COMMENT '取蜜重量(公斤)',
    water_content DECIMAL(5, 2) COMMENT '含水率(%)',
    nectar_source VARCHAR(100) COMMENT '蜜源植物',
    quality_grade VARCHAR(10) COMMENT '质量等级: A/B/C',
    notes TEXT COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (hive_id) REFERENCES hives(id) ON DELETE CASCADE
) COMMENT '取蜜记录';

CREATE TABLE migration_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    apiary_id BIGINT NOT NULL COMMENT '蜂场ID',
    departure_location VARCHAR(255) NOT NULL COMMENT '出发地',
    departure_date DATE NOT NULL COMMENT '出发日期',
    destination VARCHAR(255) NOT NULL COMMENT '目的地',
    arrival_date DATE COMMENT '到达日期',
    transport_vehicle VARCHAR(100) COMMENT '运输车辆',
    driver_name VARCHAR(50) COMMENT '司机姓名',
    driver_phone VARCHAR(20) COMMENT '司机电话',
    hive_count INT COMMENT '搬运蜂箱数量',
    reason VARCHAR(255) COMMENT '转场原因',
    distance_km DECIMAL(10, 1) COMMENT '距离(公里)',
    cost DECIMAL(10, 2) COMMENT '运输费用',
    notes TEXT COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apiary_id) REFERENCES apiaries(id) ON DELETE CASCADE
) COMMENT '转场记录';
