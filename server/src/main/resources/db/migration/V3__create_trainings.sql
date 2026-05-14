-- 创建实训表
CREATE TABLE IF NOT EXISTS trainings (
    _training_id VARCHAR(10) PRIMARY KEY,
    name TINYTEXT NOT NULL,
    content TINYTEXT NOT NULL,
    video LONGBLOB,
    time TINYINT NOT NULL,
    knowledge_points VARCHAR(10) NOT NULL,
    difficult DECIMAL(2,1) NOT NULL
);