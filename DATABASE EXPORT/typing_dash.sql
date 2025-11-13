-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 12, 2025 at 11:59 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `typing_dash`
--

-- --------------------------------------------------------

--
-- Table structure for table `scores`
--

CREATE TABLE `scores` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `wpm` double NOT NULL,
  `accuracy` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `timestamp` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `scores`
--

INSERT INTO `scores` (`id`, `user_id`, `wpm`, `accuracy`, `created_at`, `timestamp`) VALUES
(1, 1, 0, 0, '2025-11-11 08:10:28', '2025-11-11 18:10:09'),
(2, 1, 0, 0, '2025-11-11 09:09:41', '2025-11-11 18:10:09'),
(3, 1, 505, 55.19125683060109, '2025-11-11 09:32:35', '2025-11-11 18:10:09'),
(4, 1, 800, 83.51648351648352, '2025-11-11 10:11:30', '2025-11-11 18:11:30'),
(5, 1, 435, 35.22267206477733, '2025-11-11 11:00:33', '2025-11-11 19:00:33'),
(6, 1, 573.9130434782609, 17.813765182186234, '2025-11-12 05:23:26', '2025-11-12 13:23:26'),
(7, 1, 0, 0, '2025-11-12 05:33:53', '2025-11-12 13:33:53'),
(8, 1, 0, 0, '2025-11-12 05:35:07', '2025-11-12 13:35:07'),
(9, 1, 0, 0, '2025-11-12 05:44:13', '2025-11-12 13:44:13'),
(10, 1, 0, 0, '2025-11-12 05:51:32', '2025-11-12 13:51:32'),
(11, 1, 0, 0, '2025-11-12 05:59:47', '2025-11-12 13:59:47'),
(12, 1, 0, 0, '2025-11-12 06:00:14', '2025-11-12 14:00:14'),
(13, 1, 0, 0, '2025-11-12 06:01:21', '2025-11-12 14:01:21'),
(14, 1, 120, 3.278688524590164, '2025-11-12 09:00:43', '2025-11-12 17:00:43'),
(15, 1, 31.789473684210527, 82.96703296703296, '2025-11-12 09:48:48', '2025-11-12 17:48:48'),
(16, 1, 31.789473684210527, 82.96703296703296, '2025-11-12 09:48:48', '2025-11-12 17:48:48'),
(17, 1, 0, 0, '2025-11-12 09:53:11', '2025-11-12 17:53:11'),
(18, 1, 0, 0, '2025-11-12 09:53:11', '2025-11-12 17:53:11'),
(19, 1, 8.571428571428571, 4.716981132075472, '2025-11-12 10:10:23', '2025-11-12 18:10:23'),
(20, 1, 0, 0, '2025-11-12 10:10:34', '2025-11-12 18:10:34'),
(21, 1, 0, 0, '2025-11-12 10:10:34', '2025-11-12 18:10:34'),
(22, 1, 30.46153846153846, 62.264150943396224, '2025-11-12 10:17:10', '2025-11-12 18:17:10'),
(23, 1, 30.46153846153846, 62.264150943396224, '2025-11-12 10:17:10', '2025-11-12 18:17:10'),
(24, 1, 0, 0, '2025-11-12 10:17:17', '2025-11-12 18:17:17'),
(25, 1, 0, 0, '2025-11-12 10:17:19', '2025-11-12 18:17:19'),
(26, 1, 0, 0, '2025-11-12 10:17:22', '2025-11-12 18:17:22'),
(27, 1, 0, 0, '2025-11-12 10:17:49', '2025-11-12 18:17:49'),
(28, 1, 30, 73.77049180327869, '2025-11-12 10:19:40', '2025-11-12 18:19:40'),
(29, 1, 30, 73.77049180327869, '2025-11-12 10:19:40', '2025-11-12 18:19:40'),
(30, 1, 0, 0, '2025-11-12 10:19:49', '2025-11-12 18:19:49'),
(31, 1, 0, 0, '2025-11-12 10:19:54', '2025-11-12 18:19:54'),
(32, 1, 0, 0, '2025-11-12 10:22:28', '2025-11-12 18:22:28'),
(33, 1, 0, 0, '2025-11-12 10:22:28', '2025-11-12 18:22:28'),
(34, 1, 0, 0, '2025-11-12 10:22:40', '2025-11-12 18:22:40'),
(35, 1, 0, 0, '2025-11-12 10:22:40', '2025-11-12 18:22:40'),
(36, 1, 24, 17.24137931034483, '2025-11-12 10:22:55', '2025-11-12 18:22:55'),
(37, 1, 24, 17.24137931034483, '2025-11-12 10:22:55', '2025-11-12 18:22:55'),
(38, 1, 0, 0, '2025-11-12 10:25:44', '2025-11-12 18:25:44'),
(39, 1, 0, 0, '2025-11-12 10:25:49', '2025-11-12 18:25:49'),
(40, 1, 14.4, 5.042016806722689, '2025-11-12 10:26:04', '2025-11-12 18:26:04'),
(41, 1, 0, 0, '2025-11-12 10:26:12', '2025-11-12 18:26:12'),
(42, 1, 0, 0, '2025-11-12 10:27:33', '2025-11-12 18:27:33'),
(43, 1, 0, 0, '2025-11-12 10:27:39', '2025-11-12 18:27:39'),
(44, 1, 0, 0, '2025-11-12 10:29:13', '2025-11-12 18:29:13'),
(45, 1, 0, 0, '2025-11-12 10:29:22', '2025-11-12 18:29:22'),
(46, 1, 0, 0, '2025-11-12 10:29:22', '2025-11-12 18:29:22'),
(47, 1, 0, 0, '2025-11-12 10:29:32', '2025-11-12 18:29:32'),
(48, 1, 0, 0, '2025-11-12 10:29:32', '2025-11-12 18:29:32'),
(49, 1, 45.49999999999999, 85.84905660377359, '2025-11-12 10:31:24', '2025-11-12 18:31:24'),
(50, 1, 45.49999999999999, 85.84905660377359, '2025-11-12 10:31:24', '2025-11-12 18:31:24'),
(51, 1, 0, 0, '2025-11-12 10:31:44', '2025-11-12 18:31:44'),
(52, 1, 0, 0, '2025-11-12 10:32:15', '2025-11-12 18:32:15');

