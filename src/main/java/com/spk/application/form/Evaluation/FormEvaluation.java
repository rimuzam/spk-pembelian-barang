package com.spk.application.form.Evaluation;

import com.formdev.flatlaf.FlatClientProperties;
import com.spk.connection.Connections;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Ridho Multazam
 */
public class FormEvaluation extends javax.swing.JPanel {
    
    private final Map<String, JPanel> criteriaPanels = new HashMap<>();
    private final Map<String, Map<String, String>> alternativeData = new HashMap<>();

    public FormEvaluation() {
        
        initComponents();
        makeCrazyPanelTransparent();
        loadAlternatives();
        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
    }
    
  private void loadAlternatives() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Pilih alternatif");

        String sql = "SELECT kode_alternatif, nama_alternatif FROM alternatif";

        try (
            Connection connection = Connections.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                String kodeAlternatif = rs.getString("kode_alternatif");
                String namaAlternatif = rs.getString("nama_alternatif");
                comboBoxModel.addElement(kodeAlternatif + " - " + namaAlternatif);
            }

            jComboBox1.setModel(comboBoxModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading alternatives: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Logging untuk debugging
        }
    }
    
    private void loadCriteriaAndSubCriteria(String kodeAlternatif) {
        if (criteriaPanels.containsKey(kodeAlternatif)) {
            crazyPanel3.removeAll();
            crazyPanel3.add(criteriaPanels.get(kodeAlternatif), BorderLayout.NORTH);
        } else {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false); 

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 5, 10, 5); 
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.gridx = 0;

            Map<String, String> criteriaData = new HashMap<>();

            try (Connection connection = Connections.getConnection();
                 PreparedStatement psKriteria = connection.prepareStatement("SELECT kode_kriteria, nama_kriteria, bobot_kriteria FROM kriteria");
                 ResultSet rsKriteria = psKriteria.executeQuery()) {

                int row = 0;
                while (rsKriteria.next()) {
                    String kodeKriteria = rsKriteria.getString("kode_kriteria");
                    String namaKriteria = rsKriteria.getString("nama_kriteria");
                    double bobotKriteria = rsKriteria.getDouble("bobot_kriteria");

                    JLabel label = new JLabel(namaKriteria + " (Bobot: " + bobotKriteria + ")");
                    label.setOpaque(false); 
                    gbc.gridy = row++;
                    gbc.anchor = GridBagConstraints.WEST;
                    panel.add(label, gbc);

                    JComboBox<Entry<String, String>> subCriteriaComboBox = new JComboBox<>();
                    subCriteriaComboBox.setOpaque(false); 
                    loadSubCriteriaWithCode(kodeKriteria, subCriteriaComboBox);

                    subCriteriaComboBox.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if (value instanceof Entry) {
                                setText(((Entry<String, String>) value).getValue()); 
                            }
                            return this;
                        }
                    });

                    gbc.gridy = row++;
                    panel.add(subCriteriaComboBox, gbc);

                    subCriteriaComboBox.addActionListener(e -> {
                        Entry<String, String> selectedEntry = (Entry<String, String>) subCriteriaComboBox.getSelectedItem();
                        if (selectedEntry != null) {
                            String kodeSub = selectedEntry.getKey();
                            String namaSub = selectedEntry.getValue();
                            criteriaData.put(kodeKriteria, kodeSub); 
                            System.out.println("Selected Kode Sub: " + kodeSub + ", Nama Sub: " + namaSub); // Debugging line
                        }
                    });
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading criteria: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }

            JButton btSave = new JButton("Simpan");
            btSave.setPreferredSize(new Dimension(50, 30));
            btSave.addActionListener(e -> {
                saveCriteriaData(kodeAlternatif, criteriaData);
            });
            gbc.gridy = 100; 
            panel.add(btSave, gbc);

            criteriaPanels.put(kodeAlternatif, panel);
            alternativeData.put(kodeAlternatif, criteriaData);

            crazyPanel3.removeAll();
            crazyPanel3.setLayout(new BorderLayout());
            crazyPanel3.add(panel, BorderLayout.NORTH);
        }

        crazyPanel3.revalidate();
        crazyPanel3.repaint();
    }

    
    private void loadSubCriteriaWithCode(String kodeKriteria, JComboBox<Entry<String, String>> subCriteriaComboBox) {
        try (Connection connection = Connections.getConnection();
             PreparedStatement psSub = connection.prepareStatement("SELECT kode_sub, nama_sub FROM sub_criteria WHERE kode_kriteria = ?")) {

            psSub.setString(1, kodeKriteria);
            ResultSet rsSub = psSub.executeQuery();

            subCriteriaComboBox.addItem(new SimpleEntry<>("", "Pilih sub-kriteria"));

            while (rsSub.next()) {
                String kodeSub = rsSub.getString("kode_sub");
                String namaSub = rsSub.getString("nama_sub");
                subCriteriaComboBox.addItem(new SimpleEntry<>(kodeSub, namaSub));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sub-criteria: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

   private void saveCriteriaData(String kodeAlternatif, Map<String, String> criteriaData) {
        for (Map.Entry<String, String> entry : criteriaData.entrySet()) {
            String kodeSub = entry.getValue();
            if (kodeSub == null || kodeSub.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua sub-kriteria harus dipilih!", 
                        "Kesalahan", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        }

        try (Connection connection = Connections.getConnection()) {
            System.out.println("Saving criteria data for: " + kodeAlternatif);

            for (Map.Entry<String, String> entry : criteriaData.entrySet()) {
                String kodeKriteria = entry.getKey();
                String kodeSub = entry.getValue();

                if (!isValidSubCriteria(kodeKriteria, kodeSub)) {
                    JOptionPane.showMessageDialog(this, "Sub-kriteria atau Kriteria tidak valid!", 
                            "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double nilaiSub = getSubCriteriaValue(kodeKriteria, kodeSub);
                double bobotKriteria = getCriterionWeight(kodeKriteria);
                double nilaiKriteria = nilaiSub * bobotKriteria;

                String updateSql = "UPDATE penilaian SET kode_sub = ?, nilai_sub = ?, bobot_kriteria = ?, nilai_kriteria = ? "
                                   + "WHERE kode_alternatif = ? AND kode_kriteria = ?";
                try (PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                    updatePs.setString(1, kodeSub);
                    updatePs.setDouble(2, nilaiSub);
                    updatePs.setDouble(3, bobotKriteria);
                    updatePs.setDouble(4, nilaiKriteria);
                    updatePs.setString(5, kodeAlternatif);
                    updatePs.setString(6, kodeKriteria);

                    int rowsUpdated = updatePs.executeUpdate();
                    if (rowsUpdated == 0) {
                        String insertSql = "INSERT INTO penilaian (kode_alternatif, kode_kriteria, kode_sub, nilai_sub, bobot_kriteria, nilai_kriteria) "
                                         + "VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertPs = connection.prepareStatement(insertSql)) {
                            insertPs.setString(1, kodeAlternatif);
                            insertPs.setString(2, kodeKriteria);
                            insertPs.setString(3, kodeSub);
                            insertPs.setDouble(4, nilaiSub);
                            insertPs.setDouble(5, bobotKriteria);
                            insertPs.setDouble(6, nilaiKriteria);
                            insertPs.executeUpdate();
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean isValidSubCriteria(String kodeKriteria, String kodeSub) {
        try (Connection connection = Connections.getConnection()) {
            String sql = "SELECT COUNT(*) FROM sub_criteria WHERE kode_kriteria = ? AND kode_sub = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, kodeKriteria);  
                ps.setString(2, kodeSub); 
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error checking sub-criteria validity: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    
    private double getSubCriteriaValue(String kodeKriteria, String kodeSub) {
        double nilaiSub = 0;
        try (Connection connection = Connections.getConnection()) {
            String sql = "SELECT sub_nilai FROM sub_criteria WHERE kode_kriteria = ? AND kode_sub = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, kodeKriteria);
                ps.setString(2, kodeSub);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nilaiSub = rs.getDouble("sub_nilai");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching sub-criteria value: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return nilaiSub;
    }

    private double getCriterionWeight(String kodeKriteria) {
        double bobotKriteria = 0;
        try (Connection connection = Connections.getConnection()) {
            String sql = "SELECT bobot_kriteria FROM kriteria WHERE kode_kriteria = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, kodeKriteria);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    bobotKriteria = rs.getDouble("bobot_kriteria");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching criterion weight: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return bobotKriteria;
    }

    
    private void makeCrazyPanelTransparent() {
        // Make crazyPanel3 background transparent
        crazyPanel3.setOpaque(false); // This makes the background fully transparent

        // Repaint and revalidate to apply transparency
        crazyPanel3.revalidate();
        crazyPanel3.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        lb = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();

        setPreferredSize(new java.awt.Dimension(806, 460));

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
        crazyPanel1.setPreferredSize(new java.awt.Dimension(482, 496));

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

        lb.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        lb.setText("Penilaian");
        crazyPanel2.add(lb);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        crazyPanel2.add(jComboBox1);

        crazyPanel1.add(crazyPanel2);

        crazyPanel3.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "[light]border:0,0,0,0,shade(@background,5%),,20;[dark]border:0,0,0,0,tint(@background,5%),,20;[light]background:shade(@background,2%);[dark]background:tint(@background,2%)",
            null
        ));
        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "wrap 2,fillx,insets 25",
            "[grow 0,trail]15[fill]",
            "",
            new String[]{
                "",
                "span 2,al trail"
            }
        ));
        crazyPanel1.add(crazyPanel3);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String selectedAlternative = (String) jComboBox1.getSelectedItem();
        if (!"Pilih alternatif".equals(selectedAlternative)) {
            String kodeAlternatif = selectedAlternative.split(" - ")[0];
            loadCriteriaAndSubCriteria(kodeAlternatif);
        } else {
            crazyPanel3.removeAll();
            crazyPanel3.revalidate();
            crazyPanel3.repaint();
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel lb;
    // End of variables declaration//GEN-END:variables
}
