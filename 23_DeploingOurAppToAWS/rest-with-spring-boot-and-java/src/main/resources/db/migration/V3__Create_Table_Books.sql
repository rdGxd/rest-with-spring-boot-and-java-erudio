CREATE TABLE IF NOT EXISTS `books`
(
    `id`          INT AUTO_INCREMENT PRIMARY KEY,
    `author`      longtext,
    `launch_date` datetime(6)    NOT NULL,
    `price`       decimal(65, 2) NOT NULL,
    `title`       longtext
);
