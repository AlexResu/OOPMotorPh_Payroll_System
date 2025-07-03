package UnitTesing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.Deductions;

/**
 * Unit tests for the Deductions class.
 */
class DeductionsTest {
    
    private Deductions deductions;

    @BeforeEach
    void setUp() {
        deductions = new Deductions();
    }
    
    @Test
    void testCalculatePhilhealthDeduction() {
        deductions.setPhilhealthDeduction(0);
        deductions = new Deductions(30000);
        assertEquals(450, deductions.getPhilhealthDeduction(), 0.01);
    }

    @Test
    void testCalculatePagibigDeduction() {
        deductions = new Deductions(1600);
        assertEquals(32, deductions.getPagIbigDeduction(), 0.01);
    }

    @Test
    void testCalculatePagibigDeduction_CappedAt100() {
        deductions = new Deductions(10000);
        assertEquals(100, deductions.getPagIbigDeduction(), 0.01);
    }
    
    @Test
    void testCalculateTaxDeduction_NoTax() {
        deductions.calculateTaxDeduction(20000);
        assertEquals(0, deductions.getTaxDeduction(), 0.01);
    }
    
    @Test
    void testCalculateTaxDeduction_AboveThreshold() {
        deductions.calculateTaxDeduction(50000);
        assertEquals(12500, deductions.getTaxDeduction(), 0.01);
    }
    
    @Test
    void testCalculateTotalDeductions() {
        deductions = new Deductions(50000);
        deductions.calculateTaxDeduction(50000);
        double expectedTotal = deductions.getSssDeduction() + deductions.getPhilhealthDeduction() + deductions.getPagIbigDeduction() + deductions.getTaxDeduction();
        assertEquals(expectedTotal, deductions.calculateTotalDeductions(), 0.01);
    }
}

