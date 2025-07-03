/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.sql.SQLException; // Import SQLException for connection handling
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * .a
 *
 * @author kyleg
 */
public class ReportGenerator {

    /**
     * Generates a JasperReport and exports it to a specified file path,
     * using a custom Session object to obtain the database connection.
     *
     * @param session Your custom Session object, which is expected to provide a java.sql.Connection.
     * @param jrxmlFilePath The path to the JasperReports XML (JRXML) file.
     * @param parameters A map of parameters to pass to the report. Can be null if no parameters.
     * @param outputFilePath The path where the generated report (e.g., PDF) should be saved.
     * @param viewReport If true, the report will be displayed in a JasperViewer after generation.
     */
    public void generateReport(Object session, String jrxmlFilePath, Map<String, Object> parameters, String outputFilePath, boolean viewReport) {
        Connection connection = null; // Initialize connection to null

        try {
            // --- IMPORTANT: How to get a Connection from your 'session' object ---
            // This is a placeholder. You need to replace this line with the actual
            // logic to extract a java.sql.Connection from your 'session' object.
            // Example: If your session object has a method like 'getConnection()':
            // connection = ((YourCustomSessionClass) session).getConnection();
            // Or if it's passed directly as a Connection:
            // connection = (Connection) session;

            // For demonstration, I'll assume 'session' IS the Connection object if you truly aren't using Hibernate.
            // If not, you MUST replace this with how your 'session' object yields a Connection.
            if (session instanceof Connection) {
                connection = (Connection) session;
            } else {
                // If your 'session' is a different type and you can get a Connection from it,
                // replace this with the appropriate casting and method call.
                // For example:
                // connection = ((YourActualSessionClass) session).getJdbcConnection(); // Replace YourActualSessionClass
                throw new IllegalArgumentException("The 'session' object provided is not a java.sql.Connection and its type is unknown for extracting one.");
            }

            // 1. Compile the JRXML report
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilePath);

            // 2. Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // 3. Export the report to a specified output format (e.g., PDF)
            if (outputFilePath != null && !outputFilePath.isEmpty()) {
                File outputFile = new File(outputFilePath);
                try (OutputStream os = new FileOutputStream(outputFile)) {
                    JasperExportManager.exportReportToPdfStream(jasperPrint, os);
                    System.out.println("Report generated successfully and saved to: " + outputFile.getAbsolutePath());
                }
            }

            // 4. Optionally view the report
            if (viewReport) {
                JasperViewer.viewReport(jasperPrint, false);
            }

        } catch (JRException e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Configuration Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        // Note: It's usually the caller's responsibility to manage the Connection's lifecycle (opening/closing).
        // If the 'session' object is responsible for managing the connection, you might not close it here.
        // If 'session' *is* the Connection and you're responsible, you might want to close it in a finally block
        // if it was opened specifically for this report generation.
    }

    // --- Example Usage ---
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();

        // **IMPORTANT:** Replace with your actual paths and database connection/session details
        String jrxmlPath = "C:\\Users\\kyleg\\Downloads\\OOPMotorPh_Payroll_System-main\\OOPMotorPh_Payroll_System-main\\src\\main\\java\\resources\\Jaspersoft\\EmployeeReports.jrxml";
        String outputPath = "C:\\Users\\kyleg\\Downloads\\EmployeeReport.pdf"; // Where the PDF will be saved

        Map<String, Object> params = new HashMap<>();
        // Add any parameters your report needs, e.g.:
        // params.put("reportTitle", "MotorPh Employee List");

        // --- Custom Session Object Placeholder ---
        // REPLACE THIS WITH YOUR ACTUAL SESSION OBJECT OR DATABASE CONNECTION LOGIC
        // If your 'Session' object directly provides a Connection:
        // Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdb", "user", "password");
        // Object customSession = myConnection; // Passing the Connection directly as the 'session' object for this example

        // For a more realistic scenario where 'session' wraps a connection:
        // You would have a class like:
        // class MyCustomDbSession {
        //     private Connection conn;
        //     public MyCustomDbSession(Connection conn) { this.conn = conn; }
        //     public Connection getConnection() { return conn; }
        //     public void close() throws SQLException { if (conn != null) conn.close(); }
        // }
        // And then:
        // Connection rawConnection = null;
        // MyCustomDbSession customSession = null;
        try {
        
            // (Replace with your actual database URL, username, and password)
            Connection rawConnection = java.sql.DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root", "root", "motorph_db2025");
            
            // In the specific case, we are not using Hibernate,
            // and you pass a 'Session session', if that 'session' object *is*
            // the Connection itself, then you can just pass the Connection.
            // If your 'Session' is a custom wrapper, you need to instantiate it.
            // For this example, let's just pass the raw Connection directly as the 'session' parameter.
            Object customSession = rawConnection; // <-- THIS IS THE KEY PART FOR YOUR SCENARIO

            boolean viewAfterGeneration = true;

            generator.generateReport(customSession, jrxmlPath, params, outputPath, viewAfterGeneration);

            // IMPORTANT: Close the connection in a real application
            if (rawConnection != null && !rawConnection.isClosed()) {
                rawConnection.close();
                System.out.println("Database connection closed.");
            }

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An error occurred in main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
