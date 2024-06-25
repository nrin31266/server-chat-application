CREATE DATABASE chat_application;
USE chat_application;

DROP TABLE IF EXISTS `files`;
DROP TABLE IF EXISTS `files_all`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `user_account`;
DROP TABLE IF EXISTS `historychat`;

CREATE TABLE `files_all` (
  `FileID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `FileName` varchar(255) DEFAULT NULL,
  `FileExtension` varchar(255) DEFAULT NULL,
  `Status` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `files` (
  `FileID` int(10) unsigned NOT NULL,
  `FileExtension` varchar(255) DEFAULT NULL,
  `BlurHash` varchar(255) DEFAULT NULL,
  `Status` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FileID`),
  CONSTRAINT `fk_files`
  FOREIGN KEY (`FileID`)
  REFERENCES `files_all` (`FileID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `UserID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UserName` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;



CREATE TABLE `user_account` (
  `UserID` int unsigned NOT NULL,
  `UserName` varchar(255) DEFAULT NULL,
  `Gender` varchar(100) DEFAULT '',
  `Image` longblob,
  `ImageString` varchar(255) DEFAULT '',
  `Status` char(1) NOT NULL DEFAULT '1',
  `Name` varchar(255) DEFAULT '',
  `PhoneNumber` varchar(45) DEFAULT '',
  `Date` date DEFAULT NULL,
  `Email` varchar(255) DEFAULT '',
  `CoverArt` longblob,
  `Address` varchar(255) DEFAULT '',
  PRIMARY KEY (`UserID`),
  CONSTRAINT `user_account_ibfk_1` 
  FOREIGN KEY (`UserID`) 
  REFERENCES `user` (`UserID`) 
  ON DELETE CASCADE 
  ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



CREATE TABLE `historychat` (
  `ChatID` int unsigned NOT NULL AUTO_INCREMENT,
  `SenderID` int unsigned NOT NULL,
  `ReceiverID` int unsigned NOT NULL,
  `Type` int NOT NULL,
  `Message` varchar(10000) DEFAULT NULL,
  `FilePathSender` varchar(255) DEFAULT NULL,
  `FilePathReceiver` varchar(255) DEFAULT NULL,
  `FileID` int DEFAULT '0',
  `FileName` varchar(255) DEFAULT '',
  `FileSize` varchar(255) DEFAULT '',
  `Timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ChatID`),
  KEY `fk_sender_user` (`SenderID`),
  KEY `fk_receiver_user` (`ReceiverID`),
  KEY `fk_fileid_idx` (`FileID`),
  CONSTRAINT `fk_receiver_user` 
  FOREIGN KEY (`ReceiverID`) 
  REFERENCES `user` (`UserID`) 
  ON DELETE CASCADE 
  ON UPDATE CASCADE,
  CONSTRAINT `fk_sender_user` 
  FOREIGN KEY (`SenderID`) 
  REFERENCES `user` (`UserID`) 
  ON DELETE CASCADE 
  ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1159 DEFAULT CHARSET=utf8mb3;

DELIMITER //

CREATE
DEFINER=`root`@`localhost`
TRIGGER `files_all_after_insert`
AFTER INSERT ON `files_all`
FOR EACH ROW
BEGIN
  INSERT INTO files (FileID, FileExtension, Status) 
  VALUES (NEW.FileID, NEW.FileExtension, '0');
END//

DELIMITER ;
