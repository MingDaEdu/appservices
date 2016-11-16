CREATE DATABASE mdeducourse DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE USER mdedu@'localhost' IDENTIFIED BY 'mdedu';

GRANT ALL PRIVILEGES ON mdeducourse.* TO mdedu;