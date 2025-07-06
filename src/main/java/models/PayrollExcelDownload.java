package models;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PayrollExcelDownload {
    public static void exportPayrollToExcel(Payroll payroll, File file) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Employee Payslip");
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Add Payslip Header
            createPayslipHeader(sheet, headerStyle);

            // Add Employee Details
            createEmployeeDetails(sheet, payroll, dateFormat);

            // Add Earnings Section
            int rowNum = createEarningsSection(sheet, payroll, headerStyle);

            // Add Benefits Section
            rowNum = createBenefitsSection(sheet, payroll, headerStyle, rowNum);

            // Add Deductions Section
            rowNum = createDeductionsSection(sheet, payroll, headerStyle, rowNum);

            // Add Summary Section
            createSummarySection(sheet, payroll, headerStyle, rowNum);

            // Auto-size columns
            for (int i = 1; i <= 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save file
            saveFile(workbook, file);
        } catch (IOException e) {
            System.err.println("Error exporting payroll: " + e.getMessage());
        }
    }

    private static void saveFile(Workbook workbook, File file) throws IOException {
        if (!file.getName().toLowerCase().endsWith(".xlsx")) {
            file = new File(file.getAbsolutePath() + ".xlsx");
        }

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            System.out.println("Payroll exported successfully to: " + file.getAbsolutePath());
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private static void createPayslipHeader(Sheet sheet, CellStyle headerStyle) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(2);
        titleCell.setCellValue("EMPLOYEE PAYSLIP");
        titleCell.setCellStyle(headerStyle);
    }

    private static void createEmployeeDetails(Sheet sheet, Payroll payroll, SimpleDateFormat dateFormat) {
        createRowWithValues(sheet, 2, "PAYSLIP NO", payroll.getPayrollID(), "PERIOD START DATE", dateFormat.format(payroll.getWeekPeriodStart()));
        createRowWithValues(sheet, 3, "EMPLOYEE ID", payroll.getEmployee().getEmployeeID(), "PERIOD END DATE", dateFormat.format(payroll.getWeekPeriodEnd()));
        createRowWithValues(sheet, 4, "EMPLOYEE NAME", payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName(),
                "EMPLOYEE POSITION/DEPARTMENT", payroll.getEmployee().getPosition() + " / " + payroll.getEmployee().getDepartment());
    }

    private static int createEarningsSection(Sheet sheet, Payroll payroll, CellStyle headerStyle) {
        int rowNum = 6;
        createSectionHeader(sheet, rowNum++, "EARNINGS", headerStyle);
        createRowWithValues(sheet, rowNum++, "Monthly Rate:", "₱" + payroll.getEmployee().getBenefits().getBasicSalary());
        createRowWithValues(sheet, rowNum++, "Hourly Rate:", "₱" + payroll.getEmployee().getBenefits().getHourlyRate());
        createRowWithValues(sheet, rowNum++, "Hours Worked: ", payroll.getHoursWorked());
        createRowWithValues(sheet, rowNum++, "Overtime: ", payroll.getOvertimeHours());
        createRowWithValues(sheet, rowNum++, "GROSS INCOME", "₱" + payroll.getGrossIncome());
        return rowNum;
    }

    private static int createBenefitsSection(Sheet sheet, Payroll payroll, CellStyle headerStyle, int rowNum) {
        createSectionHeader(sheet, rowNum++, "BENEFITS", headerStyle);
        createRowWithValues(sheet, rowNum++, "Rice Subsidy:", "₱" + payroll.getEmployee().getBenefits().getRiceSubsidy());
        createRowWithValues(sheet, rowNum++, "Phone Allowance:", "₱" + payroll.getEmployee().getBenefits().getPhoneAllowance());
        createRowWithValues(sheet, rowNum++, "Clothing Allowance:", "₱" + payroll.getEmployee().getBenefits().getClothingAllowance());
        createRowWithValues(sheet, rowNum++, "TOTAL", "₱" + payroll.getEmployee().getBenefits().calculateTotalAllowance());
        return rowNum;
    }

    private static int createDeductionsSection(Sheet sheet, Payroll payroll, CellStyle headerStyle, int rowNum) {
        createSectionHeader(sheet, rowNum++, "DEDUCTIONS", headerStyle);
        createRowWithValues(sheet, rowNum++, "Social Security System:", "₱" + payroll.getDeductions().getSssDeduction());
        createRowWithValues(sheet, rowNum++, "PhilHealth:", "₱" + payroll.getDeductions().getPhilhealthDeduction());
        createRowWithValues(sheet, rowNum++, "Pag-ibig:", "₱" + payroll.getDeductions().getPagIbigDeduction());
        createRowWithValues(sheet, rowNum++, "Withholding Tax:", "₱" + payroll.getDeductions().getTaxDeduction());
        createRowWithValues(sheet, rowNum++, "TOTAL DEDUCTIONS", "₱" + payroll.getDeductions().calculateTotalDeductions());
        return rowNum;
    }

    private static void createSummarySection(Sheet sheet, Payroll payroll, CellStyle headerStyle, int rowNum) {
        createSectionHeader(sheet, rowNum++, "SUMMARY", headerStyle);
        createSingleCellRow(sheet, rowNum++, "Gross Income: ₱" + payroll.getGrossIncome());
        createSingleCellRow(sheet, rowNum++, "Benefits: ₱" + payroll.getEmployee().getBenefits().calculateTotalAllowance());
        createSingleCellRow(sheet, rowNum++, "Deductions: ₱" + payroll.getDeductions().calculateTotalDeductions());
        createRowWithValues(sheet, rowNum++, "TAKE HOME PAY", "₱" + payroll.getNetPay());
    }

    private static void saveFile(Workbook workbook) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Payroll Report");
        fileChooser.setSelectedFile(new File("Payslip.xlsx"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Payroll exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Open file after saving
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fileToSave);
                }
            }
        }
    }

    // Utility Methods for Creating Rows
    private static void createRowWithValues(Sheet sheet, int rowNum, String label1, Object value1, String label2, Object value2) {
        Row row = sheet.createRow(rowNum);
        row.createCell(1).setCellValue(label1);
        row.createCell(2).setCellValue(String.valueOf(value1));
        row.createCell(4).setCellValue(label2);
        row.createCell(5).setCellValue(String.valueOf(value2));
    }

    private static void createRowWithValues(Sheet sheet, int rowNum, String label, Object value) {
        Row row = sheet.createRow(rowNum);
        row.createCell(1).setCellValue(label);
        row.createCell(2).setCellValue(String.valueOf(value));
    }

    private static void createSingleCellRow(Sheet sheet, int rowNum, String value) {
        sheet.createRow(rowNum).createCell(1).setCellValue(value);
    }

    private static void createSectionHeader(Sheet sheet, int rowNum, String title, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        row.createCell(1).setCellValue(title);
        row.getCell(1).setCellStyle(style);
    }
}
