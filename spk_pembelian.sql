-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2024 at 12:54 PM
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
-- Database: `spk_pembelian`
--

-- --------------------------------------------------------

--
-- Table structure for table `alternatif`
--

CREATE TABLE `alternatif` (
  `kode_alternatif` varchar(10) NOT NULL,
  `nama_alternatif` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `alternatif`
--

INSERT INTO `alternatif` (`kode_alternatif`, `nama_alternatif`) VALUES
('A1', 'Pemasok A'),
('A2', 'Pemasok B'),
('A3', 'Pemasok C');

-- --------------------------------------------------------

--
-- Table structure for table `keputusan`
--

CREATE TABLE `keputusan` (
  `id_keputusan` int(11) NOT NULL,
  `kode_alternatif` varchar(255) NOT NULL,
  `nilai_preferensi` decimal(5,2) NOT NULL,
  `ranking` int(11) NOT NULL,
  `nama_alternatif` varchar(255) DEFAULT 'Unknown'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `keputusan`
--

INSERT INTO `keputusan` (`id_keputusan`, `kode_alternatif`, `nilai_preferensi`, `ranking`, `nama_alternatif`) VALUES
(409, 'A1', 0.60, 1, 'Pemasok A'),
(410, 'A3', 0.60, 2, 'Pemasok C'),
(411, 'A2', 0.38, 3, 'Pemasok B');

-- --------------------------------------------------------

--
-- Table structure for table `kriteria`
--

CREATE TABLE `kriteria` (
  `kode_kriteria` varchar(10) NOT NULL,
  `nama_kriteria` varchar(100) NOT NULL,
  `bobot_kriteria` decimal(5,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kriteria`
--

INSERT INTO `kriteria` (`kode_kriteria`, `nama_kriteria`, `bobot_kriteria`) VALUES
('K1', 'Kualitas', 0.25),
('K2', 'Biaya', 0.30),
('K3', 'Ketahanan Pasokan', 0.15),
('K4', 'Keberlanjutan dan Dampak Lingkungan', 0.20),
('K5', 'Reputasi dan Keandalan Pemasok', 0.10);

-- --------------------------------------------------------

--
-- Table structure for table `normalisasi`
--

CREATE TABLE `normalisasi` (
  `id_normalisasi` int(11) NOT NULL,
  `kode_alternatif` varchar(10) DEFAULT NULL,
  `kode_kriteria` varchar(10) DEFAULT NULL,
  `nilai_normalisasi` decimal(10,4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `normalisasi`
--

INSERT INTO `normalisasi` (`id_normalisasi`, `kode_alternatif`, `kode_kriteria`, `nilai_normalisasi`) VALUES
(1246, 'A1', 'K1', 1.0000),
(1247, 'A1', 'K2', 0.0000),
(1248, 'A1', 'K3', 1.0000),
(1249, 'A1', 'K4', 1.0000),
(1250, 'A1', 'K5', 0.0000),
(1251, 'A2', 'K1', 0.5000),
(1252, 'A2', 'K2', 0.5000),
(1253, 'A2', 'K3', 0.0000),
(1254, 'A2', 'K4', 0.0000),
(1255, 'A2', 'K5', 1.0000),
(1256, 'A3', 'K1', 0.0000),
(1257, 'A3', 'K2', 1.0000),
(1258, 'A3', 'K3', 0.0000),
(1259, 'A3', 'K4', 1.0000),
(1260, 'A3', 'K5', 1.0000);

-- --------------------------------------------------------

--
-- Table structure for table `penilaian`
--

CREATE TABLE `penilaian` (
  `id_penilaian` int(11) NOT NULL,
  `kode_alternatif` varchar(10) NOT NULL,
  `kode_kriteria` varchar(10) NOT NULL,
  `kode_sub` varchar(20) DEFAULT NULL,
  `nilai_sub` decimal(5,2) NOT NULL,
  `bobot_kriteria` decimal(5,2) NOT NULL,
  `nilai_kriteria` decimal(5,2) NOT NULL,
  `nama_alternatif` varchar(100) DEFAULT NULL,
  `nama_kriteria` varchar(100) DEFAULT NULL,
  `nama_sub` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `penilaian`
--

INSERT INTO `penilaian` (`id_penilaian`, `kode_alternatif`, `kode_kriteria`, `kode_sub`, `nilai_sub`, `bobot_kriteria`, `nilai_kriteria`, `nama_alternatif`, `nama_kriteria`, `nama_sub`) VALUES
(1, 'A1', 'K1', 'S1K1', 0.40, 0.25, 0.10, 'Pemasok A', 'Kualitas', 'Kualitas Tinggi'),
(2, 'A1', 'K2', 'S2K2', 0.20, 0.30, 0.06, 'Pemasok A', 'Biaya', 'Menengah'),
(3, 'A1', 'K3', 'S1K3', 0.40, 0.15, 0.06, 'Pemasok A', 'Ketahanan Pasokan', 'Pasokan Tinggi'),
(4, 'A1', 'K4', 'S1K4', 0.40, 0.20, 0.08, 'Pemasok A', 'Keberlanjutan dan Dampak Lingkungan', 'Tinggi'),
(5, 'A1', 'K5', 'S3K5', 0.30, 0.10, 0.03, 'Pemasok A', 'Reputasi dan Keandalan Pemasok', 'Menengah'),
(6, 'A2', 'K1', 'S2K1', 0.30, 0.25, 0.08, 'Pemasok B', 'Kualitas', 'Kualitas Menengah'),
(7, 'A2', 'K2', 'S3K2', 0.30, 0.30, 0.09, 'Pemasok B', 'Biaya', 'Rendah'),
(8, 'A2', 'K3', 'S2K3', 0.30, 0.15, 0.05, 'Pemasok B', 'Ketahanan Pasokan', 'Pasokan Menengah'),
(9, 'A2', 'K4', 'S2K4', 0.30, 0.20, 0.06, 'Pemasok B', 'Keberlanjutan dan Dampak Lingkungan', 'Menengah'),
(10, 'A2', 'K5', 'S2K5', 0.40, 0.10, 0.04, 'Pemasok B', 'Reputasi dan Keandalan Pemasok', 'Tinggi'),
(11, 'A3', 'K1', 'S3K1', 0.20, 0.25, 0.05, 'Pemasok C', 'Kualitas', 'Kualitas Rendah'),
(12, 'A3', 'K2', 'S4K2', 0.40, 0.30, 0.12, 'Pemasok C', 'Biaya', 'Sangat Rendah'),
(13, 'A3', 'K3', 'S2K3', 0.30, 0.15, 0.05, 'Pemasok C', 'Ketahanan Pasokan', 'Pasokan Menengah'),
(14, 'A3', 'K4', 'S1K4', 0.40, 0.20, 0.08, 'Pemasok C', 'Keberlanjutan dan Dampak Lingkungan', 'Tinggi'),
(15, 'A3', 'K5', 'S1K5', 0.40, 0.10, 0.04, 'Pemasok C', 'Reputasi dan Keandalan Pemasok', 'Sangat Tinggi');

-- --------------------------------------------------------

--
-- Table structure for table `sub_criteria`
--

CREATE TABLE `sub_criteria` (
  `kode_sub` varchar(20) NOT NULL,
  `nama_sub` varchar(255) NOT NULL,
  `sub_nilai` decimal(10,2) NOT NULL,
  `kode_kriteria` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sub_criteria`
--

INSERT INTO `sub_criteria` (`kode_sub`, `nama_sub`, `sub_nilai`, `kode_kriteria`) VALUES
('S1K1', 'Kualitas Tinggi', 0.40, 'K1'),
('S1K2', 'Tinggi', 0.10, 'K2'),
('S1K3', 'Pasokan Tinggi', 0.40, 'K3'),
('S1K4', 'Tinggi', 0.40, 'K4'),
('S1K5', 'Sangat Tinggi', 0.40, 'K5'),
('S2K1', 'Kualitas Menengah', 0.30, 'K1'),
('S2K2', 'Menengah', 0.20, 'K2'),
('S2K3', 'Pasokan Menengah', 0.30, 'K3'),
('S2K4', 'Menengah', 0.30, 'K4'),
('S2K5', 'Tinggi', 0.30, 'K5'),
('S3K1', 'Kualitas Rendah', 0.20, 'K1'),
('S3K2', 'Rendah', 0.30, 'K2'),
('S3K3', 'Pasokan Rendah', 0.20, 'K3'),
('S3K4', 'Rendah', 0.20, 'K4'),
('S3K5', 'Menengah', 0.15, 'K5'),
('S4K1', 'Non-Metalurgi', 0.10, 'K1'),
('S4K2', 'Sangat Rendah', 0.40, 'K2'),
('S4K3', 'Pasokan sangat rendah', 0.10, 'K3'),
('S4K4', 'Sangat Rendah', 0.10, 'K4'),
('S4K5', 'Rendah', 0.10, 'K5'),
('S5K5', 'Sangat Rendah', 0.05, 'K5');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'admin', 'admin123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alternatif`
--
ALTER TABLE `alternatif`
  ADD PRIMARY KEY (`kode_alternatif`);

--
-- Indexes for table `keputusan`
--
ALTER TABLE `keputusan`
  ADD PRIMARY KEY (`id_keputusan`);

--
-- Indexes for table `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`kode_kriteria`);

--
-- Indexes for table `normalisasi`
--
ALTER TABLE `normalisasi`
  ADD PRIMARY KEY (`id_normalisasi`),
  ADD KEY `kode_alternatif` (`kode_alternatif`),
  ADD KEY `kode_kriteria` (`kode_kriteria`);

--
-- Indexes for table `penilaian`
--
ALTER TABLE `penilaian`
  ADD PRIMARY KEY (`id_penilaian`),
  ADD KEY `kode_alternatif` (`kode_alternatif`),
  ADD KEY `kode_kriteria` (`kode_kriteria`),
  ADD KEY `penilaian_ibfk_3` (`kode_sub`);

--
-- Indexes for table `sub_criteria`
--
ALTER TABLE `sub_criteria`
  ADD PRIMARY KEY (`kode_sub`),
  ADD KEY `fk_kode_kriteria` (`kode_kriteria`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `keputusan`
--
ALTER TABLE `keputusan`
  MODIFY `id_keputusan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=412;

--
-- AUTO_INCREMENT for table `normalisasi`
--
ALTER TABLE `normalisasi`
  MODIFY `id_normalisasi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1261;

--
-- AUTO_INCREMENT for table `penilaian`
--
ALTER TABLE `penilaian`
  MODIFY `id_penilaian` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `normalisasi`
--
ALTER TABLE `normalisasi`
  ADD CONSTRAINT `normalisasi_ibfk_1` FOREIGN KEY (`kode_alternatif`) REFERENCES `penilaian` (`kode_alternatif`),
  ADD CONSTRAINT `normalisasi_ibfk_2` FOREIGN KEY (`kode_kriteria`) REFERENCES `kriteria` (`kode_kriteria`);

--
-- Constraints for table `penilaian`
--
ALTER TABLE `penilaian`
  ADD CONSTRAINT `penilaian_ibfk_1` FOREIGN KEY (`kode_alternatif`) REFERENCES `alternatif` (`kode_alternatif`),
  ADD CONSTRAINT `penilaian_ibfk_2` FOREIGN KEY (`kode_kriteria`) REFERENCES `kriteria` (`kode_kriteria`),
  ADD CONSTRAINT `penilaian_ibfk_3` FOREIGN KEY (`kode_sub`) REFERENCES `sub_criteria` (`kode_sub`);

--
-- Constraints for table `sub_criteria`
--
ALTER TABLE `sub_criteria`
  ADD CONSTRAINT `fk_kode_kriteria` FOREIGN KEY (`kode_kriteria`) REFERENCES `kriteria` (`kode_kriteria`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
