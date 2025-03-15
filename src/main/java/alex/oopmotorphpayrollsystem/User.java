/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Alex Resurreccion
 */
public abstract class User {
    protected int employeeID;
    protected String password;
    protected String role;
    protected String firstName;
    protected String lastName;
    protected Date birthday;
    protected String position;
    protected String address;
    protected String phoneNumber;
    protected String sssNumber;
    protected String philhealthNumber;
    protected String tinNumber;
    protected String pagibigNumber; 
    
    public User(){}
    
    //Constuctor
    public User(int employeeID){
        this.employeeID = employeeID;
    }
    
    // Encapsulation - Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public Date getBirthday() {
        return birthday;
    }
    
    public String getPosition() {
        return position;
    }
    
    public String getAddress() {
        return address;
    }
    
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getSssNumber() {
        return sssNumber;
    }
    
    public String getPhilhealthNumber() {
        return philhealthNumber;
    }
    
    public String getTinNumber() {
        return tinNumber;
    }
    
    public String getPagibigNumber() {
        return pagibigNumber;
    }
    
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public void setSssNumber(String sssNumber) {
        this.sssNumber =  sssNumber;
    }
    
    public void setPhilhealthNumber(String philhealthNumber) {
        this.philhealthNumber = philhealthNumber;
    }
    
    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }
    
    public void setPagibigNumber(String pagibigNumber) {
        this.pagibigNumber = pagibigNumber;
    }
    
    public int getAccountRole(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getUserRole(this);
        try { 
            if (result.next()) {
                return result.getInt("role_id");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        mySQL.close();
        return 0;
    }
    
    public String getRoleName(){
        MySQL mySQL = new MySQL();
        ResultSet result = mySQL.getUserRole(this);
        try { 
            if (result.next()) {
                return result.getString("role_name");
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        mySQL.close();
        return "";
    }
} 
