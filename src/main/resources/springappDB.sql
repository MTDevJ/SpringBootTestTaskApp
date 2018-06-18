-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Июн 18 2018 г., 07:22
-- Версия сервера: 10.1.32-MariaDB
-- Версия PHP: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `springapp`
--

-- --------------------------------------------------------

--
-- Структура таблицы `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_description` text NOT NULL,
  `category_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `categories`
--

INSERT INTO `categories` (`category_id`, `category_description`, `category_name`) VALUES
(1, '', 'Кондитерские изделия'),
(2, '', 'Крупы'),
(3, '', 'Напитки'),
(4, '', 'Молочная продукция'),
(5, '', 'Мясная продукция');

-- --------------------------------------------------------

--
-- Структура таблицы `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(39),
(39),
(39);

-- --------------------------------------------------------

--
-- Структура таблицы `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_amount` int(11) NOT NULL,
  `product_description` text NOT NULL,
  `product_image_name` varchar(255) DEFAULT NULL,
  `product_name` varchar(50) NOT NULL,
  `product_price` double NOT NULL,
  `category_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `products`
--

INSERT INTO `products` (`product_id`, `product_amount`, `product_description`, `product_image_name`, `product_name`, `product_price`, `category_id`) VALUES
(1, 13, 'Торт праздничный', 'a5ad8696-8124-48f3-8f3c-24db56eafc02.252-500x500.jpg', 'Торт', 34.98, 1),
(2, 116, '', '', 'Бекон', 60, 5),
(3, 887, '', '', 'Пастила', 54.57, 1),
(4, 210, '', '', 'Молоко', 91.32, 4),
(5, 34, '', '', 'Сметана', 15, 4),
(6, 12, '', '', 'Кекс', 34.98, 1),
(7, 166, '', '', 'Сок', 60, 3),
(8, 80, '', '', 'Квас', 54.57, 3),
(9, 278, '', '', 'Сыр', 91.32, 4),
(10, 25, '', '', 'Творог', 15, 4),
(11, 13, 'Сосиски молочные', 'e71719c5-a0d8-4279-b28f-ce7bec8bc4cb.21c05a04b99e546539813e7ea598d75d.jpg', 'Сосиски', 34.98, 5),
(12, 32, '', '', 'Колбаса вареная', 60, 5),
(13, 61, '', '', 'Крапа перловая', 54.57, 2),
(14, 20, '', '', 'нут', 91.32, 2),
(15, 33, '', '', 'Гречка', 15, 2);

-- --------------------------------------------------------

--
-- Структура таблицы `user_role`
--

CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `roles` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user_role`
--

INSERT INTO `user_role` (`user_id`, `roles`) VALUES
(1, 'USER'),
(2, 'ADMIN');

-- --------------------------------------------------------

--
-- Структура таблицы `usr`
--

CREATE TABLE `usr` (
  `id` int(11) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `usr`
--

INSERT INTO `usr` (`id`, `active`, `password`, `username`) VALUES
(1, b'1', 'user', 'user'),
(2, b'1', 'admin', 'admin');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `UK_41g4n0emuvcm3qyf1f6cn43c0` (`category_name`);

--
-- Индексы таблицы `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD UNIQUE KEY `UK_f55t6sm19p5lrihq24a6knota` (`product_name`),
  ADD KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`);

--
-- Индексы таблицы `user_role`
--
ALTER TABLE `user_role`
  ADD KEY `FKfpm8swft53ulq2hl11yplpr5` (`user_id`);

--
-- Индексы таблицы `usr`
--
ALTER TABLE `usr`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Ограничения внешнего ключа таблицы `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `FKfpm8swft53ulq2hl11yplpr5` FOREIGN KEY (`user_id`) REFERENCES `usr` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
