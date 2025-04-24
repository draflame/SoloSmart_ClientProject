/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author THANH PHU
 */

import Entity.TaiKhoan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class Loading extends JFrame {

    private JPanel contentPane;
    private JProgressBar progress;
    private JLabel txtLoad;
    private JLabel value;
    private JLabel lblH;
	private TaiKhoan taiKhoan_Entity;
	private Color color;

    public Loading(TaiKhoan taiKhoan) {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(color.decode("#ffffff"));
        panel.setBounds(0, 0, 900, 550);
        contentPane.add(panel);
        panel.setLayout(null);
    setIconImage(new ImageIcon(getClass().getResource("/Image/favicon_1.png")).getImage());
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setIcon(new ImageIcon(Loading.class.getResource("/Image/Exams-bro (1).png")));
        lblNewLabel.setBounds(0, 70, 900, 364);
        panel.add(lblNewLabel);

        txtLoad = new JLabel("Loading...");
        txtLoad.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtLoad.setForeground(new Color(0, 0, 0));
        txtLoad.setBounds(96, 442, 240, 30);
        panel.add(txtLoad);

        progress = new JProgressBar();
        progress.setBounds(96, 482, 706, 14);
        panel.add(progress);
        progress.setForeground(color.decode("#036867"));

        lblH = new JLabel("SoloSmart - Multiple Choice App");
        lblH.setForeground(new Color(0, 0, 0));
        lblH.setFont(new Font("Dialog", Font.BOLD, 30));
        lblH.setHorizontalAlignment(SwingConstants.CENTER);
        lblH.setBounds(0, 0, 900, 53);
        panel.add(lblH);

        value = new JLabel("0 %");
        value.setForeground(new Color(0, 0, 0));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setFont(new Font("Arial", Font.PLAIN, 15));
        value.setBounds(712, 442, 86, 30);
        panel.add(value);
    }
    
    public void updateProgress(int value, TaiKhoan taiKhoan) throws RemoteException {
        this.value.setText(value + " %");
        this.progress.setValue(value);

        switch (value) {
            case 10:
                txtLoad.setText("Turning On Modules ...");
                break;
            case 20:
                txtLoad.setText("Loading Modules ...");
                break;
            case 50:
                txtLoad.setText("Connecting to database ...");
                break;
            case 70:
                txtLoad.setText("Connection Successful!");
                break;
            case 90:
                txtLoad.setText("Launching Application ...");
                break;
            case 100:
            	this.dispose();
                Main_GUI trangChu = new Main_GUI(taiKhoan);
                trangChu.setVisible(true);
                break;
        }
    }
}
