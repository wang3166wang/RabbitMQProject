/*
Navicat MySQL Data Transfer

Source Server         : 47.96.9.32
Source Server Version : 80020
Source Host           : 47.96.9.32:3306
Source Database       : mall_order

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2022-09-23 14:25:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for transfers
-- ----------------------------
DROP TABLE IF EXISTS `transfers`;
CREATE TABLE `transfers` (
  `id` bigint NOT NULL,
  `partner_trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户订单号',
  `amount` int DEFAULT NULL COMMENT '实际提现金额（分）',
  `apply_amount` int DEFAULT NULL COMMENT '申请提现金额（分）',
  `service_charge` int DEFAULT NULL COMMENT '手续费（n%）',
  `re_user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收款用户姓名',
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户openid',
  `mch_appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户账号appid',
  `mchid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户号',
  `check_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '校验用户姓名选项(NO_CHECK：不校验真实姓名;FORCE_CHECK：强校验真实姓名)',
  `payment_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '微信付款单号',
  `payment_time` datetime DEFAULT NULL COMMENT '付款成功时间',
  `nonce_str` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '随机字符串(返回)',
  `return_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '返回状态码',
  `return_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '返回信息',
  `result_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务结果',
  `err_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '错误代码描述',
  `deleted` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '删除标识（Y,N）',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `note` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `extension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展字段',
  `agent_id` bigint DEFAULT NULL COMMENT '代理人id',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核状态（拒绝：REJECT | 待付款：WAIT | 未审核：UNCHECK | 提款成功：SUCCESS | 提款失败：FAIL）',
  `withdraw_way` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '提现方式（WX | ZFB | BANK）',
  `handle_way` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '处理方式（AUTO | MANUAL）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `orderNo` (`partner_trade_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
