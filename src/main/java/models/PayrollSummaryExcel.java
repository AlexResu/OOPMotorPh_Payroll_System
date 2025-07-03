/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.awt.Desktop;
import java.io.File;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class PayrollSummaryExcel {
    public static void generatePayrollReport(List<Payroll> payrollList, File fileToSave) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Monthly Payroll Report");

        // Define a simple date format
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        // Create Header Font
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 12);
        
        // Create Header Cell Style
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);  // Use old constant
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  // Use old constant
        headerStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); // Use HSSFCellStyle
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);  // Old way
        
        // Currency Style
        CellStyle currencyStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        currencyStyle.setDataFormat(format.getFormat("₱#,##0.00"));
        
        // Create Header Row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Employee No", "Employee Full Name", "Position", "Department", 
                            "Gross Income", "SSS No", "SSS Contribution", 
                            "PhilHealth No", "PhilHealth Contribution", 
                            "Pag-Ibig No", "Pag-Ibig Contribution", 
                            "TIN", "Withholding Tax", "Net Pay"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        // Populate Data
        int rowNum = 1;
        for (Payroll payroll : payrollList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(payroll.getEmployee().getEmployeeID());
            row.createCell(1).setCellValue(
                    payroll.getEmployee().getFirstName()+ " " 
                    + payroll.getEmployee().getLastName());
            row.createCell(2).setCellValue(payroll.getEmployee().getPosition());
            row.createCell(3).setCellValue(payroll.getEmployee().getDepartment());

            // Apply currency formatting
            Cell grossIncomeCell = row.createCell(4);
            grossIncomeCell.setCellValue(payroll.getGrossIncome());
            grossIncomeCell.setCellStyle(currencyStyle);

            row.createCell(5).setCellValue(payroll.getEmployee().getSssNumber());
            Cell sssCell = row.createCell(6);
            sssCell.setCellValue(payroll.getDeductions().getSssDeduction());
            sssCell.setCellStyle(currencyStyle);

            row.createCell(7).setCellValue(payroll.getEmployee().getPhilhealthNumber());
            Cell philHealthCell = row.createCell(8);
            philHealthCell.setCellValue(payroll.getDeductions().getPagIbigDeduction());
            philHealthCell.setCellStyle(currencyStyle);

            row.createCell(9).setCellValue(payroll.getEmployee().getPagibigNumber());
            Cell pagibigCell = row.createCell(10);
            pagibigCell.setCellValue(payroll.getDeductions().getPagIbigDeduction());
            pagibigCell.setCellStyle(currencyStyle);

            row.createCell(11).setCellValue(payroll.getEmployee().getTinNumber());
            Cell taxCell = row.createCell(12);
            taxCell.setCellValue(payroll.getDeductions().getTaxDeduction());
            taxCell.setCellStyle(currencyStyle);

            Cell netPayCell = row.createCell(13);
            netPayCell.setCellValue(payroll.getNetPay());
            netPayCell.setCellStyle(currencyStyle);
        }

         // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
            workbook.write(fileOut);
            System.out.println("File saved: " + fileToSave.getAbsolutePath());

            // Automatically open the file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fileToSave);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
