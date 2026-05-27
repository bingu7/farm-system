/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : localhost:3306
 Source Schema         : farm_system

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 05/07/2024 17:18:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$tG8ONew591uv2hFWelynWOcSrZsfr2rHITd.t4eWEkGsWg3hxkW/y', '管理员', 'http://localhost:9090/files/download/avatar.png', 'ADMIN');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '农产品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '蔬菜');
INSERT INTO `category` VALUES (2, '水果');
INSERT INTO `category` VALUES (3, '农副产品');
INSERT INTO `category` VALUES (4, '菌菇');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `descr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `specials` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '特色',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位',
  `store` int(11) NULL DEFAULT NULL COMMENT '库存量',
  `category_id` int(11) NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '农产品信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, '新鲜土豆', 'http://localhost:9090/files/download/1720146768626-土豆1.png', '新鲜土豆，产自海南，好吃不贵', '香甜软糯，口口留香', 0.85, '斤', 2200, 1);
INSERT INTO `goods` VALUES (2, '西红柿', 'http://localhost:9090/files/download/1720146162330-西红柿.png', '新疆普罗旺斯沙瓤西红柿，新鲜上市', '香糯软沙，满口留香', 9.90, 'kg', 1099, 1);
INSERT INTO `goods` VALUES (4, '黄桃', 'http://localhost:9090/files/download/1720164318655-黄桃.png', '山东蒙阴黄桃新鲜水果桃子黄金蜜桃黄毛桃', '出口品质，山泉灌溉', 5.88, '斤', 998, 2);
INSERT INTO `goods` VALUES (5, '美都西瓜', 'http://localhost:9090/files/download/1720164348761-西瓜.png', '超甜美都西瓜8424美都现摘薄皮无籽有籽当季新鲜水果超甜美都西瓜8424美都现摘薄皮无籽有籽当季新鲜水果', '花样吃法，口感更佳', 21.99, '个', 665, 2);

-- ----------------------------
-- Table structure for goods_stock
-- ----------------------------
DROP TABLE IF EXISTS `goods_stock`;
CREATE TABLE `goods_stock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` int(11) NULL DEFAULT NULL COMMENT '商品ID',
  `num` int(11) NULL DEFAULT NULL COMMENT '进货数量',
  `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '进货渠道',
  `date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '进货日期',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '农产品进货' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_stock
-- ----------------------------
INSERT INTO `goods_stock` VALUES (1, 2, 100, '农产品批发市场', '2024-07-05', '1');
INSERT INTO `goods_stock` VALUES (2, 1, 100, '农产品批发市场', '2024-07-05', NULL);
INSERT INTO `goods_stock` VALUES (3, 2, 100, '农产品批发市场', '2024-07-05', NULL);
INSERT INTO `goods_stock` VALUES (4, 1, 200, '	 农产品批发市场', '2024-07-05', NULL);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统公告' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '好物上新！新鲜黄桃低至5元起！', '好物上新！新鲜黄桃低至5元起！甄选黄桃，口味绝佳，夏日必备！', '2024-07-01 15:00:00');
INSERT INTO `notice` VALUES (2, '夏天最适合吃什么？当然是西瓜更配！', '新鲜无籽麒麟西瓜新鲜上市！农家自种，现摘现发，无添加更健康！', '2024-07-02 15:00:00');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单编号',
  `goods_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品ID',
  `num` int(11) NULL DEFAULT NULL COMMENT '购买数量',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '下单人',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单状态',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, '7a3794e825d34ec3a2ee29c5f8e78e87', '5', 1, 1, '待发货', '2024-07-05 16:43:12');
INSERT INTO `orders` VALUES (2, '13e42a7b052e42718563f28c8c7047b0', '4', 2, 1, '已完成', '2024-07-05 16:50:00');
INSERT INTO `orders` VALUES (3, '51e5ea201f604beba53b2ba3fe94318e', '2', 1, 1, '已取消', '2024-07-05 16:50:59');
INSERT INTO `orders` VALUES (4, 'aa5139c231d740f1b7e21f21b2f14f99', '5', 1, 1, '已取消', '2024-07-05 17:00:22');
INSERT INTO `orders` VALUES (5, '47901470867144b895f27ca522166533', '5', 1, 1, '已取消', '2024-07-05 17:00:54');
INSERT INTO `orders` VALUES (6, '99000795570e40efa37648e780017d54', '5', 2, 1, '已取消', '2024-07-05 17:01:42');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '普通用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'demo_user', '$2a$10$YqWv.1Ba.yY2FGsvVO7AY.h9IC4xi4AAEeUscDNYdztdGyoe4KaSa', '演示用户甲', 'http://localhost:9090/files/download/avatar.png', 'USER', '男', '13800000001', 'demo1@example.com');
INSERT INTO `user` VALUES (2, 'demo_user_2', '$2a$10$YqWv.1Ba.yY2FGsvVO7AY.h9IC4xi4AAEeUscDNYdztdGyoe4KaSa', '演示用户乙', 'http://localhost:9090/files/download/avatar.png', 'USER', '女', '13800000002', 'demo2@example.com');
INSERT INTO `user` VALUES (3, 'demo_user_3', '$2a$10$YqWv.1Ba.yY2FGsvVO7AY.h9IC4xi4AAEeUscDNYdztdGyoe4KaSa', '演示用户丙', 'http://localhost:9090/files/download/avatar.png', 'USER', '男', '13800000003', NULL);

SET FOREIGN_KEY_CHECKS = 1;
