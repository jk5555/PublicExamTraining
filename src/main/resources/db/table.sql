CREATE TABLE IF NOT EXISTS paper
            (
                id          INT PRIMARY KEY AUTO_INCREMENT,
                source_id   varchar(64) comment '来源唯一标识，指的是题源的主键',
                source_url  varchar(256) comment '题源的url',
                title       varchar(256) comment '试卷标题',
                source      varchar(256) comment '试卷来源',
                province    varchar(64) comment '省份',
                create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                UNIQUE (source_url)
            );

CREATE TABLE IF NOT EXISTS question
            (
                id              INT PRIMARY KEY AUTO_INCREMENT,
                paper_id        INT comment '试卷ID',
                source          varchar(64) comment '题源（试卷标题）',
                question_type   varchar(64) comment '问题类型',
                question_stem   text comment '问题题干',
                answer_select   text comment '答案选项，选项之间用|*|分隔',
                answer          varchar(64) comment '正确答案',
                answer_analysis text comment '答案解析',
                create_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                update_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );

CREATE TABLE IF NOT EXISTS init_flag
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    init_status     tinyint(1) DEFAULT 0 comment '试卷ID',
    create_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

MERGE INTO init_flag AS t USING (SELECT 1 AS id) AS s ON t.id = s.id WHEN NOT MATCHED THEN INSERT (id, init_status) VALUES (1,0);
