INSERT INTO togglr.APP (ID, DESCR, APP_NAME, WEBHOOK_URL) VALUES (1, 'My super cool app', 'COOL', 'http://localhost:8080/togglr-api/togglr/update');
INSERT INTO togglr.APP (ID, DESCR, APP_NAME) VALUES (2, 'A simple test app', 'TEST');

INSERT INTO togglr.SUPER_ADMINS (ID) VALUES ('admin');

INSERT INTO togglr.KEY_NAMES (APP_ID, KEY_NAME) VALUES (1, 'User');
INSERT INTO togglr.KEY_NAMES (APP_ID, KEY_NAME) VALUES (1, 'Zip');

INSERT INTO togglr.FEATURE (ID, DESCR, APP_ID, ACTIVE, NEGATION) VALUES (1, 'Version Label', 1, TRUE, FALSE);
INSERT INTO togglr.FEATURE (ID, DESCR, APP_ID, ACTIVE, NEGATION) VALUES (2, 'Logout', 1, TRUE, FALSE);
INSERT INTO togglr.FEATURE (ID, DESCR, APP_ID, ACTIVE, NEGATION) VALUES (3, 'Login', 1, TRUE, FALSE);
INSERT INTO togglr.FEATURE (ID, DESCR, APP_ID, ACTIVE, NEGATION) VALUES (4, 'Ads', 1, TRUE, FALSE);
INSERT INTO togglr.FEATURE (ID, DESCR, APP_ID, ACTIVE, NEGATION) VALUES (5, 'Super Secret Feature', 1, TRUE, FALSE);

INSERT INTO togglr.CONFIGS (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE) VALUES (1, 1, 'User', 'Superman');
INSERT INTO togglr.CONFIGS (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE) VALUES (1, 1, 'Zip', '78701');
INSERT INTO togglr.CONFIGS (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE) VALUES (1, 2, 'Zip', '78702');
INSERT INTO togglr.CONFIGS (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE) VALUES (1, 3, 'User', 'Iron Man');
INSERT INTO togglr.CONFIGS (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE) VALUES (1, 4, 'User', 'Captain America');
INSERT INTO togglr.CONFIGS (APP_ID, FEATURE_ID, KEY_NAME, CONFIG_VALUE) VALUES (1, 5, 'User', 'Thor');


INSERT INTO togglr.JWT_SECRET (JWT_SECRET_KEY, JWT_SECRET_64) VALUES (101, 'ABCKDLER902kjkl234H3sf0ED0jL')