
CREATE TABLE `files` (
  `FileID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FileExtension` varchar(255) DEFAULT NULL,
  `BlurHash` varchar(255) DEFAULT NULL,
  `Status` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UserID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UserName` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;


INSERT INTO `user` VALUES ('36', 'dara', '123');
INSERT INTO `user` VALUES ('37', 'raven', '123');
INSERT INTO `user` VALUES ('38', 'china', '123');


DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `UserID` int(10) unsigned NOT NULL,
  `UserName` varchar(255) DEFAULT NULL,
  `Gender` char(1) NOT NULL DEFAULT '',
  `Image` longblob,
  `ImageString` varchar(255) DEFAULT '',
  `Status` char(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`UserID`),
  CONSTRAINT `user_account_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_account
-- ----------------------------
INSERT INTO `user_account` VALUES ('36', 'dara', '', null, '', '1');
INSERT INTO `user_account` VALUES ('37', 'raven', '', null, '', '1');
INSERT INTO `user_account` VALUES ('38', 'china', '', null, '', '1');

-- Tạo bảng files_all
CREATE TABLE `files_all` (
  `FileID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FileName` varchar(255) DEFAULT NULL,
  `FileExtension` varchar(255) DEFAULT NULL,
  `Status` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Thêm khóa ngoại vào bảng files_all
ALTER TABLE `files`
ADD CONSTRAINT `fk_files`
FOREIGN KEY (`FileID`)
REFERENCES `files_all` (`FileID`)
ON DELETE CASCADE
ON UPDATE CASCADE;
CREATE TABLE `HistoryChat` (
  `ChatID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `SenderID` int(10) unsigned NOT NULL,
  `ReceiverID` int(10) unsigned NOT NULL,
  `Type` int(1) NOT NULL,
  `Message` varchar(1000) DEFAULT NULL,
  `FilePath` varchar(255) DEFAULT NULL,  -- Đường dẫn tệp
  `Timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ChatID`),
  CONSTRAINT `fk_sender_user` FOREIGN KEY (`SenderID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_receiver_user` FOREIGN KEY (`ReceiverID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;