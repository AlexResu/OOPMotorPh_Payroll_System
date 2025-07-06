/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import models.AccountAccess;
import models.PayrollExcelDownload;
import utils.PayrollSummaryExcel;
import models.User;
import models.Employee;
import models.HRPersonnel;
import models.Benefits;
import models.Deductions;
import models.Payroll;
import dao.EmployeeDao;
import dao.HRPersonnelDao;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import utils.DbConnection;
import static utils.DbConnection.getConnection;
import utils.ReportGenerator;

/**
 *
 * @author Alex Resurreccion
 */
public class EmployeePayslip extends javax.swing.JPanel {
    private AccountAccess account;
    private User user;
    private Payroll payslip;
    private List<User> employees;
    /**
     * Creates new form EmployeePayslip
     */
    public EmployeePayslip() {
        initComponents();
    }
    
    public EmployeePayslip(AccountAccess account, User user) {
        this.account = account;
        this.user = user;
        initComponents();
        clearPayroll();
        populateWeekComboBox();
        if(user instanceof HRPersonnel){
            populateEmployeeList();
        } else {
            searchEmp.setVisible(false);
            searchEmpValue.setVisible(false);
        }
        noDataLabel.setVisible(false);
    }
    
    private void populateEmployeeList() {
        HRPersonnelDao hrPersonnelDao = new HRPersonnelDao();
        this.employees = hrPersonnelDao.loadEmployeeList();
        searchEmpValue.removeAllItems(); // Clear existing items
        searchEmpValue.addItem("Select Employee");
        for (User emp : employees) {
            searchEmpValue.addItem(
                    emp.getFirstName() + " " + emp.getLastName()); // Assuming Employee has a getName() method
        }
    }
    
    private void loadPayslip(){
        EmployeeDao employeeDao = new EmployeeDao();
        HRPersonnelDao hrPersonnelDao = new HRPersonnelDao();
        Date[] selectedDates = getSelectedBiMonthlyDates();
//        String status = (String) selectStatusValue.getSelectedItem();
        Payroll payroll = new Payroll();
        if(user instanceof Employee){
            System.out.println(user.getEmployeeID());
            payroll = employeeDao.loadPayslip(
                    user.getEmployeeID(), selectedDates[0], selectedDates[1]);
        } else {
            int selectedIndex = searchEmpValue.getSelectedIndex();
            if (selectedIndex > 0) {
                User selectedEmployee = employees.get(selectedIndex-1);
                payroll = hrPersonnelDao.loadPayslip(
                    selectedDates[0], selectedDates[1], 
                    (Employee) selectedEmployee);
            }
        }
        
        if(payroll != null){
            this.payslip = payroll;
            System.out.println("Employee ID" + payroll.getEmployee().getEmployeeID());
            download.setEnabled(true);
            noDataLabel.setVisible(false);
            mapPayrollView();
        } else {
            clearPayroll();
            download.setEnabled(false);
            noDataLabel.setVisible(true);
        }
    }
    
    private void clearPayroll(){
        payslipNumValue.setText("");
        empIdValue.setText("");
        empNameValue.setText("");
        periodStartValue.setText("");
        periodEndValue.setText("");
        empPositionValue.setText("");
        monthlyRatevalue.setText("");
        dailyRateValue.setText("");
        daysWorkedValue.setText("");
        overtimeValue.setText("");
        grossIncomeValue.setText("");
        riceSubsidyValue.setText("");
        phoneAllowValue.setText("");
        clothingAllowValue.setText("");
        totalBenefitsValue.setText("");
        sssDeductionvalue.setText("");
        philhealthDeductionValue.setText("");
        pagibigDeductionValue.setText("");
        taxDeductionValue.setText("");
        totalDeductionValue.setText("");
        sumGrossIncomeValue.setText("");
        sumBenefitsValue.setText("");
        sumDeductionValue.setText("");
        netPayValue.setText("");
    }
    
