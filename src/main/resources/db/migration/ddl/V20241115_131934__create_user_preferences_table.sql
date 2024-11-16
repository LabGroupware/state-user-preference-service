CREATE TABLE user_preferences (
    user_preference_id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL UNIQUE,
    version INTEGER DEFAULT 0 NOT NULL,
    timezone VARCHAR(50),
    theme VARCHAR(50),
    language VARCHAR(50),
    notification_setting_id VARCHAR(100),
    created_at date NOT NULL,
    created_by varchar(50) NOT NULL,
    updated_at date DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL
);

CREATE INDEX user_preferences_user_id_index ON user_preferences (user_id);