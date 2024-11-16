package com.spk.application.form.Decision;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Component;
import com.spk.application.form.Calculation.FormCalculation;
import com.spk.asset.ReportCompiler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
public class FormDecision extends javax.swing.JPanel {

    public FormDecision() {
        initComponents();
        applyTableStyle(tableKeputusan);
        loadDataToTable();
    }
    
    private void applyTableStyle(JTable table) {
        cmdPrint.setIcon(new FlatSVGIcon("icon/svg/print.svg", 0.35f));
        
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
    
     private void loadDataToTable() {
        String url = "jdbc:mysql://localhost:3306/spk_pembelian"; // Adjust your database URL
        String user = "root"; // Database username
        String password = ""; // Database password

        DefaultTableModel model = (DefaultTableModel) tableKeputusan.getModel();
        model.setRowCount(0); // Clear the table before adding new data

        // Get preference data
        Map<String, Double> preferensiMap = new FormCalculation().getPreferensi();

        // Sort preferences in descending order
        LinkedHashMap<String, Double> sortedPreferensiMap = preferensiMap.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));

        int rank = 1;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Clear the "keputusan" table before inserting new data
            String clearQuery = "DELETE FROM keputusan";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(clearQuery);
            }

            // Insert sorted data into the database and table
            String insertQuery = "INSERT INTO keputusan (nama_alternatif, nilai_preferensi, ranking) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                for (Map.Entry<String, Double> entry : sortedPreferensiMap.entrySet()) {
                    String namaAlternatif = entry.getKey();
                    double nilaiPreferensi = entry.getValue();

                    // Insert the data into the database
                    ps.setString(1, namaAlternatif);
                    ps.setDouble(2, nilaiPreferensi);
                    ps.setInt(3, rank);
                    ps.addBatch(); // Add to batch for efficiency

                    // Add the data to the table model
                    model.addRow(new Object[]{namaAlternatif, nilaiPreferensi, rank});
                    rank++;
                }
                ps.executeBatch(); // Execute all batched insert operations
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions properly
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        cmdPrint = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableKeputusan = new javax.swing.JTable();

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
        jLabel1.setText("Keputusan");
        crazyPanel2.add(jLabel1);

        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });
        crazyPanel2.add(cmdPrint);

        crazyPanel1.add(crazyPanel2);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Hasil AKhir");
        crazyPanel1.add(jLabel2);

        tableKeputusan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Alternatif", "Nilai Preferensi", "Ranking"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableKeputusan);

        crazyPanel1.add(jScrollPane1);

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

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        new ReportCompiler().print("Decision.jasper");
    }//GEN-LAST:event_cmdPrintActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdPrint;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableKeputusan;
    // End of variables declaration//GEN-END:variables
}
