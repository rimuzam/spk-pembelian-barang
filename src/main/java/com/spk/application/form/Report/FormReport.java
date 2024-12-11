package com.spk.application.form.Report;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.asset.ReportCompiler;
import java.awt.Color;

/**
 *
 * @author Ridho Multazam
 */
public class FormReport extends javax.swing.JPanel {

    public FormReport() {
        initComponents();
        
        
        cmdPrint1.setIcon(new FlatSVGIcon("icon/svg/print.svg", 0.35f));
        cmdPrint2.setIcon(new FlatSVGIcon("icon/svg/print.svg", 0.35f));
        cmdPrint3.setIcon(new FlatSVGIcon("icon/svg/print.svg", 0.35f));
        cmdPrint4.setIcon(new FlatSVGIcon("icon/svg/print.svg", 0.35f));
    }
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        crazyPanel1 = new raven.crazypanel.CrazyPanel();
        crazyPanel2 = new raven.crazypanel.CrazyPanel();
        jLabel1 = new javax.swing.JLabel();
        crazyPanel3 = new raven.crazypanel.CrazyPanel();
        jLabel3 = new javax.swing.JLabel();
        cmdPrint1 = new javax.swing.JButton();
        crazyPanel4 = new raven.crazypanel.CrazyPanel();
        jLabel4 = new javax.swing.JLabel();
        cmdPrint2 = new javax.swing.JButton();
        crazyPanel5 = new raven.crazypanel.CrazyPanel();
        jLabel5 = new javax.swing.JLabel();
        cmdPrint3 = new javax.swing.JButton();
        crazyPanel6 = new raven.crazypanel.CrazyPanel();
        jLabel6 = new javax.swing.JLabel();
        cmdPrint4 = new javax.swing.JButton();

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
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Laporan");
        jLabel1.setToolTipText("");
        crazyPanel2.add(jLabel1);

        crazyPanel1.add(crazyPanel2);

        crazyPanel3.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Search;background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
            }
        ));
        crazyPanel3.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Data SPK");
        crazyPanel3.add(jLabel3);

        cmdPrint1.setText("Cetak");
        cmdPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrint1ActionPerformed(evt);
            }
        });
        crazyPanel3.add(cmdPrint1);

        crazyPanel1.add(crazyPanel3);

        crazyPanel4.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Search;background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
            }
        ));
        crazyPanel4.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Penilaian");
        crazyPanel4.add(jLabel4);

        cmdPrint2.setText("Cetak");
        cmdPrint2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrint2ActionPerformed(evt);
            }
        });
        crazyPanel4.add(cmdPrint2);

        crazyPanel1.add(crazyPanel4);

        crazyPanel5.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Search;background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
            }
        ));
        crazyPanel5.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Perhitungan");
        crazyPanel5.add(jLabel5);

        cmdPrint3.setText("Cetak");
        cmdPrint3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrint3ActionPerformed(evt);
            }
        });
        crazyPanel5.add(cmdPrint3);

        crazyPanel1.add(crazyPanel5);

        crazyPanel6.setFlatLafStyleComponent(new raven.crazypanel.FlatLafStyleComponent(
            "background:$Table.background",
            new String[]{
                "JTextField.placeholderText=Search;background:@background",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1",
                "background:lighten(@background,8%);borderWidth:1"
            }
        ));
        crazyPanel6.setMigLayoutConstraints(new raven.crazypanel.MigLayoutConstraints(
            "",
            "[]push[][]",
            "",
            new String[]{
                "width 200"
            }
        ));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel6.setText("Keputusan");
        crazyPanel6.add(jLabel6);

        cmdPrint4.setText("Cetak");
        cmdPrint4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrint4ActionPerformed(evt);
            }
        });
        crazyPanel6.add(cmdPrint4);

        crazyPanel1.add(crazyPanel6);

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

    private void cmdPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrint1ActionPerformed
        new ReportCompiler().print("dataDSS.jasper");
    }//GEN-LAST:event_cmdPrint1ActionPerformed

    private void cmdPrint2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrint2ActionPerformed
        new ReportCompiler().print("Evaluation.jasper");
    }//GEN-LAST:event_cmdPrint2ActionPerformed

    private void cmdPrint3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrint3ActionPerformed
        new ReportCompiler().print("Calculation.jasper");
    }//GEN-LAST:event_cmdPrint3ActionPerformed

    private void cmdPrint4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrint4ActionPerformed
        new ReportCompiler().print("Decision.jasper");
    }//GEN-LAST:event_cmdPrint4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdPrint1;
    private javax.swing.JButton cmdPrint2;
    private javax.swing.JButton cmdPrint3;
    private javax.swing.JButton cmdPrint4;
    private raven.crazypanel.CrazyPanel crazyPanel1;
    private raven.crazypanel.CrazyPanel crazyPanel2;
    private raven.crazypanel.CrazyPanel crazyPanel3;
    private raven.crazypanel.CrazyPanel crazyPanel4;
    private raven.crazypanel.CrazyPanel crazyPanel5;
    private raven.crazypanel.CrazyPanel crazyPanel6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
