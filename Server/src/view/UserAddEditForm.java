package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import beans.GroupRepository;
import beans.UserRepository;
import controller.Controller;

public class UserAddEditForm extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserRepository user;
	private boolean service = false;
	private JPanel contentPane;
	private JTextField userNamefield;
	private JTextField firstnameField;
	private JTextField lastnameField;
	private JTextField passwordField;
	private JTextField confirmPasswordField;
	private JRadioButton serviceradioButton;
	private JList <GroupRepository>list;
	private JPanel listAndRadioButtonPanel;
	private JButton restartButton;
	private JButton addButton;

	/**
	 * Create the frame.
	 */
	public UserAddEditForm(UserRepository user) {
		this.user = user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 440);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//Panel qui contient le formulaire de saisie et le titre et icon
		JPanel formPanel = new JPanel();
		formPanel.setPreferredSize(new Dimension(350, 10));
		formPanel.setOpaque(false);
		contentPane.add(formPanel, BorderLayout.WEST);
		formPanel.setLayout(new BorderLayout(0, 0));
		
		//Panel qui contient le titre et icone 
		JPanel titlePanel = new JPanel();
		titlePanel.setOpaque(false);
		titlePanel.setPreferredSize(new Dimension(10, 70));
		formPanel.add(titlePanel, BorderLayout.NORTH);
		titlePanel.setLayout(new BorderLayout(0, 0));
		
		//Titre
		JLabel titleLabel = new JLabel("Add User");
		//Icon
		titleLabel.setIcon(new ImageIcon("res//myLogo (2).png"));
		titleLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		titleLabel.setForeground(new Color(255, 0, 0));
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		
		//Panel ui contient le formulaire
		JPanel formZone = new JPanel();
		formZone.setOpaque(false);
		formPanel.add(formZone, BorderLayout.CENTER);
		formZone.setLayout(new BoxLayout(formZone, BoxLayout.PAGE_AXIS));
		
		//Label de username et TextField
		JPanel userNamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,20,20));
		userNamePanel.setPreferredSize(new Dimension(10, 30));
		userNamePanel.setOpaque(false);
		formZone.add(userNamePanel);
		
		JLabel userNameLabel = new JLabel("UserName");
		userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		userNameLabel.setForeground(new Color(255, 0, 0));
		userNamePanel.add(userNameLabel);
		
		userNamefield = new JTextField();
		if(user != null) {
			this.service = user.isCampusUser();
			titleLabel.setText("Edit User");
			userNamefield.setText(user.getUsername());
			userNamefield.setEnabled(false);
		}
			
		userNamefield.setPreferredSize(new Dimension(7, 25));
		userNamefield.setFont(new Font("Tahoma", Font.PLAIN, 13));
		userNamefield.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		userNamePanel.add(userNamefield);
		userNamefield.setColumns(20);
		
		//Label de firstName et TextField
		JPanel firstNamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,20,20));
		firstNamePanel.setPreferredSize(new Dimension(10, 30));
		firstNamePanel.setOpaque(false);
		formZone.add(firstNamePanel);
		
		JLabel firstNameLabel = new JLabel("FirstName ");
		firstNameLabel.setForeground(Color.RED);
		firstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		firstNamePanel.add(firstNameLabel);
		
		firstnameField = new JTextField();
		if(user != null) {
			firstnameField.setText(user.getFirstname());
		}
		firstnameField.setPreferredSize(new Dimension(7, 25));
		firstnameField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		firstnameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		firstnameField.setColumns(20);
		firstNamePanel.add(firstnameField);
		
		//Label de lastName et TextField
		JPanel lastNamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,20,20));
		lastNamePanel.setOpaque(false);
		lastNamePanel.setPreferredSize(new Dimension(10, 30));
		formZone.add(lastNamePanel);
		
		JLabel lastNameLabel = new JLabel("LastName ");
		lastNameLabel.setForeground(Color.RED);
		lastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lastNamePanel.add(lastNameLabel);
		
		lastnameField = new JTextField();
		if(user != null) {
			lastnameField.setText(user.getName());
		}
		lastnameField.setPreferredSize(new Dimension(7, 25));
		lastnameField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		lastnameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lastnameField.setColumns(20);
		lastNamePanel.add(lastnameField);
		
		//Label de password et TextField
		JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,20,20));
		passwordPanel.setOpaque(false);
		formZone.add(passwordPanel);
		passwordPanel.setPreferredSize(new Dimension(10, 30));
		
		JLabel passwordLabel = new JLabel("Password  ");
		passwordLabel.setForeground(Color.RED);
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordPanel.add(passwordLabel);
		
		passwordField = new JTextField();
		if(user != null) {
			passwordField.setText(user.getPassword());
		}
		passwordField.setPreferredSize(new Dimension(7, 25));
		passwordField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		passwordField.setColumns(20);
		passwordPanel.add(passwordField);
		
		//Label de confirmPassword et TextField
		JPanel confirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,20,20));
		confirmPasswordPanel.setOpaque(false);
		formZone.add(confirmPasswordPanel);
		confirmPasswordPanel.setPreferredSize(new Dimension(10, 30));
		
		JLabel confirmPasswordLabel = new JLabel("Confirm    ");
		confirmPasswordLabel.setForeground(Color.RED);
		confirmPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		confirmPasswordPanel.add(confirmPasswordLabel);
		
		confirmPasswordField = new JTextField();
		if(user != null) {
			confirmPasswordField.setText(user.getPassword());
		}
		confirmPasswordField.setPreferredSize(new Dimension(7, 25));
		confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		confirmPasswordField.setColumns(20);
		confirmPasswordField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		confirmPasswordPanel.add(confirmPasswordField);
		
		//Panel qui contient bouton d'envoie et et reinitialistation
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
		buttonPanel.setOpaque(false);
		buttonPanel.setPreferredSize(new Dimension(10, 70));
		formPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		restartButton = new JButton("Restart");
		restartButton.setForeground(Color.WHITE);
		restartButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		restartButton.setBackground(new Color(255, 0, 51));
		restartButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		restartButton.setPreferredSize(new Dimension(100, 30));
		restartButton.addActionListener(this);
		buttonPanel.add(restartButton);
		
		addButton = new JButton("Add");
		if(user != null)
			addButton.setText("Edit");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addButton.setForeground(Color.WHITE);
		addButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		addButton.setBackground(new Color(255, 0, 51));
		addButton.setPreferredSize(new Dimension(100, 30));
		addButton.addActionListener(this);
		buttonPanel.add(addButton);
		
		//Panel qui contient la list de groups pour selectionner
		JPanel userListPanel = new JPanel();
		userListPanel.setOpaque(false);
		contentPane.add(userListPanel);
		userListPanel.setLayout(new BorderLayout(0, 0));
		
		listAndRadioButtonPanel = new JPanel();
		listAndRadioButtonPanel.setOpaque(false);
		userListPanel.add(listAndRadioButtonPanel, BorderLayout.CENTER);
		
		serviceradioButton = new JRadioButton("Service");
		if(service)
			serviceradioButton.setSelected(true);
		if(user != null)
			serviceradioButton.setEnabled(false);
		serviceradioButton.addActionListener(this);
		serviceradioButton.setForeground(Color.RED);
		serviceradioButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		serviceradioButton.setOpaque(false);
		listAndRadioButtonPanel.add(serviceradioButton);
		createGroupJList();
		
		JPanel emptyPanel = new JPanel();
		emptyPanel.setOpaque(false);
		emptyPanel.setPreferredSize(new Dimension(10, 70));
		userListPanel.add(emptyPanel, BorderLayout.NORTH);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setResizable(false);
		setVisible(true);
	}

	private void createGroupJList() {
		NavigableSet<GroupRepository> allGroups = Controller.getInstance().getGroups(service);
		listAndRadioButtonPanel.removeAll();
		listAndRadioButtonPanel.add(serviceradioButton);
		DefaultListModel<GroupRepository> model = new DefaultListModel<>();
		for(Iterator<GroupRepository> groups = allGroups.iterator();groups.hasNext();) {
			model.addElement(groups.next());
		}
		list = new JList<GroupRepository>();
		list.setModel(model);
		list.setBackground(new Color(240, 240, 240));
		list.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JScrollPane scrollPan = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPan.setBorder(null);
		scrollPan.setPreferredSize(new Dimension(250, 280));
		scrollPan.setBorder(new LineBorder(Color.RED));
		listAndRadioButtonPanel.add(scrollPan);		
		listAndRadioButtonPanel.updateUI();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == serviceradioButton) {
			if(serviceradioButton.isSelected()) {
				service = true;
				createGroupJList();
			}else {
				service = false;
				createGroupJList();
			}
		}
		if(e.getSource() == addButton) {
		 boolean edit = false;
		 UserRepository userRep;
		 if(user != null) {//Cas d'edit
			 userRep = user;
			 edit = true;
		 }else {//Cas d'ajout
			 userRep = new UserRepository();
		 }
		  
		 if(verifieFields()) {
			userRep.setCampusUser(service);
			userRep.setFirstname(this.firstnameField.getText());
			userRep.setName(this.lastnameField.getText());
			userRep.setPassword(this.passwordField.getText());
			userRep.setUsername(this.userNamefield.getText());
			if(edit) {
				//EDIT USER
				userRep.setUserId(this.user.getUserId());
				String chaine = "User Information Updated";
				if(!Controller.getInstance().updateUser(userRep)) {
					chaine = "Information Not Updated";
				}
				if(!list.getSelectedValuesList().isEmpty()) {//ajout de utilisateur aux groupes
					List<GroupRepository> groups = list.getSelectedValuesList();
					String chaine2 = " !!!";
					for(GroupRepository group : groups) {
						if(!Controller.getInstance().addUserToGroup(userRep, group)) {
							chaine2 = "  But the group affectation is not updated !!!";
						}
					}
					chaine += chaine2;
					if(chaine2.equals(" !!!"))
						this.dispose();
				}
				JOptionPane.showMessageDialog(this, chaine);
				AdminSide.getInstance().createJList(AdminSide.getInstance().mode);
				this.dispose();
			}else {//ADD USER
				if(!list.getSelectedValuesList().isEmpty()) {
					String chaine = "User Information Updated";
					if(!Controller.getInstance().registerUser(userRep.getUsername(),userRep.getPassword()
							, userRep.getName(),userRep.getFirstname(), service,list.getSelectedValuesList())) {
						chaine = "Information Not Updated";
					}
					AdminSide.getInstance().createJList(AdminSide.getInstance().mode);
					JOptionPane.showMessageDialog(this, chaine);
					if(chaine == "User Information Updated")
						this.dispose();
					
				}else {//La list des groupes est vide
					JOptionPane.showMessageDialog(this, "Select at least one group please !!");
				}
						
			}
		 }
			
		}
		if(e.getSource() == restartButton) {//restartButton action
			this.firstnameField.setText("");
			this.lastnameField.setText("");
			this.passwordField.setText("");
			this.confirmPasswordField.setText("");
			if(user == null)
				this.userNamefield.setText("");
			createGroupJList();
		}
		
	}

	private boolean verifieFields() {
		boolean valid = true;
		if(firstnameField.getText().isBlank() || lastnameField.getText().isBlank() || passwordField.getText().isBlank() || userNamefield.getText().isBlank() ) {
			JOptionPane.showMessageDialog(this, "You left a field blank !!");
			valid = false;
		}
		else if(!passwordField.getText().equals(confirmPasswordField.getText())) {
			JOptionPane.showMessageDialog(this, "The two passwords aren't the same !!");
			valid = false;
		}
		return valid;
		
	}


}
