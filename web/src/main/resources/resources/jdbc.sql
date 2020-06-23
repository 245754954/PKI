/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.106
Source Server Version : 50730
Source Host           : 192.168.1.106:3306
Source Database       : jdbc

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-05-29 22:27:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cert
-- ----------------------------
DROP TABLE IF EXISTS `cert`;
CREATE TABLE `cert` (
  `serial_number` varchar(255) NOT NULL,
  `algorithm` varchar(255) DEFAULT NULL,
  `ca_type` int(11) DEFAULT NULL,
  `cert_dir` varchar(255) DEFAULT NULL,
  `issuerdn` varchar(255) DEFAULT NULL,
  `keysize` int(11) DEFAULT NULL,
  `not_after` datetime DEFAULT NULL,
  `not_before` datetime DEFAULT NULL,
  `p12_path` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `pri_pem_path` varchar(255) DEFAULT NULL,
  `pub_pem_path` varchar(255) DEFAULT NULL,
  `sig_alg` varchar(255) DEFAULT NULL,
  `signature_algorithm` varchar(255) DEFAULT NULL,
  `signature_value` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `user_alias` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `encryptpassword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cert
-- ----------------------------
INSERT INTO `cert` VALUES ('1590567614525', 'RSA', '1', 'E:\\client\\cert\\rsa\\client/1590567614525', 'CN=RSA National University of Defence Technology,OU=Department of Computer Science,O=NUDT,L=HuNan,ST=ChangSha', '1024', '2027-05-30 00:00:00', '2020-05-27 00:00:00', 'E:\\client\\cert\\rsa\\client/1590567614525/zfx.p12', '123456', 'E:\\client\\cert\\rsa\\client/1590567614525/zfx_pri.pem', 'E:\\client\\cert\\rsa\\client/1590567614525/zfx_pub.pem', 'SHA256WITHRSA', 'SHA256WITHRSA', null, 'validity', 'zfx', 'zfx', '3', '');

-- ----------------------------
-- Table structure for crl
-- ----------------------------
DROP TABLE IF EXISTS `crl`;
CREATE TABLE `crl` (
  `serial_number` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `is_validate` bit(1) DEFAULT NULL,
  `pri_key` varchar(5000) DEFAULT NULL,
  `pub_key` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crl
-- ----------------------------
INSERT INTO `crl` VALUES ('1590555143500', '2020-05-27 14:19:13', '\0', 'RSA Private CRT Key [b7:47:52:36:b5:f9:6a:3a:50:11:f9:bf:c6:61:bc:cc:99:d1:72:e0]\r\n            modulus: ab57ac4ff2d7798858387adc8c7eca1681e57f0f56fc5ac49bc951c1d331b1f6d82fcc31637dd6829caec14f2498042648658b121871b592ef7eefed14a5109909218e5847d24ff8f5f062d5738f9899df311f0c44736689be8dc8d027fb9af7f773dcb2f20ba02d89947d45912722f574db822a8093f8fb30134a9a4e5b9973763571f4c314b0e82ceee035c995db3abd669d3994f649e9e51886840438804c439a2e7922a1d5561129e057c44cfea9a0f6052f7c5c64b1bbd62aa396671a527a3b7130e98600fe5e9faeb81722988917513da60b31b1ae9f214ca65b2a3bd3538881694f3cc4710d89419c72d82f294eb85b48413b31cb6464274c27087b5d\r\n    public exponent: 10001\r\n', 'RSA Public Key [b7:47:52:36:b5:f9:6a:3a:50:11:f9:bf:c6:61:bc:cc:99:d1:72:e0]\r\n            modulus: ab57ac4ff2d7798858387adc8c7eca1681e57f0f56fc5ac49bc951c1d331b1f6d82fcc31637dd6829caec14f2498042648658b121871b592ef7eefed14a5109909218e5847d24ff8f5f062d5738f9899df311f0c44736689be8dc8d027fb9af7f773dcb2f20ba02d89947d45912722f574db822a8093f8fb30134a9a4e5b9973763571f4c314b0e82ceee035c995db3abd669d3994f649e9e51886840438804c439a2e7922a1d5561129e057c44cfea9a0f6052f7c5c64b1bbd62aa396671a527a3b7130e98600fe5e9faeb81722988917513da60b31b1ae9f214ca65b2a3bd3538881694f3cc4710d89419c72d82f294eb85b48413b31cb6464274c27087b5d\r\n    public exponent: 10001\r\n');
INSERT INTO `crl` VALUES ('1590564386914', '2020-05-27 15:54:43', '\0', 'RSA Private CRT Key [a1:16:54:a1:80:ab:6c:90:b9:31:26:22:2e:10:22:16:f0:53:4c:e2]\r\n            modulus: bd982343cbb624a97ab84218c05652e1099ed981eb09e746775257c3f657701cdf502a9be9613060c3f15adf713396878b871db1d1069dd9b9fa0efaa4bbbf4a99271c2ace35d098167f3f41285a48cd52781622d926ce1168fbb26c28a642336c447656c4eefa72bfa57a30a6bc6c54ad5d518234fd284a61f15a99594305ea545e00996aff58decb1b33ef25c663fbeb0f42e56679e693393ea1fe5d95c8455d802349d9bedce2086cdfe89d2cd1fb2d60ad248b7985e122b3eb31e0da8b36358df8d9b91716e530e4f28e9bc5ebfb0f91cd64fe4a48c15daa7433271a59bc6959dc7b7dc3e89ddf692202d7063edebf279b84e4e840db1131eed98f8b6ad7\r\n    public exponent: 10001\r\n', 'RSA Public Key [a1:16:54:a1:80:ab:6c:90:b9:31:26:22:2e:10:22:16:f0:53:4c:e2]\r\n            modulus: bd982343cbb624a97ab84218c05652e1099ed981eb09e746775257c3f657701cdf502a9be9613060c3f15adf713396878b871db1d1069dd9b9fa0efaa4bbbf4a99271c2ace35d098167f3f41285a48cd52781622d926ce1168fbb26c28a642336c447656c4eefa72bfa57a30a6bc6c54ad5d518234fd284a61f15a99594305ea545e00996aff58decb1b33ef25c663fbeb0f42e56679e693393ea1fe5d95c8455d802349d9bedce2086cdfe89d2cd1fb2d60ad248b7985e122b3eb31e0da8b36358df8d9b91716e530e4f28e9bc5ebfb0f91cd64fe4a48c15daa7433271a59bc6959dc7b7dc3e89ddf692202d7063edebf279b84e4e840db1131eed98f8b6ad7\r\n    public exponent: 10001\r\n');
INSERT INTO `crl` VALUES ('1590566717707', '2020-05-27 16:05:18', '\0', 'RSA Private CRT Key [96:93:5d:95:b3:4a:38:f5:c8:e2:8c:7e:4a:ec:30:13:3c:bc:94:49]\r\n            modulus: ada16447587195d975a038e645bc8ac7f6b4995440b192d8939f6d52a70d3673a73864119fdc22f44f83e820ddae118786a8c2cccaa9e83c42fee670106c55089bfe123a826d0c91faf5cb5781cbad215e29eed18f38dc854cb7f8c10c23211f1d7aec12982335ba5040388d44e6a870fe29bc14ede6254fd95ab6c5b8ba4945\r\n    public exponent: 10001\r\n', 'RSA Public Key [96:93:5d:95:b3:4a:38:f5:c8:e2:8c:7e:4a:ec:30:13:3c:bc:94:49]\r\n            modulus: ada16447587195d975a038e645bc8ac7f6b4995440b192d8939f6d52a70d3673a73864119fdc22f44f83e820ddae118786a8c2cccaa9e83c42fee670106c55089bfe123a826d0c91faf5cb5781cbad215e29eed18f38dc854cb7f8c10c23211f1d7aec12982335ba5040388d44e6a870fe29bc14ede6254fd95ab6c5b8ba4945\r\n    public exponent: 10001\r\n');
INSERT INTO `crl` VALUES ('1590567168111', '2020-05-27 16:12:48', '\0', 'RSA Private CRT Key [47:98:4f:4c:35:c6:ee:16:d6:bd:6f:fe:14:8b:29:8b:b9:91:81:e0]\r\n            modulus: ca074cf552f0d798ac5b705336281eb1d83f6a3b9dbe2e427c39aaf157871a2f791ecb205e0db7409bed659a94652ee11571e53dc67299e6239ad340ca75ffb69c67ac82985449685f534a422635e18b991fbb2d37d619a18586cea5226a0fc25cc73dd74324bd836566d1be86973df676c7b3966834dad0fabaab684cd8b39f\r\n    public exponent: 10001\r\n', 'RSA Public Key [47:98:4f:4c:35:c6:ee:16:d6:bd:6f:fe:14:8b:29:8b:b9:91:81:e0]\r\n            modulus: ca074cf552f0d798ac5b705336281eb1d83f6a3b9dbe2e427c39aaf157871a2f791ecb205e0db7409bed659a94652ee11571e53dc67299e6239ad340ca75ffb69c67ac82985449685f534a422635e18b991fbb2d37d619a18586cea5226a0fc25cc73dd74324bd836566d1be86973df676c7b3966834dad0fabaab684cd8b39f\r\n    public exponent: 10001\r\n');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `checked` bit(1) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `open` bit(1) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '', 'glyphicon glyphicon-user', '权限菜单', '', '0', null);
INSERT INTO `permission` VALUES ('4', '', 'glyphicon glyphicon-user', '国防大学业务系统1', '', '0', null);
INSERT INTO `permission` VALUES ('5', '', 'glyphicon glyphicon-user', '国防大学业务系统2', '', '0', '/usr');
INSERT INTO `permission` VALUES ('15', '', 'glyphicon glyphicon-user', '访问内部网络', '', '5', '/per/permission');
INSERT INTO `permission` VALUES ('17', '', 'glyphicon glyphicon-user', '访问外部网络', '', '5', '/per/permission');
INSERT INTO `permission` VALUES ('21', '', 'glyphicon glyphicon-user', '访问业务服务器1', '', '5', '访问业务服务器1');
INSERT INTO `permission` VALUES ('26', '', 'glyphicon glyphicon-user', '内部网络', '', '4', '/net/inner');
INSERT INTO `permission` VALUES ('27', '', 'glyphicon glyphicon-user', '外部网络', '', '4', '/net/outer');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '少校', '少校');
INSERT INTO `role` VALUES ('2', '中校', '中校');
INSERT INTO `role` VALUES ('3', '服务管理员隶属于业务服务器', '服务管理员');
INSERT INTO `role` VALUES ('4', '管理员管理服务器', '管理员');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('31', '4', '4');
INSERT INTO `role_permission` VALUES ('32', '27', '4');

-- ----------------------------
-- Table structure for service
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `service_description` varchar(255) DEFAULT NULL,
  `serviceip` varchar(255) DEFAULT NULL,
  `service_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of service
-- ----------------------------
INSERT INTO `service` VALUES ('2', '123456', '隶属于国防大学融合交换网关下的业务服务系统1', '127.0.0.1', '国防大学业务系统1');
INSERT INTO `service` VALUES ('3', '123456', '隶属于国防大学融合交换网关下的业务服务系统2', '127.0.0.1', '国防大学业务系统2');

-- ----------------------------
-- Table structure for service_permission
-- ----------------------------
DROP TABLE IF EXISTS `service_permission`;
CREATE TABLE `service_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_id` int(11) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of service_permission
-- ----------------------------
INSERT INTO `service_permission` VALUES ('2', '4', '2');
INSERT INTO `service_permission` VALUES ('3', '5', '3');
INSERT INTO `service_permission` VALUES ('4', '15', '3');
INSERT INTO `service_permission` VALUES ('5', '16', '4');
INSERT INTO `service_permission` VALUES ('6', '17', '3');
INSERT INTO `service_permission` VALUES ('8', '20', '5');
INSERT INTO `service_permission` VALUES ('9', '21', '3');
INSERT INTO `service_permission` VALUES ('12', '26', '2');
INSERT INTO `service_permission` VALUES ('13', '27', '2');

-- ----------------------------
-- Table structure for service_role
-- ----------------------------
DROP TABLE IF EXISTS `service_role`;
CREATE TABLE `service_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) DEFAULT NULL,
  `sid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of service_role
-- ----------------------------
INSERT INTO `service_role` VALUES ('1', '1', '3');
INSERT INTO `service_role` VALUES ('2', '2', '3');
INSERT INTO `service_role` VALUES ('3', '3', '2');
INSERT INTO `service_role` VALUES ('4', '4', '2');

-- ----------------------------
-- Table structure for service_staff
-- ----------------------------
DROP TABLE IF EXISTS `service_staff`;
CREATE TABLE `service_staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) DEFAULT NULL,
  `staff_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of service_staff
-- ----------------------------
INSERT INTO `service_staff` VALUES ('1', '2', '1');
INSERT INTO `service_staff` VALUES ('2', '2', '2');
INSERT INTO `service_staff` VALUES ('4', '3', '4');
INSERT INTO `service_staff` VALUES ('5', '2', '5');
INSERT INTO `service_staff` VALUES ('6', '3', '6');
INSERT INTO `service_staff` VALUES ('7', '2', '7');
INSERT INTO `service_staff` VALUES ('8', '2', '8');
INSERT INTO `service_staff` VALUES ('9', '2', '9');

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `code` int(11) DEFAULT NULL,
  `countrycode` varchar(255) DEFAULT NULL,
  `county` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES ('1', 'HUNASHENG, CHANGSHASHI', '成都市', '1', 'CN', '青羊区', '计算科学系', '隶属于国防科技大学终端用户', '245754954@qq.com', 'NUDT', '123456', '四川省', null, '18392025041', 'nl');
INSERT INTO `staff` VALUES ('2', '湖南省长沙市开福区德雅路', '成都市', '1', 'CN', '锦江区', '计算科学系', '隶属于国防科技大学终端用户', '245754954@qq.com', 'NUDT', '123456', '四川省', null, '18392025041', 'zfx');
INSERT INTO `staff` VALUES ('4', '湖南省长沙市', '成都市', '0', 'CN', '青羊区', '网络安全系', null, '245754954@qq.com', 'NUD', '123456', '四川省', null, '18392025041', '云天明');
INSERT INTO `staff` VALUES ('5', '两河镇两河村', '大连市', '1', 'US', '西岗区', '网络安全系', '隶属于科大', '245754954@qq.com', 'NUDT', '123456', '辽宁省', null, '18392025041', 'wch');
INSERT INTO `staff` VALUES ('6', '两河村', '成都市', '0', 'CN', null, '软件工程系', '隶属于国防大学业务系统2', '245754954@qq.com', 'NUD', '123456', '四川省', null, '18293945608', '程欣');
INSERT INTO `staff` VALUES ('7', '连云港区', '齐齐哈尔市', '0', 'CN', '铁锋区', '计算科学系', null, '245754954@qq.com', 'NUDT', '123456', '黑龙江省', null, '18392025041', '章北海');
INSERT INTO `staff` VALUES ('8', '湖南省', '成都市', '0', 'CN', '青羊区', '计算科学系', null, '245754954@qq.com', 'NUDT', '123456', '四川省', null, '18392025041', '逻辑');

-- ----------------------------
-- Table structure for staff_role
-- ----------------------------
DROP TABLE IF EXISTS `staff_role`;
CREATE TABLE `staff_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) DEFAULT NULL,
  `sid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staff_role
-- ----------------------------
INSERT INTO `staff_role` VALUES ('4', '1', '4');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `ismenu` int(11) DEFAULT NULL,
  `isopen` int(11) DEFAULT NULL,
  `levels` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `pcode` varchar(255) DEFAULT NULL,
  `pcodes` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
