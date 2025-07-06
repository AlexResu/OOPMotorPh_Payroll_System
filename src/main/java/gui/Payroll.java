/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import models.HRPersonnel;
import models.AccountAccess;
import alex.oopmotorphpayrollsystem.*;
import dao.HRPersonnelDao;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex Resurreccion
 */
public class Payroll extends javax.swing.JPanel {
    private AccountAccess account;
    private HRPersonnel user;

    /**
     * Creates new form Payroll
     */
    public Payroll() {
        initComponents();
    }
    
    public Payroll(AccountAccess account, HRPersonnel user) {
        this.account = account;
        this.user = user;
        initComponents();
        populateWeekComboBox();
        loadPayrollData();
    }
    
    private void populateWeekComboBox() {
        Map<String, String[]> valueMap = generateBiMonthlyValues();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        // Populate JComboBox with formatted week ranges
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
    
    private void loadPayrollData(){
        HRPersonnelDao hrPersonnelDao = new HRPersonnelDao();
        Date[] selectedDates = getSelectedBiMonthlyDates();
        List<Map<String, Object>> payrollList = hrPersonnelDao.loadPayrollList(
                    selectedDates[0], selectedDates[1]);
        DefaultTableModel model = (DefaultTableModel) payrollReportTable.getModel();
        model.setRowCount(0);
        boolean hasPending = false;
        
        // Check if there's data
        if (payrollList != null && !payrollList.isEmpty()) {
            noDataLabel.setVisible(false); // hide label if data exists
            
            for (Map<String, Object> payroll : payrollList) {
                Object data[] = {
                    payroll.get("payslip_no"),
                    payroll.get("employee_id"),
                    payroll.get("position_department"),
                    payroll.get("employee_name"),
                    payroll.get("take_home_pay"),
    //                payroll.get("status"),
                };

                // Add the row to the table model
                model.addRow(data);
                if ("Pending".equals(payroll.get("status"))) {
                    hasPending = true;
                }
            }
        } else {
            noDataLabel.setVisible(true); // show label if no data
        }
            calculate.setEnabled(hasPending);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane9 = new javax.swing.JScrollPane();
        payrollReportTable = new javax.swing.JTable();
        payrollReport = new javax.swing.JLabel();
        selectPeriod = new javax.swing.JLabel();
        selectPeriodValue = new javax.swing.JComboBox<>();
        calculate = new javax.swing.JButton();
        noDataLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(860, 590));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane9.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane9MouseWheelMoved(evt);
            }
        });

        payrollReportTable.setAutoCreateRowSorter(true);
        payrollReportTable.setBackground(new java.awt.Color(255, 255, 255));
        payrollReportTable.setForeground(new java.awt.Color(0, 0, 0));
        payrollReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"180001", "10001", "Executive", "Manuel III A.", "26,000.00"},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "PAYROLL ID", "EMPLOYEE ID", "DEPARTMENT", "NAME", "EARNINGS"
            }
        ));
        payrollReportTable.setGridColor(new java.awt.Color(102, 102, 102));
        payrollReportTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
        payrollReportTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        payrollReportTable.setShowGrid(true);
        jScrollPane9.setViewportView(payrollReportTable);

        add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 860, 460));

        payrollReport.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        payrollReport.setForeground(new java.awt.Color(0, 0, 204));
        payrollReport.setText("Payroll");
        add(payrollReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 170, -1));

        selectPeriod.setForeground(new java.awt.Color(0, 0, 0));
        selectPeriod.setText("Select Period");
        selectPeriod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        add(selectPeriod, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 110, 30));

        selectPeriodValue.setBackground(new java.awt.Color(255, 255, 255));
        selectPeriodValue.setForeground(new java.awt.Color(0, 0, 0));
        selectPeriodValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        selectPeriodValue.setLightWeightPopupEnabled(false);
        selectPeriodValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPeriodValueActionPerformed(evt);
            }
        });
        add(selectPeriodValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 270, 30));

        calculate.setBackground(new java.awt.Color(255, 255, 255));
        calculate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        calculate.setForeground(new java.awt.Color(0, 0, 204));
        calculate.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\calculator.png")); // NOI18N
        calculate.setText("Approve");
        calculate.setBorder(null);
        calculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateActionPerformed(evt);
            }
        });
        add(calculate, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 70, 100, 50));

        noDataLabel.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        noDataLabel.setForeground(new java.awt.Color(255, 0, 51));
        noDataLabel.setText("No data available for the selected period");
        add(noDataLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, 230, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void selectPeriodValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPeriodValueActionPerformed
        loadPayrollData();
    }//GEN-LAST:event_selectPeriodValueActionPerformed

    private void calculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateActionPerformed
        Date[] selectedDates = getSelectedBiMonthlyDates();
        boolean isSuccess = user.processPayroll(selectedDates[0], selectedDates[1]);
        if(isSuccess){
            loadPayrollData();
            // success message?
        } else {
            //Display fail?
        }
    }//GEN-LAST:event_calculateActionPerformed

    private void jScrollPane9MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane9MouseWheelMoved
        int rotation = evt.getWheelRotation();
                JScrollBar verticalScrollBar = jScrollPane9.getVerticalScrollBar();
                
                // Control the scroll speed here (higher value = faster scroll)
                int scrollAmount = rotation * 38;  // Adjust this multiplier for smoothness
                
                verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
    }//GEN-LAST:event_jScrollPane9MouseWheelMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculate;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel noDataLabel;
    private javax.swing.JLabel payrollReport;
    private javax.swing.JTable payrollReportTable;
    private javax.swing.JLabel selectPeriod;
    private javax.swing.JComboBox<String> selectPeriodValue;
    // End of variables declaration//GEN-END:variables
}
