import GUI.DangNhapGUI;
import com.formdev.flatlaf.FlatLightLaf;

import javax.naming.NamingException;
import javax.swing.*;
import java.rmi.RemoteException;

public class Runner {
    public static void main(String[] args) throws RemoteException, NamingException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        DangNhapGUI dangNhapGUI = new DangNhapGUI();
        dangNhapGUI.setVisible(true);
    }
}