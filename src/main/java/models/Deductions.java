/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * The Deductions class is responsible for calculating various deductions (SSS, PhilHealth, Pag-IBIG, and tax) 
 * from an employee's salary. It uses external data (CSV file) for SSS deductions and applies specific rules for 
 * PhilHealth, Pag-IBIG, and tax deductions.
 * 
 * @author Alex Resurreccion
 */
public class Deductions {
    
    // Instance variables representing the deductions
    private double sssDeduction;
    private double philhealthDeduction;
    private double pagIbigDeduction;
    private double taxDeduction;
    
    /**
     * Constructor to initialize the deductions based on specific values.
     * 
     * @param sssDeduction The SSS deduction for the employee
     * @param philhealthDeduction The PhilHealth deduction for the employee
     * @param pagIbigDeduction The PagIBIG deduction for the employee
     * @param taxDeduction The tax deduction for the employee
     */
    public Deductions(double sssDeduction, double philhealthDeduction, double pagIbigDeduction, 
            double taxDeduction) {
        this.sssDeduction = sssDeduction;
        this.philhealthDeduction = philhealthDeduction;
        this.pagIbigDeduction = pagIbigDeduction;
        this.taxDeduction =  taxDeduction;
    }
    
    // Default constructor if no deductions are specified
    public Deductions() {}

    // Getter and Setter methods for each deduction field
    public double getSssDeduction() {
        return sssDeduction;
    }

    public void setSssDeduction(double sssDeduction) {
        this.sssDeduction = sssDeduction;
    }

    public double getPhilhealthDeduction() {
        return philhealthDeduction;
    }

    public void setPhilhealthDeduction(double philhealthDeduction) {
        this.philhealthDeduction = philhealthDeduction;
    }

    public double getPagIbigDeduction() {
        return pagIbigDeduction;
    }

    public void setPagIbigDeduction(double pagIbigDeduction) {
        this.pagIbigDeduction = pagIbigDeduction;
    }

    public double getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(double taxDeduction) {
        this.taxDeduction = taxDeduction;
    }
    
    /**
     * Constructor to initialize deductions based on the employee's basic salary.
     * Calculates the SSS, PhilHealth, and PagIBIG deductions based on the salary.
     * 
     * @param basicSalary The employee's basic salary
     */
    public Deductions(double basicSalary) {
        calculateSssDeduction(basicSalary);
        calculatePhilhealthDeduction(basicSalary);
        calculatePagibigDeduction(basicSalary);
    }
    
    /**
     * Calculates the SSS deduction based on the employee's basic salary.
     * It uses data from an external CSV file to determine the deduction amount.
     * 
     * @param basicSalary The basic salary of the employee
     */
    private void calculateSssDeduction(double basicSalary) {
        try{
            // Open the file containing SSS contribution data
            File myObj = new File(getClass().getClassLoader().getResource("SSS Contribution.csv").toURI());
            ArrayList<Double> amountDeduction;
            ArrayList<Double> salaryTable;
            // Initialize arrays to store salary table and corresponding deduction amounts
            try (Scanner scanner = new Scanner(myObj)) {
                amountDeduction = new ArrayList<>();
                salaryTable = new ArrayList<>();
                // Read data from the file
                while (scanner.hasNextLine()) {
                    String data = scanner.nextLine();
                    String[] separatedValue = data.split(",", 2);
                    // Parse and store salary and deduction amount
                    salaryTable.add(Double.valueOf(separatedValue[0]));
                    amountDeduction.add(Double.valueOf(separatedValue[1]));
                }
            }
            // Determine the deduction amount based on the provided basic salary
            if (basicSalary <= salaryTable.get(1)){
                this.sssDeduction = amountDeduction.get(0);
            }
            
            int arrayIndex = amountDeduction.size() - 1;
            while(arrayIndex > 0){
                if (basicSalary > salaryTable.get(arrayIndex)){
                    this.sssDeduction = amountDeduction.get(arrayIndex);
                }
                arrayIndex--;
            }
//            for(int arrayIndex = amountDeduction.size() - 1; arrayIndex > 0; arrayIndex--){
//               if (basicSalary > salaryTable.get(arrayIndex)){
//                    return amountDeduction.get(arrayIndex);
//                } 
//            }
             // Throw FileNotFoundException if the file is not found
        } catch(FileNotFoundException e) {
            System.out.println(e);
        } catch(Exception e){
            System.out.println(e);
        }
    }
    
    /**
     * Calculates the PhilHealth deduction based on the basic salary.
     * The deduction is 3% of the salary, but divided by 2 (employer-employee share).
     * 
     * @param basicSalary The basic salary of the employee
     */
    private void calculatePhilhealthDeduction(double basicSalary) {
        if(basicSalary <=0) {
           this.philhealthDeduction = 0;
        }
        this.philhealthDeduction = ((basicSalary * 0.03)/ 2); // 3% split between employer and employee
    }
    
    /**
     * Calculates the Pag-IBIG deduction based on the basic salary.
     * Pag-IBIG is 2% of salary for those earning above 1500, but capped at 100.
     * 
     * @param basicSalary The basic salary of the employee
     */
    private void calculatePagibigDeduction(double basicSalary) {
        double pagIbigDeduction;
        if(basicSalary > 1500){
            pagIbigDeduction = (basicSalary * 0.02); // 2% for salaries over 1500
        } else if(basicSalary >=1000){
            pagIbigDeduction = (basicSalary * 0.01); // 1% for salaries between 1000-1500
        } else {
            pagIbigDeduction = 0; // No deduction for salaries less than 1000
        }
        
        // Cap the Pag-IBIG deduction at 100
        if(pagIbigDeduction < 100){
            this.pagIbigDeduction =  pagIbigDeduction;
        } else {
            this.pagIbigDeduction = 100;
        }
    }
    
    /**
     * Calculates the tax deduction based on the employee's taxable income.
     * The tax deduction follows a tiered structure based on income brackets.
     * 
     * @param taxableIncome The taxable income of the employee
     */
    public void calculateTaxDeduction(double taxableIncome) {
        if(taxableIncome >= 666667 ){
            this.taxDeduction = (taxableIncome * 0.35); // 35% for income above 666,667
        }
        else if (taxableIncome >=166667){
            this.taxDeduction = (taxableIncome * 0.32); // 32% for income between 166,667 and 666,667         
        }
        else if (taxableIncome >=66667){
            this.taxDeduction = (taxableIncome * 0.30); // 30% for income between 66,667 and 166,667
        }
        else if (taxableIncome >= 33333){
            this.taxDeduction = (taxableIncome * 0.25); // 25% for income between 33,333 and 66,667
        }
        else if (taxableIncome >= 20833){
            this.taxDeduction = (taxableIncome * 0.20); // 20% for income between 20,833 and 33,333
        }
        else{
            this.taxDeduction = 0; // No tax deduction for income below 20,833
        }
    }
    
    /**
     * Calculates the total deductions (SSS, PhilHealth, Pag-IBIG, and tax).
     * 
     * @return The total deductions amount
     */
    public double calculateTotalDeductions() {
        return sssDeduction + philhealthDeduction + pagIbigDeduction + taxDeduction;
    }
}
