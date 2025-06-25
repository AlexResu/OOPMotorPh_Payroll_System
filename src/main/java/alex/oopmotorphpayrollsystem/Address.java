/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;

/**
 *
 * @author Alex Resurreccion
 */
public class Address {
    private String street;
    private String barangay;
    private String city;
    private String province;
    private String zipcode;
    
    public Address() {}
    
    public Address(String street, String barangay, String city, String province, String zipcode){
        this.street = street;
        this.barangay = barangay;
        this.city = city;
        this.province = province;
        this.zipcode = zipcode;
    }
    
    //Getters
    public String getStreet() {
        return street;
    }
    
    public String getBarangay() {
        return barangay;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getProvince() {
        return province;
    }
    
    public String getZipcode() {
        return zipcode;
    }
    
    //Setters
    public void setStreet(String street) {
        this.street = street;
    }
    
    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    //To display full address  as a single string
//    public String getFullAddress() {
//        return street + ", " + barangay ", " + city ", " + province " " + zipcode; 
//    }
}
