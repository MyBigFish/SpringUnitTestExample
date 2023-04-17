DROP TABLE IF EXISTS user;

CREATE TABLE `user`
(
    `id`              bigint(20)                             NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `nickname`        varchar(50)                            DEFAULT NULL COMMENT '昵称',
    `is_enable`       tinyint(1)                              DEFAULT NULL COMMENT '0 不可用  1可用',
    `create_time`     datetime                               NOT NULL COMMENT '创建时间',
    `update_time`     datetime                                DEFAULT NULL COMMENT '修改时间',
    `is_deleted`      tinyint(1)                             NOT NULL COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8mb4
COMMENT ='用户';