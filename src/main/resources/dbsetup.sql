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

CREATE TABLE IF NOT EXISTS homeData (
    uuid VARCHAR(36) NOT NULL,
    homesAvailable INT NOT NULL,
    PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS homes (
    uuid VARCHAR(36) NOT NULL,
    home VARCHAR(50) NOT NULL,
    world VARCHAR(100) NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    z DOUBLE NOT NULL,
    yaw FLOAT NOT NULL,
    pitch FLOAT NOT NULL,
    FOREIGN KEY (uuid) REFERENCES homeData(uuid),
    PRIMARY KEY (uuid, home)
);

CREATE TABLE IF NOT EXISTS warps (
    name VARCHAR(50) NOT NULL,
    world VARCHAR(100) NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    z DOUBLE NOT NULL,
    yaw FLOAT NOT NULL,
    pitch FLOAT NOT NULL,
    item TEXT NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS spawn (
    world VARCHAR(100) NOT NULL,
    x DOUBLE NOT NULL,
    y DOUBLE NOT NULL,
    z DOUBLE NOT NULL,
    yaw FLOAT NOT NULL,
    pitch FLOAT NOT NULL,
    PRIMARY KEY (world, x, y, z, yaw, pitch)
);

CREATE TABLE IF NOT EXISTS autobroadcast (
    uuid VARCHAR(36) NOT NULL,
    PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS notes (
    uuid VARCHAR(36) NOT NULL,
    note VARCHAR NOT NULL,
    addedBy VARCHAR(36) NOT NULL,
    addedOn VARCHAR(50) NOT NULL,
    PRIMARY KEY (uuid, note)
);