package view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import client.Client;


public class LoginGui extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private final Client client;
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton restartButton = new JButton("Restart"); //boutton de reinitialisation
	private JButton loginButton = new JButton("Login"); //boutton d'envoie d'authentification

	/**
	 * Create the frame.
	 */
	public LoginGui(Client client) {
		this.client = client;
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		//Panel qui contient la photo et discription
		//Debut >>
		JPanel introPanel = new JPanel();
		introPanel.setBackground(Color.WHITE);
		introPanel.setBounds(0, 0, 281, 368);
		getContentPane().add(introPanel);
		introPanel.setLayout(null);
		//Photo
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("res//bg.jpg"));
		lblNewLabel.setBounds(0, 0, 281, 239);
		introPanel.add(lblNewLabel);
		//Fin Photo
		//Discription
		JLabel titleLabel = new JLabel("BKA");
		titleLabel.setForeground(new Color(255, 0, 51));
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		titleLabel.setBounds(116, 250, 66, 38);
		introPanel.add(titleLabel);
		
		JLabel discriptionLabel = new JLabel("Your Solution For \r\nBuilding Softwares");
		discriptionLabel.setForeground(new Color(255, 0, 51));
		discriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		discriptionLabel.setBounds(37, 277, 207, 47);
		introPanel.add(discriptionLabel);
		//Fin Discription
		//Fin Panel
		
		
		//Panel de formulaire de saisie
		//debut >>
		JPanel mainFormPanel = new JPanel();
		mainFormPanel.setOpaque(false);
		mainFormPanel.setBounds(273, 0, 344, 368);
		getContentPane().add(mainFormPanel);
		mainFormPanel.setLayout(null);
		//Photo et le titre
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("res//myLogo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
		ImageIcon image  = new ImageIcon(dimg);
		JLabel logo = new JLabel(image);
		logo.setPreferredSize(new Dimension(100, 100));
		logo.setBounds(new Rectangle(140, 24, 61, 63));
		logo.setBounds(81, 11, 61, 63);
		mainFormPanel.add(logo);
		
		JLabel loginLabel = new JLabel("Login");
		loginLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		loginLabel.setForeground(Color.RED);
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginLabel.setBounds(128, 11, 94, 50);
		mainFormPanel.add(loginLabel);
		//Fin photo et titre 
		//Debut de formulaire
		JPanel formSaisiePanel = new JPanel();
		formSaisiePanel.setLayout(null);
		formSaisiePanel.setOpaque(false);
		formSaisiePanel.setBounds(10, 72, 315, 265);
		mainFormPanel.add(formSaisiePanel);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setForeground(new Color(255, 0, 51));
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		usernameLabel.setBounds(27, 11, 103, 25);
		formSaisiePanel.add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(new Color(255, 0, 51));
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordLabel.setBounds(27, 122, 103, 14);
		formSaisiePanel.add(passwordLabel);
		
		usernameField = new JTextField();
		usernameField.setText("Username");
		usernameField.setOpaque(false);
		usernameField.setColumns(10);
		usernameField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		usernameField.setBounds(37, 47, 268, 37);
		formSaisiePanel.add(usernameField);
		
		passwordField = new JPasswordField();
		passwordField.setText("Password");
		passwordField.setOpaque(false);
		passwordField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		passwordField.setBounds(40, 147, 265, 37);
		formSaisiePanel.add(passwordField);
		
		//boutton de reinitialisation
		
		restartButton.setForeground(Color.WHITE);
		restartButton.setBackground(new Color(255, 0, 51));
		restartButton.setBounds(10, 202, 116, 37);
		formSaisiePanel.add(restartButton);
		restartButton.addActionListener(this);
		//boutton d'envoie
		
		loginButton.setForeground(Color.WHITE);
		loginButton.setBackground(new Color(255, 0, 51));
		loginButton.setBounds(166, 202, 122, 37);
		loginButton.addActionListener(this);
		formSaisiePanel.add(loginButton);
		//Fin de formulaire 
		//Fin Panel de formulaire de saisie
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 397);
		setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        if(e.getWindow().isVisible())
		            Client.getInstance().logout();   
		      }
		    });
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == loginButton) {
			String username = usernameField.getText();
			@SuppressWarnings("deprecation")
			String password = passwordField.getText();
			System.out.println("Attempt to login : "+username+" - "+password);
			if(!client.login(username, password)) {
				JOptionPane.showMessageDialog(this, "Authentication failure");
			}else {
				new ClientGuiV3(client);
				this.setVisible(false);
			}
			
			
		}
		if(e.getSource() == restartButton) {
			usernameField.setText("");
			passwordField.setText("");
		}
	}
}
