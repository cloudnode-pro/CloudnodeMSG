CREATE TABLE IF NOT EXISTS `cloudnodemsg_mail`
(
    `id`        CHAR(32) COLLATE NOCASE PRIMARY KEY,
    `sender`    CHAR(32) COLLATE NOCASE NOT NULL,
    `recipient` CHAR(32) COLLATE NOCASE NOT NULL,
    `message`   TEXT                NOT NULL,
    `seen`      BOOLEAN DEFAULT 0,
    `starred`   BOOLEAN DEFAULT 0
);
