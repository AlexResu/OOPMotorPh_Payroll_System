/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package alex.oopmotorphpayrollsystem;

import gui.LoginGui;
import java.util.Scanner;

/**
 * Main class to run the OOPMotorPhPayrollSystem application.
 * It initializes and displays the login GUI when the program starts.
 * @author Alex Resurreccion
 */
public class OOPMotorPhPayrollSystem {

    public static void main(String[] args) {
          // Create an instance of your JFrame subclass
        LoginGui frame = new LoginGui();
        
        // Display the frame
        frame.setVisible(true);
        Scanner scanner = new Scanner(System.in);
    }
}


