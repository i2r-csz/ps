-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 30, 2012 at 04:20 PM
-- Server version: 5.1.44
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `java9`
--

-- --------------------------------------------------------

--
-- Table structure for table `avatar`
--

CREATE TABLE IF NOT EXISTS `avatar` (
  `aid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `albumid` int(11) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `image_h` int(11) NOT NULL,
  `image_w` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `modified_on` datetime NOT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `avatar`
--

INSERT INTO `avatar` (`aid`, `uid`, `albumid`, `filename`, `image_h`, `image_w`, `created_on`, `modified_on`) VALUES
(1, 3, 0, 'male.png', 300, 300, '0000-00-00 00:00:00', '0000-00-00 00:00:00'),
(2, 3, 0, 'female.png', 301, 301, '0000-00-00 00:00:00', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `contact`
--

CREATE TABLE IF NOT EXISTS `contact` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `contact` varchar(255) NOT NULL,
  `content` varchar(1024) NOT NULL,
  `modified_on` datetime NOT NULL,
  `created_on` datetime NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `contact`
--


-- --------------------------------------------------------

--
-- Table structure for table `lookup`
--

CREATE TABLE IF NOT EXISTS `lookup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `code` int(11) NOT NULL,
  `type` varchar(128) NOT NULL,
  `position` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `lookup`
--

INSERT INTO `lookup` (`id`, `name`, `code`, `type`, `position`) VALUES
(1, '男', 1, 'user-sex', 1),
(2, '女', 2, 'user-sex', 2),
(3, 'member', 1, 'user-role', 1),
(4, 'staff', 2, 'user-role', 2),
(5, 'admin', 3, 'user-role', 3);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `body` text,
  `is_read` enum('0','1') NOT NULL DEFAULT '0',
  `deleted_by` enum('sender','receiver') DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sender` (`sender_id`),
  KEY `reciever` (`receiver_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`id`, `sender_id`, `receiver_id`, `body`, `is_read`, `deleted_by`, `created_at`) VALUES
(1, 3, 4, '你好', '1', NULL, '2012-06-30 15:23:41'),
(2, 3, 4, '你好', '1', NULL, '2012-06-30 15:24:02'),
(3, 3, 4, '3232', '1', NULL, '2012-06-30 22:49:48'),
(4, 3, 4, 'dsfsd', '1', NULL, '2012-06-30 22:52:22'),
(5, 3, 4, 'dfsd', '1', NULL, '2012-06-30 22:52:43'),
(6, 3, 4, '32432', '1', NULL, '2012-06-30 22:52:58'),
(7, 3, 4, '12', '1', NULL, '2012-06-30 22:53:05'),
(8, 3, 4, 'sdfsd', '0', NULL, '2012-06-30 23:58:18');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `email` char(20) NOT NULL,
  `groupid` int(11) NOT NULL DEFAULT '0',
  `password` char(64) NOT NULL,
  `name` char(20) DEFAULT NULL,
  `nickname` char(15) NOT NULL,
  `birthday` date DEFAULT NULL,
  `sex` enum('1','2') DEFAULT NULL COMMENT '1:male 2:female',
  `role` enum('1','2','3') NOT NULL COMMENT '1) Member 2) Staff 3) Admin',
  `hp_number` varchar(32) DEFAULT NULL,
  `avatarid` int(11) NOT NULL COMMENT 'reference to a picture',
  `rrid` int(11) DEFAULT NULL COMMENT 'renren id',
  `sinaid` int(11) DEFAULT NULL COMMENT 'sina open id',
  `token` char(255) NOT NULL COMMENT 'token for retrieve password',
  `token_time` int(11) NOT NULL,
  `created_on` datetime NOT NULL,
  `modified_on` datetime NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uid`, `email`, `groupid`, `password`, `name`, `nickname`, `birthday`, `sex`, `role`, `hp_number`, `avatarid`, `rrid`, `sinaid`, `token`, `token_time`, `created_on`, `modified_on`) VALUES
(3, 'e-vv@163.com', 0, 'ccbbb6a22fed7d0965f59e33aa7947ee', 'Eric', 'csz', '1994-06-02', '2', '3', '93275018', 1, NULL, NULL, '0e097b57be1c7ed957997633c1b5102e', 1340470779, '0000-00-00 00:00:00', '2012-06-30 10:20:53'),
(4, '57237925@qq.com', 0, 'ccbbb6a22fed7d0965f59e33aa7947ee', NULL, 'Eric', '1994-06-02', '1', '1', '93275018', 2, NULL, NULL, '', 0, '0000-00-00 00:00:00', '0000-00-00 00:00:00');
