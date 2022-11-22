-- asap.userreview definition

-- asap.userreview definition

CREATE TABLE `userreview` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `dist` double NOT NULL,
                              `cost` double NOT NULL,
                              `discontent` int NOT NULL DEFAULT -1,
                              `user_id` int DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `userreview_FK` (`user_id`),
                              CONSTRAINT `userreview_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO userreview (dist, cost, discontent) VALUES (3, 3000, 0);