    private void mapPayrollView(){
        System.out.println("Employee ID2: " + this.payslip.getEmployee().getEmployeeID());
        Employee employee = this.payslip.getEmployee();
        Benefits benefit = employee.getBenefits();
        Deductions deduction = this.payslip.getDeductions();
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        String formattedStartDate = formatter.format(payslip.getWeekPeriodStart());
        String formattedEndDate = formatter.format(payslip.getWeekPeriodEnd());
        
        payslipNumValue.setText(this.payslip.getPayrollID());
        empIdValue.setText(Integer.toString(employee.getEmployeeID()));
        empNameValue.setText(
                employee.getFirstName() + " " + employee.getLastName());
        periodStartValue.setText(formattedStartDate);
        periodEndValue.setText(formattedEndDate);
        empPositionValue.setText(employee.getPosition());
        monthlyRatevalue.setText("P" + benefit.getBasicSalary());
        dailyRateValue.setText("P" + benefit.getHourlyRate());
        daysWorkedValue.setText("P" + this.payslip.getHoursWorked());
        overtimeValue.setText("P" + this.payslip.getOvertimeHours());
        grossIncomeValue.setText("P" + this.payslip.getGrossIncome());
        riceSubsidyValue.setText("P" + benefit.getRiceSubsidy());
        phoneAllowValue.setText("P" + benefit.getPhoneAllowance());
        clothingAllowValue.setText("P" + benefit.getClothingAllowance());
        totalBenefitsValue.setText("P" + benefit.calculateTotalAllowance());
        sssDeductionvalue.setText("P" + deduction.getSssDeduction());
        philhealthDeductionValue.setText("P" + deduction.getPhilhealthDeduction());
        pagibigDeductionValue.setText("P" + deduction.getPagIbigDeduction());
        taxDeductionValue.setText("P" + deduction.getTaxDeduction());
        totalDeductionValue.setText("P" + deduction.calculateTotalDeductions());
        sumGrossIncomeValue.setText("P" + this.payslip.getGrossIncome());
        sumBenefitsValue.setText("P" + benefit.calculateTotalAllowance());
        sumDeductionValue.setText("P" + deduction.calculateTotalDeductions());
        netPayValue.setText("P" + this.payslip.getNetPay());
    }
    
    private void populateWeekComboBox() {
        Map<String, String[]> valueMap = generateBiMonthlyValues();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Populate JComboBox with formatted week ranges
        model.addElement("Select bi-monthly range");
        for (String displayText : valueMap.keySet()) {
            model.addElement(displayText);
        }

        selectPeriodValue.setModel(model);
        selectPeriodValue.putClientProperty("valueMap", valueMap); // Store mapping

        // Set default selected value to the latest Monday
        if (model.getSize() > 0) {
            selectPeriodValue.setSelectedIndex(0);
        }
    }
    
    private Map<String, String[]> generateBiMonthlyValues() {
        Map<String, String[]> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
        Calendar calendar = Calendar.getInstance();

        // Generate past 6 months of bi-monthly periods (approx. 12 periods)
        for (int i = 0; i < 36; i++) {
            // Period 2: 16th to end of month
            int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            String start2 = sdf.format(getDate(calendar, 16));
            String end2 = sdf.format(getDate(calendar, lastDay));
            map.put(start2 + " - " + end2, new String[]{start2, end2});

            // Period 1: 1st to 15th
            String start1 = sdf.format(getDate(calendar, 1));
            String end1 = sdf.format(getDate(calendar, 15));
            map.put(start1 + " - " + end1, new String[]{start1, end1});

            // Move calendar back 1 month
            calendar.add(Calendar.MONTH, -1);
        }

        return map;
    }

    // Helper method to get a specific day of the current calendar month
    private Date getDate(Calendar baseCal, int day) {
        Calendar cal = (Calendar) baseCal.clone();
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }
    
