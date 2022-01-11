package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.Database;
import exception.DAOException;
import server.Server;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class DataBaseLogin extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField urlField;
	private JTextField DBUserNField;
	private JPasswordField passwordField;
	private JButton restartButton;
	private JButton loginButton;

	/**
	 * Create the frame.
	 */
	public DataBaseLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 400);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel titleLabel = new JLabel("DataBase Login");
		titleLabel.setPreferredSize(new Dimension(74, 70));
		titleLabel.setIcon(new ImageIcon("res//myLogo (2).png"));
		titleLabel.setForeground(Color.RED);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(titleLabel, BorderLayout.NORTH);
		
		JPanel formPanel = new JPanel();
		formPanel.setOpaque(false);
		contentPane.add(formPanel, BorderLayout.CENTER);
		formPanel.setLayout(null);
		
		JLabel urlDbLabel = new JLabel("DataBase URL");
		urlDbLabel.setForeground(Color.RED);
		urlDbLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		urlDbLabel.setBounds(31, 28, 104, 14);
		formPanel.add(urlDbLabel);
		
		urlField = new JTextField();
		urlField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		urlField.setText("jdbc:mysql://localhost:3306/test");
		urlField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		urlField.setBounds(157, 20, 260, 30);
		formPanel.add(urlField);
		urlField.setColumns(10);
		
		DBUserNField = new JTextField();
		DBUserNField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		DBUserNField.setText("root");
		DBUserNField.setColumns(10);
		DBUserNField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		DBUserNField.setBounds(157, 73, 260, 30);
		formPanel.add(DBUserNField);
		
		JLabel dataBaseUserNameLabel = new JLabel("User Name");
		dataBaseUserNameLabel.setForeground(Color.RED);
		dataBaseUserNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dataBaseUserNameLabel.setBounds(31, 81, 104, 14);
		formPanel.add(dataBaseUserNameLabel);
		
		JLabel dataBaseUserNameLabel_1 = new JLabel("Password");
		dataBaseUserNameLabel_1.setForeground(Color.RED);
		dataBaseUserNameLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		dataBaseUserNameLabel_1.setBounds(31, 135, 104, 14);
		formPanel.add(dataBaseUserNameLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		passwordField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		passwordField.setBounds(157, 129, 260, 30);
		formPanel.add(passwordField);
		
		restartButton = new JButton("Restart");
		restartButton.setForeground(Color.WHITE);
		restartButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		restartButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		restartButton.setBackground(new Color(255, 0, 51));
		restartButton.setBounds(76, 197, 130, 30);
		restartButton.addActionListener(this);
		formPanel.add(restartButton);
		
		loginButton = new JButton("Login");
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		loginButton.setBackground(new Color(255, 0, 51));
		loginButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		loginButton.setBounds(254, 197, 130, 30);
		loginButton.addActionListener(this);
		formPanel.add(loginButton);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton) {
			if(verifieField()) {
				try {
					Database.loginDataBase(this.urlField.getText(), DBUserNField.getText(), passwordField.getText());
				} catch (DAOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this,e1.getMessage());
				}
			}
			Thread thd = new Thread() {

				@Override
				public void run() {
					Server.start();
				}
				
			};
			thd.start();
			this.dispose();
		}
		
		if(e.getSource() == restartButton) {
			this.DBUserNField.setText("");
			this.urlField.setText("");
		}
	}

	private boolean verifieField() {
		// TODO Auto-generated method stub
		if(this.urlField.getText().isBlank() || this.DBUserNField.getText().isBlank()) {
			JOptionPane.showMessageDialog(this,"You left either username or url empty !!!");
			return false;
		}
		return true;
	}
	
}
