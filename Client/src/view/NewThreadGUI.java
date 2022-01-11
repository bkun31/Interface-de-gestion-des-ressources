package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import client.Client;
import data.Group;
import model.Model;

public class NewThreadGUI extends JFrame implements ActionListener,NotificationListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Client client;
	private JPanel contentPane;
	private JTextField objectField;
	private JButton restartButton;
	private JButton sendButton;
	private JTextArea messageField;
	private JComboBox <Group> groupsBox;
	

	/**
	 * Create the frame.
	 */
	public NewThreadGUI(Client client){
		this.client = client;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 482);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel formSaisiePanel = new JPanel();
		formSaisiePanel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		formSaisiePanel.setOpaque(false);
		formSaisiePanel.setBounds(44, 67, 333, 340);
		contentPane.add(formSaisiePanel);
		formSaisiePanel.setLayout(null);
		
		//Logo et Label de New Thread
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("res//myLogo.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(100,100,Image.SCALE_SMOOTH);
		ImageIcon image  = new ImageIcon(dimg);
		JLabel logo = new JLabel(image);
		logo.setBounds(10, 13, 61, 63);
		contentPane.add(logo);
		logo.setPreferredSize(new Dimension(100, 100));
		
		JLabel newThreadLabel = new JLabel("New Thread");
		newThreadLabel.setBounds(153, 13, 120, 50);
		contentPane.add(newThreadLabel);
		newThreadLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newThreadLabel.setForeground(Color.RED);
		newThreadLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		//Fin Logo et Label
		//ComboBox de group et Label Group
		JLabel recepientLabel = new JLabel("Recepient");
		recepientLabel.setForeground(new Color(255, 0, 51));
		recepientLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		recepientLabel.setBounds(31, 11, 72, 21);
		formSaisiePanel.add(recepientLabel);
		
		DefaultComboBoxModel <Group>groupList = new DefaultComboBoxModel<>();
		for(Group group : client.getGroups())
			groupList.addElement(group);
		groupsBox = new JComboBox<>(groupList);
		groupsBox.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		groupsBox.setBounds(43, 43, 199, 33);
		formSaisiePanel.add(groupsBox);
		//Fin ComboBox
		
		//TextField Object et Label Object
		JLabel objectlabel = new JLabel("Object");
		objectlabel.setForeground(new Color(255, 0, 51));
		objectlabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		objectlabel.setBounds(31, 87, 60, 21);
		formSaisiePanel.add(objectlabel);
		
		objectField = new JTextField();
		objectField.setText("     Object......");
		objectField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		objectField.setBounds(41, 118, 201, 33);
		formSaisiePanel.add(objectField);
		objectField.setColumns(10);
		//Fin TextField Object et Label Object
		
		//TextArea Message et Label Message
		JLabel messageLabel = new JLabel("Message");
		messageLabel.setForeground(new Color(255, 0, 51));
		messageLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		messageLabel.setBounds(31, 167, 72, 21);
		formSaisiePanel.add(messageLabel);
		
		//TextArea Message et Label Message
		messageField = new JTextArea();
		messageField.setText("  Message......");
		messageField.setLineWrap(true);
		messageField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		messageField.setBounds(41, 192, 262, 68);
		JScrollPane scrollPane = new JScrollPane(messageField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.setBounds(41, 192, 262, 68);
		formSaisiePanel.add(scrollPane);
		//Fin TextArea Message et Label Message
		
		//Button send
		sendButton = new JButton("Send");
		sendButton.setBackground(new Color(255, 0, 51));
		sendButton.setForeground(Color.WHITE);
		sendButton.setBounds(186, 282, 117, 33);
		formSaisiePanel.add(sendButton);
		sendButton.addActionListener(this);//ajout de action Listener
		
		//Button Restart
		restartButton = new JButton("Restart");
		restartButton.setBackground(new Color(255, 0, 51));
		restartButton.setForeground(Color.WHITE);
		restartButton.setBounds(31, 282, 117, 33);
		formSaisiePanel.add(restartButton);
		restartButton.addActionListener(this);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		Model.getInstance().addNotificationListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sendButton) {
			if(objectField.getText() == "" ||messageField.getText() == "") {
				JOptionPane.showMessageDialog(this, "Your Object Or Message is Empty !!!");
			}else {
				client.createNewDiscussionThread(groupsBox.getItemAt(groupsBox.getSelectedIndex())
						, objectField.getText(),messageField.getText());
				this.dispose();
			}
		}
		if(e.getSource() == restartButton) {
			objectField.setText("");
			messageField.setText("");
		}
		
	}

	@Override
	public void newMessageArrived() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageSeen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateInfo() {
		Thread monThread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DefaultComboBoxModel <Group>groupList = new DefaultComboBoxModel<>();
				groupsBox.removeAllItems();
				for(Group group : client.getGroups())
					groupList.addElement(group);
				groupsBox.setModel(groupList);
				groupsBox.updateUI();
			}
		 };
		 JOptionPane.showMessageDialog(this, "New Information Updated !!!");
		 monThread.start();
	}
	
}
