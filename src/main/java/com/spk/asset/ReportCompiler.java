package com.spk.asset;

import com.spk.connection.Connections;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

public class ReportCompiler {
    private final Connection connection;

    public ReportCompiler() {
        this.connection = new Connections().getConnection();
    }

    public void print(String fileName) {
        print(fileName, null); // Memanggil overload dengan null untuk parameter
    }

    public void print(String fileName, HashMap<String, Object> parameters) {
    try {
        // File path in the resources folder
        String filePath = "jasper/" + fileName;

        // Load the report stream from the resources folder
        InputStream reportStream = getClass().getClassLoader().getResourceAsStream(filePath);
        
        // Check if the report stream is null (file not found)
        if (reportStream == null) {
            throw new Exception("Laporan tidak ditemukan di path: " + filePath);
        }

        // Fill the report with data and parameters
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, connection);
        JasperViewer jviewer = new JasperViewer(jasperPrint, false);
        jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jviewer.setVisible(true);

    } catch (Exception e) {
        // Handle errors and show a message to the user
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + e.getMessage());
    }
}


}
