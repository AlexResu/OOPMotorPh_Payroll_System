/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import models.LeaveRequest;
import models.Employee;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import dao.EmployeeDao;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Alex Resurreccion
 */
public class FileALeave extends javax.swing.JFrame {
    private Employee user;
    private HomePagePanel parentPanel;
    private List<Map<String, Object>> leaveCredits;
    /**
     * Creates new form FileALeave
     */
    public FileALeave() {
        initComponents();
    }
    
    public FileALeave(Employee user, HomePagePanel parentPanel) {
        EmployeeDao employeeDao = new EmployeeDao();
        this.user = user;
        this.parentPanel = parentPanel;
        initComponents();
        jLabel3.setVisible(false);
        days1.setVisible(false);
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        empNumber.setText(Integer.toString(user.getEmployeeID()));
        phoneNumber.setText(user.getPhoneNumber());

        setDefaultDateRange();
        dateFrom.getDateEditor().addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Date selectedDateFrom = dateFrom.getDate();
                if (selectedDateFrom != null) {
                    updateDateUntilRange();
                }
            }
        });
        dateUntil.getDateEditor().addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                updateDaysApplied();
            }
        });
        leaveCredits = employeeDao.loadLeaveCredits(user.getEmployeeID());
        days.setVisible(false);
    }
    
    // Set initial default date range for 'dateFrom' and 'dateUntil'
    private void setDefaultDateRange() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, -7); 
        Date minDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date maxDateForDateFrom = calendar.getTime();

        dateFrom.setSelectableDateRange(minDate, maxDateForDateFrom);
        dateUntil.setSelectableDateRange(minDate, maxDateForDateFrom);
    }

    private void updateDateUntilRange() {
        Calendar calendar = Calendar.getInstance();
        Date selectedDateFrom = dateFrom.getDate();
        Date selectedDateUntil = dateUntil.getDate();

        if (selectedDateUntil != null && selectedDateUntil.before(selectedDateFrom)) {
            dateUntil.setDate(null);
        }

        dateUntil.setMinSelectableDate(selectedDateFrom);

        calendar.setTime(selectedDateFrom);
        calendar.add(Calendar.YEAR, 1); 
        dateUntil.setMaxSelectableDate(calendar.getTime());

        if (selectedDateUntil != null) {
            updateDaysApplied();
        }
    }
    
    private void updateDaysApplied(){
        Date selectedDateFrom = dateFrom.getDate();
        long diffInDays = 0;
    
        // Loop through the days from selectedDateFrom to the calculated maxDateForDateUntil
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(selectedDateFrom);
        
        if(dateUntil.getDate() != null){
            while (!startCalendar.getTime().after(dateUntil.getDate())) {
                // Check if the current day is not Saturday (6) or Sunday (7)
                if (startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY 
                        && startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    diffInDays++; // Count this day if it's not a weekend
                }
                startCalendar.add(Calendar.DAY_OF_MONTH, 1);  // Move to the next day
            }
        }
        

        // Set the day difference in the JTextField
        // If the dates are the same, display 1. If 1 day difference, display 2, and so on.
        dayApplied.setText(String.valueOf(diffInDays));
        String selectedLeaveType = (String) type.getSelectedItem();
    
        // Find the selected leave type in leaveCredits
        Map<String, Object> selectedLeave = findLeaveByType(selectedLeaveType);

        if (selectedLeave != null) {
            // Get the available leave for the selected leave type
            int availableLeave = (int) selectedLeave.get("Available");

            if(diffInDays > availableLeave){
                days.setText("You only have " + availableLeave 
                        + " of days left for this leave type.");
                days.setVisible(true);
                submit.setEnabled(false);
            } else {
                days.setVisible(false);
                submit.setEnabled(true);
            }
        } else {
            days.setVisible(false);
            submit.setEnabled(true);
        }
    }
    
    private Map<String, Object> findLeaveByType(String leaveType) {
        for (Map<String, Object> leave : leaveCredits) {
            if (leave.get("Type").equals(leaveType)) {
                return leave;
            }
        }
        return null; // Return null if no matching leave type found
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lastName = new javax.swing.JTextField();
        empNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dayApplied = new javax.swing.JTextField();
        phoneNumber = new javax.swing.JTextField();
        firstName = new javax.swing.JTextField();
        type = new javax.swing.JComboBox<>();
        dateUntil = new com.toedter.calendar.JDateChooser();
        dateFrom = new com.toedter.calendar.JDateChooser();
        jScrollPane4 = new javax.swing.JScrollPane();
        reason = new javax.swing.JTextArea();
        cancel = new javax.swing.JButton();
        submit = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        days = new javax.swing.JLabel();
        days1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        kGradientPanel1.setkEndColor(new java.awt.Color(0, 0, 204));
        kGradientPanel1.setkStartColor(new java.awt.Color(153, 0, 204));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\OPPORTUNITIES (5) - Copy.png")); // NOI18N
        kGradientPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 190, 100));

        jLabel7.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("LEAVE REQUEST FORM");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kGradientPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 340, 72));

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("The company can use this form for the employeees to fill for leave.");
        kGradientPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 600, -1, -1));

        jLabel27.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\MotorPhApp\\resources\\icons8-close-window-48.png")); // NOI18N
        jLabel27.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel27MouseClicked(evt);
            }
        });
        kGradientPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, -1, 30));

        jScrollPane3.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane3MouseWheelMoved(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Name");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 76, 20));

        jLabel10.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Employee number");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 150, 20));

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Phone Number");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 168, 20));

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Number of Days Applied");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 168, 20));

        jLabel8.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Leave Date From");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 168, 20));

        jLabel9.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Leave Date Until");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 168, 20));

        jLabel11.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Leave Type");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 168, 20));

        jLabel13.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Reason for Leave ");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 168, 20));

        lastName.setEditable(false);
        lastName.setForeground(new java.awt.Color(0, 0, 0));
        lastName.setEnabled(false);
        lastName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastNameActionPerformed(evt);
            }
        });
        jPanel2.add(lastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 20, 154, -1));

        empNumber.setEditable(false);
        empNumber.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        empNumber.setForeground(new java.awt.Color(0, 0, 0));
        empNumber.setEnabled(false);
        empNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empNumberActionPerformed(evt);
            }
        });
        jPanel2.add(empNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 320, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Last Name");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, -1, -1));

        dayApplied.setEditable(false);
        dayApplied.setForeground(new java.awt.Color(0, 0, 0));
        dayApplied.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayAppliedActionPerformed(evt);
            }
        });
        jPanel2.add(dayApplied, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 180, -1));

        phoneNumber.setEditable(false);
        phoneNumber.setForeground(new java.awt.Color(0, 0, 0));
        phoneNumber.setEnabled(false);
        phoneNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneNumberActionPerformed(evt);
            }
        });
        jPanel2.add(phoneNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 250, -1));

        firstName.setEditable(false);
        firstName.setForeground(new java.awt.Color(0, 0, 0));
        firstName.setEnabled(false);
        firstName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstNameActionPerformed(evt);
            }
        });
        jPanel2.add(firstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 154, -1));

        type.setBackground(new java.awt.Color(255, 255, 255));
        type.setForeground(new java.awt.Color(0, 0, 0));
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Please select", "Emergency Leave", "Sick Leave", "Vacation Leave" }));
        type.setLightWeightPopupEnabled(false);
        type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeActionPerformed(evt);
            }
        });
        jPanel2.add(type, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 190, -1));

        dateUntil.setBackground(new java.awt.Color(255, 255, 255));
        dateUntil.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(dateUntil, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 320, 240, -1));

        dateFrom.setBackground(new java.awt.Color(255, 255, 255));
        dateFrom.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 240, -1));

        reason.setBackground(new java.awt.Color(255, 255, 255));
        reason.setColumns(20);
        reason.setForeground(new java.awt.Color(0, 0, 0));
        reason.setRows(5);
        jScrollPane4.setViewportView(reason);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 290, 70));

        cancel.setBackground(new java.awt.Color(153, 153, 153));
        cancel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cancel.setForeground(new java.awt.Color(255, 255, 255));
        cancel.setText("Cancel");
        cancel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        cancel.setFocusPainted(false);
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jPanel2.add(cancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 540, 130, 40));

        submit.setBackground(new java.awt.Color(0, 153, 0));
        submit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        submit.setForeground(new java.awt.Color(255, 255, 255));
        submit.setText("Submit");
        submit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        submit.setFocusPainted(false);
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });
        jPanel2.add(submit, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 130, 40));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("First Name");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, -1, -1));
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 650, 650, 10));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setText("Successfully leave request submitted");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 600, 420, -1));
        jPanel2.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 650, 10));

        days.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        days.setForeground(new java.awt.Color(255, 0, 51));
        days.setText("You only have # of days left for this leave type.");
        jPanel2.add(days, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, 320, 20));

        days1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        days1.setForeground(new java.awt.Color(255, 0, 51));
        days1.setText("Cannot proceed please input valid inputs.");
        jPanel2.add(days1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 620, 220, 20));

        jScrollPane3.setViewportView(jPanel2);

        kGradientPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 720, 500));

        getContentPane().add(kGradientPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, -2, 720, 630));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel27MouseClicked

    private void lastNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lastNameActionPerformed

    private void empNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empNumberActionPerformed

    private void dayAppliedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dayAppliedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dayAppliedActionPerformed

    private void phoneNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneNumberActionPerformed

    private void firstNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_firstNameActionPerformed

    private void typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_typeActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        days1.setVisible(false);
        String durationText = dayApplied.getText();
        String reasonText = reason.getText();
        Object selectedType = type.getSelectedItem();
        if (durationText == null || durationText.trim().isEmpty() 
                || dateFrom.getDate() == null
                || dateUntil.getDate() == null
                || reasonText == null || reasonText.trim().isEmpty()
                || selectedType == null || selectedType.toString().trim().isEmpty()) {
            days1.setVisible(true);
            return;
        } else {
            jLabel3.setVisible(true);
            jLabel3.setText("Sending leave request to HR for approval");
        }
        
        EmployeeDao employeeDao = new EmployeeDao();
        LeaveRequest newLeave = new LeaveRequest();
        newLeave.setEmployee(user);
        newLeave.setDuration(Integer.parseInt(dayApplied.getText()));
        newLeave.setStartDate(dateFrom.getDate());
        newLeave.setEndDate(dateUntil.getDate());
        newLeave.setReason(reason.getText());
        newLeave.setLeaveType((String) type.getSelectedItem());
        
        // Run DB task in background
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                EmployeeDao employeeDao = new EmployeeDao();
                return employeeDao.createLeaveRequest(newLeave);
            }

            @Override
            protected void done() {
                try {
                    boolean result = get();
                    if (result) {
                        jLabel3.setText("Successfully submitted leave request. This page will close in 3 seconds.");
                        new java.util.Timer().schedule(new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dispose();
                                parentPanel.loadLeaveRecords();
                            }
                        }, 3000);
                    } else {
                        jLabel3.setText("Oops! Something went wrong while submitting your leave request. Please try again.");
                    }
                } catch (Exception ex) {
                    jLabel3.setText("An unexpected error occurred.");
                    ex.printStackTrace();
                }
            }
        };

        worker.execute(); // Start background task
    }//GEN-LAST:event_submitActionPerformed

    private void jScrollPane3MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane3MouseWheelMoved
        int rotation = evt.getWheelRotation();
                JScrollBar verticalScrollBar = jScrollPane3.getVerticalScrollBar();
                
                // Control the scroll speed here (higher value = faster scroll)
                int scrollAmount = rotation * 38;  // Adjust this multiplier for smoothness
                
                verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
    }//GEN-LAST:event_jScrollPane3MouseWheelMoved

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileALeave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileALeave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileALeave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileALeave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileALeave().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateUntil;
    private javax.swing.JTextField dayApplied;
    private javax.swing.JLabel days;
    private javax.swing.JLabel days1;
    private javax.swing.JTextField empNumber;
    private javax.swing.JTextField firstName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JTextField lastName;
    private javax.swing.JTextField phoneNumber;
    private javax.swing.JTextArea reason;
    private javax.swing.JButton submit;
    private javax.swing.JComboBox<String> type;
    // End of variables declaration//GEN-END:variables
}
