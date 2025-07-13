/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Component;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author Alex Resurreccion
 */
public class Helpers {
    public static class FieldWithLabel {
        private InputField field;
        private JLabel label;
        private List<String> validationRules;
        
        public FieldWithLabel(InputField field){
            this.field = field;
        }

        public FieldWithLabel(InputField field, JLabel label, List<String> validationRules) {
            this.field = field;
            this.label = label;
            this.validationRules = validationRules;
        }

        public InputField getField() {
            return field;
        }

        public JLabel getLabel() {
            return label;
        }
        
        public List<String> getValidationRules() {
            return validationRules;
        }
    }
    
    public static class InputField {
        private JTextField textField;
        private JDateChooser dateChooser;
        private JComboBox comboBox;

        public InputField(JTextField textField) {
            this.textField = textField;
        }

        public InputField(JDateChooser dateChooser) {
            this.dateChooser = dateChooser;
        }
        
        public InputField(JComboBox comboBox) {
            this.comboBox = comboBox;
        }

        public JTextField getTextField() {
            return textField;
        }

        public JDateChooser getDateChooser() {
            return dateChooser;
        }
        
        public JComboBox getComboBox() {
            return comboBox;
        }

        public String getValue() {
            if (textField != null) {
                return textField.getText();
            } else if (dateChooser != null) {
                Date date = dateChooser.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return (date != null) ? sdf.format(date) : "";
            }
            return "";
        }
        
        // Method to check if the field is empty
        public boolean isEmpty() {
                return getValue().trim().isEmpty();
            }
        }
    
        public static boolean isEmptyCheck(FieldWithLabel fieldWithLabel) {
            InputField inputField = fieldWithLabel.getField();
            if (inputField.isEmpty()) {
                fieldWithLabel.getLabel().setText("This is required.");
                fieldWithLabel.getLabel().setVisible(true);
                return false; // Field is empty, return false
            }
            return true; // Field is not empty, return true
        }
    
        public static boolean validateForm(List<FieldWithLabel> fields) {
            boolean isClear = true;

            for (FieldWithLabel fieldWithLabel : fields) {
                InputField inputField = fieldWithLabel.getField();
                JLabel label = fieldWithLabel.getLabel();
                List<String> validationRules = fieldWithLabel.getValidationRules();

                // Check for empty field first using the new isEmptyCheck method
                if (!isEmptyCheck(fieldWithLabel)) {
                    isClear = false;
                }
                
                System.out.println(fieldWithLabel.getField().getValue());

                // Check other validation rules
                for (String rule : validationRules) {
                    boolean isValid = true;
                    System.out.println(rule);
                    switch (rule) {
                        case "Required":
                            isValid = isEmptyCheck(fieldWithLabel);// "isEmpty" is already handled by isEmptyCheck, so we skip it
                            break;
                        case "AlphabetsAndSpecialChars":
                            isValid = isAlphabetsAndSpecialChars(fieldWithLabel);
                            break;
                        case "NumbersAndDash":
                            isValid = isNumbersAndDash(fieldWithLabel);
                            break;
                        case "AlphabetsAndDash":
                            isValid = isAlphabetsAndDash(fieldWithLabel);
                            break;
                        case "DateSmallerThan":
                            // You need to handle this case with two date fields, so skipping this for now
                            break;
                        case "AlphabetsOnly":
                            isValid = isAlphabetsOnly(fieldWithLabel);
                            break;
                        case "NumbersOnly":
                            isValid = isNumberOnly(fieldWithLabel);
                            break;
                        case "WorkingAge":
                            isValid = isAtLeast18YearsOld(fieldWithLabel);
                            break;
                        default:
                            // Handle unknown rules if necessary
                            break;
                    }

                    // If any validation fails, set isClear to false and break out of the loop
                    if (!isValid) {
                        System.out.println("Not clear");
                        isClear = false;
                        break; // No need to check further rules for this field
                    }
                }
            }

            return isClear;
        }
    
