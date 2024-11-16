package com.spk.application.form.Calculation;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Ridho Multazam
 */
public class FormCalculation extends javax.swing.JPanel {

    public FormCalculation() {
        initComponents();
        applyTableStyle(tableMatrixKeputusanX);
        applyTableStyle(tableNormalisasiMatrixX);
        applyTableStyle(tablePerhitungan);
        loadNormalisasiMatrixX();
        loadMatrixKeputusanX();
        loadTablePerhitungan();

    }
    
    private void applyTableStyle(JTable table) {
        //  Change scroll style
        JScrollPane scroll = (JScrollPane) table.getParent().getParent();
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Table.background;"
                + "track:$Table.background;"
                + "trackArc:999");

        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
        table.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

        //  To Create table alignment
        table.getTableHeader().setDefaultRenderer(getAlignmentCellRender(table.getTableHeader().getDefaultRenderer(), true));
        table.setDefaultRenderer(Object.class, getAlignmentCellRender(table.getDefaultRenderer(Object.class), false));
    }
    
    private TableCellRenderer getAlignmentCellRender(TableCellRenderer oldRender, boolean header) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component com = oldRender.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (com instanceof JLabel) {
                    JLabel label = (JLabel) com;

                    // Center-align all columns
                    label.setHorizontalAlignment(SwingConstants.CENTER);

                   
                }
                return com;
            }
        };
    }
    
     private void loadMatrixKeputusanX() {
        DefaultTableModel model = (DefaultTableModel) tableMatrixKeputusanX.getModel();
        model.setRowCount(0); // Menghapus semua baris sebelumnya

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/spk_pembelian", "root", "");
             Statement stmt = conn.createStatement()) {

            // Query untuk mendapatkan kriteria unik
            String queryKriteria = "SELECT kode_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            List<String> listKriteria = new ArrayList<>();

            while (rsKriteria.next()) {
                listKriteria.add(rsKriteria.getString("kode_kriteria"));
            }

            // Menambahkan kolom sesuai kriteria ke JTable
            for (String kriteria : listKriteria) {
                model.addColumn(kriteria);
            }

            // Query untuk mengambil data penilaian
            String query = "SELECT p.kode_alternatif, p.kode_kriteria, p.nilai_sub " +
                           "FROM penilaian p " +
                           "ORDER BY p.kode_alternatif, p.kode_kriteria";
            ResultSet rs = stmt.executeQuery(query);

            // Membuat map untuk mengelompokkan nilai berdasarkan alternatif
            Map<String, Map<String, Double>> dataMap = new LinkedHashMap<>();

            while (rs.next()) {
                String alternatif = rs.getString("kode_alternatif");
                String kriteria = rs.getString("kode_kriteria");
                Double nilaiSub = rs.getDouble("nilai_sub");

                dataMap.putIfAbsent(alternatif, new LinkedHashMap<>());
                dataMap.get(alternatif).put(kriteria, nilaiSub);
            }

            // Menyusun data ke dalam tabel
            int no = 1;
            for (Map.Entry<String, Map<String, Double>> entry : dataMap.entrySet()) {
                String alternatif = entry.getKey();
                Map<String, Double> nilaiSubMap = entry.getValue();

                Object[] row = new Object[listKriteria.size() + 2];
                row[0] = no++; // Nomor
                row[1] = alternatif; // Nama Alternatif

                for (int i = 0; i < listKriteria.size(); i++) {
                    String kriteria = listKriteria.get(i);
                    row[i + 2] = nilaiSubMap.getOrDefault(kriteria, 0.0); // Default 0.0 jika tidak ada nilai
                }

                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     
    private void loadNormalisasiMatrixX() {
        DefaultTableModel model = (DefaultTableModel) tableNormalisasiMatrixX.getModel();
        model.setRowCount(0); // Menghapus semua baris sebelumnya

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/spk_pembelian", "root", "");
             Statement stmt = conn.createStatement()) {

            // Query untuk mendapatkan kriteria unik
            String queryKriteria = "SELECT kode_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            List<String> listKriteria = new ArrayList<>();

            while (rsKriteria.next()) {
                listKriteria.add(rsKriteria.getString("kode_kriteria"));
            }

            // Menambahkan kolom sesuai kriteria ke JTable
            for (String kriteria : listKriteria) {
                model.addColumn(kriteria);
            }

            // Query untuk mengambil data penilaian
            String query = "SELECT p.kode_alternatif, p.kode_kriteria, p.nilai_sub " +
                           "FROM penilaian p " +
                           "ORDER BY p.kode_alternatif, p.kode_kriteria";
            ResultSet rs = stmt.executeQuery(query);

            // Membuat map untuk mengelompokkan nilai berdasarkan alternatif
            Map<String, Map<String, Double>> dataMap = new LinkedHashMap<>();
            Map<String, Double> maxValues = new LinkedHashMap<>();
            Map<String, Double> minValues = new LinkedHashMap<>();

            while (rs.next()) {
                String alternatif = rs.getString("kode_alternatif");
                String kriteria = rs.getString("kode_kriteria");
                Double nilaiSub = rs.getDouble("nilai_sub");

                dataMap.putIfAbsent(alternatif, new LinkedHashMap<>());
                dataMap.get(alternatif).put(kriteria, nilaiSub);

                // Cari nilai maksimum untuk normalisasi
                maxValues.put(kriteria, Math.max(maxValues.getOrDefault(kriteria, Double.MIN_VALUE), nilaiSub));
                // Cari nilai minimum untuk normalisasi
                minValues.put(kriteria, Math.min(minValues.getOrDefault(kriteria, Double.MAX_VALUE), nilaiSub));
            }

            // Hapus data lama dari tabel `normalisasi`
            String deleteQuery = "DELETE FROM normalisasi";
            stmt.executeUpdate(deleteQuery);

            // Siapkan query untuk menyimpan hasil normalisasi
            String insertQuery = "INSERT INTO normalisasi (kode_alternatif, kode_kriteria, nilai_normalisasi) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);

            // Menyusun data normalisasi ke dalam tabel dan menyimpannya ke database
            int no = 1;
            for (Map.Entry<String, Map<String, Double>> entry : dataMap.entrySet()) {
                String alternatif = entry.getKey();
                Map<String, Double> nilaiSubMap = entry.getValue();

                Object[] row = new Object[listKriteria.size() + 2];
                row[0] = no++; // Nomor
                row[1] = alternatif; // Nama Alternatif

                for (int i = 0; i < listKriteria.size(); i++) {
                    String kriteria = listKriteria.get(i);
                    Double nilaiSub = nilaiSubMap.getOrDefault(kriteria, 0.0);
                    Double maxValue = maxValues.getOrDefault(kriteria, 1.0);
                    Double minValue = minValues.getOrDefault(kriteria, 0.0);

                    // Normalisasi Min-Max
                    double nilaiNormalisasi = 0.0;
                    if (maxValue != minValue) { // Hindari pembagian dengan 0
                        nilaiNormalisasi = (nilaiSub - minValue) / (maxValue - minValue);
                    }

                    row[i + 2] = nilaiNormalisasi;

                    // Simpan hasil normalisasi ke database
                    pstmt.setString(1, alternatif);
                    pstmt.setString(2, kriteria);
                    pstmt.setDouble(3, nilaiNormalisasi);
                    pstmt.addBatch();
                }

                model.addRow(row);
            }

            // Eksekusi batch penyimpanan data
            pstmt.executeBatch();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private void loadTablePerhitungan() {
        DefaultTableModel model = (DefaultTableModel) tablePerhitungan.getModel();
        model.setRowCount(0); // Hapus semua data sebelumnya

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/spk_pembelian", "root", "");
             Statement stmt = conn.createStatement()) {

            // Dapatkan kriteria dan bobot
            String queryKriteria = "SELECT kode_kriteria, bobot_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            Map<String, Double> bobotKriteria = new LinkedHashMap<>();
            while (rsKriteria.next()) {
                bobotKriteria.put(rsKriteria.getString("kode_kriteria"), rsKriteria.getDouble("bobot_kriteria"));
            }

            // Dapatkan data normalisasi dari tabel `normalisasi`
            String queryNormalisasi = "SELECT kode_alternatif, kode_kriteria, nilai_normalisasi FROM normalisasi ORDER BY kode_alternatif, kode_kriteria";
            ResultSet rsNormalisasi = stmt.executeQuery(queryNormalisasi);
            Map<String, Map<String, Double>> normalisasiMap = new LinkedHashMap<>();

            while (rsNormalisasi.next()) {
                String alternatif = rsNormalisasi.getString("kode_alternatif");
                String kriteria = rsNormalisasi.getString("kode_kriteria");
                double nilaiNormalisasi = rsNormalisasi.getDouble("nilai_normalisasi");

                normalisasiMap.putIfAbsent(alternatif, new LinkedHashMap<>());
                normalisasiMap.get(alternatif).put(kriteria, nilaiNormalisasi);
            }

            // Hitung preferensi
            int no = 1;
            for (Map.Entry<String, Map<String, Double>> entry : normalisasiMap.entrySet()) {
                String alternatif = entry.getKey();
                Map<String, Double> normalisasi = entry.getValue();
                double totalPreferensi = 0.0;
                StringBuilder perhitunganBuilder = new StringBuilder();

                for (String kriteria : bobotKriteria.keySet()) {
                    double nilaiNormalisasi = normalisasi.getOrDefault(kriteria, 0.0);
                    double bobot = bobotKriteria.getOrDefault(kriteria, 0.0);
                    double kontribusi = nilaiNormalisasi * bobot;

                    perhitunganBuilder.append(String.format("(%.2f * %.2f) + ", nilaiNormalisasi, bobot));
                    totalPreferensi += kontribusi;
                }

                // Hapus "+ " terakhir
                String perhitungan = perhitunganBuilder.toString();
                if (perhitungan.endsWith(" + ")) {
                    perhitungan = perhitungan.substring(0, perhitungan.length() - 3);
                }

                model.addRow(new Object[]{
                    no++,
                    alternatif,
                    perhitungan,
                    String.format("%.2f", totalPreferensi)
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public Map<String, Double> getPreferensi() {
        Map<String, Double> preferensiMap = new LinkedHashMap<>();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/spk_pembelian", "root", "");
             Statement stmt = conn.createStatement()) {

            // Dapatkan semua kriteria dan bobotnya
            String queryKriteria = "SELECT kode_kriteria, bobot_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);

            Map<String, Double> bobotKriteria = new LinkedHashMap<>();
            while (rsKriteria.next()) {
                String kodeKriteria = rsKriteria.getString("kode_kriteria");
                double bobot = rsKriteria.getDouble("bobot_kriteria");
                bobotKriteria.put(kodeKriteria, bobot);
            }

            // Dapatkan nilai normalisasi dari tabel `normalisasi`
            String queryNormalisasi = """
                    SELECT kode_alternatif, kode_kriteria, nilai_normalisasi 
                    FROM normalisasi
                    ORDER BY kode_alternatif, kode_kriteria
                    """;

            ResultSet rsNormalisasi = stmt.executeQuery(queryNormalisasi);
            Map<String, Map<String, Double>> normalisasiMap = new LinkedHashMap<>();

            while (rsNormalisasi.next()) {
                String alternatif = rsNormalisasi.getString("kode_alternatif");
                String kriteria = rsNormalisasi.getString("kode_kriteria");
                double nilaiNormalisasi = rsNormalisasi.getDouble("nilai_normalisasi");

                normalisasiMap.putIfAbsent(alternatif, new LinkedHashMap<>());
                normalisasiMap.get(alternatif).put(kriteria, nilaiNormalisasi);
            }

            // Proses perhitungan preferensi
            for (Map.Entry<String, Map<String, Double>> entry : normalisasiMap.entrySet()) {
                String alternatif = entry.getKey();
                Map<String, Double> normalisasi = entry.getValue();

                double totalPreferensi = 0.0;
                for (Map.Entry<String, Double> normalisasiEntry : normalisasi.entrySet()) {
                    String kriteria = normalisasiEntry.getKey();
                    double nilaiNormalisasi = normalisasiEntry.getValue();
                    double bobot = bobotKriteria.getOrDefault(kriteria, 0.0);
                    double kontribusi = nilaiNormalisasi * bobot;

                    totalPreferensi += kontribusi;
                }

                preferensiMap.put(alternatif, totalPreferensi);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return preferensiMap;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMatrixKeputusanX = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableNormalisasiMatrixX = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablePerhitungan = new javax.swing.JTable();

        crazyPanel1.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background;[light]border:0,0,0,0,shade(@background,5%),,20;[dark]border:0,0,0,0,tint(@background,5%),,20",
            null
        ));
        crazyPanel1.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap,fill,insets 15",
            "[fill]",
            "[grow 0][fill]",
            null
        ));

        crazyPanel2.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Search;background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
            }
        ));
        crazyPanel2.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        jLabel1.setText("Perhitungan");
        crazyPanel2.add(jLabel1);

        crazyPanel1.add(crazyPanel2);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Matrix Keputusan X");
        crazyPanel1.add(jLabel2);

        tableMatrixKeputusanX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No ", "Nama Alternatif"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableMatrixKeputusanX);

        crazyPanel1.add(jScrollPane1);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Normalisasi Matrix X");
        crazyPanel1.add(jLabel3);

        tableNormalisasiMatrixX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No ", "Nama Alternatif"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableNormalisasiMatrixX);

        crazyPanel1.add(jScrollPane2);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Perkalian Matrix Normalisasi dengan Bobot Kriteria");
        crazyPanel1.add(jLabel4);

        tablePerhitungan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No ", "Nama Alternatif", "Perhitungan", "Total Nilai Preferensi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablePerhitungan);

        crazyPanel1.add(jScrollPane3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tableMatrixKeputusanX;
    private javax.swing.JTable tableNormalisasiMatrixX;
    private javax.swing.JTable tablePerhitungan;
    // End of variables declaration//GEN-END:variables
}
