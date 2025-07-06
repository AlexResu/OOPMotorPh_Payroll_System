/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;
import models.AccountAccess;
import models.AttendanceRecord;
import models.Employee;
import models.HRPersonnel;
import models.SystemAdministrator;
import models.User;
import models.LeaveRequest;
import dao.AttendanceRecordDao;
import dao.EmployeeDao;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex Resurreccion
 */
public class HomePagePanel extends javax.swing.JPanel {
    private AccountAccess account;
    private User user;

    /**
     * Creates new form HomePagePanel
     */
    public HomePagePanel() {
        initComponents();
    }
    
    public HomePagePanel(AccountAccess account, User user) {
        this.account = account;
        this.user = user;
        initComponents();
        setUserInfo();
        
        timeInTimeOutRecordedLabel.setVisible(false);
        if(!(user instanceof Employee)){
            hideNonEmployeeComponents();
        } else {
            loadLeaveRecords();
            loadTimeLog();
        }
    }
    
    private void hideNonEmployeeComponents(){
        timeInLabel.setVisible(false);
        timeInLabelValue.setVisible(false);
        timeOutLabel.setVisible(false);
        timeOutValue.setVisible(false);
        dateTimeInLabel.setVisible(false);
        dateTimeInValue.setVisible(false);
        dateTimeOutLabel.setVisible(false);
        dateTimeOutValue.setVisible(false);
        timeIn.setVisible(false);
        timeOut.setVisible(false);
        timeInTimeOutRecordedLabel.setVisible(false);
        fileALeave.setVisible(false);
        jSeparator5.setVisible(false);
        jSeparator9.setVisible(false);
    }
    
    private void setUserInfo(){
        empNumberValue.setText(String.valueOf(user.getEmployeeID()));
        lastNameValue.setText(user.getLastName());
        positionLabelValue1.setText(user.getPosition());
        firstNameValue.setText(user.getFirstName());
        departmentLabelValue.setText(user.getDepartment());
        Date dateHired = user.getDateHired();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy");
        dateHiredLabelValue.setText(formatter.format(dateHired));
    }
    
    protected void loadLeaveRecords(){
        EmployeeDao employeeDao = new EmployeeDao();
        List<LeaveRequest> leaves = employeeDao.loadLeaveRequests(user.getEmployeeID());
        DefaultTableModel model = (DefaultTableModel) recordsTable.getModel();
        model.setRowCount(0);
        System.out.println(leaves.size());
        for (LeaveRequest record : leaves) {
            Object data[] = {
                record.getLeaveType(), record.getStartDate(), 
                record.getDuration(), record.getLeaveStatus(), 
                !("PENDING".equals(record.getLeaveStatus())) ? 
                    record.getApprovedBy().getFirstName() + " " 
                    + record.getApprovedBy().getLastName() : "--"};

            // Add the row to the table model
            model.addRow(data);
        }
        
        List<Map<String, Object>> leaveCredits = employeeDao.loadLeaveCredits(user.getEmployeeID());
        model = (DefaultTableModel) leaveCreditsTable.getModel();
        model.setRowCount(0);
        System.out.println(leaveCredits.size());
        System.out.println("Leave credits");
        
        for (Map<String, Object> leave : leaveCredits) {
            Object data[] = {leave.get("Type"), leave.get("Allowable"),
                leave.get("Available")
            };
            System.out.println(leave.get("Available"));
            model.addRow(data);
        }
    }
    
