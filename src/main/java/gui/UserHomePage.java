/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;
import javax.swing.*;
import java.util.*;
import keeptoo.KGradientPanel;
import alex.oopmotorphpayrollsystem.AccountAccess;
import alex.oopmotorphpayrollsystem.User;
import alex.oopmotorphpayrollsystem.Employee;
import alex.oopmotorphpayrollsystem.HRPersonnel;
import alex.oopmotorphpayrollsystem.SystemAdministrator;
import dao.AccountAccessDao;
import gui.LoginGui;

/**
 *
 * @author Alex Resurreccion
 */

public class UserHomePage extends javax.swing.JFrame {
    public boolean selectedFrame = true;
    private com.k33ptoo.components.KButton activeButton = null;
    private AccountAccess account;
    private User user;
    /**
     * Creates new form UserHomePage
     */
    public UserHomePage() {
        initAll();
    }
    
     public UserHomePage(AccountAccess account) {
        this.account = account;
        initAll();
    }
     
    private void initAll(){
        AccountAccessDao accountDao = new AccountAccessDao();
        user = accountDao.getUserInfo(account.getEmployeeNumber(), account.getRole(), account.getIsAuthenticated());
        initComponents();
        welcomeLabel.setText("Welcome, " + user.getFirstName());
        adminProfile.setText(account.getRole());
        initializeMenus();
    }

    private void initializeMenus() {
        //Set default content
        javax.swing.JPanel homepage;
        if(!(user instanceof Employee)){
            homepage = new Dashboard(account, user);
        } else {
            homepage = new HomePagePanel(account, ((Employee) user));
        }
        replacePanelContent(homepage);
        
        createSidebar();
    }

    private KGradientPanel createSidebar() {
        KGradientPanel sidebar = new KGradientPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Add menu items for the selected user type
        List<String> menuItems = this.account.getPermissions();
        int baseYCoordinate = 220;
        int buttonYMargin = 60;
        
        boolean isEmployee = user instanceof Employee;

        for (String item : menuItems) {
            com.k33ptoo.components.KButton sideBarButton = new com.k33ptoo.components.KButton();
            sideBarButton.setText(item);
            sideBarButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            sideBarButton.setHideActionText(true);
            sideBarButton.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
            sideBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            sideBarButton.setIconTextGap(30);
            sideBarButton.setBorderPainted(false);
            sideBarButton.setkBackGroundColor(new java.awt.Color(255, 153, 0));
            sideBarButton.setkBorderRadius(30);
            sideBarButton.setkEndColor(new java.awt.Color(0, 0, 205));
            sideBarButton.setkHoverEndColor(new java.awt.Color(255, 153, 0));
            sideBarButton.setkHoverForeGround(new java.awt.Color(255, 255, 255));
            sideBarButton.setkHoverStartColor(new java.awt.Color(255, 153, 0));
            sideBarButton.setkIndicatorThickness(10);
            sideBarButton.setkPressedColor(new java.awt.Color(255, 102, 0));
            sideBarButton.setkSelectedColor(new java.awt.Color(255, 102, 0));
            sideBarButton.setkStartColor(new java.awt.Color(153, 51, 255));
            sideBarButton.setRequestFocusEnabled(false);
            sideBarButton.setRolloverEnabled(false);
            sideBarButton.setVerticalAlignment(javax.swing.SwingConstants.TOP);

            // Set default active button based on user type
            if (isEmployee && item.equals("Home")) {
                sideBarButton.setkStartColor(new java.awt.Color(255, 102, 0));  // Active color for Home (if Employee)
                sideBarButton.setkEndColor(new java.awt.Color(255, 102, 0));    // Active color for Home (if Employee)
                activeButton = sideBarButton;
            } else if (!isEmployee && item.equals("Dashboard")) {
                sideBarButton.setkStartColor(new java.awt.Color(255, 102, 0));  // Active color for Dashboard (if not Employee)
                sideBarButton.setkEndColor(new java.awt.Color(255, 102, 0));    // Active color for Dashboard (if not Employee)
                activeButton = sideBarButton;
            }

            sideBarButton.addActionListener(e -> {
                // Reset previous active button
                if (activeButton != null) {
                    activeButton.setkStartColor(new java.awt.Color(153, 51, 255)); // Restore purple
                    activeButton.setkEndColor(new java.awt.Color(0, 0, 205));     // Restore blue
                }

                // Set new active button color
                sideBarButton.setkStartColor(new java.awt.Color(255, 102, 0));
                sideBarButton.setkEndColor(new java.awt.Color(255, 102, 0));

                // Set the active button
                activeButton = sideBarButton;
                onMenuItemClick(item);
            });
            kGradientPanel1.add(sideBarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, baseYCoordinate, -1, 45));
            baseYCoordinate += buttonYMargin;
        }

