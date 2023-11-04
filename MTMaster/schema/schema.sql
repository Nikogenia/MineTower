CREATE TABLE `player` (
  `uuid` VARCHAR(36) PRIMARY KEY NOT NULL,
  `name` VARCHAR(16) UNIQUE NOT NULL,
  `reconnect` BIGINT NOT NULL,
  `vanish` BOOL NOT NULL DEFAULT false,
  `ban_until` BIGINT,
  `ban_reason` VARCHAR(255),
  `status` BIGINT
);

CREATE TABLE `rank` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `permission` VARCHAR(255) UNIQUE NOT NULL,
  `display` VARCHAR(255) NOT NULL,
  `color` VARCHAR(32) NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP())
);

CREATE TABLE `shop_member` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `shop` BIGINT NOT NULL,
  `player` VARCHAR(36) NOT NULL
);

CREATE TABLE `server` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `image` VARCHAR(32) NOT NULL DEFAULT "nikogenia/mt-paper",
  `version` VARCHAR(32) NOT NULL DEFAULT "latest",
  `memory` VARCHAR(32) NOT NULL DEFAULT "2048M",
  `agent` BIGINT NOT NULL,
  `host` VARCHAR(15) NOT NULL,
  `ports` VARCHAR(255) NOT NULL,
  `proxy` BIGINT NOT NULL,
  `address` VARCHAR(255) UNIQUE NOT NULL,
  `cluster` BIGINT,
  `mode` VARCHAR(32) NOT NULL DEFAULT "off",
  `whitelist` BIGINT,
  `hidden` BOOL NOT NULL DEFAULT false
);

CREATE TABLE `general` (
  `name` VARCHAR(32) PRIMARY KEY NOT NULL,
  `data` TEXT NOT NULL
);

CREATE TABLE `whitelist` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `message` TEXT
);

CREATE TABLE `shop` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `owner` VARCHAR(36),
  `server` BIGINT NOT NULL,
  `world` VARCHAR(32) NOT NULL,
  `area` VARCHAR(255) NOT NULL
);

CREATE TABLE `cluster` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `template` BIGINT NOT NULL,
  `agent` BIGINT NOT NULL,
  `subnet` VARCHAR(13) NOT NULL
);

CREATE TABLE `proxy` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `image` VARCHAR(32) NOT NULL DEFAULT "nikogenia/mt-waterfall",
  `version` VARCHAR(32) NOT NULL DEFAULT "latest",
  `memory` VARCHAR(32) NOT NULL DEFAULT "512M",
  `agent` BIGINT NOT NULL,
  `host` VARCHAR(15) NOT NULL,
  `ports` VARCHAR(255) NOT NULL,
  `mode` VARCHAR(32) NOT NULL DEFAULT "off",
  `motd` BIGINT NOT NULL,
  `whitelist` BIGINT
);

CREATE TABLE `agent` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP())
);

CREATE TABLE `console_user` (
  `uuid` VARCHAR(36) PRIMARY KEY NOT NULL,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `password` TEXT NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `last_login` BIGINT,
  `admin` BOOL NOT NULL DEFAULT false,
  `read_only` BOOL NOT NULL DEFAULT true
);

CREATE TABLE `home` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `player` VARCHAR(36) NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `server` BIGINT NOT NULL,
  `world` VARCHAR(32) NOT NULL,
  `x` DOUBLE NOT NULL,
  `y` DOUBLE NOT NULL,
  `z` DOUBLE NOT NULL
);

CREATE TABLE `proxy_session` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `player` VARCHAR(36) NOT NULL,
  `proxy` BIGINT NOT NULL,
  `connected` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `disconnected` BIGINT
);

CREATE TABLE `server_session` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `proxy_session` BIGINT NOT NULL,
  `server` BIGINT NOT NULL,
  `connected` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `disconnected` BIGINT
);

CREATE TABLE `status` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `display` VARCHAR(255) NOT NULL
);

CREATE TABLE `template` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `image` VARCHAR(32) NOT NULL,
  `version` VARCHAR(32) NOT NULL DEFAULT "latest",
  `memory` VARCHAR(32) NOT NULL,
  `ports` VARCHAR(255) NOT NULL
);

CREATE TABLE `motd` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) UNIQUE NOT NULL,
  `created` BIGINT NOT NULL DEFAULT (UNIX_TIMESTAMP()),
  `line1` VARCHAR(255) NOT NULL,
  `line2` VARCHAR(255) NOT NULL
);

CREATE TABLE `whitelist_member` (
  `id` BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `player` VARCHAR(36) NOT NULL,
  `whitelist` BIGINT NOT NULL
);

ALTER TABLE `home` ADD FOREIGN KEY (`server`) REFERENCES `server` (`id`);

ALTER TABLE `player` ADD FOREIGN KEY (`reconnect`) REFERENCES `server` (`id`);

ALTER TABLE `shop` ADD FOREIGN KEY (`server`) REFERENCES `server` (`id`);

ALTER TABLE `server_session` ADD FOREIGN KEY (`server`) REFERENCES `server` (`id`);

ALTER TABLE `cluster` ADD FOREIGN KEY (`agent`) REFERENCES `agent` (`id`);

ALTER TABLE `server` ADD FOREIGN KEY (`agent`) REFERENCES `agent` (`id`);

ALTER TABLE `proxy` ADD FOREIGN KEY (`agent`) REFERENCES `agent` (`id`);

ALTER TABLE `shop_member` ADD FOREIGN KEY (`player`) REFERENCES `player` (`uuid`);

ALTER TABLE `shop` ADD FOREIGN KEY (`owner`) REFERENCES `player` (`uuid`);

ALTER TABLE `home` ADD FOREIGN KEY (`player`) REFERENCES `player` (`uuid`);

ALTER TABLE `whitelist_member` ADD FOREIGN KEY (`player`) REFERENCES `player` (`uuid`);

ALTER TABLE `proxy_session` ADD FOREIGN KEY (`player`) REFERENCES `player` (`uuid`);

ALTER TABLE `cluster` ADD FOREIGN KEY (`template`) REFERENCES `template` (`id`);

ALTER TABLE `proxy` ADD FOREIGN KEY (`whitelist`) REFERENCES `whitelist` (`id`);

ALTER TABLE `server` ADD FOREIGN KEY (`whitelist`) REFERENCES `whitelist` (`id`);

ALTER TABLE `whitelist_member` ADD FOREIGN KEY (`whitelist`) REFERENCES `whitelist` (`id`);

ALTER TABLE `player` ADD FOREIGN KEY (`status`) REFERENCES `status` (`id`);

ALTER TABLE `server` ADD FOREIGN KEY (`cluster`) REFERENCES `cluster` (`id`);

ALTER TABLE `server` ADD FOREIGN KEY (`proxy`) REFERENCES `proxy` (`id`);

ALTER TABLE `proxy_session` ADD FOREIGN KEY (`proxy`) REFERENCES `proxy` (`id`);

ALTER TABLE `proxy` ADD FOREIGN KEY (`motd`) REFERENCES `motd` (`id`);

ALTER TABLE `shop_member` ADD FOREIGN KEY (`shop`) REFERENCES `shop` (`id`);

ALTER TABLE `server_session` ADD FOREIGN KEY (`proxy_session`) REFERENCES `proxy_session` (`id`);
