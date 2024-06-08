USE `document-management`;
DROP TABLE IF EXISTS `transaction_user`;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `transaction_user` (
   `id`int NOT NULL AUTO_INCREMENT,
  `date`varchar(50)  DEFAULT NULL,
  `user`varchar(50)  DEFAULT NULL,
  `user_id`varchar(50)  DEFAULT NULL,
  `transaction_type`varchar(128)  DEFAULT NULL,

   PRIMARY KEY (`id`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
