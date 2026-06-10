CREATE SCHEMA `clinic_system_db` ;
CREATE TABLE `clinic_system_db`.`admins` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);
CREATE TABLE `clinic_system_db`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(11) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `birth_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idcustomers_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC) VISIBLE);
  CREATE TABLE `clinic_system_db`.`reservationscustomers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `reserved_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idreservations_UNIQUE` (`id` ASC) VISIBLE);
ALTER TABLE `clinic_system_db`.`admins` 
CHANGE COLUMN `password_hash` `password_hash` VARCHAR(255) NOT NULL ;
ALTER TABLE `clinic_system_db`.`customers` 
CHANGE COLUMN `phone_number` `phone_number` VARCHAR(20) NOT NULL ;
ALTER TABLE `clinic_system_db`.`reservations` 
CHANGE COLUMN `created_at` `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ;

INSERT INTO `clinic_system_db`.`customers` 
    (`name`, `phone_number`, `email`, `birth_date`) 
VALUES 
    ('高巻杏', '09011111111', 'an@gmail.com', '2000-11-12'),
    ('新島真', '09022222222', 'makoto@gmail.com', '2000-04-23'),
    ('佐倉双', '09033333333', 'futaba@gmail.com', '2000-02-19'),
    ('奥村春', '09044444444', 'haru@gmail.com', '2000-12-05'),
    ('芳澤かすみ', '09055555555', 'kasumi@gmail.com', '2000-03-25');
    
INSERT INTO `clinic_system_db`.`admins` (`name`, `password_hash`) VALUES ('佐藤花子', '$2a$08$NM0tCdsWR6SCDjffn8w79ulkCI7m7HhlgvX/NRLVbYVn7e19FujF6');

UPDATE `clinic_system_db`.`customers` SET `email` = 'an@example.com' WHERE (`id` = '1');
UPDATE `clinic_system_db`.`customers` SET `email` = 'makoto@example.com' WHERE (`id` = '2');
UPDATE `clinic_system_db`.`customers` SET `email` = 'futaba@example.com' WHERE (`id` = '3');
UPDATE `clinic_system_db`.`customers` SET `email` = 'haru@example.com' WHERE (`id` = '4');
UPDATE `clinic_system_db`.`customers` SET `email` = 'kasumi@example.com' WHERE (`id` = '5');
