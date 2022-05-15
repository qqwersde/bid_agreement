CREATE TABLE if not exists `bid_agreement`
(
    `id`          bigint UNSIGNED AUTO_INCREMENT NOT NULL COMMENT 'id',
    `status`  VARCHAR(32)                    NOT NULL COMMENT '状态',
    `create_time` DATETIME                       NOT NULL COMMENT '创建时间',
    `modify_time` DATETIME                       NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;