    public static void disableFields(List<FieldWithLabel> fields) {
        for (FieldWithLabel fieldWithLabel : fields) {
            InputField inputField = fieldWithLabel.getField();
            if(inputField.getComboBox() != null){
                inputField.getComboBox().setEditable(false);
                
                inputField.getComboBox().setFocusable(false); // optional: avoid focus highlight

                inputField.getComboBox().setUI(new BasicComboBoxUI() {
                    @Override
                    protected ComboPopup createPopup() {
                        return new BasicComboPopup(inputField.getComboBox()) {
                            @Override
                            public void show() {
                                // Do nothing: prevent the popup from showing
                            }
                        };
                    }
                });
            }
            
            // Disable text field if it exists
            if (inputField.getTextField() != null) {
                inputField.getTextField().setEditable(false);
            }

            // Disable date chooser if it exists
            if (inputField.getDateChooser() != null) {
                ((JTextFieldDateEditor) inputField.getDateChooser(
                    ).getDateEditor()).setEditable(false);
                
                for (Component comp : inputField.getDateChooser().getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setEnabled(false); // disables calendar popup button
                    }
                }
            }
            
            JLabel label = fieldWithLabel.getLabel();
            if(label != null){
                label.setVisible(false);
            }
        }
    }
    
    public static void isDateSmallerThan(
            FieldWithLabel fieldWithLabel1, FieldWithLabel fieldWithLabel2) {
        InputField inputField1 = fieldWithLabel1.getField();
        InputField inputField2 = fieldWithLabel2.getField();

        // Extract dates from both fields
        Date date1 = inputField1.getDateChooser() != null 
                ? inputField1.getDateChooser().getDate() : null;
        Date date2 = inputField2.getDateChooser() != null 
                ? inputField2.getDateChooser().getDate() : null;

        if (date1 != null && date2 != null && date1.after(date2)) {
            // If date1 is after date2, show the error label and message
            fieldWithLabel2.getLabel().setText(
                    "Date should be greater than or equal to " 
                    + fieldWithLabel1.getField().getTextField().getName());
            fieldWithLabel2.getLabel().setVisible(true);
        } else {
            // Hide the label if validation passes
            fieldWithLabel2.getLabel().setVisible(false);
        }
    }

    // 2. String Checker: Check if the field contains only letters (no numbers or special characters)
    public static boolean isAlphabetsOnly(FieldWithLabel fieldWithLabel) {
        InputField inputField = fieldWithLabel.getField();
        String fieldValue = inputField.getTextField() != null 
                ? inputField.getTextField().getText() : "";

        if (fieldValue != null && !fieldValue.matches("[a-zA-Z]+")) {
            // If the field contains non-alphabet characters, show the error label and message
            fieldWithLabel.getLabel().setText(
                    "Should only contain alphabets");
            fieldWithLabel.getLabel().setVisible(true);
            return false;
        } else {
            // Hide the label if validation passes
            fieldWithLabel.getLabel().setVisible(false);
            
        }
        return true;
    }

    // 3. Number Checker: Check if the field contains only numbers
    public static boolean isNumberOnly(FieldWithLabel fieldWithLabel) {
        InputField inputField = fieldWithLabel.getField();
        String fieldValue = inputField.getTextField() != null 
                ? inputField.getTextField().getText() : "";

        if (fieldValue != null && !fieldValue.matches("\\d+(\\.\\d+)?")) {
            // If the field contains non-numeric characters, show the error label and message
            fieldWithLabel.getLabel().setText(
                    "Should only contain numbers");
            fieldWithLabel.getLabel().setVisible(true);
            return false;
        } else {
            // Hide the label if validation passes
            fieldWithLabel.getLabel().setVisible(false);
        }
        return true;
    }

    // 4. Date Difference: Calculate the difference in days between two dates
    public static double getDateDifferenceInDays(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            long diffInMillis = Math.abs(date2.getTime() - date1.getTime());
            // Convert milliseconds to days and return as double
            return diffInMillis / (24.0 * 60 * 60 * 1000);  
        }
        return -1.0;
    }
    
    public static boolean isAlphabetsAndSpecialChars(FieldWithLabel fieldWithLabel) {
        InputField inputField = fieldWithLabel.getField();
        String fieldValue = inputField.getTextField() != null ? inputField.getTextField().getText() : "";

        // Match alphabets, spaces, and special characters (anything except digits)
        if (fieldValue != null && !fieldValue.matches("[a-zA-Z\\s\\p{Punct}]*")) {
            // If the field contains invalid characters, show the error label and message
            fieldWithLabel.getLabel().setText("Should only contain alphabets and special characters");
            fieldWithLabel.getLabel().setVisible(true);
            return false;
        } else {
            // Hide the label if validation passes
            fieldWithLabel.getLabel().setVisible(false);
        }
        return true;
    }

    // 2. Numbers and Dash (-): Check if the field contains only numbers and dash (not negative numbers)
    public static boolean isNumbersAndDash(FieldWithLabel fieldWithLabel) {
        InputField inputField = fieldWithLabel.getField();
        String fieldValue = inputField.getTextField() != null ? inputField.getTextField().getText() : "";

        // Match only positive numbers and dash (no leading dash for negative numbers)
        if (fieldValue != null && !fieldValue.matches("[0-9\\-]+") || fieldValue.startsWith("-")) {
            // If the field contains invalid characters or is a negative number, show the error label and message
            fieldWithLabel.getLabel().setText("Should only contain numbers and dash (no negative numbers)");
            fieldWithLabel.getLabel().setVisible(true);
            return false;
        } else {
            // Hide the label if validation passes
            fieldWithLabel.getLabel().setVisible(false);
        }
        return true;
    }

    // 3. Alphabets and Dash (-): Check if the field contains only alphabets and dash
    public static boolean isAlphabetsAndDash(FieldWithLabel fieldWithLabel) {
        InputField inputField = fieldWithLabel.getField();
        String fieldValue = inputField.getTextField() != null ? inputField.getTextField().getText() : "";

        // Match only alphabets and dashes
        if (fieldValue != null && !fieldValue.matches("[a-zA-Z-]+")) {
            // If the field contains invalid characters, show the error label and message
            fieldWithLabel.getLabel().setText("Should only contain alphabets and dash");
            fieldWithLabel.getLabel().setVisible(true);
            return false;
        } else {
            // Hide the label if validation passes
            fieldWithLabel.getLabel().setVisible(false);
        }
        return true;
    }
    
    public static boolean isAtLeast18YearsOld(FieldWithLabel fieldWithLabel) {
        InputField inputField = fieldWithLabel.getField();
        Date inputDate = inputField.getDateChooser().getDate();

        // Define the date format (assuming the format is "yyyy-MM-dd")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            
            // Get the current date
            Calendar today = Calendar.getInstance();
            
            // Create a calendar for 18 years ago
            Calendar eighteenYearsAgo = Calendar.getInstance();
            eighteenYearsAgo.add(Calendar.YEAR, -18); // Subtract 18 years from today's date
            
            // If the input date is before 18 years ago, it's valid
            if (inputDate != null && inputDate.before(eighteenYearsAgo.getTime())) {
                // Hide the error label if the date is valid
                fieldWithLabel.getLabel().setVisible(false);
                return true;
            } else {
                // Show error message if the date is not at least 18 years ago
                fieldWithLabel.getLabel().setText("Date must be at least 18 years ago.");
                fieldWithLabel.getLabel().setVisible(true);
                return false;
            }
        } catch (Exception e) {
            // Handle invalid date format
            System.out.println("Error date:" + e);
            fieldWithLabel.getLabel().setText("Invalid date format. Use yyyy-MM-dd.");
            fieldWithLabel.getLabel().setVisible(true);
            return false;
        }
    }
}



// Helper class to store field and label together

