/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.util.Map;

public class ReportGenerator {
    public boolean generateReport(
            Connection connection, 
            String source,
            String outputPath,
            Map<String, Object> parameters) {
        try {
            // Compile JRXML to Jasper file
            JasperReport jasperReport = JasperCompileManager.compileReport(source);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                connection
            );

            // View the report
//            JasperViewer.viewReport(jasperPrint, false);
            
            // Export to PDF (downloads/saves the report)
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            return true;
        } catch (Exception e) {
            System.err.println("Error encounter in generating report");
            e.printStackTrace();
        }
        return false;
    }
}
