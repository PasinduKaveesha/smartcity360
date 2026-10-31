-- SmartCity360 Database Schema
-- Target: MySQL 8.x, InnoDB engine, utf8mb4 charset

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    role VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS zone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    level VARCHAR(20) NOT NULL,
    parent_id BIGINT NULL,
    powered BOOLEAN DEFAULT 1,
    base_usage_kwh DECIMAL(12,2) DEFAULT 0,
    FOREIGN KEY (parent_id) REFERENCES zone(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS energy_meter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meter_code VARCHAR(50) NOT NULL UNIQUE,
    zone_id BIGINT NOT NULL,
    layers VARCHAR(100),
    installed_at DATETIME NOT NULL,
    FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS meter_reading (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    meter_id BIGINT NOT NULL,
    reading_kwh DECIMAL(12,3) NOT NULL,
    raw_size INT NOT NULL,
    secured_size INT NOT NULL,
    layers_applied VARCHAR(100),
    recorded_at DATETIME NOT NULL,
    FOREIGN KEY (meter_id) REFERENCES energy_meter(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS staff_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS resident (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(200),
    zone_id BIGINT,
    FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS complaint (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    severity VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    resolved_by_staff_id BIGINT NULL,
    created_at DATETIME NOT NULL,
    resolved_at DATETIME NULL,
    FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE CASCADE,
    FOREIGN KEY (resolved_by_staff_id) REFERENCES staff_member(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS complaint_trace (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    complaint_id BIGINT NOT NULL,
    step_no INT NOT NULL,
    handler_role VARCHAR(20) NOT NULL,
    handler_name VARCHAR(100) NOT NULL,
    action VARCHAR(20) NOT NULL,
    note VARCHAR(255),
    logged_at DATETIME NOT NULL,
    FOREIGN KEY (complaint_id) REFERENCES complaint(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS streetlight_model (
    model_code VARCHAR(40) PRIMARY KEY,
    lamp_type VARCHAR(50) NOT NULL,
    wattage INT NOT NULL,
    max_lumens INT NOT NULL,
    dim_profile VARCHAR(50) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS streetlight (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    model_code VARCHAR(40) NOT NULL,
    zone_id BIGINT NOT NULL,
    latitude DECIMAL(9,6) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL,
    state VARCHAR(20) NOT NULL,
    brightness_percent INT NOT NULL,
    FOREIGN KEY (model_code) REFERENCES streetlight_model(model_code) ON DELETE RESTRICT,
    FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sensor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sensor_code VARCHAR(50) NOT NULL UNIQUE,
    sensor_type VARCHAR(20) NOT NULL,
    zone_id BIGINT NOT NULL,
    active BOOLEAN DEFAULT 1,
    FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS sensor_reading (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sensor_id BIGINT NOT NULL,
    value DOUBLE NOT NULL,
    recorded_at DATETIME NOT NULL,
    FOREIGN KEY (sensor_id) REFERENCES sensor(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS waste_bin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bin_code VARCHAR(50) NOT NULL UNIQUE,
    zone_id BIGINT NOT NULL,
    location_desc VARCHAR(200),
    capacity_litres INT NOT NULL,
    fill_percent INT NOT NULL,
    last_collected_at DATETIME NULL,
    FOREIGN KEY (zone_id) REFERENCES zone(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS policy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    rule_text VARCHAR(500) NOT NULL,
    action_text VARCHAR(200) NOT NULL,
    enabled BOOLEAN DEFAULT 1,
    updated_at DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS policy_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_id BIGINT NOT NULL,
    triggered BOOLEAN NOT NULL,
    context_snapshot VARCHAR(500) NOT NULL,
    evaluated_at DATETIME NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES policy(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
