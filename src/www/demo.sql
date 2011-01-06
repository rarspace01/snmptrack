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
INSERT INTO `tints_comment` VALUES ('16', '15', 'WEP', '14');
INSERT INTO `tints_comment` VALUES ('17', '15', 'Stellen Sie auf WPA 2, welches in Ihrem Netzwerk einegsetzt wird und versuchen Sie es nochmal.', '14');
INSERT INTO `tints_comment` VALUES ('15', '15', 'Welche VerschlÃ¼sselungsart haben Sie gewÃ¤hlt?', '14');
INSERT INTO `tints_comment` VALUES ('18', '15', 'Vielen Dank!\r\nHat funktioniert', '14');
INSERT INTO `tints_comment` VALUES ('19', '16', 'Senden Sie mir die Datei bitte mal an unsere E-Mail Adresse. Ich werde mir das Dokument anschauen und versuchen den Fehler nachzuvollziehen.', '14');
INSERT INTO `tints_comment` VALUES ('20', '17', 'Da kÃ¶nnen wir ihnen leider nicht SoftwaremÃ¤ÃŸig helfen.\r\nEntweder Sie schaffen sich leistungsfÃ¤higere Hardware an oder sie erhÃ¶hen Ihren RAM.', '14');
INSERT INTO `tints_comment` VALUES ('21', '17', 'Ok dann bleibt mir nichts anderes Ã¼brig.\r\nVielen Dank!', '14');
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
INSERT INTO `tints_tickets` VALUES ('15', '14', 'Keine Verbindung mÃ¶glich', 'Die Verbindung mit dem WLAN schlÃ¤gt immer fehl.\r\nMeldung: Key ist falsch.', '3', '15', '1', '3', '0', '2010-04-22 23:09:44');
INSERT INTO `tints_tickets` VALUES ('16', '14', 'Word 2003 stÃ¼rzt ab', 'Beim Ã–ffnen eines groÃŸen Dokumentes stÃ¼rzt Word immer ab und lÃ¤sst sich nicht bedienen!', '1', '18', '2', '2', '0', '2010-04-22 23:09:10');
INSERT INTO `tints_tickets` VALUES ('17', '14', 'Photoshop zu viel Speicher!', 'Photoshop verbraucht soviel Speicher, dass ein normales arbeiten an meinem Mac nicht mÃ¶glich ist!', '1', '20', '7', '4', '0', '2010-04-22 23:11:58');
INSERT INTO `tints_users` VALUES ('1', 'denis', 'ccee6ff53cc2d05417aeec370743a95323bef3b4', 'Denis', 'testrarspace01@arcor.de', '1', '0', 'http://www.google.de/intl/de_de/images/logo.gif', '2', null);
INSERT INTO `tints_users` VALUES ('18', 'mailtest', 'mailtest', null, 'rarspace01@arcor.de', null, '0', null, '1', '58337a58');
INSERT INTO `tints_users` VALUES ('14', 'patrick', '680990854eb710e7b862cb1311578dcb090aa92f ', '', 'start@123de', '5', '0', '', '2', 'e9c2f44f');
INSERT INTO `tints_users` VALUES ('15', 'test', '9f175575283636ad76f0bf3ca100a602fefa1857', 'Deaktivierter User', 'test@test.de', '0', '0', '', '-1', 'de6ab05a');
INSERT INTO `tints_users` VALUES ('19', 'rarspace07', 'ccee6ff53cc2d05417aeec370743a95323bef3b4', '', 'rarspace07@googlemail.com', '0', '0', 'http://www.google.de/intl/de_de/images/logo.gif', '1', '1c513558');
INSERT INTO `tints_users` VALUES ('20', 'andre', '680990854eb710e7b862cb1311578dcb090aa92f', 'andre', 'andre.lannert@googlemail.com', '-4', '0', 'http://freuli.net/blog/wp-content/uploads/2010/02/bla1.jpg', '2', 'a496b268');
INSERT INTO `tints_users` VALUES ('21', 'testen', '680990854eb710e7b862cb1311578dcb090aa92f', null, 'spaminator@spambog.com', null, '0', null, '0', 'd738d4c6');
INSERT INTO `tints_users` VALUES ('22', 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'admin', 'admin@localhost', '0', '0', null, '2', null);
INSERT INTO `tints_users` VALUES ('23', 'user', '12dea96fec20593566ab75692c9949596833adc9', 'user', 'user@localhost', '0', '0', null, '1', null);
