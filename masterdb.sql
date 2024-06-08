-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.30 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table masterdb.address
CREATE TABLE IF NOT EXISTS `address` (
  `customer_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_default` bit(1) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `district_code` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `province_code` varchar(255) DEFAULT NULL,
  `ward` varchar(255) DEFAULT NULL,
  `ward_code` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK93c3js0e22ll1xlu21nvrhqgg` (`customer_id`),
  CONSTRAINT `FK93c3js0e22ll1xlu21nvrhqgg` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.balance_variant
CREATE TABLE IF NOT EXISTS `balance_variant` (
  `balance_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `real_quantity` int NOT NULL,
  `saved_quantity` int NOT NULL,
  `variant_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKggrctc42mw5do93o7t7wh4ahn` (`variant_id`),
  KEY `FK2h24nth1v3f6q3aolm5ytqhd2` (`balance_id`),
  CONSTRAINT `FK2h24nth1v3f6q3aolm5ytqhd2` FOREIGN KEY (`balance_id`) REFERENCES `warehouse_balance` (`id`),
  CONSTRAINT `FKggrctc42mw5do93o7t7wh4ahn` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.base_product
CREATE TABLE IF NOT EXISTS `base_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` tinyint(1) DEFAULT '0',
  `shop_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `attribute1` varchar(255) DEFAULT NULL,
  `attribute2` varchar(255) DEFAULT NULL,
  `attribute3` varchar(255) DEFAULT NULL,
  `description` longtext,
  `label` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1emt9gsggr71h2cly2kh2ue70` (`shop_id`),
  CONSTRAINT `FK1emt9gsggr71h2cly2kh2ue70` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.base_product_sales_channel
CREATE TABLE IF NOT EXISTS `base_product_sales_channel` (
  `active` bit(1) NOT NULL,
  `base_product_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `sales_channel_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKegt6rr7ltanooirmq1g6v13g1` (`base_product_id`),
  KEY `FK1c0p7i3of292noiy4ww6y816` (`sales_channel_id`),
  CONSTRAINT `FK1c0p7i3of292noiy4ww6y816` FOREIGN KEY (`sales_channel_id`) REFERENCES `sales_channel` (`id`),
  CONSTRAINT `FKegt6rr7ltanooirmq1g6v13g1` FOREIGN KEY (`base_product_id`) REFERENCES `base_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.cart_item
CREATE TABLE IF NOT EXISTS `cart_item` (
  `active` bit(1) DEFAULT NULL,
  `cart_id` int DEFAULT NULL,
  `discount` double DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `price` double DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf9e1brwb4ea9cxtvxbs0wugdt` (`cart_id`),
  KEY `FKljvkbcl2k83yen3y6lhpcgr1b` (`product_id`),
  CONSTRAINT `FKf9e1brwb4ea9cxtvxbs0wugdt` FOREIGN KEY (`cart_id`) REFERENCES `shopping_cart` (`id`),
  CONSTRAINT `FKljvkbcl2k83yen3y6lhpcgr1b` FOREIGN KEY (`product_id`) REFERENCES `base_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` longtext,
  `meta_title` varchar(255) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `group_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT '5',
  `created_at` datetime(6) DEFAULT NULL,
  `date_of_birth` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `customer_code` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlkies97eyvye3jidco7p4db1i` (`group_id`),
  KEY `FKo2oh87rk6lunf0lic1svc9y75` (`role_id`),
  CONSTRAINT `FKlkies97eyvye3jidco7p4db1i` FOREIGN KEY (`group_id`) REFERENCES `customer_group` (`id`),
  CONSTRAINT `FKo2oh87rk6lunf0lic1svc9y75` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.customer_group
CREATE TABLE IF NOT EXISTS `customer_group` (
  `discount` double NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `shop_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dl4it04rsafxcy89j1wir4fnl` (`name`),
  KEY `FKfenqg14mdke8rn41ugnxfcgk5` (`shop_id`),
  CONSTRAINT `FKfenqg14mdke8rn41ugnxfcgk5` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.feedback
CREATE TABLE IF NOT EXISTS `feedback` (
  `customer_id` int DEFAULT NULL,
  `evaluate` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `person_in_charge` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpi2y2j7n01ypo49fone3knjry` (`customer_id`),
  KEY `FKff7hj937ulehi1ysj40lwxnkd` (`person_in_charge`),
  CONSTRAINT `FKff7hj937ulehi1ysj40lwxnkd` FOREIGN KEY (`person_in_charge`) REFERENCES `user` (`id`),
  CONSTRAINT `FKpi2y2j7n01ypo49fone3knjry` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.import_item
CREATE TABLE IF NOT EXISTS `import_item` (
  `discount` int NOT NULL,
  `import_id` int NOT NULL,
  `import_price` int DEFAULT NULL,
  `quantity` int NOT NULL,
  `variant_id` int NOT NULL,
  PRIMARY KEY (`import_id`,`variant_id`),
  KEY `FK2v3adg34uq4voqbil3fmqa9nc` (`variant_id`),
  CONSTRAINT `FK2v3adg34uq4voqbil3fmqa9nc` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`),
  CONSTRAINT `FK7ljyd6dlkrp89sjwyr8v0wx4j` FOREIGN KEY (`import_id`) REFERENCES `import_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.import_order
CREATE TABLE IF NOT EXISTS `import_order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `person_in_charge` int DEFAULT NULL,
  `shipment_status` tinyint DEFAULT NULL,
  `vendor_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh2aoealeqisxupg31wyjyqlpw` (`person_in_charge`),
  KEY `FKfy3sqeq1tf5hkmyd2mr611l04` (`vendor_id`),
  CONSTRAINT `FKfy3sqeq1tf5hkmyd2mr611l04` FOREIGN KEY (`vendor_id`) REFERENCES `vendor` (`id`),
  CONSTRAINT `FKh2aoealeqisxupg31wyjyqlpw` FOREIGN KEY (`person_in_charge`) REFERENCES `user` (`id`),
  CONSTRAINT `import_order_chk_1` CHECK ((`shipment_status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.order_line
CREATE TABLE IF NOT EXISTS `order_line` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `price` int NOT NULL,
  `quantity` int NOT NULL,
  `return_quantity` int DEFAULT '0',
  `variant_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK27k1n0p6fl3w72nur1clauqre` (`order_id`),
  KEY `FKroikilpfif4yfqiigotg1ql4p` (`variant_id`),
  CONSTRAINT `FK27k1n0p6fl3w72nur1clauqre` FOREIGN KEY (`order_id`) REFERENCES `_order` (`id`),
  CONSTRAINT `FKroikilpfif4yfqiigotg1ql4p` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.payment
CREATE TABLE IF NOT EXISTS `payment` (
  `amount` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `pay_date` date DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `order_type` enum('IMPORT','ORDER','RETURN') DEFAULT NULL,
  `payment_method` enum('CASH','COD','CREDIT','TRANSFER') DEFAULT NULL,
  `payment_status` enum('COMPLETE','INIT') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.product_category
CREATE TABLE IF NOT EXISTS `product_category` (
  `category_id` int NOT NULL,
  `product_id` int NOT NULL,
  KEY `FKmd55q8r8prvxq3b82umrhmqea` (`product_id`),
  KEY `FKkud35ls1d40wpjb5htpp14q4e` (`category_id`),
  CONSTRAINT `FKkud35ls1d40wpjb5htpp14q4e` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKmd55q8r8prvxq3b82umrhmqea` FOREIGN KEY (`product_id`) REFERENCES `base_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.promotion
CREATE TABLE IF NOT EXISTS `promotion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `value` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `policy_apply` enum('ALL','CATEGORY','COUPON','PRODUCT') DEFAULT NULL,
  `status` enum('active','expired','scheduled') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `value_type` enum('FIXED','PERCENTAGE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.promotion_category
CREATE TABLE IF NOT EXISTS `promotion_category` (
  `category_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  KEY `FKifob7v9b53tll9sw212do1c4y` (`category_id`),
  KEY `FKi6u8xhobf3x84rxy6ccou7k0l` (`promotion_id`),
  CONSTRAINT `FKi6u8xhobf3x84rxy6ccou7k0l` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`),
  CONSTRAINT `FKifob7v9b53tll9sw212do1c4y` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.promotion_product
CREATE TABLE IF NOT EXISTS `promotion_product` (
  `product_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  KEY `FKbmjs9viwnubxynk91ypgjy6td` (`product_id`),
  KEY `FKeq9krkiyh71kekr3ji9ats5qk` (`promotion_id`),
  CONSTRAINT `FKbmjs9viwnubxynk91ypgjy6td` FOREIGN KEY (`product_id`) REFERENCES `base_product` (`id`),
  CONSTRAINT `FKeq9krkiyh71kekr3ji9ats5qk` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.return_order
CREATE TABLE IF NOT EXISTS `return_order` (
  `base_order` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `person_in_charge` int DEFAULT NULL,
  `swap_order` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `return_reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_augin0gjmdjyuajo9or8i7dd7` (`swap_order`),
  KEY `FKgo8vk6a384eetolgv545fkwsj` (`base_order`),
  KEY `FKeaxhspo6qtesxxq2r4gpm9d7y` (`person_in_charge`),
  CONSTRAINT `FKeaxhspo6qtesxxq2r4gpm9d7y` FOREIGN KEY (`person_in_charge`) REFERENCES `user` (`id`),
  CONSTRAINT `FKgo8vk6a384eetolgv545fkwsj` FOREIGN KEY (`base_order`) REFERENCES `_order` (`id`),
  CONSTRAINT `FKppivrep58o7rj0b0rp9i4yw2` FOREIGN KEY (`swap_order`) REFERENCES `_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.return_order_line
CREATE TABLE IF NOT EXISTS `return_order_line` (
  `id` int NOT NULL AUTO_INCREMENT,
  `original_price` int DEFAULT NULL,
  `return_id` int DEFAULT NULL,
  `return_price` int DEFAULT NULL,
  `return_quantity` int DEFAULT NULL,
  `variant_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK40ia2uul22s6trglrbdumw6sg` (`return_id`),
  KEY `FKlw5ppn8bo87qqhx16o8376l4o` (`variant_id`),
  CONSTRAINT `FK40ia2uul22s6trglrbdumw6sg` FOREIGN KEY (`return_id`) REFERENCES `return_order` (`id`),
  CONSTRAINT `FKlw5ppn8bo87qqhx16o8376l4o` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.role
CREATE TABLE IF NOT EXISTS `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` enum('ADMIN','CARE','CUSTOMER','SALE','WAREHOUSE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `role` (`id`, `created_at`, `updated_at`, `name`) VALUES
    (1, NULL, NULL, 'ADMIN'),
    (2, NULL, NULL, 'SALE'),
    (3, NULL, NULL, 'CARE'),
    (4, NULL, NULL, 'WAREHOUSE'),
    (5, NULL, NULL, 'CUSTOMER');
-- Data exporting was unselected.

-- Dumping structure for table masterdb.sales_channel
CREATE TABLE IF NOT EXISTS `sales_channel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.shop
CREATE TABLE IF NOT EXISTS `shop` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vat` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `business` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `wards` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.shopping_cart
CREATE TABLE IF NOT EXISTS `shopping_cart` (
  `customer_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `variant_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `status` enum('Abandoned','Cart','Checkout','Complete','New','Paid') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6a233w2y93tf1moa8havy9ee3` (`customer_id`,`variant_id`),
  KEY `FKrvoc3g62hf6eb3im2wlo5hyfw` (`variant_id`),
  CONSTRAINT `FKh7db6pdgs2ol2t6k73wn3xj75` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKrvoc3g62hf6eb3im2wlo5hyfw` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.token
CREATE TABLE IF NOT EXISTS `token` (
  `customer_id` int DEFAULT NULL,
  `expired` bit(1) NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `revoked` bit(1) NOT NULL,
  `user_id` int DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` enum('BEARER') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pddrhgwxnms2aceeku9s2ewy5` (`token`),
  KEY `FKke9j5rnuwqfkmnyyfa9k2p3ug` (`customer_id`),
  KEY `FKe32ek7ixanakfqsdaokm4q9y2` (`user_id`),
  CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKke9j5rnuwqfkmnyyfa9k2p3ug` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.user
CREATE TABLE IF NOT EXISTS `user` (
  `dob` date DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_active` tinyint(1) DEFAULT '0',
  `shop_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `work_status` enum('QUIT','WORKING') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_589idila9li6a4arw1t8ht1gx` (`phone`),
  KEY `FKophlo4m4uodyxvpnqf1a6d4vn` (`shop_id`),
  CONSTRAINT `FKophlo4m4uodyxvpnqf1a6d4vn` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.user_role
CREATE TABLE IF NOT EXISTS `user_role` (
  `role_id` int NOT NULL,
  `user_id` int NOT NULL,
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.variant
CREATE TABLE IF NOT EXISTS `variant` (
  `base_id` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `import_price` int DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `quantity` int DEFAULT '0',
  `retail_price` int DEFAULT '0',
  `wholesale_price` int DEFAULT '0',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sku` varchar(255) DEFAULT NULL,
  `value1` varchar(255) DEFAULT NULL,
  `value2` varchar(255) DEFAULT NULL,
  `value3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_llpabmolrn143l5uh3dp92bgy` (`sku`),
  KEY `FKdgvfgiduminfsf4njbnip63n8` (`base_id`),
  CONSTRAINT `FKdgvfgiduminfsf4njbnip63n8` FOREIGN KEY (`base_id`) REFERENCES `base_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.vendor
CREATE TABLE IF NOT EXISTS `vendor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `shop_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `tax` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2xmu542vpxadksjt6trq9tkcr` (`shop_id`),
  CONSTRAINT `FK2xmu542vpxadksjt6trq9tkcr` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb.warehouse_balance
CREATE TABLE IF NOT EXISTS `warehouse_balance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` tinyint(1) DEFAULT '0',
  `person_in_charge` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4cf8y4hvv6gtfsdluypq1ui0f` (`person_in_charge`),
  CONSTRAINT `FK4cf8y4hvv6gtfsdluypq1ui0f` FOREIGN KEY (`person_in_charge`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Data exporting was unselected.

-- Dumping structure for table masterdb._order
CREATE TABLE IF NOT EXISTS `_order` (
  `customer_id` int DEFAULT NULL,
  `discount` int DEFAULT '0',
  `id` int NOT NULL AUTO_INCREMENT,
  `person_in_charge` int DEFAULT NULL,
  `sales_channel_id` int DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `customer_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK89rlip6yd7m162yywywl56e7q` (`customer_id`),
  KEY `FK7wk3g5m31l8juu4ea2qexsrq6` (`sales_channel_id`),
  KEY `FKbv28aihj1d90tivqmtwxj2apn` (`person_in_charge`),
  CONSTRAINT `FK7wk3g5m31l8juu4ea2qexsrq6` FOREIGN KEY (`sales_channel_id`) REFERENCES `sales_channel` (`id`),
  CONSTRAINT `FK89rlip6yd7m162yywywl56e7q` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FKbv28aihj1d90tivqmtwxj2apn` FOREIGN KEY (`person_in_charge`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
