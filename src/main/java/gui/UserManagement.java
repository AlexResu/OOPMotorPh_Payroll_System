/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import models.AccountAccess;
import models.AttendanceRecord;
import models.User;
import models.HRPersonnel;
import models.Employee;
import gui.AdminAddNewEmp;
import gui.ConfirmationPopUp;
import models.SystemAdministrator;
import dao.HRPersonnelDao;
import dao.SystemAdministratorDao;
import dao.UserDao;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex Resurreccion
 */
public class UserManagement extends javax.swing.JPanel {
    private AccountAccess account;
    private User user;
    private List<User> employees;
    private final UserDao userDao;
    private DefaultTableModel tableModel;
    
    /**
     * Creates new form HREmployeeManagement
     */
    public UserManagement() {
        initComponents();
        this.userDao = new UserDao();
        initializeTable();
        initListeners();
    }
    
    public UserManagement(AccountAccess account, User user) {
        this.account = account;
        this.user = user;
        this.userDao = new UserDao();
        initComponents();
        initializeTable();
        loadEmployeeList();;
        initListeners();
        if(user instanceof SystemAdministrator){
            employeeList.setText("User List");
            searchEmp.setText("Search User");
            addNew.setText("Add new user");
            unlockAccountButton.setVisible(true);
        } else {
            unlockAccountButton.setVisible(false);
        }    
    }
    