    private void loadTimeLog(){
        AttendanceRecordDao attendanceRecordDao = new AttendanceRecordDao();
        AttendanceRecord attendance = attendanceRecordDao.getAttendanceRecord(user.getEmployeeID(), new Date());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormatter.format(new Date());
        if(attendance.getTimeIn() != null){
            String timeInText = timeFormatter.format(attendance.getTimeIn());
            timeInLabelValue.setText(timeInText);
            timeIn.setEnabled(false);
            timeOut.setEnabled(true);
            dateTimeInValue.setText(formattedDate);
        }
        if(attendance.getTimeOut() != null){
            String timeOutText = timeFormatter.format(attendance.getTimeOut());
            timeOutValue.setText(timeOutText);
            dateTimeOutValue.setText(formattedDate);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        empHomePage = new javax.swing.JPanel();
        empPhoto = new javax.swing.JLabel();
        lastName = new javax.swing.JLabel();
        lastNameValue = new javax.swing.JLabel();
        firstName = new javax.swing.JLabel();
        firstNameValue = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        emailLabelValue = new javax.swing.JLabel();
        empNum = new javax.swing.JLabel();
        empNumberValue = new javax.swing.JLabel();
        departmentLabel1 = new javax.swing.JLabel();
        positionLabelValue1 = new javax.swing.JLabel();
        departmentLabel = new javax.swing.JLabel();
        departmentLabelValue = new javax.swing.JLabel();
        dateHiredLabel = new javax.swing.JLabel();
        dateHiredLabelValue = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        leaveCreditsLabel = new javax.swing.JLabel();
        recordsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        leaveCreditsTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        recordsTable = new javax.swing.JTable();
        fileALeave = new com.k33ptoo.components.KButton();
        jSeparator9 = new javax.swing.JSeparator();
        timeInLabel = new javax.swing.JLabel();
        timeInLabelValue = new javax.swing.JLabel();
        timeOutLabel = new javax.swing.JLabel();
        timeOutValue = new javax.swing.JLabel();
        dateTimeInLabel = new javax.swing.JLabel();
        dateTimeInValue = new javax.swing.JLabel();
        dateTimeOutLabel = new javax.swing.JLabel();
        dateTimeOutValue = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        timeIn = new com.k33ptoo.components.KButton();
        timeOut = new com.k33ptoo.components.KButton();
        timeInTimeOutRecordedLabel = new javax.swing.JLabel();

        empHomePage.setBackground(new java.awt.Color(255, 255, 255));
        empHomePage.setPreferredSize(new java.awt.Dimension(860, 590));
        empHomePage.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        empPhoto.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\MotorPhApp\\resources\\Employee Icon.png")); // NOI18N
        empPhoto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(empPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 6, -1, 120));

        lastName.setForeground(new java.awt.Color(0, 0, 0));
        lastName.setText("Last Name");
        empHomePage.add(lastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, -1));

