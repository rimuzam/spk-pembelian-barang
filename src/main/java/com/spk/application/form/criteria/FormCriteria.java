package com.spk.application.form.criteria;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.connection.Connections;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
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
public class FormCriteria extends javax.swing.JPanel {

    public FormCriteria() {
        initComponents();
        applyTableStyle(tableCriteria);
        tittlePanel.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
        loadDataToTable();
    }
    
    // Method untuk memuat data dari database ke JTable
    private void loadDataToTable() {
        DefaultTableModel model = (DefaultTableModel) tableCriteria.getModel();
        model.setRowCount(0); // Mengosongkan tabel sebelum menambahkan data baru

        String query = "SELECT kode_kriteria, nama_kriteria, bobot_kriteria FROM kriteria";

        try (
            Connection conn = Connections.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String kode = rs.getString("kode_kriteria");
                String nama = rs.getString("nama_kriteria");
                double bobot = rs.getDouble("bobot_kriteria");

                model.addRow(new Object[]{false, kode, nama, bobot});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + e.getMessage());
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

                    // Set horizontal alignment for all columns to center
                    label.setHorizontalAlignment(SwingConstants.CENTER);

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
        tittlePanel = new javax.swing.JLabel();
        cmdAdd = new javax.swing.JButton();
        cmdUpdate = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCriteria = new javax.swing.JTable();

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

        tittlePanel.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        tittlePanel.setText("Kriteria");
        crazyPanel2.add(tittlePanel);

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

        tableCriteria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Kode", "Nama", "Bobot"
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
        jScrollPane1.setViewportView(tableCriteria);

        crazyPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 618, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        addCriteria a = new addCriteria();
        a.setVisible(true);
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed
        // TODO add your handling code here:
        int selectedRow = tableCriteria.getSelectedRow(); // Get the selected row index
        if (selectedRow != -1) { // Check if a row is selected
            String kodeKriteria = tableCriteria.getValueAt(selectedRow, 1).toString(); // Get the kode from the table
            String namaKriteria = tableCriteria.getValueAt(selectedRow, 2).toString(); // Get the nama from the table
            double bobotKriteria = (double) tableCriteria.getValueAt(selectedRow, 3); // Get the bobot from the table

            // Open the changeCriteria form and set the values
            changeCriteria changeForm = new changeCriteria();
            changeForm.setKodeKriteria(kodeKriteria);
            changeForm.setNamaKriteria(namaKriteria);
            changeForm.setBobotKriteria(bobotKriteria);
            changeForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih kriteria yang ingin diubah.");
        }
    }//GEN-LAST:event_cmdUpdateActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // TODO add your handling code here:
        deleteSelectedItems();
    }//GEN-LAST:event_cmdDeleteActionPerformed

    
    private void deleteSelectedItems() {
        DefaultTableModel model = (DefaultTableModel) tableCriteria.getModel();
        int rowCount = model.getRowCount();
        boolean itemsDeleted = false;

        // Menghapus baris yang dipilih
        for (int i = rowCount - 1; i >= 0; i--) {
            Boolean isSelected = (Boolean) model.getValueAt(i, 0); // Kolom checkbox
            if (isSelected != null && isSelected) {
                String kodeKriteria = model.getValueAt(i, 1).toString(); // Ambil kode kriteria

                // Hapus dari database
                deleteFromDatabase(kodeKriteria); // Tambahkan metode ini untuk menghapus dari database

                model.removeRow(i); // Menghapus baris dari model tabel
                itemsDeleted = true;
            }
        }

        if (itemsDeleted) {
            javax.swing.JOptionPane.showMessageDialog(this, "Item yang dipilih berhasil dihapus.");
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Tidak ada item yang dipilih untuk dihapus.");
        }
    }
    
    private void deleteFromDatabase(String kodeKriteria) {
        String query = "DELETE FROM kriteria WHERE kode_kriteria = ?";
        try {
            Connection conn = Connections.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, kodeKriteria);
            stmt.executeUpdate();

            // Menutup koneksi
            stmt.close();
            Connections.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage());
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdUpdate;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableCriteria;
    private javax.swing.JLabel tittlePanel;
    // End of variables declaration//GEN-END:variables
}