-- --------------------------------------------------------

--
-- Table structure for table `soal`
--

CREATE TABLE `soal` (
  `soal_id` int(11) NOT NULL,
  `data` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `soal`
--

INSERT INTO `soal` (`soal_id`, `data`, `created_at`) VALUES
(1, 'Ku tahu semua akan berakhir, tapi ku tak rela melepaskanmu. Kau tanya mengapa aku tak ingin pergi darimu, dan mulutku diam membisu.', '2025-11-12 10:02:22'),
(2, 'Detik indah di pulang sekolah, di siang lewat pukul dua belas. Basah tubuhku terguyur hujan, bersama berdua tertawa bahagia.', '2025-11-12 10:02:22'),
(3, 'Tlah banyak cara Tuhan menghadirkan cinta. Namun engkau datang di saat yang tidak tepat, cintaku telah dimiliki.', '2025-11-12 10:02:22'),
(4, 'Di antara lampu kota yang redup, ku pulang membawa rindu. Langkah kecilku menyusuri hujan, berharap namamu jatuh di setiap tetes yang mengalun.', '2025-11-12 10:18:43'),
(5, 'Kau bilang, \'tenang saja\'—tapi matamu bercerita lain. Malam merangkum jarak, dan jam dinding tak sabar menghitung detik yang menggigil.', '2025-11-12 10:18:43'),
(6, 'Angin memutar kisah yang tak selesai. Kita menukar tawa dengan diam, lalu pergi tanpa salam, seperti senja yang lupa kembali.', '2025-11-12 10:18:43'),
(7, 'Aku menulis namamu di kaca berembun, lalu menghapusnya pelan-pelan. Biar rindu belajar sabar, biar hati menemukan tempat pulang.', '2025-11-12 10:18:43'),
(8, 'Tak perlu kata-kata megah; cukup sapa yang hangat. Kopi di meja mendingin, tapi harapan tetap mengepul di dada yang tak lelah.', '2025-11-12 10:18:43'),
(9, 'Jika esok adalah lagu, biarlah kuketik nadanya sekarang. Kita salah nada berkali-kali, namun tetap seirama saat jatuh dan bangun.', '2025-11-12 10:18:43'),
(10, 'Jalan pulang sering berkelok, tapi cahaya kecil itu setia. Ia menunggu di ujung gelap, memanggil lirih: \'Sudah jauh, tapi belum terlambat.\'', '2025-11-12 10:18:43'),
(11, 'Ada yang patah tapi tumbuh, ada yang hilang tapi utuh. Kita belajar tertawa lagi, mengikat luka seperti pita di hadiah yang rapuh.', '2025-11-12 10:18:43'),
(12, 'Malam ini kuajak sepi berdamai. Kita duduk di balkon, menghitung bintang yang malu-malu, sambil menata kalimat yang belum sempat diucap.', '2025-11-12 10:18:43'),
(13, 'Kabar baik tak selalu riuh; kadang berbisik dari jendela. Kau menoleh, senyummu singkat—cukup untuk menenangkan badai di kepala.', '2025-11-12 10:18:43'),
(14, 'Detik mendesis di kulit waktu, mengajakku bergegas. Namun aku memilih pelan, sebab yang indah sering datang saat kita tak terburu-buru.', '2025-11-12 10:18:43'),
(15, 'Tak semua pintu harus diketuk. Ada yang cukup kau tatap, lalu ia mengerti: bahwa hati pulang bukan karena dipanggil, melainkan karena dipahami.', '2025-11-12 10:18:43');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password_hash` char(64) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password_hash`, `created_at`) VALUES
(1, 'alfathmav', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', '2025-11-11 07:55:40');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `scores`
--
ALTER TABLE `scores`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `soal`
--
ALTER TABLE `soal`
  ADD PRIMARY KEY (`soal_id`);
ALTER TABLE `soal` ADD FULLTEXT KEY `ft_data` (`data`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `scores`
--
ALTER TABLE `scores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `soal`
--
ALTER TABLE `soal`
  MODIFY `soal_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `scores`
--
ALTER TABLE `scores`
  ADD CONSTRAINT `scores_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
