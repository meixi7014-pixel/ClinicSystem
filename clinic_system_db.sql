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
  UNIQUE INDEX `idcustomers_UcustomersNIQUE` (`id` ASC) VISIBLE,
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
    ('佐倉双', '09033333333', 'futaba@gcustomersmail.com', '2000-02-19'),
    ('奥村春', '09044444444', 'haru@gmail.com', '2000-12-05'),
    ('芳澤かすみ', '09055555555', 'kasumi@gmail.com', '2000-03-25');
    
INSERT INTO `clinic_system_db`.`admins` (`name`, `password_hash`) VALUES ('佐藤花子', '$2a$08$NM0tCdsWR6SCDjffn8w79ulkCI7m7HhlgvX/NRLVbYVn7e19FujF6');

UPDATE `clinic_system_db`.`customers` SET `email` = 'an@example.com' WHERE (`id` = '1');
UPDATE `clinic_system_db`.`customers` SET `email` = 'makoto@example.com' WHERE (`id` = '2');
UPDATE `clinic_system_db`.`customers` SET `email` = 'futaba@example.com' WHERE (`id` = '3');
UPDATE `clinic_system_db`.`customers` SET `email` = 'haru@example.com' WHERE (`id` = '4');
UPDATE `clinic_system_db`.`customers` SET `email` = 'kasumi@example.com' WHERE (`id` = '5');

INSERT INTO `clinic_system_db`.`customers` 
    (`id`, `name`, `phone_number`, `email`, `birth_date`) 
VALUES 
    (1, '佐藤美優', '00060336710', 'miyu.001@example.com', '2001-04-12'),
    (2, '鈴木結衣', '00071895577', 'yui.002@example.com', '1995-11-08'),
    (3, '高橋陽菜', '00089648005', 'hina.003@example.com', '2005-07-23'),
    (4, '田中葵', '00074991268', 'aoi.004@example.com', '1988-02-14'),
    (5, '伊藤咲良', '00038328787', 'sakura.005@example.com', '2008-10-30'),
    (6, '渡辺凛', '00012606180', 'rin.006@example.com', '1992-05-19'),
    (7, '山本愛菜', '00068498047', 'aina.007@example.com', '2003-12-05'),
    (8, '中村杏', '00026170927', 'an.008@example.com', '1985-08-27'),
    (9, '小林紬', '00006497960', 'tsumugi.009@example.com', '2007-01-15'),
    (10, '加藤莉央', '00098069464', 'rio.010@example.com', '1999-06-02'),
    (11, '吉田芽依', '00004270977', 'mei.011@example.com', '2004-03-22'),
    (12, '山田楓', '00093697494', 'kaede.012@example.com', '1990-09-11'),
    (13, '佐々木美月', '00053069842', 'mizuki.013@example.com', '1983-04-07'),
    (14, '山口詩織', '00095995405', 'shiori.014@example.com', '1997-12-25'),
    (15, '松本彩華', '00063208863', 'ayaka.015@example.com', '2006-05-14'),
    (16, '井上千尋', '00076535374', 'chihiro.016@example.com', '1981-11-03'),
    (17, '木村莉子', '00090573326', 'riko.017@example.com', '2002-08-18'),
    (18, '林心春', '00012516445', 'koharu.018@example.com', '2009-02-27'),
    (19, '斎藤明日香', '00065329476', 'asuka.019@example.com', '1994-07-09'),
    (20, '清水舞', '00074055584', 'mai.020@example.com', '1987-01-21'),
    (21, '山崎琴音', '00093684761', 'kotone.021@example.com', '2005-11-12'),
    (22, '森彩乃', '00006649606', 'ayano.022@example.com', '1991-03-30'),
    (23, '池田遥', '00010833866', 'haruka.023@example.com', '1984-10-14'),
    (24, '橋本優奈', '00040646147', 'yuna.024@example.com', '2000-05-06'),
    (25, '阿部実咲', '00001771474', 'misaki.025@example.com', '1996-12-18'),
    (26, '石川日和', '00001041869', 'hiyori.026@example.com', '2007-08-04'),
    (27, '山下乃愛', '00027149531', 'noa.027@example.com', '2004-04-29'),
    (28, '中島穂乃香', '00094613167', 'honoka.028@example.com', '1998-02-11'),
    (29, '小川美咲', '00087185329', 'misaki.029@example.com', '1989-07-25'),
    (30, '前田朱里', '00011714609', 'akari.030@example.com', '2002-11-21');

INSERT INTO `clinic_system_db`.`reservations` 
    (`id`, `customer_id`, `status`, `reserved_at`) 
VALUES 
    (1, 1, 1, '2026-06-15 10:00:00'),
    (2, 2, 1, '2026-06-15 11:00:00'),
    (3, 3, 1, '2026-06-15 14:00:00'),
    (4, 4, 1, '2026-06-16 11:00:00'),
    (5, 5, 1, '2026-06-16 14:00:00'),
    (6, 6, 1, '2026-06-16 16:00:00'),
    (7, 7, 1, '2026-06-17 12:00:00'),
    (8, 8, 1, '2026-06-17 13:00:00'),
    (9, 9, 1, '2026-06-17 15:00:00'),
    (10, 10, 1, '2026-06-18 12:00:00'),
    (11, 11, 1, '2026-06-18 15:00:00'),
    (12, 12, 1, '2026-06-18 17:00:00'),
    (13, 13, 1, '2026-06-19 11:00:00'),
    (14, 14, 1, '2026-06-19 13:00:00'),
    (15, 15, 1, '2026-06-19 18:00:00'),
    (16, 16, 1, '2026-06-20 10:00:00'),
    (17, 17, 1, '2026-06-20 14:00:00'),
    (18, 18, 1, '2026-06-20 16:00:00'),
    (19, 19, 1, '2026-06-22 10:00:00'),
    (20, 20, 1, '2026-06-22 13:00:00'),
    (21, 21, 1, '2026-06-22 16:00:00'),
    (22, 22, 1, '2026-06-23 12:00:00'),
    (23, 23, 1, '2026-06-23 15:00:00'),
    (24, 24, 1, '2026-06-23 17:00:00'),
    (25, 25, 1, '2026-06-24 10:00:00'),
    (26, 26, 1, '2026-06-24 11:00:00'),
    (27, 27, 1, '2026-06-24 17:00:00'),
    (28, 28, 1, '2026-06-25 11:00:00'),
    (29, 29, 1, '2026-06-25 14:00:00'),
    (30, 30, 1, '2026-06-25 15:00:00');