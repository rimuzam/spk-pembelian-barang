package com.spk.application.form.Subcriteria;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.connection.Connections;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class FormsubCriteria extends javax.swing.JPanel {
    
    public static String selectedKriteria; // Variable to hold the selected kriteria
    DefaultTableModel tableModel; // Table model for displaying sub-criteria
    
    public FormsubCriteria() {
        initComponents();
        applyTableStyle(tableSubCriteria);
        loadKriteriaToComboBox(); 
        
        // Update the table model with proper titles and types
       tableModel = new DefaultTableModel(new Object[]{"", "Kode Sub", "Nama Sub", "Nilai"}, 0) {
        Class[] types = new Class[] {
            Boolean.class,  // First column for checkbox
            String.class,   // Second column for Kode
            String.class,   // Third column for Nama
            Double.class    // Fourth column for Bobot
        };

        // Only the checkbox column is editable
        boolean[] canEdit = new boolean[] {
            true,  // Checkbox column is editable
            false, // Kode column is not editable
            false, // Nama column is not editable
            false  // Bobot column is not editable
        };

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }
    };

        tableSubCriteria.setModel(tableModel);
    }
    
    private void loadKriteriaToComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Tambahkan item tidak dapat dipilih
        model.addElement("Pilih Kriteria"); // Item dummy

        // Menghubungkan ke database
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spk_pembelian", "root", "");
            Statement stmt = conn.createStatement();
            String sql = "SELECT kode_kriteria, nama_kriteria FROM kriteria";
            ResultSet rs = stmt.executeQuery(sql);

            // Mengisi data ke dalam ComboBox
            while (rs.next()) {
                String kodeKriteria = rs.getString("kode_kriteria");
                String namaKriteria = rs.getString("nama_kriteria");
                model.addElement(kodeKriteria + " - " + namaKriteria);
            }

            jComboBox1.setModel(model);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set action listener untuk jComboBox1
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (jComboBox1.getSelectedItem().equals("Pilih Kriteria")) {
                    // Jika "Pilih Kriteria" dipilih, tidak melakukan apa-apa
                    jComboBox1.setSelectedIndex(0); // Set kembali ke "Pilih Kriteria"
                } else {
                    jComboBox1ActionPerformed(evt); // Jalankan metode jika bukan "Pilih Kriteria"
                }
            }
        });
    }

    
    private void loadSubCriteria(String kodeKriteria) {
        // Kosongkan data yang ada di model tabel
        tableModel.setRowCount(0);

        try {
            Connection conn = Connections.getConnection();
            String sql = "SELECT kode_sub, nama_sub, sub_nilai FROM sub_criteria WHERE kode_kriteria = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kodeKriteria);

            ResultSet rs = stmt.executeQuery();

            // Debugging output
            System.out.println("Menjalankan query: " + sql + " dengan kode_kriteria: " + kodeKriteria);

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(this, "Tidak ada sub-kriteria untuk kriteria ini.");
            }

            // Iterasi hasil dan tambahkan ke model tabel
            while (rs.next()) {
                String kodeSub = rs.getString("kode_sub");
                String namaSub = rs.getString("nama_sub");
                double subNilai = rs.getDouble("sub_nilai");

                // Tambahkan baris baru ke model tabel dengan checkbox
                tableModel.addRow(new Object[]{false, kodeSub, namaSub, subNilai}); // First element as false for unchecked
            }

            // Tutup sumber daya
            rs.close();
            stmt.close();
            Connections.closeConnection(conn);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading sub-criteria: " + e.getMessage());
        }
    }

     private void applyTableStyle(JTable table) {
        cmdAdd.setIcon(new FlatSVGIcon("icon/svg/add.svg", 0.35f));
        cmdUpdate.setIcon(new FlatSVGIcon("icon/svg/edit.svg", 0.35f));
        cmdDelete.setIcon(new FlatSVGIcon("icon/svg/delete.svg", 0.35f));

        // Change scroll style
        JScrollPane scroll = (JScrollPane) table.getParent().getParent();
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Table.background;"
                + "track:$Table.background;"
                + "trackArc:999");

        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");
        table.putClientProperty(FlatClientProperties.STYLE_CLASS, "table_style");

        // Create table alignment
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

                    // Set horizontal alignment for the "Nilai" column (index 3)
                    if (column == 3) { // Kolom Nilai adalah kolom ke-3 (indeks 0)
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        label.setHorizontalAlignment(SwingConstants.CENTER); // Set default untuk kolom lain
                    }

                    // Maintain default foreground color for all cells
                    if (isSelected) {
                        com.setForeground(table.getSelectionForeground());
                    } else {
                        com.setForeground(table.getForeground());
                    }
                }
                return com;
            }
        };
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        cmdAdd = new javax.swing.JButton();
        cmdUpdate = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSubCriteria = new javax.swing.JTable();

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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        crazyPanel2.add(jComboBox1);

        cmdAdd.setText("Tambah");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        crazyPanel2.add(cmdAdd);

        cmdUpdate.setText("Ubah");
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });
        crazyPanel2.add(cmdUpdate);

        cmdDelete.setText("Hapus");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        crazyPanel2.add(cmdDelete);

        crazyPanel1.add(crazyPanel2);

        tableSubCriteria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Kode", "Nama", "Nilai"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableSubCriteria);

        crazyPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // Get selected item from jComboBox1
        if (jComboBox1.getSelectedItem() != null) {
            selectedKriteria = jComboBox1.getSelectedItem().toString(); // Update the selectedKriteria variable
            String kodeKriteria = selectedKriteria.split(" - ")[0]; // Get kode_kriteria from selection

            // Load the sub-criteria for the selected kriteria
            loadSubCriteria(kodeKriteria);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        String selectedKriteria = jComboBox1.getSelectedItem().toString();
        String kodeKriteria = selectedKriteria.split(" - ")[0]; // Get the selected kriteria code
        addSubCriteria addSubCriteriaForm = new addSubCriteria(kodeKriteria); // Pass the selected kriteria code
        addSubCriteriaForm.setVisible(true);
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed
        // Mengambil indeks baris yang dipilih
        int selectedRow = tableSubCriteria.getSelectedRow();

        // Memeriksa apakah ada baris yang dipilih
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih sub-kriteria yang ingin diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mengambil kode sub-kriteria dari kolom kedua (kode)
        String kodeSub = tableModel.getValueAt(selectedRow, 1).toString();
        String namaSub = tableModel.getValueAt(selectedRow, 2).toString();
        double nilaiSub = (double) tableModel.getValueAt(selectedRow, 3); // Ambil nilai sub-kriteria

        // Membuka form untuk mengubah sub-kriteria
        changeSubCriteria updateForm = new changeSubCriteria(kodeSub, namaSub, nilaiSub); // Membuat instance form update
        updateForm.setVisible(true);

        // Setelah form update ditutup, Anda mungkin ingin memuat ulang sub-kriteria
        updateForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Memuat ulang sub-kriteria setelah update
                loadSubCriteria(jComboBox1.getSelectedItem().toString().split(" - ")[0]);
            }
        });
    }//GEN-LAST:event_cmdUpdateActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        int selectedRow = tableSubCriteria.getSelectedRow();
 
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih sub-kriteria yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String kodeSub = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus sub-kriteria dengan kode: " + kodeSub + "?", "Konfirmasi Penghapusan", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection conn = Connections.getConnection();
                String sql = "DELETE FROM sub_criteria WHERE kode_sub = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, kodeSub);
                int rowsAffected = stmt.executeUpdate();

  
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Sub-kriteria berhasil dihapus.");

                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus sub-kriteria. Kode tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                }


                stmt.close();
                Connections.closeConnection(conn);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saat menghapus sub-kriteria: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdUpdate;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableSubCriteria;
    // End of variables declaration//GEN-END:variables
}
