package com.spk.application.form.alternative;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class FormAlternative extends javax.swing.JPanel {

    public FormAlternative() {
        initComponents();
        applyTableStyle(tableAlternative);
        loadAlternatives();
        jLabel1.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
    }
    
     private void loadAlternatives() {
        DefaultTableModel model = (DefaultTableModel) tableAlternative.getModel();
        model.setRowCount(0); 

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spk_pembelian", "root", "");

            // Query to select existing alternatives
            String query = "SELECT kode_alternatif, nama_alternatif FROM alternatif ORDER BY kode_alternatif ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String kodeAlternatif = rs.getString("kode_alternatif");
                String namaAlternatif = rs.getString("nama_alternatif");
                model.addRow(new Object[]{false, kodeAlternatif, namaAlternatif, "", ""});
            }

            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    private void applyTableStyle(JTable table) {

        cmdAdd.setIcon(new FlatSVGIcon("icon/svg/add.svg", 0.35f));
        cmdUpdate.setIcon(new FlatSVGIcon("icon/svg/edit.svg", 0.35f));
        cmdDelete.setIcon(new FlatSVGIcon("icon/svg/delete.svg", 0.35f));

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

                    if (header == false) {
                        // Cek apakah value bukan null dan bukan kosong
                        if (value != null && !value.toString().isEmpty()) {
                            try {
                                if (column == 4) {
                                    double val = Double.parseDouble(value.toString());
                                    if (val > 0) {
                                        com.setForeground(new Color(17, 182, 60));
                                        label.setText("+" + val);
                                    } else {
                                        com.setForeground(new Color(202, 48, 48));
                                    }
                                }
                            } catch (NumberFormatException e) {
                                // Jika terjadi error parsing, bisa diabaikan atau beri nilai default
                                com.setForeground(table.getForeground());
                            }
                        } else {
                            com.setForeground(table.getForeground());  // Default jika nilai kosong
                        }
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
        jLabel1 = new javax.swing.JLabel();
        cmdAdd = new javax.swing.JButton();
        cmdUpdate = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAlternative = new javax.swing.JTable();

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        jLabel1.setText("Alternatif");
        crazyPanel2.add(jLabel1);

        cmdAdd.setText("Tambah");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        crazyPanel2.add(cmdAdd);

        cmdUpdate.setText("Ubah");
        crazyPanel2.add(cmdUpdate);

        cmdDelete.setText("Hapus");
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        crazyPanel2.add(cmdDelete);

        crazyPanel1.add(crazyPanel2);

        tableAlternative.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Kode Alternatif", "Nama Alternatif"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableAlternative);

        crazyPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(crazyPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        // TODO add your handling code here:
        addAlternative a = new addAlternative();
        a.setVisible(true);

    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        // Get the selected row index
        int selectedRow = tableAlternative.getSelectedRow();

        // Check if a row is selected
        if (selectedRow != -1) {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus alternatif ini?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Get the kode_alternatif from the selected row
                int kodeAlternatif = (int) tableAlternative.getValueAt(selectedRow, 1);

                try {
                    // Connect to the database
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spk_pembelian", "root", "");

                    // Prepare the delete query
                    String deleteQuery = "DELETE FROM alternatif WHERE kode_alternatif = ?";
                    PreparedStatement ps = conn.prepareStatement(deleteQuery);
                    ps.setInt(1, kodeAlternatif);

                    // Execute the delete statement
                    int rowsDeleted = ps.executeUpdate();

                    if (rowsDeleted > 0) {
                        // Remove the row from the table model
                        DefaultTableModel model = (DefaultTableModel) tableAlternative.getModel();
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(this, "Alternatif berhasil dihapus.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Kesalahan: Alternatif tidak dapat dihapus.", "Hapus Kesalahan", JOptionPane.ERROR_MESSAGE);
                    }

                    // Close the connection
                    conn.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an alternative to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdUpdate;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableAlternative;
    // End of variables declaration//GEN-END:variables
}
