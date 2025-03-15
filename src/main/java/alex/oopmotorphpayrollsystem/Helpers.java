/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.oopmotorphpayrollsystem;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 *
 * @author Alex Resurreccion
 */
public class Helpers {
    public static class FieldWithLabel {
        private InputField field;
        private JLabel label;

        public FieldWithLabel(InputField field, JLabel label) {
            this.field = field;
            this.label = label;
        }

        public InputField getField() {
            return field;
        }

        public JLabel getLabel() {
            return label;
        }
    }
    
    public static class InputField {
        private JTextField textField;
        private JDateChooser dateChooser;

        public InputField(JTextField textField) {
            this.textField = textField;
        }

        public InputField(JDateChooser dateChooser) {
            this.dateChooser = dateChooser;
        }

        public JTextField getTextField() {
            return textField;
        }

        public JDateChooser getDateChooser() {
            return dateChooser;
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
    
    public static boolean validateForm(List<FieldWithLabel> fields) {
        boolean isClear = true;
        for (FieldWithLabel fieldWithLabel : fields) {
            InputField inputField = fieldWithLabel.getField();
            JLabel label = fieldWithLabel.getLabel();

            if (inputField.isEmpty()) {
                // If the field is empty, show the label
                label.setVisible(true);
                isClear = false;
            } else {
                // Otherwise, hide the label
                label.setVisible(false);
            }
        }
        return isClear;
    }
    
    public static void disableFields(List<FieldWithLabel> fields) {
        for (FieldWithLabel fieldWithLabel : fields) {
            InputField inputField = fieldWithLabel.getField();
            JLabel label = fieldWithLabel.getLabel();
            
            // Disable text field if it exists
            if (inputField.getTextField() != null) {
                inputField.getTextField().setEnabled(false);
            }

            // Disable date chooser if it exists
            if (inputField.getDateChooser() != null) {
                inputField.getDateChooser().setEnabled(false);
            }
            label.setVisible(false);
        }
    }
}



// Helper class to store field and label together

