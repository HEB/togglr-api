CREATE SCHEMA IF NOT EXISTS togglr;

CREATE TABLE togglr.APP
(
  ID INTEGER PRIMARY KEY auto_increment,
  APP_NAME VARCHAR(255),
  DESCR VARCHAR(255),
  WEBHOOK_URL VARCHAR(512)
);

CREATE TABLE togglr.FEATURE
(
  ID INTEGER PRIMARY KEY auto_increment,
  DESCR VARCHAR(255),
  APP_ID INTEGER NOT NULL,
  ACTIVE BIT NOT NULL DEFAULT 0,
  NEGATION BIT NOT NULL DEFAULT 0,
  CONSTRAINT fk_feature_app FOREIGN KEY (APP_ID) REFERENCES app (ID)
);

CREATE TABLE togglr.KEY_NAMES
(
  APP_ID INTEGER NOT NULL,
  KEY_NAME VARCHAR(255) NOT NULL,
  CONSTRAINT fk_key_app FOREIGN KEY (APP_ID) REFERENCES app (ID),
  CONSTRAINT pk_keys PRIMARY KEY (APP_ID, KEY_NAME)
);

CREATE TABLE togglr.CONFIGS
(
  APP_ID INTEGER NOT NULL,
  FEATURE_ID INTEGER NOT NULL,
  KEY_NAME VARCHAR(255) NOT NULL,
  CONFIG_VALUE VARCHAR(255) NOT NULL,
  CONSTRAINT pk_configs PRIMARY KEY (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE),
  CONSTRAINT fk_config_app FOREIGN KEY (APP_ID) REFERENCES app (ID),
  CONSTRAINT fk_configs_keys FOREIGN KEY (APP_ID, KEY_NAME) REFERENCES key_names (APP_ID, KEY_NAME),
  CONSTRAINT fk_features_app FOREIGN KEY (FEATURE_ID) REFERENCES feature (ID)
);

CREATE TABLE togglr.ADMINS
(
  ID VARCHAR(12) NOT NULL,
  APP_ID INTEGER,
  CONSTRAINT pk_admins PRIMARY KEY (ID, APP_ID),
  CONSTRAINT fk_admins_app FOREIGN KEY (APP_ID) REFERENCES app (ID)
);

CREATE TABLE togglr.SUPER_ADMINS
(
  ID VARCHAR(12) NOT NULL PRIMARY KEY,
);

CREATE TABLE togglr.JWT
(
  JWT_ID INTEGER PRIMARY KEY auto_increment,
  ISS_TO_UID VARCHAR(256),
  LST_USE_TS TIMESTAMP,
  VLD_SW CHAR
);

CREATE TABLE togglr.JWT_SECRET
(
  JWT_SECRET_KEY INTEGER PRIMARY KEY,
  JWT_SECRET_64 VARCHAR (256) NOT NULL
);