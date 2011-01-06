/*
MySQL Data Transfer
Source Host: localhost
Source Database: tints
Target Host: localhost
Target Database: tints
Date: 23.04.2010 01:43:49
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for tints_categories
-- ----------------------------
DROP TABLE IF EXISTS `tints_categories`;
CREATE TABLE `tints_categories` (
  `catid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cattitle` varchar(255) NOT NULL,
  `catdesc` text,
  PRIMARY KEY (`catid`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tints_comment
-- ----------------------------
DROP TABLE IF EXISTS `tints_comment`;
CREATE TABLE `tints_comment` (
  `commentid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ticketid` int(11) unsigned NOT NULL,
  `comment` text,
  `userid` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`commentid`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tints_os
-- ----------------------------
DROP TABLE IF EXISTS `tints_os`;
CREATE TABLE `tints_os` (
  `osid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `version` varchar(255) DEFAULT NULL,
  `desc` text,
  PRIMARY KEY (`osid`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tints_projects
-- ----------------------------
DROP TABLE IF EXISTS `tints_projects`;
CREATE TABLE `tints_projects` (
  `projectid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `projecttitle` varchar(255) NOT NULL,
  `projectdesc` text,
  PRIMARY KEY (`projectid`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tints_tickets
-- ----------------------------
DROP TABLE IF EXISTS `tints_tickets`;
CREATE TABLE `tints_tickets` (
  `ticketid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` int(11) unsigned NOT NULL,
  `title` varchar(255) NOT NULL,
  `ticketdesc` text,
  `projectid` int(11) unsigned NOT NULL,
  `catid` int(11) unsigned NOT NULL,
  `osid` int(11) unsigned DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  `percentage` int(11) DEFAULT '0',
  `created_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ticketid`),
  UNIQUE KEY `tid_index` (`ticketid`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for tints_users
-- ----------------------------
DROP TABLE IF EXISTS `tints_users`;
CREATE TABLE `tints_users` (
  `userid` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `passwd` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `timezone_utc` int(11) DEFAULT NULL,
  `language` int(11) unsigned DEFAULT '0',
  `imgurl` varchar(255) DEFAULT NULL,
  `userlevel` int(11) DEFAULT '0',
  `registerkey` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `uid_indx` (`userid`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `tints_categories` VALUES ('15', 'Internet', null);
INSERT INTO `tints_categories` VALUES ('13', 'Datenbank', null);
INSERT INTO `tints_categories` VALUES ('17', 'ERP', null);
INSERT INTO `tints_categories` VALUES ('18', 'Office', null);
INSERT INTO `tints_categories` VALUES ('14', 'Intranet', null);
INSERT INTO `tints_categories` VALUES ('19', 'Web-Projekte', null);
INSERT INTO `tints_categories` VALUES ('20', 'Bildbearbeitung', null);
INSERT INTO `tints_os` VALUES ('1', 'Windows', '7', 'Windows 7 ist auf Andres PC');
INSERT INTO `tints_os` VALUES ('2', 'Windows', 'XP', null);
INSERT INTO `tints_os` VALUES ('3', 'Windows', '2000', null);
INSERT INTO `tints_os` VALUES ('4', 'Windows', 'Vista', null);
INSERT INTO `tints_os` VALUES ('5', 'Windows', '98', null);
INSERT INTO `tints_os` VALUES ('6', 'Windows', '95', null);
INSERT INTO `tints_os` VALUES ('7', 'Mac OS', '10.6.3', null);
INSERT INTO `tints_os` VALUES ('8', 'Ubuntu', '9.10', null);
INSERT INTO `tints_os` VALUES ('9', 'Fedora', '12.0', null);
INSERT INTO `tints_os` VALUES ('10', 'Debian', '5.0', null);
INSERT INTO `tints_os` VALUES ('11', 'Red Hat Enterprise', '5', null);
INSERT INTO `tints_projects` VALUES ('1', 'Tints', 'Tints ist toll!');
INSERT INTO `tints_projects` VALUES ('2', '08/15', null);
INSERT INTO `tints_projects` VALUES ('3', 'Testprojekt', null);
INSERT INTO `tints_users` VALUES ('22', 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'admin', 'admin@localhost', '0', '0', null, '2', null);
INSERT INTO `tints_users` VALUES ('23', 'user', '12dea96fec20593566ab75692c9949596833adc9', 'user', 'user@localhost', '0', '0', null, '1', null);
