USE example_0000;

CREATE TABLE user_id_seq (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  stub CHAR(1) NOT NULL,
  UNIQUE KEY stub (stub)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_unicode_ci;

INSERT INTO user_id_seq (stub) VALUES ('A');

DELIMITER $$
CREATE FUNCTION next_user_id()
RETURNS BIGINT
DETERMINISTIC
BEGIN
  DECLARE epoch BIGINT DEFAULT 1420070400000; -- 2015-01-01 00:00:00
  DECLARE seq_id BIGINT;
  DECLARE now BIGINT;
  DECLARE shard_id INT DEFAULT 0;
  DECLARE next_id BIGINT;

  REPLACE INTO user_id_seq (stub) values ('A');
  SELECT LAST_INSERT_ID() % 1024 INTO seq_id;
  SELECT FLOOR(UNIX_TIMESTAMP(NOW(3)) * 1000) INTO now;
  SET next_id = (now - epoch) << 23;
  SET next_id = next_id | (shard_id << 10);
  SET next_id = next_id | seq_id;

  RETURN next_id;
END $$
DELIMITER ;

CREATE TABLE user (
  id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
  bytes blob,
  email char(255)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE UNIQUE INDEX user_email ON user (email);
