-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema notifications
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema notifications
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `notifications` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `notifications` ;

-- -----------------------------------------------------
-- Table `notifications`.`NOTIFICATION_TYPES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notifications`.`notification_types` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idNOTIFICATION_TYPES_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `type_UNIQUE` (`type` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `notifications`.`USERS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notifications`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `last_name` VARCHAR(100) NULL DEFAULT NULL,
  `email` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `notifications`.`NOTIFICATIONS_ALLOWED`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notifications`.`notifications_allowed` (
  `user_id` INT NOT NULL,
  `notification_type_id` INT NOT NULL,
  INDEX `notifications_allowed_users_idx` (`user_id` ASC) INVISIBLE,
  INDEX `notifications_allowed_notification_types_idx` (`notification_type_id` ASC) VISIBLE,
  PRIMARY KEY (`user_id`, `notification_type_id`),
  CONSTRAINT `notifications_allowed_notification_types`
    FOREIGN KEY (`notification_type_id`)
    REFERENCES `notifications`.`notification_types` (`id`),
  CONSTRAINT `notifications_allowed_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `notifications`.`users` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `notifications`.`SUBSCRIPTIONS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `notifications`.`subscriptions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `endpoint` VARCHAR(255) NOT NULL COMMENT 'This is a user browser ID, when an App requests permission to get notifications, this is how the users browser that opened the app is identified... The app could be opened from different browsers, so a user may have multiple subscriptions.',
  `p256dh_key` VARCHAR(150) NOT NULL COMMENT 'This is the subscription public key this is needed to decrypting the message from the browser.',
  `auth_key` VARCHAR(150) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `endpoint_UNIQUE` (`endpoint` ASC) VISIBLE,
  INDEX `subscriptions_users_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `subscriptions_users_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `notifications`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Populates `NOTIFICATION_TYPES`
-- -----------------------------------------------------
INSERT INTO `notifications`.`notification_types` (`type`)
    VALUES ("advertising"), ("promotions"), ("offer"), ("new arrivals");

-- -----------------------------------------------------
-- Populates `USERS`
-- -----------------------------------------------------
INSERT INTO notifications.users (`name`, last_name, email)
	VALUES 	("Jose", "Ramirez", "jose@email.com"), 
			    ("John", "Doe", "john@email.com"),
          ("Jennifer", "Smith", "jenny@email.com");

-- -----------------------------------------------------
-- Populates `NOTIFICATIONS_ALLOWED`
-- -----------------------------------------------------
INSERT INTO notifications.notifications_allowed (user_id, notification_type_id)
	VALUES 	(1, 1), 
			    (1, 2),
          (1, 3),
          (2, 1),
          (2, 3),
          (3, 3),
          (3, 4);