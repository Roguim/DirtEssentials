CREATE TABLE IF NOT EXISTS players (
    uuid VARCHAR(36) NOT NULL,
    username VARCHAR(16) NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    lastIpAddress VARCHAR(50) NOT NULL,
    isStaff BOOLEAN NOT NULL,
    leaveDate VARCHAR(50),
    locX DOUBLE,
    locY DOUBLE,
    locZ DOUBLE,
    locYaw FLOAT,
    locPitch FLOAT,
    locWorld VARCHAR(100),
    PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS balances (
    uuid VARCHAR(36) NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS kit_tracker (
    uuid VARCHAR(36) NOT NULL,
    kit VARCHAR(50) NOT NULL,
    lastClaimed VARCHAR(50) NOT NULL,
    PRIMARY KEY (uuid, kit)
);

CREATE TABLE IF NOT EXISTS homes (
    uuid VARCHAR(36) NOT NULL,
    home VARCHAR(50) NOT NULL,
    world VARCHAR(50) NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    z DOUBLE NOT NULL,
    yaw FLOAT NOT NULL,
    pitch FLOAT NOT NULL,
    PRIMARY KEY (uuid, home)
);