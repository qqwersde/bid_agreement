CREATE TABLE `deposit_payment`
(
    `id`          bigint UNSIGNED AUTO_INCREMENT NOT NULL COMMENT 'id',
    `payer_phone`     VARCHAR(32)                    NOT NULL COMMENT '支付人电话',
    `payer_account`    VARCHAR(32)                    NOT NULL COMMENT '支付人账户',
    `payer_name`      VARCHAR(32)                    NOT NULL COMMENT '支付人姓名',
    `deposit_amount`  DOUBLE                         NOT NULL COMMENT '金额',
    `pay_no`          VARCHAR(32)                    NOT NULL COMMENT '请求支付编号',
    `status`          VARCHAR(32)                    NOT NULL COMMENT '状态',
    `payment_id`          VARCHAR(32)                    NOT NULL COMMENT '支付编号',
    `create_time` DATETIME                   NOT NULL COMMENT '创建时间',
    `modify_time` DATETIME                   NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
)
