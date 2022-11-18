CREATE TABLE `parkinglot` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,

                              `PARKING_CODE` int(11) DEFAULT NULL,
                              `PARKING_NAME` varchar(255) COLLATE utf8mb4_bin NOT NULL,
                              `ADDR` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                              `PARKING_TYPE` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                              `TEL` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,

                              `CAPACITY` int(11) DEFAULT NULL,
                              `CAPACITY_AVAILABLE` int(11) DEFAULT NULL,

                              `WEEKDAY_BEGIN_TIME` int(11) DEFAULT NULL,
                              `WEEKDAY_END_TIME` int(11) DEFAULT NULL,
                              `WEEKEND_BEGIN_TIME` int(11) DEFAULT NULL,
                              `WEEKEND_END_TIME` int(11) DEFAULT NULL,
                              `HOLIDAY_BEGIN_TIME` int(11) DEFAULT NULL,
                              `HOLIDAY_END_TIME` int(11) DEFAULT NULL,

                              `PAY_YN` bit(1) DEFAULT NULL,
                              `SATURDAY_PAY_YN` bit(1) DEFAULT NULL,
                              `HOLIDAY_PAY_YN` bit(1) DEFAULT NULL,

                              `RATES` int(11) DEFAULT NULL,
                              `TIME_RATE` int(11) DEFAULT NULL,
                              `ADD_RATES` int(11) DEFAULT NULL,
                              `ADD_TIME_RATE` int(11) DEFAULT NULL,
                              `DAY_MAXIMUM` int(11) DEFAULT NULL,

                              `LAT` double NOT NULL,
                              `LNG` double NOT NULL,

                              `WIDE_YN` bit(1) DEFAULT NULL,
                              `MECHANICAL_YN` bit(1) DEFAULT NULL,
                              `RATES_PER_HOUR` int(11) DEFAULT NULL,

                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


CREATE TABLE `user` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,

                              `username` varchar(255) COLLATE utf8mb4_bin NOT NULL,
                              `email` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                              `password` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
                              `role` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,

                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


CREATE TABLE `user_preference` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `user_id` int(11) NOT NULL,

                        `dist_prefer` double NOT NULL DEFAULT 0.0,
                        `dist_weight` double NOT NULL DEFAULT 0.4,
                        `cost_prefer` double NOT NULL DEFAULT 0.0,
                        `cost_weight` double NOT NULL DEFAULT 0.6,
                        `can_mechanical` tinyint(1) NOT NULL DEFAULT 1,
                        `can_narrow` tinyint(1) NOT NULL DEFAULT 1,

                        PRIMARY KEY (`id`),
                        FOREIGN KEY (user_id) REFERENCES user(id) ON UPDATE cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO user (username, email, password, role) VALUES ('testadmin', 'admin@test.com', '12345678', 'ROLE_ADMIN');
INSERT INTO user (username, email, password, role) VALUES ('testuser', 'user@test.com', '12345678', 'ROLE_USER');