    private void initializeTable() {
        String[] columns = {"Employee no.", "Last Name", "First Name", "SSS no.", "Philhealth no.", "TIN no.", "PagIbig no.", "Lock Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        EmpMngTable.setModel(tableModel);
    }
    
    protected void loadEmployeeList() {
        HRPersonnelDao hrPersonnelDao = new HRPersonnelDao();
        SystemAdministratorDao systemAdminDao = new SystemAdministratorDao();
        
        if (user instanceof HRPersonnel || user instanceof SystemAdministrator) {
            // This is your original logic to fetch the full employee list
            if (user instanceof HRPersonnel) {
                HRPersonnel hrUser = (HRPersonnel) user;
                if (searchEmpValue.getText().trim().isEmpty()) {
                    employees = hrPersonnelDao.loadEmployeeList();
                } else {
                    employees = hrPersonnelDao.loadEmployeeList(searchEmpValue.getText());
                }
            } else {
                SystemAdministrator adminUser = (SystemAdministrator) user;
                if (searchEmpValue.getText().trim().isEmpty()) {
                    employees = systemAdminDao.loadEmployeeList();
                } else {
                    employees = systemAdminDao.loadEmployeeList(searchEmpValue.getText());
                }
            }
        }
        
        tableModel.setRowCount(0); // Clear the table before populating
        if (employees != null) {
            for (User emp : employees) {
                // For each employee, get their lock status
                boolean isLocked = userDao.isAccountLocked(String.valueOf(emp.getEmployeeID()));
                
                Object[] rowData = {
                    emp.getEmployeeID(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilhealthNumber(),
                    emp.getTinNumber(),
                    emp.getPagibigNumber(),
                    isLocked ? "Locked" : "Active" // The 8th column
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void initListeners(){
            EmpMngTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    // If at least one row is selected, enable the button
                    if (EmpMngTable.getSelectedRowCount() > 0) {
                        view.setEnabled(true);
                        edit.setEnabled(true);
                        delete.setEnabled(true);
                        unlockAccountButton.setEnabled(true);
                    } else {
                        view.setEnabled(false);
                        edit.setEnabled(false);
                        delete.setEnabled(false);
                        unlockAccountButton.setEnabled(false);
                    }
                }
            });
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        employeeList = new javax.swing.JLabel();
        searchEmp = new javax.swing.JLabel();
        searchEmpValue = new javax.swing.JTextField();
        adminEmpMngSearchButton = new javax.swing.JButton();
        addNew = new javax.swing.JButton();
        view = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        EmpMngTable = new javax.swing.JTable();
        unlockAccountButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(860, 590));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        employeeList.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        employeeList.setForeground(new java.awt.Color(0, 0, 204));
        employeeList.setText("Employee List");
        add(employeeList, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        searchEmp.setText("Search Employee");
        add(searchEmp, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 140, -1));
        add(searchEmpValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 180, 30));

        adminEmpMngSearchButton.setForeground(new java.awt.Color(255, 255, 255));
        adminEmpMngSearchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchIcon.png"))); // NOI18N
        adminEmpMngSearchButton.setFocusPainted(false);
        adminEmpMngSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminEmpMngSearchButtonActionPerformed(evt);
            }
        });
        add(adminEmpMngSearchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 30, 30));

        addNew.setBackground(new java.awt.Color(0, 153, 51));
        addNew.setForeground(new java.awt.Color(255, 255, 255));
        addNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons8-add-48.png"))); // NOI18N
        addNew.setText("Add new employee");
        addNew.setBorder(null);
        addNew.setFocusPainted(false);
        addNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewActionPerformed(evt);
            }
        });
        add(addNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, 190, 40));

        view.setBackground(new java.awt.Color(0, 0, 204));
        view.setForeground(new java.awt.Color(255, 255, 255));
        view.setText("View");
        view.setBorder(null);
        view.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        view.setEnabled(false);
        view.setFocusPainted(false);
        view.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewMouseClicked(evt);
            }
        });
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        add(view, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, 140, 30));

        edit.setBackground(new java.awt.Color(255, 102, 0));
        edit.setForeground(new java.awt.Color(255, 255, 255));
        edit.setText("Edit");
        edit.setBorder(null);
        edit.setEnabled(false);
        edit.setFocusPainted(false);
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        add(edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 140, 30));

        delete.setBackground(new java.awt.Color(204, 0, 0));
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setText("Delete");
        delete.setBorder(null);
        delete.setEnabled(false);
        delete.setFocusPainted(false);
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        add(delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 110, 140, 30));

        jScrollPane4.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                jScrollPane4MouseWheelMoved(evt);
            }
        });

        EmpMngTable.setAutoCreateRowSorter(true);
        EmpMngTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        EmpMngTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"10001", "Garcia", "Manuel III", "10/11/1983", "966-860-270", "Regular", "Chief Executive Officer"},
                {"10002", "Lim", "Antonio", "06/19/1988", "171-867-411", "Regular", "Chief Operating Officer"},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Employee no.", "Last Name", "First Name", "SSS no.", "Philhealth no.", "TIN no.", "PagIbig no."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EmpMngTable.setGridColor(new java.awt.Color(153, 153, 153));
        EmpMngTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
        EmpMngTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        EmpMngTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        EmpMngTable.setShowGrid(true);
        jScrollPane4.setViewportView(EmpMngTable);

        add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 860, 440));

        unlockAccountButton.setBackground(new java.awt.Color(204, 0, 204));
        unlockAccountButton.setForeground(new java.awt.Color(255, 255, 255));
        unlockAccountButton.setText("Unlock");
        unlockAccountButton.setBorder(null);
        unlockAccountButton.setEnabled(false);
        unlockAccountButton.setFocusPainted(false);
        unlockAccountButton.setMaximumSize(new java.awt.Dimension(25, 16));
        unlockAccountButton.setMinimumSize(new java.awt.Dimension(25, 16));
        unlockAccountButton.setPreferredSize(new java.awt.Dimension(25, 16));
        unlockAccountButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unlockAccountButtonActionPerformed(evt);
            }
        });
        add(unlockAccountButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 140, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void adminEmpMngSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminEmpMngSearchButtonActionPerformed
//        this.employeeListSearch = searchEmpValue.getText();
        loadEmployeeList();
    }//GEN-LAST:event_adminEmpMngSearchButtonActionPerformed

    private void addNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewActionPerformed
        AdminAddNewEmp newEmpFrame = new AdminAddNewEmp(account, user, this);
        newEmpFrame.setVisible(true);
    }//GEN-LAST:event_addNewActionPerformed

    private void viewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_viewMouseClicked

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        int selectedRow = EmpMngTable.getSelectedRow();
        // get selectedRow employee and call to new frame
        if (selectedRow != -1) { // Ensure a row is selected
            User selectedUser = employees.get(selectedRow); // Get the corresponding employee
            AdminAddNewEmp newEmpFrame = new AdminAddNewEmp(
                    account, user, selectedUser, "View", this);
            newEmpFrame.setVisible(true);
        }
    }//GEN-LAST:event_viewActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        int selectedRow = EmpMngTable.getSelectedRow();
        // get selectedRow employee and call to new frame
        if (selectedRow != -1) { // Ensure a row is selected
            User selectedUser = employees.get(selectedRow); // Get the corresponding employee
            AdminAddNewEmp newEmpFrame = new AdminAddNewEmp(
                    account, user, selectedUser, "Edit", this);
            newEmpFrame.setVisible(true);
        }
    }//GEN-LAST:event_editActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        int selectedRow = EmpMngTable.getSelectedRow();
        // get selectedRow employee and call to new frame
        if (selectedRow != -1) { // Ensure a row is selected
            User selectedUser = employees.get(selectedRow);
            ConfirmationPopUp newEmpFrame = new ConfirmationPopUp(user, selectedUser, this);
            newEmpFrame.setVisible(true);
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void jScrollPane4MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_jScrollPane4MouseWheelMoved
        int rotation = evt.getWheelRotation();
                JScrollBar verticalScrollBar = jScrollPane4.getVerticalScrollBar();
                
                // Control the scroll speed here (higher value = faster scroll)
                int scrollAmount = rotation * 38;  // Adjust this multiplier for smoothness
                
                verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
    }//GEN-LAST:event_jScrollPane4MouseWheelMoved

    private void unlockAccountButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unlockAccountButtonActionPerformed
        int selectedRow = EmpMngTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table to unlock.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int employeeId = Integer.parseInt(EmpMngTable.getValueAt(selectedRow, 0).toString());
            String lockStatus = EmpMngTable.getValueAt(selectedRow, 7).toString(); // Check the 8th column (index 7)

            if ("Active".equals(lockStatus)) {
                JOptionPane.showMessageDialog(this, "This account is already active.", "Account Active", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to unlock the account for employee ID: " + employeeId + "?",
                    "Confirm Account Unlock",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                SystemAdministratorDao adminDao = new SystemAdministratorDao();
                boolean success = adminDao.unlockUserAccount(employeeId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Account for employee " + employeeId + " has been successfully unlocked.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployeeList();
                    revalidate();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to unlock the account. The user may not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "The selected row does not contain a valid Employee ID.", "Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_unlockAccountButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable EmpMngTable;
    private javax.swing.JButton addNew;
    private javax.swing.JButton adminEmpMngSearchButton;
    private javax.swing.JButton delete;
    private javax.swing.JButton edit;
    private javax.swing.JLabel employeeList;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel searchEmp;
    private javax.swing.JTextField searchEmpValue;
    private javax.swing.JButton unlockAccountButton;
    private javax.swing.JButton view;
    // End of variables declaration//GEN-END:variables
}
