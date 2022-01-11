package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import beans.GroupRepository;
import beans.UserRepository;
import controller.Controller;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.NavigableSet;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import java.awt.FlowLayout;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

public class GroupEditForm extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField groupNameField;
	private JButton editButton;
	private JButton restartButton;
	private JRadioButton serviceradioButton;
	private JPanel jListPanel;
	private JList<UserRepository> list;
	private boolean service = false;
	private boolean addOption = true;//Mode d'ajout des utilisateurs
	private JRadioButton usersOptionButton;
	private GroupRepository groupRep;

	/**
	 * Create the frame.
	 */
	public GroupEditForm(GroupRepository groupRep) {
		this.groupRep = groupRep;
		service = groupRep.isCampus_group();
		setBounds(100, 100, 640, 369);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel titleLabel = new JLabel("Edit Group");
		titleLabel.setIconTextGap(0);
		titleLabel.setIcon(new ImageIcon("res//myLogo (2).png"));
		titleLabel.setForeground(Color.RED);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setPreferredSize(new Dimension(51, 70));
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(titleLabel, BorderLayout.NORTH);
		
		//Panel qui contient le formulaire et la list des utilisateur
		JPanel formPanel = new JPanel();
		formPanel.setOpaque(false);
		contentPane.add(formPanel, BorderLayout.CENTER);
		formPanel.setLayout(new BorderLayout(0, 0));
		
		//Panel de formulaire de saisie
		JPanel inputPanel = new JPanel();
		inputPanel.setPreferredSize(new Dimension(320, 10));
		inputPanel.setOpaque(false);
		formPanel.add(inputPanel, BorderLayout.WEST);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
		
		//Panel qui contient label et textField de group name
		JPanel groupNamePanel = new JPanel();
		groupNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 30));
		groupNamePanel.setPreferredSize(new Dimension(10, 5));
		groupNamePanel.setOpaque(false);
		inputPanel.add(groupNamePanel);
		
		JLabel groupNameLabel = new JLabel("Group Name");
		groupNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		groupNameLabel.setForeground(Color.RED);
		groupNamePanel.add(groupNameLabel);
		
		groupNameField = new JTextField();
		groupNameField.setText(groupRep.getGroupName());
		groupNameField.setPreferredSize(new Dimension(7, 25));
		groupNameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		groupNameField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		groupNamePanel.add(groupNameField);
		groupNameField.setColumns(15);
		
		//Panel qui contient le radio button et label de type de groupe 
		JPanel groupTypePanel = new JPanel();
		groupTypePanel.setPreferredSize(new Dimension(10, 5));
		groupTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 30));
		groupTypePanel.setOpaque(false);
		inputPanel.add(groupTypePanel);
		
		JLabel groupTypeButton = new JLabel("Group Type");
		groupTypeButton.setForeground(Color.RED);
		groupTypeButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		groupTypePanel.add(groupTypeButton);
		
		serviceradioButton = new JRadioButton("Service ?");
		serviceradioButton.setEnabled(false);
		if(service)
			serviceradioButton.setSelected(true);
		serviceradioButton.setForeground(Color.RED);
		serviceradioButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		serviceradioButton.setOpaque(false);
		groupTypePanel.add(serviceradioButton);
		
		//Panel qui contient bouton d'envoie et de reinitialisation
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(10, 5));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
		buttonPanel.setOpaque(false);
		inputPanel.add(buttonPanel);
		
		restartButton = new JButton("Restart");
		restartButton.setPreferredSize(new Dimension(100, 30));
		restartButton.setForeground(Color.WHITE);
		restartButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		restartButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		restartButton.setBackground(new Color(255, 0, 51));
		restartButton.addActionListener(this);
		buttonPanel.add(restartButton);
		
		editButton = new JButton("Edit");
		editButton.setPreferredSize(new Dimension(100, 30));
		editButton.setForeground(Color.WHITE);
		editButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		editButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		editButton.setBackground(new Color(255, 0, 51));
		editButton.addActionListener(this);
		buttonPanel.add(editButton);
		
		//PAnel qui contient la liste des utilisateurs et radio Button pour option ajou ou suppression des user
		JPanel usersListPanel = new JPanel();
		usersListPanel.setOpaque(false);
		formPanel.add(usersListPanel, BorderLayout.CENTER);
		usersListPanel.setLayout(new BorderLayout(0, 0));
		
		jListPanel = new JPanel();
		jListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
		jListPanel.setOpaque(false);
		usersListPanel.add(jListPanel, BorderLayout.CENTER);
		
		usersOptionButton = new JRadioButton("Add Mode");
		usersOptionButton.setSelected(true);
		usersOptionButton.setHorizontalAlignment(SwingConstants.CENTER);
		usersOptionButton.setOpaque(false);
		usersOptionButton.setForeground(Color.RED);
		usersOptionButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		usersOptionButton.addActionListener(this);
		usersListPanel.add(usersOptionButton, BorderLayout.NORTH);
		createUserJList();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	private void createUserJList() {
		NavigableSet<UserRepository> allUsers;
		if(addOption)
			allUsers = Controller.getInstance().getUsers(service);
		else
		    allUsers = Controller.getInstance().getUsersByGroup(groupRep);
		//NavigableSet<GroupRepository> userGroups = Controller.getInstance().getGroupsByUser(user);
		jListPanel.removeAll();
		DefaultListModel<UserRepository> model = new DefaultListModel<>();
		for(Iterator<UserRepository> groups = allUsers.iterator();groups.hasNext();) {
			model.addElement(groups.next());
		}
		list = new JList<UserRepository>();
		list.setModel(model);
		list.setBackground(new Color(240, 240, 240));
		list.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JScrollPane scrollPan = new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPan.setBorder(null);
		scrollPan.setPreferredSize(new Dimension(200, 200));
		scrollPan.setBorder(new LineBorder(Color.RED));
		jListPanel.add(scrollPan);		
		jListPanel.updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == editButton) {
			if(verifieFields()) {
				groupRep.setGroupName(groupNameField.getText());
				String chaine = "Group Information Updated ";
				if(!Controller.getInstance().updateGroup(groupRep)) {
					chaine = "Group Information not Updated ";
				}
				String chaine2 = "!!!";
				Controller.getInstance().updateGroup(groupRep);
				if(addOption) {
					for(UserRepository user : list.getSelectedValuesList()) {
						if(!Controller.getInstance().addUserToGroup(user, groupRep)) {
							chaine2 = "But Group affectation is not updated !!!";
						}
					}
				}else {
					for(UserRepository user : list.getSelectedValuesList()) {
						if(!Controller.getInstance().deleteUserFromGroup(user, groupRep)) {
							chaine2 = "But Group affectation is not updated !!!";
						}
					}
				}
				AdminSide.getInstance().createJList(AdminSide.getInstance().mode);
				chaine += chaine2;
				JOptionPane.showMessageDialog(this, chaine);
				if(chaine2.equals("!!!"))
					this.dispose();
				
			}
		}
		if(e.getSource() == usersOptionButton) {
			addOption = usersOptionButton.isSelected();
			if(addOption) {
				usersOptionButton.setText("Add Mode");
			}else {
				usersOptionButton.setText("Delete Mode");
			}
			createUserJList();
		}
		
	}
	
	private boolean verifieFields() {
		if(groupNameField.getText().isBlank() ) {
			JOptionPane.showMessageDialog(this, "You left the group name field blank !!");
			return false;
		}
		return true;
		
	}

}