    private Date[] getSelectedBiMonthlyDates() {
        String selectedText = (String) selectPeriodValue.getSelectedItem();
        Map<String, String[]> valueMap = (Map<String, String[]>) selectPeriodValue.getClientProperty("valueMap");

        if (valueMap != null && selectedText != null && valueMap.containsKey(selectedText)) {
            String[] dateRange = valueMap.get(selectedText); // Contains [startDate, endDate]
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");

            try {
                Date startDate = sdf.parse(dateRange[0]);
                Date endDate = sdf.parse(dateRange[1]);
                return new Date[]{startDate, endDate};
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane6 = new javax.swing.JScrollPane();
        employeePayslipPanel = new javax.swing.JPanel();
        empNameValue = new javax.swing.JLabel();
        monthlyRatevalue = new javax.swing.JLabel();
        payslipNumValue = new javax.swing.JLabel();
        periodStartValue = new javax.swing.JLabel();
        periodEndValue = new javax.swing.JLabel();
        empPositionValue = new javax.swing.JLabel();
        heading = new keeptoo.KGradientPanel();
        logo = new javax.swing.JLabel();
        periodStart = new javax.swing.JLabel();
        empIdValue = new javax.swing.JLabel();
        empId = new javax.swing.JLabel();
        empName = new javax.swing.JLabel();
        empPosition = new javax.swing.JLabel();
        periodEnd = new javax.swing.JLabel();
        earnings = new javax.swing.JLabel();
        payslipNum = new javax.swing.JLabel();
        monthlyRate = new javax.swing.JLabel();
        dailyRate = new javax.swing.JLabel();
        dailyRateValue = new javax.swing.JLabel();
        daysWorked = new javax.swing.JLabel();
        daysWorkedValue = new javax.swing.JLabel();
        overtime = new javax.swing.JLabel();
        overtimeValue = new javax.swing.JLabel();
        grossIncome = new javax.swing.JLabel();
        grossIncomeValue = new javax.swing.JLabel();
        benefits = new javax.swing.JLabel();
        riceSubsidy = new javax.swing.JLabel();
        riceSubsidyValue = new javax.swing.JLabel();
        phoneAllow = new javax.swing.JLabel();
        phoneAllowValue = new javax.swing.JLabel();
        clothingAllow = new javax.swing.JLabel();
        clothingAllowValue = new javax.swing.JLabel();
        totalBenefits = new javax.swing.JLabel();
        totalBenefitsValue = new javax.swing.JLabel();
        sssDeduction = new javax.swing.JLabel();
        sssDeductionvalue = new javax.swing.JLabel();
        philhealthDeduction = new javax.swing.JLabel();
        philhealthDeductionValue = new javax.swing.JLabel();
        pagibigDeduction = new javax.swing.JLabel();
        pagibigDeductionValue = new javax.swing.JLabel();
        deductions = new javax.swing.JLabel();
        taxDeduction = new javax.swing.JLabel();
        taxDeductionValue = new javax.swing.JLabel();
        totalDeduction = new javax.swing.JLabel();
        totalDeductionValue = new javax.swing.JLabel();
        summary = new javax.swing.JLabel();
        sumGrossIncome = new javax.swing.JLabel();
        sumGrossIncomeValue = new javax.swing.JLabel();
        sumBenefits = new javax.swing.JLabel();
        sumBenefitsValue = new javax.swing.JLabel();
        sumDeduction = new javax.swing.JLabel();
        sumDeductionValue = new javax.swing.JLabel();
        netPay = new javax.swing.JLabel();
        netPayValue = new javax.swing.JLabel();
        empPayslip = new javax.swing.JLabel();
        searchEmp = new javax.swing.JLabel();
        searchEmpValue = new javax.swing.JComboBox<>();
        generate = new javax.swing.JButton();
        selectPeriod = new javax.swing.JLabel();
        selectPeriodValue = new javax.swing.JComboBox<>();
        download = new javax.swing.JButton();
        noDataLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(860, 590));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane6.setPreferredSize(new java.awt.Dimension(850, 960));
        jScrollPane6.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane6MouseWheelMoved(evt);
            }
        });

        employeePayslipPanel.setBackground(new java.awt.Color(255, 255, 255));
        employeePayslipPanel.setPreferredSize(new java.awt.Dimension(848, 1006));
        employeePayslipPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        empNameValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(empNameValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 210, 30));

        monthlyRatevalue.setBackground(new java.awt.Color(255, 255, 255));
        monthlyRatevalue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        monthlyRatevalue.setForeground(new java.awt.Color(0, 0, 0));
        monthlyRatevalue.setText("₱53,500.00");
        monthlyRatevalue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        monthlyRatevalue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeePayslipPanel.add(monthlyRatevalue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 260, 390, 30));

        payslipNumValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(payslipNumValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 210, 30));

        periodStartValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(periodStartValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 100, 210, 30));

        periodEndValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(periodEndValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 130, 210, 30));

        empPositionValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(empPositionValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 210, 30));

        heading.setkEndColor(new java.awt.Color(153, 0, 255));
        heading.setkGradientFocus(1000);
        heading.setkStartColor(new java.awt.Color(0, 0, 204));
        heading.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\OPPORTUNITIES (8).png")); // NOI18N
        heading.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 280, 70));

        employeePayslipPanel.add(heading, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 80));

        periodStart.setBackground(new java.awt.Color(255, 255, 255));
        periodStart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        periodStart.setForeground(new java.awt.Color(0, 0, 0));
        periodStart.setText("PERIOD START DATE");
        periodStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(periodStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, 170, 30));

        empIdValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(empIdValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 210, 30));

        empId.setBackground(new java.awt.Color(255, 255, 255));
        empId.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        empId.setForeground(new java.awt.Color(0, 0, 0));
        empId.setText("EMPLOYEE ID");
        empId.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(empId, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 170, 30));

        empName.setBackground(new java.awt.Color(255, 255, 255));
        empName.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        empName.setForeground(new java.awt.Color(0, 0, 0));
        empName.setText("EMPLOYEE NAME");
        empName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(empName, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 170, 30));

        empPosition.setBackground(new java.awt.Color(255, 255, 255));
        empPosition.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        empPosition.setForeground(new java.awt.Color(0, 0, 0));
        empPosition.setText("EMPLOYEE POSITION/DEPARTMENT ");
        empPosition.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(empPosition, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 160, 170, 30));

        periodEnd.setBackground(new java.awt.Color(255, 255, 255));
        periodEnd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        periodEnd.setForeground(new java.awt.Color(0, 0, 0));
        periodEnd.setText("PERIOD END DATE");
        periodEnd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(periodEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 130, 170, 30));

        earnings.setBackground(new java.awt.Color(255, 255, 255));
        earnings.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        earnings.setForeground(new java.awt.Color(0, 0, 204));
        earnings.setText("EARNINGS");
        earnings.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        employeePayslipPanel.add(earnings, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 760, 20));

        payslipNum.setBackground(new java.awt.Color(255, 255, 255));
        payslipNum.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        payslipNum.setForeground(new java.awt.Color(0, 0, 0));
        payslipNum.setText("PAYSLIP NO");
        payslipNum.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 255)));
        employeePayslipPanel.add(payslipNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 170, 30));

        monthlyRate.setBackground(new java.awt.Color(255, 255, 255));
        monthlyRate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        monthlyRate.setForeground(new java.awt.Color(0, 0, 0));
        monthlyRate.setText("Monthly Rate");
        monthlyRate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(monthlyRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 370, 30));

        dailyRate.setBackground(new java.awt.Color(255, 255, 255));
        dailyRate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dailyRate.setForeground(new java.awt.Color(0, 0, 0));
        dailyRate.setText("Hourly Rate");
        dailyRate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(dailyRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 370, 30));

        dailyRateValue.setBackground(new java.awt.Color(255, 255, 255));
        dailyRateValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        dailyRateValue.setForeground(new java.awt.Color(0, 0, 0));
        dailyRateValue.setText("₱2,675.00");
        dailyRateValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(dailyRateValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 290, 390, 30));

        daysWorked.setBackground(new java.awt.Color(255, 255, 255));
        daysWorked.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        daysWorked.setForeground(new java.awt.Color(0, 0, 0));
        daysWorked.setText("Hours Worked");
        daysWorked.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(daysWorked, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 370, 30));

        daysWorkedValue.setBackground(new java.awt.Color(255, 255, 255));
        daysWorkedValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        daysWorkedValue.setForeground(new java.awt.Color(0, 0, 0));
        daysWorkedValue.setText("10");
        daysWorkedValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(daysWorkedValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 320, 390, 30));

        overtime.setBackground(new java.awt.Color(255, 255, 255));
        overtime.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        overtime.setForeground(new java.awt.Color(0, 0, 0));
        overtime.setText("Overtime");
        overtime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(overtime, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 370, 30));

        overtimeValue.setBackground(new java.awt.Color(255, 255, 255));
        overtimeValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        overtimeValue.setForeground(new java.awt.Color(0, 0, 0));
        overtimeValue.setText("0");
        overtimeValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(overtimeValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 350, 390, 30));

        grossIncome.setBackground(new java.awt.Color(238, 238, 238));
        grossIncome.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        grossIncome.setForeground(new java.awt.Color(0, 0, 0));
        grossIncome.setText("GROSS INCOME");
        grossIncome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(grossIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 370, 30));

        grossIncomeValue.setBackground(new java.awt.Color(238, 238, 238));
        grossIncomeValue.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        grossIncomeValue.setForeground(new java.awt.Color(0, 0, 0));
        grossIncomeValue.setText("₱26,750.00");
        grossIncomeValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(grossIncomeValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 380, 390, 30));

        benefits.setBackground(new java.awt.Color(255, 255, 255));
        benefits.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        benefits.setForeground(new java.awt.Color(0, 0, 204));
        benefits.setText("BENEFITS");
        benefits.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        employeePayslipPanel.add(benefits, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 440, 760, 20));

        riceSubsidy.setBackground(new java.awt.Color(255, 255, 255));
        riceSubsidy.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        riceSubsidy.setForeground(new java.awt.Color(0, 0, 0));
        riceSubsidy.setText("Rice Subsidy");
        riceSubsidy.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(riceSubsidy, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, 370, 30));

        riceSubsidyValue.setBackground(new java.awt.Color(255, 255, 255));
        riceSubsidyValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        riceSubsidyValue.setForeground(new java.awt.Color(0, 0, 0));
        riceSubsidyValue.setText("₱1,500.00");
        riceSubsidyValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        riceSubsidyValue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeePayslipPanel.add(riceSubsidyValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 460, 390, 30));

        phoneAllow.setBackground(new java.awt.Color(255, 255, 255));
        phoneAllow.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        phoneAllow.setForeground(new java.awt.Color(0, 0, 0));
        phoneAllow.setText("Phone Allowance");
        phoneAllow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(phoneAllow, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 370, 30));

        phoneAllowValue.setBackground(new java.awt.Color(255, 255, 255));
        phoneAllowValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        phoneAllowValue.setForeground(new java.awt.Color(0, 0, 0));
        phoneAllowValue.setText("₱2,000.00");
        phoneAllowValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(phoneAllowValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 490, 390, 30));

        clothingAllow.setBackground(new java.awt.Color(255, 255, 255));
        clothingAllow.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        clothingAllow.setForeground(new java.awt.Color(0, 0, 0));
        clothingAllow.setText("Clothing Allowance");
        clothingAllow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(clothingAllow, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, 370, 30));

        clothingAllowValue.setBackground(new java.awt.Color(255, 255, 255));
        clothingAllowValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        clothingAllowValue.setForeground(new java.awt.Color(0, 0, 0));
        clothingAllowValue.setText("₱1,000.00");
        clothingAllowValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(clothingAllowValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 520, 390, 30));

        totalBenefits.setBackground(new java.awt.Color(238, 238, 238));
        totalBenefits.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        totalBenefits.setForeground(new java.awt.Color(0, 0, 0));
        totalBenefits.setText("TOTAL");
        totalBenefits.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(totalBenefits, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 550, 370, 30));

        totalBenefitsValue.setBackground(new java.awt.Color(238, 238, 238));
        totalBenefitsValue.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        totalBenefitsValue.setForeground(new java.awt.Color(0, 0, 0));
        totalBenefitsValue.setText("₱4,500.00");
        totalBenefitsValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(totalBenefitsValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 550, 390, 30));

        sssDeduction.setBackground(new java.awt.Color(255, 255, 255));
        sssDeduction.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sssDeduction.setForeground(new java.awt.Color(0, 0, 0));
        sssDeduction.setText("Social Security System");
        sssDeduction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(sssDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 630, 370, 30));

        sssDeductionvalue.setBackground(new java.awt.Color(255, 255, 255));
        sssDeductionvalue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sssDeductionvalue.setForeground(new java.awt.Color(0, 0, 0));
        sssDeductionvalue.setText("₱1,400.00");
        sssDeductionvalue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        sssDeductionvalue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeePayslipPanel.add(sssDeductionvalue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 630, 390, 30));

        philhealthDeduction.setBackground(new java.awt.Color(255, 255, 255));
        philhealthDeduction.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        philhealthDeduction.setForeground(new java.awt.Color(0, 0, 0));
        philhealthDeduction.setText("Philhealth");
        philhealthDeduction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(philhealthDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 660, 370, 30));

        philhealthDeductionValue.setBackground(new java.awt.Color(255, 255, 255));
        philhealthDeductionValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        philhealthDeductionValue.setForeground(new java.awt.Color(0, 0, 0));
        philhealthDeductionValue.setText("₱1,400.00");
        philhealthDeductionValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(philhealthDeductionValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 660, 390, 30));

        pagibigDeduction.setBackground(new java.awt.Color(255, 255, 255));
        pagibigDeduction.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pagibigDeduction.setForeground(new java.awt.Color(0, 0, 0));
        pagibigDeduction.setText("Pag-Ibig");
        pagibigDeduction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(pagibigDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 370, 30));

        pagibigDeductionValue.setBackground(new java.awt.Color(255, 255, 255));
        pagibigDeductionValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        pagibigDeductionValue.setForeground(new java.awt.Color(0, 0, 0));
        pagibigDeductionValue.setText("₱200.00");
        pagibigDeductionValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(pagibigDeductionValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 690, 390, 30));

        deductions.setBackground(new java.awt.Color(255, 255, 255));
        deductions.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        deductions.setForeground(new java.awt.Color(0, 0, 204));
        deductions.setText("DEDUCTIONS");
        deductions.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        employeePayslipPanel.add(deductions, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 610, 760, 20));

        taxDeduction.setBackground(new java.awt.Color(255, 255, 255));
        taxDeduction.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        taxDeduction.setForeground(new java.awt.Color(0, 0, 0));
        taxDeduction.setText("Withholding Tax");
        taxDeduction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(taxDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 720, 370, 30));

        taxDeductionValue.setBackground(new java.awt.Color(255, 255, 255));
        taxDeductionValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        taxDeductionValue.setForeground(new java.awt.Color(0, 0, 0));
        taxDeductionValue.setText("₱1,500.00");
        taxDeductionValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(taxDeductionValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 720, 390, 30));

        totalDeduction.setBackground(new java.awt.Color(238, 238, 238));
        totalDeduction.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        totalDeduction.setForeground(new java.awt.Color(0, 0, 0));
        totalDeduction.setText("TOTAL DEDUCTIONS");
        totalDeduction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(totalDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 750, 370, 30));

        totalDeductionValue.setBackground(new java.awt.Color(238, 238, 238));
        totalDeductionValue.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        totalDeductionValue.setForeground(new java.awt.Color(0, 0, 0));
        totalDeductionValue.setText("₱4,500.00");
        totalDeductionValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(totalDeductionValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 750, 390, 30));

        summary.setBackground(new java.awt.Color(255, 255, 255));
        summary.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        summary.setForeground(new java.awt.Color(0, 0, 204));
        summary.setText("SUMMARY");
        summary.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        employeePayslipPanel.add(summary, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 810, 760, 20));

        sumGrossIncome.setBackground(new java.awt.Color(255, 255, 255));
        sumGrossIncome.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sumGrossIncome.setForeground(new java.awt.Color(0, 0, 0));
        sumGrossIncome.setText("Gross Income");
        sumGrossIncome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(sumGrossIncome, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 830, 370, 30));

        sumGrossIncomeValue.setBackground(new java.awt.Color(255, 255, 255));
        sumGrossIncomeValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sumGrossIncomeValue.setForeground(new java.awt.Color(0, 0, 0));
        sumGrossIncomeValue.setText("₱26,750.00");
        sumGrossIncomeValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        sumGrossIncomeValue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        employeePayslipPanel.add(sumGrossIncomeValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 830, 390, 30));

        sumBenefits.setBackground(new java.awt.Color(255, 255, 255));
        sumBenefits.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sumBenefits.setForeground(new java.awt.Color(0, 0, 0));
        sumBenefits.setText("Benefits");
        sumBenefits.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(sumBenefits, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 860, 370, 30));

        sumBenefitsValue.setBackground(new java.awt.Color(255, 255, 255));
        sumBenefitsValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sumBenefitsValue.setForeground(new java.awt.Color(0, 0, 0));
        sumBenefitsValue.setText("₱4,500.00");
        sumBenefitsValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(sumBenefitsValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 860, 390, 30));

        sumDeduction.setBackground(new java.awt.Color(255, 255, 255));
        sumDeduction.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sumDeduction.setForeground(new java.awt.Color(0, 0, 0));
        sumDeduction.setText("Deductions");
        sumDeduction.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(sumDeduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 890, 370, 30));

        sumDeductionValue.setBackground(new java.awt.Color(255, 255, 255));
        sumDeductionValue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sumDeductionValue.setForeground(new java.awt.Color(0, 0, 0));
        sumDeductionValue.setText("₱4,500.00");
        sumDeductionValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(sumDeductionValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 890, 390, 30));

        netPay.setBackground(new java.awt.Color(238, 238, 238));
        netPay.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        netPay.setForeground(new java.awt.Color(0, 0, 0));
        netPay.setText("NET PAY");
        netPay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(netPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 920, 370, 40));

        netPayValue.setBackground(new java.awt.Color(238, 238, 238));
        netPayValue.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        netPayValue.setForeground(new java.awt.Color(0, 0, 0));
        netPayValue.setText("₱26,750.00");
        netPayValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        employeePayslipPanel.add(netPayValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 920, 390, 40));

        jScrollPane6.setViewportView(employeePayslipPanel);

        add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 860, 430));

        empPayslip.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        empPayslip.setForeground(new java.awt.Color(0, 0, 204));
        empPayslip.setText("Payslip");
        add(empPayslip, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 110, -1));

        searchEmp.setForeground(new java.awt.Color(0, 0, 0));
        searchEmp.setText("Search Employee");
        searchEmp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        add(searchEmp, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 100, 110, 30));

        searchEmpValue.setBackground(new java.awt.Color(255, 255, 255));
        searchEmpValue.setForeground(new java.awt.Color(0, 0, 0));
        searchEmpValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        searchEmpValue.setLightWeightPopupEnabled(false);
        searchEmpValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchEmpValueActionPerformed(evt);
            }
        });
        add(searchEmpValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, 230, 30));

        generate.setBackground(new java.awt.Color(255, 255, 255));
        generate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        generate.setForeground(new java.awt.Color(0, 0, 204));
        generate.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\icons8-search-48.png")); // NOI18N
        generate.setText("Search");
        generate.setBorder(null);
        generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateActionPerformed(evt);
            }
        });
        add(generate, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 90, 110, 40));

        selectPeriod.setForeground(new java.awt.Color(0, 0, 0));
        selectPeriod.setText("Select Period");
        selectPeriod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        add(selectPeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 110, 30));

        selectPeriodValue.setBackground(new java.awt.Color(255, 255, 255));
        selectPeriodValue.setForeground(new java.awt.Color(0, 0, 0));
        selectPeriodValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        selectPeriodValue.setLightWeightPopupEnabled(false);
        selectPeriodValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPeriodValueActionPerformed(evt);
            }
        });
        add(selectPeriodValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 230, 30));

        download.setBackground(new java.awt.Color(255, 255, 255));
        download.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        download.setForeground(new java.awt.Color(0, 0, 204));
        download.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\download (1).png")); // NOI18N
        download.setText("Download");
        download.setBorder(null);
        download.setEnabled(false);
        download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadActionPerformed(evt);
            }
        });
        add(download, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, 120, 40));

        noDataLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        noDataLabel.setForeground(new java.awt.Color(204, 0, 0));
        noDataLabel.setText("No data available for the selected period.");
        add(noDataLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 126, -1, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void searchEmpValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchEmpValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchEmpValueActionPerformed

    private void selectPeriodValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPeriodValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPeriodValueActionPerformed

    private void downloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadActionPerformed
        try (Connection conn = DbConnection.getConnection()) {
            ReportGenerator generator = new ReportGenerator();

            String source = "resources/Reports/EmployeePayslipReport.jrxml";

            // Output PDF path
            String userHome = System.getProperty("user.home");
            String fileName = payslip.getPayrollID() + "_" 
                    + payslip.getEmployee().getLastName()+ ".pdf";
            String outputPath = userHome + "/Downloads/payslip_" +  fileName;

            // Optional: Report parameters
            Map<String, Object> params = new HashMap<>();
            params.put("employee_id", payslip.getEmployee().getEmployeeID());
            params.put("payslip_no", payslip.getPayrollID());

            boolean isReportGenerated = generator.generateReport(conn, source, outputPath, params);
            DownloadPDFPopUp frame = new DownloadPDFPopUp();
            frame.setVisible(true);
            frame.updateStatus(isReportGenerated, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_downloadActionPerformed

    private void generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateActionPerformed
        if(user instanceof HRPersonnel && searchEmpValue.getSelectedIndex() == 0){
            return;
        }
        if(selectPeriodValue.getSelectedIndex() != 0){
            loadPayslip();
        }
    }//GEN-LAST:event_generateActionPerformed

    private void jScrollPane6MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane6MouseWheelMoved
        int rotation = evt.getWheelRotation();
                JScrollBar verticalScrollBar = jScrollPane6.getVerticalScrollBar();
                
                // Control the scroll speed here (higher value = faster scroll)
                int scrollAmount = rotation * 38;  // Adjust this multiplier for smoothness
                
                verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
    }//GEN-LAST:event_jScrollPane6MouseWheelMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel benefits;
    private javax.swing.JLabel clothingAllow;
    private javax.swing.JLabel clothingAllowValue;
    private javax.swing.JLabel dailyRate;
    private javax.swing.JLabel dailyRateValue;
    private javax.swing.JLabel daysWorked;
    private javax.swing.JLabel daysWorkedValue;
    private javax.swing.JLabel deductions;
    private javax.swing.JButton download;
    private javax.swing.JLabel earnings;
    private javax.swing.JLabel empId;
    private javax.swing.JLabel empIdValue;
    private javax.swing.JLabel empName;
    private javax.swing.JLabel empNameValue;
    private javax.swing.JLabel empPayslip;
    private javax.swing.JLabel empPosition;
    private javax.swing.JLabel empPositionValue;
    private javax.swing.JPanel employeePayslipPanel;
    private javax.swing.JButton generate;
    private javax.swing.JLabel grossIncome;
    private javax.swing.JLabel grossIncomeValue;
    private keeptoo.KGradientPanel heading;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel logo;
    private javax.swing.JLabel monthlyRate;
    private javax.swing.JLabel monthlyRatevalue;
    private javax.swing.JLabel netPay;
    private javax.swing.JLabel netPayValue;
    private javax.swing.JLabel noDataLabel;
    private javax.swing.JLabel overtime;
    private javax.swing.JLabel overtimeValue;
    private javax.swing.JLabel pagibigDeduction;
    private javax.swing.JLabel pagibigDeductionValue;
    private javax.swing.JLabel payslipNum;
    private javax.swing.JLabel payslipNumValue;
    private javax.swing.JLabel periodEnd;
    private javax.swing.JLabel periodEndValue;
    private javax.swing.JLabel periodStart;
    private javax.swing.JLabel periodStartValue;
    private javax.swing.JLabel philhealthDeduction;
    private javax.swing.JLabel philhealthDeductionValue;
    private javax.swing.JLabel phoneAllow;
    private javax.swing.JLabel phoneAllowValue;
    private javax.swing.JLabel riceSubsidy;
    private javax.swing.JLabel riceSubsidyValue;
    private javax.swing.JLabel searchEmp;
    private javax.swing.JComboBox<String> searchEmpValue;
    private javax.swing.JLabel selectPeriod;
    private javax.swing.JComboBox<String> selectPeriodValue;
    private javax.swing.JLabel sssDeduction;
    private javax.swing.JLabel sssDeductionvalue;
    private javax.swing.JLabel sumBenefits;
    private javax.swing.JLabel sumBenefitsValue;
    private javax.swing.JLabel sumDeduction;
    private javax.swing.JLabel sumDeductionValue;
    private javax.swing.JLabel sumGrossIncome;
    private javax.swing.JLabel sumGrossIncomeValue;
    private javax.swing.JLabel summary;
    private javax.swing.JLabel taxDeduction;
    private javax.swing.JLabel taxDeductionValue;
    private javax.swing.JLabel totalBenefits;
    private javax.swing.JLabel totalBenefitsValue;
    private javax.swing.JLabel totalDeduction;
    private javax.swing.JLabel totalDeductionValue;
    // End of variables declaration//GEN-END:variables
}
