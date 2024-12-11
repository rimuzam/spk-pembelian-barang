package com.spk.application.form.Calculation;

import com.formdev.flatlaf.FlatClientProperties;
import com.spk.connection.Connections;
import java.awt.Component;
import java.sql.Connection;
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
        applyTableStyle(tableMinmaxMatrixX);
        applyTableStyle(tableNormalisasiMatrixX);
        applyTableStyle(tablePerhitungan);
        loadNormalisasiMatrixX();
        loadMinmaxMatrixX();
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
        model.setRowCount(0); 
        model.setColumnCount(0); 

        try (Connection conn = Connections.getConnection(); 
         Statement stmt = conn.createStatement())  {

            String queryKriteria = "SELECT kode_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            List<String> listKriteria = new ArrayList<>();

            while (rsKriteria.next()) {
                listKriteria.add(rsKriteria.getString("kode_kriteria"));
            }

            model.addColumn("Nama Alternatif");
            for (String kriteria : listKriteria) {
                model.addColumn(kriteria);
            }

            String query = "SELECT p.kode_alternatif, p.kode_kriteria, p.nilai_sub " +
                           "FROM penilaian p " +
                           "ORDER BY p.kode_alternatif, p.kode_kriteria";
            ResultSet rs = stmt.executeQuery(query);

            Map<String, Map<String, Double>> dataMap = new LinkedHashMap<>();

            while (rs.next()) {
                String alternatif = rs.getString("kode_alternatif");
                String kriteria = rs.getString("kode_kriteria");
                Double nilaiSub = rs.getDouble("nilai_sub");

                dataMap.putIfAbsent(alternatif, new LinkedHashMap<>());
                dataMap.get(alternatif).put(kriteria, nilaiSub);
            }

            for (Map.Entry<String, Map<String, Double>> entry : dataMap.entrySet()) {
                String alternatif = entry.getKey();
                Map<String, Double> nilaiSubMap = entry.getValue();

                Object[] row = new Object[listKriteria.size() + 1];
                row[0] = alternatif; 

                for (int i = 0; i < listKriteria.size(); i++) {
                    String kriteria = listKriteria.get(i);
                    row[i + 1] = nilaiSubMap.getOrDefault(kriteria, 0.0); 
                }

                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     
    private void loadMinmaxMatrixX() {
        DefaultTableModel model = (DefaultTableModel) tableMinmaxMatrixX.getModel();
        model.setRowCount(0); // Clear previous rows

         try (Connection conn = Connections.getConnection(); 
         Statement stmt = conn.createStatement()){

            String queryKriteria = "SELECT kode_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            List<String> listKriteria = new ArrayList<>();

            while (rsKriteria.next()) {
                listKriteria.add(rsKriteria.getString("kode_kriteria"));
            }

            String[] columns = new String[listKriteria.size() + 1];
            columns[0] = "Nama";
            for (int i = 0; i < listKriteria.size(); i++) {
                columns[i + 1] = listKriteria.get(i);
            }
            model.setColumnIdentifiers(columns);

            String query = "SELECT p.kode_kriteria, p.nilai_sub FROM penilaian p ORDER BY p.kode_kriteria";
            ResultSet rs = stmt.executeQuery(query);

            Map<String, List<Double>> nilaiMap = new LinkedHashMap<>();

            while (rs.next()) {
                String kriteria = rs.getString("kode_kriteria");
                Double nilaiSub = rs.getDouble("nilai_sub");

                nilaiMap.putIfAbsent(kriteria, new ArrayList<>());
                nilaiMap.get(kriteria).add(nilaiSub);
            }

            model.addRow(new Object[] { "Minimum" });
            model.addRow(new Object[] { "Maximize" });

            for (int i = 0; i < listKriteria.size(); i++) {
                String kriteria = listKriteria.get(i);
                List<Double> nilaiSubList = nilaiMap.get(kriteria);

                if (nilaiSubList != null && !nilaiSubList.isEmpty()) {
                    Double minValue = nilaiSubList.stream().min(Double::compare).orElse(Double.NaN);
                    Double maxValue = nilaiSubList.stream().max(Double::compare).orElse(Double.NaN);

                    model.setValueAt(minValue, 0, i + 1);  

                    model.setValueAt(maxValue, 1, i + 1);  
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void loadNormalisasiMatrixX() {
        DefaultTableModel model = (DefaultTableModel) tableNormalisasiMatrixX.getModel();
        model.setRowCount(0); 
        model.setColumnCount(0);

        try (Connection conn = Connections.getConnection(); 
         Statement stmt = conn.createStatement()){

            String queryKriteria = "SELECT kode_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            List<String> listKriteria = new ArrayList<>();

            while (rsKriteria.next()) {
                listKriteria.add(rsKriteria.getString("kode_kriteria"));
            }

            model.addColumn("Nama Alternatif");
            for (String kriteria : listKriteria) {
                model.addColumn(kriteria);
            }

            String query = "SELECT p.kode_alternatif, p.kode_kriteria, p.nilai_sub " +
                           "FROM penilaian p " +
                           "ORDER BY p.kode_alternatif, p.kode_kriteria";
            ResultSet rs = stmt.executeQuery(query);

            Map<String, Map<String, Double>> dataMap = new LinkedHashMap<>();
            Map<String, Double> maxValues = new LinkedHashMap<>();
            Map<String, Double> minValues = new LinkedHashMap<>();

            while (rs.next()) {
                String alternatif = rs.getString("kode_alternatif");
                String kriteria = rs.getString("kode_kriteria");
                Double nilaiSub = rs.getDouble("nilai_sub");

                dataMap.putIfAbsent(alternatif, new LinkedHashMap<>());
                dataMap.get(alternatif).put(kriteria, nilaiSub);

                maxValues.put(kriteria, Math.max(maxValues.getOrDefault(kriteria, Double.MIN_VALUE), nilaiSub));
                minValues.put(kriteria, Math.min(minValues.getOrDefault(kriteria, Double.MAX_VALUE), nilaiSub));
            }

            for (Map.Entry<String, Map<String, Double>> entry : dataMap.entrySet()) {
                String alternatif = entry.getKey();
                Map<String, Double> nilaiSubMap = entry.getValue();

                Object[] row = new Object[listKriteria.size() + 1];
                row[0] = alternatif; 

                for (int i = 0; i < listKriteria.size(); i++) {
                    String kriteria = listKriteria.get(i);
                    Double nilaiSub = nilaiSubMap.getOrDefault(kriteria, 0.0);
                    Double maxValue = maxValues.getOrDefault(kriteria, 1.0);
                    Double minValue = minValues.getOrDefault(kriteria, 0.0);

                    double nilaiNormalisasi = 0.0;
                    if (maxValue != minValue) { 
                        nilaiNormalisasi = (nilaiSub - minValue) / (maxValue - minValue);
                    }

                    row[i + 1] = nilaiNormalisasi;
                }

                model.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadTablePerhitungan() {
        DefaultTableModel model = (DefaultTableModel) tablePerhitungan.getModel();

        if (tablePerhitungan.getColumnCount() > 0 && tablePerhitungan.getColumnName(0).equalsIgnoreCase("No")) {
            tablePerhitungan.removeColumn(tablePerhitungan.getColumnModel().getColumn(0));
        }

        model.setRowCount(0); 
            try (Connection conn = Connections.getConnection(); 
             Statement stmt = conn.createStatement()){

            String queryKriteria = "SELECT kode_kriteria, bobot_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);
            Map<String, Double> bobotKriteria = new LinkedHashMap<>();
            while (rsKriteria.next()) {
                bobotKriteria.put(rsKriteria.getString("kode_kriteria"), rsKriteria.getDouble("bobot_kriteria"));
            }

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

                String perhitungan = perhitunganBuilder.toString();
                if (perhitungan.endsWith(" + ")) {
                    perhitungan = perhitungan.substring(0, perhitungan.length() - 3);
                }

                model.addRow(new Object[]{
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

         try (Connection conn = Connections.getConnection(); 
             Statement stmt = conn.createStatement()){

            String queryKriteria = "SELECT kode_kriteria, bobot_kriteria FROM kriteria ORDER BY kode_kriteria";
            ResultSet rsKriteria = stmt.executeQuery(queryKriteria);

            Map<String, Double> bobotKriteria = new LinkedHashMap<>();
            while (rsKriteria.next()) {
                String kodeKriteria = rsKriteria.getString("kode_kriteria");
                double bobot = rsKriteria.getDouble("bobot_kriteria");
                bobotKriteria.put(kodeKriteria, bobot);
            }

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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMatrixKeputusanX = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableMinmaxMatrixX = new javax.swing.JTable();
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
            "wrap,fill,insets 10",
            "[grow,fill]",
            "[grow,fill]",
            new String[]{
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            }
        ));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Matrix Keputusan X");
        crazyPanel1.add(jLabel2);

        tableMatrixKeputusanX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Alternatif"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableMatrixKeputusanX);

        crazyPanel1.add(jScrollPane1);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Minimum dan Maksimum Matrix X");
        crazyPanel1.add(jLabel5);

        tableMinmaxMatrixX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tableMinmaxMatrixX);

        crazyPanel1.add(jScrollPane4);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Normalisasi Matrix X");
        crazyPanel1.add(jLabel3);

        tableNormalisasiMatrixX.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Alternatif"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
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
                "Nama Alternatif", "Perhitungan", "Total Nilai Preferensi"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true
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
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tableMatrixKeputusanX;
    private javax.swing.JTable tableMinmaxMatrixX;
    private javax.swing.JTable tableNormalisasiMatrixX;
    private javax.swing.JTable tablePerhitungan;
    // End of variables declaration//GEN-END:variables
}