        lastNameValue.setForeground(new java.awt.Color(0, 0, 0));
        lastNameValue.setText("  Admin");
        lastNameValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lastNameValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(lastNameValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 140, 26));

        firstName.setForeground(new java.awt.Color(0, 0, 0));
        firstName.setText("First Name");
        empHomePage.add(firstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, -1, -1));

        firstNameValue.setForeground(new java.awt.Color(0, 0, 0));
        firstNameValue.setText("  User");
        firstNameValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        firstNameValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(firstNameValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 140, 26));

        emailLabel.setForeground(new java.awt.Color(0, 0, 0));
        emailLabel.setText("Email");
        empHomePage.add(emailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, -1, -1));

        emailLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        emailLabelValue.setText("  @example.com");
        emailLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        emailLabelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(emailLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 140, 26));

        empNum.setForeground(new java.awt.Color(0, 0, 0));
        empNum.setText("Employee Number");
        empHomePage.add(empNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 126, -1, -1));

        empNumberValue.setForeground(new java.awt.Color(0, 0, 0));
        empNumberValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        empNumberValue.setText("10000");
        empNumberValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        empNumberValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(empNumberValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 146, 100, 20));

        departmentLabel1.setForeground(new java.awt.Color(0, 0, 0));
        departmentLabel1.setText("Position");
        empHomePage.add(departmentLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, -1, -1));

        positionLabelValue1.setForeground(new java.awt.Color(0, 0, 0));
        positionLabelValue1.setText("  Human Resource");
        positionLabelValue1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        positionLabelValue1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(positionLabelValue1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 140, 26));

        departmentLabel.setForeground(new java.awt.Color(0, 0, 0));
        departmentLabel.setText("Department");
        empHomePage.add(departmentLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 120, -1, -1));

        departmentLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        departmentLabelValue.setText("  Human Resource");
        departmentLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        departmentLabelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(departmentLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 140, 26));

        dateHiredLabel.setForeground(new java.awt.Color(0, 0, 0));
        dateHiredLabel.setText("Date Hired");
        empHomePage.add(dateHiredLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, -1, -1));

        dateHiredLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        dateHiredLabelValue.setText("  May 15, 2024");
        dateHiredLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        dateHiredLabelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        empHomePage.add(dateHiredLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 140, 140, 26));

        jSeparator7.setForeground(new java.awt.Color(187, 187, 187));
        empHomePage.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 176, 860, 18));

        leaveCreditsLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        leaveCreditsLabel.setForeground(new java.awt.Color(0, 0, 0));
        leaveCreditsLabel.setText("Leave Credits");
        empHomePage.add(leaveCreditsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 190, -1));

        recordsLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        recordsLabel.setForeground(new java.awt.Color(0, 0, 0));
        recordsLabel.setText("Records");
        empHomePage.add(recordsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, -1, -1));

        jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane1MouseWheelMoved(evt);
            }
        });

        leaveCreditsTable.setBackground(new java.awt.Color(255, 255, 255));
        leaveCreditsTable.setForeground(new java.awt.Color(0, 0, 0));
        leaveCreditsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"         EL", "         5", "         5"},
                {"         SL", "        15", "        15"},
                {"         VL", "        20", "        20"},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Type", "Allowable", "Available"
            }
        ));
        leaveCreditsTable.setCellSelectionEnabled(true);
        leaveCreditsTable.setGridColor(new java.awt.Color(102, 102, 102));
        leaveCreditsTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
        leaveCreditsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        leaveCreditsTable.setShowGrid(true);
        jScrollPane1.setViewportView(leaveCreditsTable);

        empHomePage.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, 390, 122));

        jScrollPane2.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane2MouseWheelMoved(evt);
            }
        });

        recordsTable.setBackground(new java.awt.Color(255, 255, 255));
        recordsTable.setForeground(new java.awt.Color(0, 0, 0));
        recordsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"SL - Sick Leave", "05-16-2022", "0.5", "Approved", null},
                {"SL - Sick Leave", "06-16-2022", "1", "Submitted", null},
                {"VL - Vacation Leave", "07-16-2022", "5", "Submitted", null},
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
                "Leave Type", "Date from", "Days", "Remarks", "Approved by"
            }
        ));
        recordsTable.setGridColor(new java.awt.Color(102, 102, 102));
        recordsTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
        recordsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        recordsTable.setShowGrid(false);
        recordsTable.setShowVerticalLines(true);
        jScrollPane2.setViewportView(recordsTable);

        empHomePage.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 210, 390, 122));

        fileALeave.setBorder(null);
        fileALeave.setText("File a Leave");
        fileALeave.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        fileALeave.setHideActionText(true);
        fileALeave.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        fileALeave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fileALeave.setIconTextGap(30);
        fileALeave.setkBackGroundColor(new java.awt.Color(255, 153, 0));
        fileALeave.setkBorderRadius(15);
        fileALeave.setkEndColor(new java.awt.Color(153, 51, 255));
        fileALeave.setkHoverEndColor(new java.awt.Color(204, 102, 255));
        fileALeave.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        fileALeave.setkHoverStartColor(new java.awt.Color(51, 51, 255));
        fileALeave.setkIndicatorThickness(10);
        fileALeave.setkPressedColor(new java.awt.Color(102, 0, 255));
        fileALeave.setkSelectedColor(new java.awt.Color(0, 0, 204));
        fileALeave.setkStartColor(new java.awt.Color(0, 0, 255));
        fileALeave.setRequestFocusEnabled(false);
        fileALeave.setRolloverEnabled(false);
        fileALeave.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fileALeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileALeaveActionPerformed(evt);
            }
        });
        empHomePage.add(fileALeave, new org.netbeans.lib.awtextra.AbsoluteConstraints(277, 341, 318, 40));

        jSeparator9.setForeground(new java.awt.Color(187, 187, 187));
        empHomePage.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 396, 860, 18));

        timeInLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        timeInLabel.setForeground(new java.awt.Color(0, 0, 0));
        timeInLabel.setText("Time in:");
        empHomePage.add(timeInLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 414, 65, 27));

        timeInLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        timeInLabelValue.setText("  No time in yet");
        timeInLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        empHomePage.add(timeInLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 414, 110, 30));

        timeOutLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        timeOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        timeOutLabel.setText("Time out:");
        empHomePage.add(timeOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(483, 412, 65, 27));

        timeOutValue.setForeground(new java.awt.Color(0, 0, 0));
        timeOutValue.setText("  No time in yet");
        timeOutValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        empHomePage.add(timeOutValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 412, 110, 30));

        dateTimeInLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dateTimeInLabel.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeInLabel.setText("Date: ");
        empHomePage.add(dateTimeInLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 454, 50, 27));

        dateTimeInValue.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeInValue.setText("  mm/dd/yy");
        dateTimeInValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        empHomePage.add(dateTimeInValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 454, 110, 30));

        dateTimeOutLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dateTimeOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeOutLabel.setText("Date: ");
        empHomePage.add(dateTimeOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(483, 452, 50, 27));

        dateTimeOutValue.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeOutValue.setText("  mm/dd/yy");
        dateTimeOutValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        empHomePage.add(dateTimeOutValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 452, 110, 30));

        jSeparator5.setForeground(new java.awt.Color(187, 187, 187));
        empHomePage.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 494, 860, 18));

        timeIn.setBorder(null);
        timeIn.setText("Time in");
        timeIn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        timeIn.setHideActionText(true);
        timeIn.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        timeIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        timeIn.setIconTextGap(30);
        timeIn.setkBackGroundColor(new java.awt.Color(255, 153, 0));
        timeIn.setkBorderRadius(15);
        timeIn.setkEndColor(new java.awt.Color(153, 51, 255));
        timeIn.setkHoverEndColor(new java.awt.Color(204, 102, 255));
        timeIn.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        timeIn.setkHoverStartColor(new java.awt.Color(51, 51, 255));
        timeIn.setkIndicatorThickness(50);
        timeIn.setkPressedColor(new java.awt.Color(102, 0, 255));
        timeIn.setkSelectedColor(new java.awt.Color(0, 0, 204));
        timeIn.setkStartColor(new java.awt.Color(0, 0, 255));
        timeIn.setRequestFocusEnabled(false);
        timeIn.setRolloverEnabled(false);
        timeIn.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        timeIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeInActionPerformed(evt);
            }
        });
        empHomePage.add(timeIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 504, 180, 40));

        timeOut.setBorder(null);
        timeOut.setText("Time out");
        timeOut.setEnabled(false);
        timeOut.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        timeOut.setHideActionText(true);
        timeOut.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        timeOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        timeOut.setIconTextGap(30);
        timeOut.setkBackGroundColor(new java.awt.Color(255, 153, 0));
        timeOut.setkBorderRadius(15);
        timeOut.setkEndColor(new java.awt.Color(255, 102, 102));
        timeOut.setkHoverEndColor(new java.awt.Color(51, 51, 255));
        timeOut.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        timeOut.setkHoverStartColor(new java.awt.Color(153, 51, 255));
        timeOut.setkIndicatorThickness(10);
        timeOut.setkPressedColor(new java.awt.Color(102, 0, 255));
        timeOut.setkSelectedColor(new java.awt.Color(0, 0, 204));
        timeOut.setkStartColor(new java.awt.Color(153, 0, 204));
        timeOut.setRequestFocusEnabled(false);
        timeOut.setRolloverEnabled(false);
        timeOut.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        timeOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeOutActionPerformed(evt);
            }
        });
        empHomePage.add(timeOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 504, 180, 40));

        timeInTimeOutRecordedLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        timeInTimeOutRecordedLabel.setForeground(new java.awt.Color(0, 0, 204));
        timeInTimeOutRecordedLabel.setText("Attendance recorded. Thank you!");
        empHomePage.add(timeInTimeOutRecordedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 554, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(empHomePage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 590, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(empHomePage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fileALeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileALeaveActionPerformed
        FileALeave frame = new FileALeave((Employee) user, this);
        frame.setVisible(true);
    }//GEN-LAST:event_fileALeaveActionPerformed

    private void timeInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeInActionPerformed
        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.timeIn(user.getEmployeeID());
        timeIn.setEnabled(false);
        timeIn.setFocusable(false);
        timeIn.repaint();  // Force UI update
        timeOut.setEnabled(true);
        timeInTimeOutRecordedLabel.setVisible(true);
        
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
        String formattedTime = timeFormatter.format(new Date());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormatter.format(new Date());
        timeInLabelValue.setText(formattedTime);
        dateTimeInValue.setText(formattedDate);
    }//GEN-LAST:event_timeInActionPerformed

    private void timeOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeOutActionPerformed
        EmployeeDao employeeDao = new EmployeeDao();
        employeeDao.timeOut(user.getEmployeeID());
        timeOut.setEnabled(false);  // Disable Time Out button after clicking
        timeOut.repaint();  // Force UI update
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
        String formattedTime = timeFormatter.format(new Date());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = dateFormatter.format(new Date());
        timeOutValue.setText(formattedTime);
        dateTimeOutValue.setText(formattedDate);
        timeInTimeOutRecordedLabel.setVisible(true);
    }//GEN-LAST:event_timeOutActionPerformed

    private void jScrollPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane1MouseWheelMoved
        int rotation = evt.getWheelRotation();
                JScrollBar verticalScrollBar = jScrollPane1.getVerticalScrollBar();
                
                // Control the scroll speed here (higher value = faster scroll)
                int scrollAmount = rotation * 38;  // Adjust this multiplier for smoothness
                
                verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
    }//GEN-LAST:event_jScrollPane1MouseWheelMoved

    private void jScrollPane2MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane2MouseWheelMoved
        int rotation = evt.getWheelRotation();
                JScrollBar verticalScrollBar = jScrollPane2.getVerticalScrollBar();
                
                // Control the scroll speed here (higher value = faster scroll)
                int scrollAmount = rotation * 38;  // Adjust this multiplier for smoothness
                
                verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
    }//GEN-LAST:event_jScrollPane2MouseWheelMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateHiredLabel;
    private javax.swing.JLabel dateHiredLabelValue;
    private javax.swing.JLabel dateTimeInLabel;
    private javax.swing.JLabel dateTimeInValue;
    private javax.swing.JLabel dateTimeOutLabel;
    private javax.swing.JLabel dateTimeOutValue;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel departmentLabel1;
    private javax.swing.JLabel departmentLabelValue;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel emailLabelValue;
    private javax.swing.JPanel empHomePage;
    private javax.swing.JLabel empNum;
    private javax.swing.JLabel empNumberValue;
    private javax.swing.JLabel empPhoto;
    private com.k33ptoo.components.KButton fileALeave;
    private javax.swing.JLabel firstName;
    private javax.swing.JLabel firstNameValue;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lastName;
    private javax.swing.JLabel lastNameValue;
    private javax.swing.JLabel leaveCreditsLabel;
    private javax.swing.JTable leaveCreditsTable;
    private javax.swing.JLabel positionLabelValue1;
    private javax.swing.JLabel recordsLabel;
    private javax.swing.JTable recordsTable;
    private com.k33ptoo.components.KButton timeIn;
    private javax.swing.JLabel timeInLabel;
    private javax.swing.JLabel timeInLabelValue;
    private javax.swing.JLabel timeInTimeOutRecordedLabel;
    private com.k33ptoo.components.KButton timeOut;
    private javax.swing.JLabel timeOutLabel;
    private javax.swing.JLabel timeOutValue;
    // End of variables declaration//GEN-END:variables
}