        return sidebar;
    }
    
    private void onMenuItemClick(String menuItem) {
        // Logic to display content based on the clicked menu item
        javax.swing.JPanel newPanel;
        switch (menuItem) {
            case "Home":
                newPanel = new HomePagePanel(account, user);
                break;
            case "Dashboard":
                newPanel = new Dashboard(account, user);
                break;
            case "Employee Management":
                newPanel = new UserManagement(account, user);
                break;
            case "Attendance":
                newPanel = new Attendance(account, user);
                break;
            case "Payoll & Report":
                newPanel = new Payroll(account, ((HRPersonnel) user));
                break;
            case "Payslip":
                newPanel = new EmployeePayslip(account, user);
                break;
            case "Leave Request":
                newPanel = new LeaveRequests(account, ((HRPersonnel) user));
                break;
            case "My Details":
                newPanel = new EmpMyDetails(account, ((Employee) user));
                break;
            case "Payroll Generation":
                newPanel = new Payroll(account, ((HRPersonnel) user));
                break;
            case "Summary Report":
                newPanel = new SummaryReport(account, ((HRPersonnel) user));
                break;
            case "My Payslip":
                newPanel = new EmployeePayslip(account, user);
                break;
            case "User Management":
                newPanel = new UserManagement(account, user);
                break;
            default:
                return;
        }
        replacePanelContent(newPanel);
    }
    
    private void replacePanelContent(javax.swing.JPanel newPanel) {

        // Add the new panel (newPanel) to the same location (AbsoluteConstraints)
//        kGradientPanel1.add(jPanel, new AbsoluteConstraints(250, 70, 860, 590));

        // Revalidate and repaint to ensure the changes are displayed
        kGradientPanel1.revalidate();
        kGradientPanel1.repaint();
        pagePanel.removeAll();
        pagePanel.add(newPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 860, 590));
        pagePanel.revalidate();
        pagePanel.repaint();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        adminHomePanel1 = new javax.swing.JPanel();
        dateTimeOutLabel = new javax.swing.JLabel();
        timeOutLabel = new javax.swing.JLabel();
        timeInLabel = new javax.swing.JLabel();
        dateTimeInLabel = new javax.swing.JLabel();
        timeInTimeOutRecordedLabel = new javax.swing.JLabel();
        timeOutValue = new javax.swing.JLabel();
        dateTimeOutValue = new javax.swing.JLabel();
        recordsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        leaveCreditsTable = new javax.swing.JTable();
        leaveCreditsLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        recordsTable = new javax.swing.JTable();
        timeInLabelValue = new javax.swing.JLabel();
        dateTimeInValue = new javax.swing.JLabel();
        adminPhoto = new javax.swing.JLabel();
        employeeNumberLabel = new javax.swing.JLabel();
        employeeNumberValue = new javax.swing.JLabel();
        lastName = new javax.swing.JLabel();
        lastNameValue = new javax.swing.JLabel();
        firstName = new javax.swing.JLabel();
        firstNameValue = new javax.swing.JLabel();
        departmentLabel = new javax.swing.JLabel();
        positionLabelValue = new javax.swing.JLabel();
        dateHiredLabel = new javax.swing.JLabel();
        dateHiredLabelValue = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        emailLabelValue = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        adminProfile1 = new javax.swing.JLabel();
        departmentLabel1 = new javax.swing.JLabel();
        positionLabelValue1 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        loginkButton6 = new com.k33ptoo.components.KButton();
        loginkButton8 = new com.k33ptoo.components.KButton();
        jSeparator7 = new javax.swing.JSeparator();
        loginkButton10 = new com.k33ptoo.components.KButton();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        topPanel = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        adminProfile = new javax.swing.JLabel();
        logout = new javax.swing.JButton();
        motorPhLogo = new javax.swing.JLabel();
        welcomeLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        pagePanel = new javax.swing.JPanel();

        adminHomePanel1.setBackground(new java.awt.Color(255, 255, 255));
        adminHomePanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dateTimeOutLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dateTimeOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeOutLabel.setText("Date: ");
        adminHomePanel1.add(dateTimeOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 520, 50, 27));

        timeOutLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        timeOutLabel.setForeground(new java.awt.Color(0, 0, 0));
        timeOutLabel.setText("Time out:");
        adminHomePanel1.add(timeOutLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 480, 65, 27));

        timeInLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        timeInLabel.setForeground(new java.awt.Color(0, 0, 0));
        timeInLabel.setText("Time in:");
        adminHomePanel1.add(timeInLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, 65, 27));

        dateTimeInLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        dateTimeInLabel.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeInLabel.setText("Date: ");
        adminHomePanel1.add(dateTimeInLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 520, 50, 27));

        timeInTimeOutRecordedLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        timeInTimeOutRecordedLabel.setForeground(new java.awt.Color(0, 0, 204));
        timeInTimeOutRecordedLabel.setText("Attendance recorded. Thank you!");
        adminHomePanel1.add(timeInTimeOutRecordedLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 620, -1, -1));

        timeOutValue.setForeground(new java.awt.Color(0, 0, 0));
        timeOutValue.setText("  No time in yet");
        timeOutValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        adminHomePanel1.add(timeOutValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 480, 110, 30));

        dateTimeOutValue.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeOutValue.setText("  mm/dd/yy");
        dateTimeOutValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        adminHomePanel1.add(dateTimeOutValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 520, 110, 30));

        recordsLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        recordsLabel.setForeground(new java.awt.Color(0, 0, 0));
        recordsLabel.setText("Records");
        adminHomePanel1.add(recordsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 260, -1, -1));

        leaveCreditsTable.setBackground(new java.awt.Color(192, 237, 253));
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
        leaveCreditsTable.setGridColor(new java.awt.Color(153, 153, 153));
        leaveCreditsTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
        leaveCreditsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        leaveCreditsTable.setShowGrid(true);
        jScrollPane1.setViewportView(leaveCreditsTable);

        adminHomePanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 390, 110));

        leaveCreditsLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        leaveCreditsLabel.setForeground(new java.awt.Color(0, 0, 0));
        leaveCreditsLabel.setText("Leave Credits");
        adminHomePanel1.add(leaveCreditsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 190, -1));

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
                {null, null, null, null, null}
            },
            new String [] {
                "Leave Type", "Date from", "Days", "Remarks", "Approved by"
            }
        ));
        recordsTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
        recordsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(recordsTable);

        adminHomePanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 280, 390, 170));

        timeInLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        timeInLabelValue.setText("  No time in yet");
        timeInLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        adminHomePanel1.add(timeInLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 480, 110, 30));

        dateTimeInValue.setForeground(new java.awt.Color(0, 0, 0));
        dateTimeInValue.setText("  mm/dd/yy");
        dateTimeInValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        adminHomePanel1.add(dateTimeInValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 520, 110, 30));

        adminPhoto.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\MotorPhApp\\resources\\Employee Icon.png")); // NOI18N
        adminPhoto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(adminPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, 120));

        employeeNumberLabel.setForeground(new java.awt.Color(0, 0, 0));
        employeeNumberLabel.setText("Employee Number");
        adminHomePanel1.add(employeeNumberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        employeeNumberValue.setForeground(new java.awt.Color(0, 0, 0));
        employeeNumberValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeNumberValue.setText("10000");
        employeeNumberValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        employeeNumberValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(employeeNumberValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 100, 20));

        lastName.setForeground(new java.awt.Color(0, 0, 0));
        lastName.setText("Last Name");
        adminHomePanel1.add(lastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, -1, -1));

        lastNameValue.setForeground(new java.awt.Color(0, 0, 0));
        lastNameValue.setText("  Admin");
        lastNameValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lastNameValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(lastNameValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 150, 140, 26));

        firstName.setForeground(new java.awt.Color(0, 0, 0));
        firstName.setText("First Name");
        adminHomePanel1.add(firstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, -1, -1));

        firstNameValue.setForeground(new java.awt.Color(0, 0, 0));
        firstNameValue.setText("  User");
        firstNameValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        firstNameValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(firstNameValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 140, 26));

        departmentLabel.setForeground(new java.awt.Color(0, 0, 0));
        departmentLabel.setText("Department");
        adminHomePanel1.add(departmentLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, -1, -1));

        positionLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        positionLabelValue.setText("  Human Resourse");
        positionLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        positionLabelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(positionLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 140, 26));

        dateHiredLabel.setForeground(new java.awt.Color(0, 0, 0));
        dateHiredLabel.setText("Date Hired");
        adminHomePanel1.add(dateHiredLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 190, -1, -1));

        dateHiredLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        dateHiredLabelValue.setText("  May 15, 2024");
        dateHiredLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        dateHiredLabelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(dateHiredLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 210, 140, 26));

        emailLabel.setForeground(new java.awt.Color(0, 0, 0));
        emailLabel.setText("Email");
        adminHomePanel1.add(emailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, -1, -1));

        emailLabelValue.setForeground(new java.awt.Color(0, 0, 0));
        emailLabelValue.setText("  @example.com");
        emailLabelValue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        emailLabelValue.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(emailLabelValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 150, 140, 26));

        jSeparator10.setForeground(new java.awt.Color(204, 204, 204));
        adminHomePanel1.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 860, 10));

        jButton2.setBackground(new java.awt.Color(255, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\icons8-logout-48.png")); // NOI18N
        jButton2.setText("Log out");
        jButton2.setBorder(null);
        jButton2.setBorderPainted(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        adminHomePanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 100, 40));

        adminProfile1.setBackground(new java.awt.Color(204, 204, 204));
        adminProfile1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        adminProfile1.setForeground(new java.awt.Color(0, 0, 204));
        adminProfile1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\MotorPhApp\\resources\\UserIcon.png")); // NOI18N
        adminProfile1.setText("Admin User");
        adminHomePanel1.add(adminProfile1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 240, 50));

        departmentLabel1.setForeground(new java.awt.Color(0, 0, 0));
        departmentLabel1.setText("Position");
        adminHomePanel1.add(departmentLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, -1, -1));

        positionLabelValue1.setForeground(new java.awt.Color(0, 0, 0));
        positionLabelValue1.setText("  Human Resourse");
        positionLabelValue1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        positionLabelValue1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        adminHomePanel1.add(positionLabelValue1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 210, 140, 26));

        jSeparator11.setForeground(new java.awt.Color(187, 187, 187));
        adminHomePanel1.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 860, 18));

        loginkButton6.setText("Time out");
        loginkButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginkButton6.setHideActionText(true);
        loginkButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        loginkButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loginkButton6.setIconTextGap(30);
        loginkButton6.setkBackGroundColor(new java.awt.Color(255, 153, 0));
        loginkButton6.setkBorderRadius(15);
        loginkButton6.setkEndColor(new java.awt.Color(255, 102, 102));
        loginkButton6.setkHoverEndColor(new java.awt.Color(51, 51, 255));
        loginkButton6.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        loginkButton6.setkHoverStartColor(new java.awt.Color(153, 51, 255));
        loginkButton6.setkIndicatorThickness(10);
        loginkButton6.setkPressedColor(new java.awt.Color(102, 0, 255));
        loginkButton6.setkSelectedColor(new java.awt.Color(0, 0, 204));
        loginkButton6.setkStartColor(new java.awt.Color(153, 0, 204));
        loginkButton6.setRequestFocusEnabled(false);
        loginkButton6.setRolloverEnabled(false);
        loginkButton6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        loginkButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginkButton6ActionPerformed(evt);
            }
        });
        adminHomePanel1.add(loginkButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 560, 180, 50));

        loginkButton8.setText("File a Leave");
        loginkButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        loginkButton8.setHideActionText(true);
        loginkButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        loginkButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loginkButton8.setIconTextGap(30);
        loginkButton8.setkBackGroundColor(new java.awt.Color(255, 153, 0));
        loginkButton8.setkBorderRadius(15);
        loginkButton8.setkEndColor(new java.awt.Color(153, 51, 255));
        loginkButton8.setkHoverEndColor(new java.awt.Color(204, 102, 255));
        loginkButton8.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        loginkButton8.setkHoverStartColor(new java.awt.Color(51, 51, 255));
        loginkButton8.setkIndicatorThickness(10);
        loginkButton8.setkPressedColor(new java.awt.Color(102, 0, 255));
        loginkButton8.setkSelectedColor(new java.awt.Color(0, 0, 204));
        loginkButton8.setkStartColor(new java.awt.Color(0, 0, 255));
        loginkButton8.setRequestFocusEnabled(false);
        loginkButton8.setRolloverEnabled(false);
        loginkButton8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        loginkButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginkButton8ActionPerformed(evt);
            }
        });
        adminHomePanel1.add(loginkButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, 230, 40));

        jSeparator7.setForeground(new java.awt.Color(187, 187, 187));
        adminHomePanel1.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 860, 18));

        loginkButton10.setText("Time in");
        loginkButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginkButton10.setHideActionText(true);
        loginkButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        loginkButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loginkButton10.setIconTextGap(30);
        loginkButton10.setkBackGroundColor(new java.awt.Color(255, 153, 0));
        loginkButton10.setkBorderRadius(15);
        loginkButton10.setkEndColor(new java.awt.Color(153, 51, 255));
        loginkButton10.setkHoverEndColor(new java.awt.Color(204, 102, 255));
        loginkButton10.setkHoverForeGround(new java.awt.Color(255, 255, 255));
        loginkButton10.setkHoverStartColor(new java.awt.Color(51, 51, 255));
        loginkButton10.setkIndicatorThickness(10);
        loginkButton10.setkPressedColor(new java.awt.Color(102, 0, 255));
        loginkButton10.setkSelectedColor(new java.awt.Color(0, 0, 204));
        loginkButton10.setkStartColor(new java.awt.Color(0, 0, 255));
        loginkButton10.setRequestFocusEnabled(false);
        loginkButton10.setRolloverEnabled(false);
        loginkButton10.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        loginkButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginkButton10ActionPerformed(evt);
            }
        });
        adminHomePanel1.add(loginkButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 560, 180, 50));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        kGradientPanel1.setkEndColor(new java.awt.Color(0, 0, 204));
        kGradientPanel1.setkStartColor(new java.awt.Color(153, 0, 204));
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(1110, 655));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topPanel.setBackground(new java.awt.Color(255, 255, 255));
        topPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator6.setForeground(new java.awt.Color(204, 204, 204));
        topPanel.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 860, 20));

        adminProfile.setBackground(new java.awt.Color(204, 204, 204));
        adminProfile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        adminProfile.setForeground(new java.awt.Color(0, 0, 204));
        adminProfile.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\MotorPhApp\\resources\\UserIcon.png")); // NOI18N
        adminProfile.setText("Admin User");
        topPanel.add(adminProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 240, 60));

        logout.setBackground(new java.awt.Color(255, 255, 255));
        logout.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        logout.setForeground(new java.awt.Color(0, 0, 204));
        logout.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\OOPMotorPhPayrollSystem\\resources\\icons8-logout-48.png")); // NOI18N
        logout.setText("Log out");
        logout.setBorder(null);
        logout.setBorderPainted(false);
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        logout.setFocusPainted(false);
        logout.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        topPanel.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, 110, 40));

        kGradientPanel1.add(topPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 860, 70));

        motorPhLogo.setIcon(new javax.swing.ImageIcon("C:\\Users\\Alex Resurreccion\\Documents\\NetBeansProjects\\MotorPhApp\\resources\\aesthetics-removebg-preview1.png")); // NOI18N
        kGradientPanel1.add(motorPhLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 220, 140));

        welcomeLabel.setBackground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeLabel.setText(" Welcome, Admin!");
        welcomeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        kGradientPanel1.add(welcomeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 250, 50));

        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        kGradientPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 250, 20));

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));
        kGradientPanel1.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 250, 10));

        pagePanel.setBackground(new java.awt.Color(255, 255, 255));
        kGradientPanel1.add(pagePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 860, 600));

        getContentPane().add(kGradientPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        dispose();
        LoginGui frame = new LoginGui();
        frame.setVisible(true);
    }//GEN-LAST:event_logoutActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MousePressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void loginkButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginkButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginkButton6ActionPerformed

    private void loginkButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginkButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginkButton8ActionPerformed

    private void loginkButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginkButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginkButton10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main() {
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
            java.util.logging.Logger.getLogger(UserHomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserHomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserHomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserHomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserHomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adminHomePanel1;
    private javax.swing.JLabel adminPhoto;
    private javax.swing.JLabel adminProfile;
    private javax.swing.JLabel adminProfile1;
    private javax.swing.JLabel dateHiredLabel;
    private javax.swing.JLabel dateHiredLabelValue;
    private javax.swing.JLabel dateTimeInLabel;
    private javax.swing.JLabel dateTimeInValue;
    private javax.swing.JLabel dateTimeOutLabel;
    private javax.swing.JLabel dateTimeOutValue;
    private javax.swing.JLabel departmentLabel;
    private javax.swing.JLabel departmentLabel1;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel emailLabelValue;
    private javax.swing.JLabel employeeNumberLabel;
    private javax.swing.JLabel employeeNumberValue;
    private javax.swing.JLabel firstName;
    private javax.swing.JLabel firstNameValue;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JLabel lastName;
    private javax.swing.JLabel lastNameValue;
    private javax.swing.JLabel leaveCreditsLabel;
    private javax.swing.JTable leaveCreditsTable;
    private com.k33ptoo.components.KButton loginkButton10;
    private com.k33ptoo.components.KButton loginkButton6;
    private com.k33ptoo.components.KButton loginkButton8;
    private javax.swing.JButton logout;
    private javax.swing.JLabel motorPhLogo;
    private javax.swing.JPanel pagePanel;
    private javax.swing.JLabel positionLabelValue;
    private javax.swing.JLabel positionLabelValue1;
    private javax.swing.JLabel recordsLabel;
    private javax.swing.JTable recordsTable;
    private javax.swing.JLabel timeInLabel;
    private javax.swing.JLabel timeInLabelValue;
    private javax.swing.JLabel timeInTimeOutRecordedLabel;
    private javax.swing.JLabel timeOutLabel;
    private javax.swing.JLabel timeOutValue;
    private javax.swing.JPanel topPanel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
