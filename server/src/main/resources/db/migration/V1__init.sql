CREATE TABLE admins (
    account VARCHAR(10) PRIMARY KEY NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(5) NOT NULL,
    phone VARCHAR(13),
    avatar BLOB
);

CREATE TABLE users (
    account VARCHAR(10) PRIMARY KEY NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(5) NOT NULL,
    education VARCHAR(3),
    phone VARCHAR(13),
    address VARCHAR(15),
    email VARCHAR(20),
    avatar BLOB
);

CREATE TABLE courses (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(10) NOT NULL,
    teacher_profile TINYTEXT,
    introduction TINYTEXT,
    video BLOB,
    time TINYINT UNSIGNED,
    star VARCHAR(5)
);

CREATE TABLE knowledge_points (
    type_id INT PRIMARY KEY NOT NULL,
    name VARCHAR(10) NOT NULL,
    content TEXT,
    video BLOB,
    difficult DECIMAL(3,1),
    importance DECIMAL(3,1),
    example TEXT
);

CREATE TABLE materials (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(10) NOT NULL,
    content TEXT,
    video BLOB,
    difficult DECIMAL(3,1),
    importance DECIMAL(3,1)
);

CREATE TABLE exercises (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title TINYTEXT NOT NULL,
    answer TINYTEXT NOT NULL,
    option_a VARCHAR(100),
    option_b VARCHAR(100),
    option_c VARCHAR(100),
    option_d VARCHAR(100),
    knowledge_points INT,
    difficult DECIMAL(3,1),
    CONSTRAINT fk_exercise_kp FOREIGN KEY (knowledge_points) REFERENCES knowledge_points (type_id)
);

CREATE TABLE mock_questions (
    type_id VARCHAR(10) PRIMARY KEY NOT NULL,
    title TINYTEXT NOT NULL,
    answer TINYTEXT NOT NULL,
    option_a VARCHAR(100),
    option_b VARCHAR(100),
    option_c VARCHAR(100),
    option_d VARCHAR(100),
    knowledge_points INT,
    difficult DECIMAL(3,1),
    CONSTRAINT fk_mock_kp FOREIGN KEY (knowledge_points) REFERENCES knowledge_points (type_id)
);

CREATE TABLE real_questions (
    type_id VARCHAR(10) PRIMARY KEY NOT NULL,
    title TINYTEXT NOT NULL,
    answer TINYTEXT NOT NULL,
    year DATETIME,
    knowledge_points INT,
    difficult DECIMAL(3,1),
    CONSTRAINT fk_real_kp FOREIGN KEY (knowledge_points) REFERENCES knowledge_points (type_id)
);

CREATE TABLE exam_papers (
    type_id INT PRIMARY KEY NOT NULL,
    exam_time DATETIME,
    problem TEXT NOT NULL,
    answer TEXT NOT NULL,
    difficult DECIMAL(3,1)
);

CREATE TABLE scores (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(5) NOT NULL,
    paper_id INT,
    exam_time DATETIME,
    score INT,
    CONSTRAINT fk_score_paper FOREIGN KEY (paper_id) REFERENCES exam_papers (type_id)
);

CREATE TABLE posts (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    user_account VARCHAR(10),
    create_time DATETIME,
    CONSTRAINT fk_post_user FOREIGN KEY (user_account) REFERENCES users (account)
);