-- 1. データベース
CREATE SCHEMA IF NOT EXISTS `clinic_system_db` DEFAULT CHARACTER SET utf8mb4 ;
USE `clinic_system_db` ;

-- 2. 管理者テーブル
CREATE TABLE IF NOT EXISTS `clinic_system_db`.`admins` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;

-- 3. 顧客テーブル
CREATE TABLE IF NOT EXISTS `clinic_system_db`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(20) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `birth_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idcustomers_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC) VISIBLE)
ENGINE = InnoDB;

-- 4. 予約テーブル
CREATE TABLE IF NOT EXISTS `clinic_system_db`.`reservations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `reserved_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idreservations_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;

ALTER TABLE `clinic_system_db`.`reservations` 
-- 予約の状態（1:仮確定, 2:本予約, 3:キャンセル）を管理するカラムを追加（デフォルトは仮確定）
ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1:仮確定, 2:本予約, 3:キャンセル' AFTER `customer_id`,
-- 仮確定の有効期限（15分後）を格納するカラムを追加（本予約時はNULLを許容）
ADD COLUMN `temporary_expires_at` DATETIME NULL AFTER `reserved_at`,
-- キャンセルが実行された日時を記録するカラムを追加
ADD COLUMN `canceled_at` DATETIME NULL AFTER `temporary_expires_at`;

-- 予約枠の重複を防ぐため、同じ日時の同じステータス（本予約）の管理を容易にするインデックスを追加
ALTER TABLE `clinic_system_db`.`reservations` 
ADD INDEX `idx_reserved_at_status` (`reserved_at` ASC, `status` ASC) VISIBLE;

# 埋まっている枠をカウントするSQL
SELECT 
    `reserved_at`, 
    COUNT(*) AS `reserved_count`
FROM 
    `clinic_system_db`.`reservations`
WHERE 
    -- 本予約、または有効期限内の仮確定を対象とする
    (`status` = 2) 
    OR (`status` = 1 AND `temporary_expires_at` > NOW())
    -- 例として2026年5月25日の1日分を抽出
    AND `reserved_at` >= '2026-05-25 00:00:00' 
    AND `reserved_at` <= '2026-05-25 23:59:00'
GROUP BY 
    `reserved_at`;
    
# 仮予約SQL
-- 1. 先に顧客情報をインサート
INSERT INTO `clinic_system_db`.`customers` (`name`, `phone_number`, `email`, `birth_date`)
VALUES ('テスト花子', '09099999999', 'test@example.com', '2000-04-01');

-- 2. 発行された顧客IDを使って、15分間の仮確定レコード
INSERT INTO `clinic_system_db`.`reservations` (`customer_id`, `status`, `reserved_at`, `temporary_expires_at`)
VALUES (1, 1, '2026-05-26 14:00:00', DATE_ADD(NOW(), INTERVAL 15 MINUTE));

# 本予約確定SQL
-- 対象の予約ID（例: 1）のステータスを「2:本予約」に変更
UPDATE `clinic_system_db`.`reservations` 
SET `status` = 2, `temporary_expires_at` = NULL 
WHERE `id` = 1 AND `status` = 1;

# 予約キャンセルSQL
UPDATE `clinic_system_db`.`reservations` 
SET `status` = 3, `canceled_at` = NOW() 
WHERE `id` = 1;

-- 予約テーブルと顧客テーブルのデータをすべて削除
TRUNCATE TABLE `clinic_system_db`.`reservations`;
TRUNCATE TABLE `clinic_system_db`.`customers`;

-- テスト顧客追加
INSERT INTO `clinic_system_db`.`customers` 
    (`id`, `name`, `phone_number`, `email`, `birth_date`) 
VALUES 
    (1, 'テスト花子', '09011111111', 'ｈａｎａｋｏ@example.com', '2000-01-01');
    
INSERT INTO `clinic_system_db`.`reservations` 
    (`customer_id`, `status`, `created_at`, `reserved_at`, `temporary_expires_at`, `canceled_at`)
VALUES 
    (1, 3, '2026-05-21 11:20:33', '2026-05-26 14:00:00', NULL, '2026-05-21 11:25:00');

INSERT INTO `clinic_system_db`.`reservations` 
    (`customer_id`, `status`, `created_at`, `reserved_at`, `temporary_expires_at`, `canceled_at`)
VALUES 
    (1, 2, '2026-05-21 11:20:33', '2026-05-26 14:00:00', NULL, NULL);

-- 管理者データの挿入
INSERT INTO `clinic_system_db`.`admins` (`id`, `name`, `password_hash`) VALUES 
(1, '高巻杏', '$2a$08$iBseSEgcNPjH7KlJtsv/r.xQQxIouHluDp2affjslM2WME1V1RhRC'),
(2, '新島真', '$2a$08$iBseSEgcNPjH7KlJtsv/r.xQQxIouHluDp2affjslM2WME1V1RhRC'),
(3, '佐倉双', '$2a$08$iBseSEgcNPjH7KlJtsv/r.xQQxIouHluDp2affjslM2WME1V1RhRC'),
(4, '奥村春', '$2a$08$iBseSEgcNPjH7KlJtsv/r.xQQxIouHluDp2affjslM2WME1V1RhRC'),
(5, '芳澤かすみ', '$2a$08$iBseSEgcNPjH7KlJtsv/r.xQQxIouHluDp2affjslM2WME1V1RhRC');