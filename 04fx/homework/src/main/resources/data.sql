CREATE DATABASE IF NOT EXISTS `test`;

USER `test`;

CREATE TABLE IF NOT EXISTS  `users` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `password` varchar(16) NOT NULL,
  `phone_number` int(11) NOT NULL,
  `money` int(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;