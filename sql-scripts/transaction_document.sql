USE `document-management`;
DROP TABLE IF EXISTS `transaction_document`;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `transaction_document` (
   `id`int NOT NULL AUTO_INCREMENT,
  `date`varchar(50)  DEFAULT NULL,
  `user`varchar(50)  DEFAULT NULL,
  `document_id`varchar(50)  DEFAULT NULL,
  `transaction_type`varchar(128)  DEFAULT NULL,

   PRIMARY KEY (`id`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
