package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.NavigableSet;

import javax.swing.border.LineBorder;

import beans.GroupRepository;
import beans.UserRepository;
import controller.Controller;

import javax.swing.JScrollPane;


public class AdminSide extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AdminSide admineSide;
	private JPanel contentPane;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	public Mode mode;
	private JButton userModeButton;
	private JButton groupModeButton;
	private JList<UserRepository> listUsers;
	private JList<GroupRepository> listGroups;
	private JPanel listUserGoupPanel;
	private UserRepository selectedUser;
	private GroupRepository selectedGroup;

	/**
	 * Create the frame.
	 */
	public AdminSide() {
		mode = Mode.USER;
		admineSide = this;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 650);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		//Top Panel contient le titre et le logo
		JPanel logoTitlePanel = new JPanel();
		logoTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT,100,0));
		logoTitlePanel.setOpaque(false);
		logoTitlePanel.setPreferredSize(new Dimension(10, 70));
		contentPane.add(logoTitlePanel, BorderLayout.NORTH);
		
		JLabel titleLabel = new JLabel("Admin Side");
		titleLabel.setIcon(new ImageIcon("res//myLogo (2).png"));
		titleLabel.setForeground(new Color(255, 0, 51));
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		logoTitlePanel.add(titleLabel);
		
		//Le panel de centre qui Contient La list des utilisateurs/Groupes et les Boutons de mode
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		
		//panel de la liste des utilisateus/groupes
		JPanel listPanel = new JPanel();
		listPanel.setBorder(new LineBorder(new Color(255, 0, 0)));
		listPanel.setPreferredSize(new Dimension(400, 450));
		centerPanel.add(listPanel);
		listPanel.setLayout(new BorderLayout(0, 0));
		
		//Panel qui contient de boutons un pour le mode user autre mode groupe
		JPanel modePanel = new JPanel();
		modePanel.setPreferredSize(new Dimension(10, 70));
		listPanel.add(modePanel, BorderLayout.NORTH);
		modePanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		//Bouton de mode User
		userModeButton = new JButton("Users");
		userModeButton.setOpaque(false);
		userModeButton.setBackground(new Color(240,240,240));
		userModeButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		userModeButton.setBorder(null);
		userModeButton.addActionListener(this);
		modePanel.add(userModeButton);
		
		//Bouton de Mode Group
		groupModeButton = new JButton("Groups");
		groupModeButton.setBackground(new Color(255, 153, 153));
		groupModeButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		groupModeButton.setBorder(null);
		groupModeButton.addActionListener(this);
		modePanel.add(groupModeButton);
		
		listUserGoupPanel = new JPanel();
		listUserGoupPanel.setOpaque(false);
		listPanel.add(listUserGoupPanel, BorderLayout.CENTER);
		listUserGoupPanel.setLayout(new BorderLayout(0, 0));
		
		createJList(mode);

		
		//Panel qui contient les boutons d'ajout / suppression / modification des utilisateurs / groupes
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
		buttonsPanel.setPreferredSize(new Dimension(10, 70));
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);
		
		//Bouton d'ajout d'un utilisateur / groupe 
		addButton = new JButton("Add User");
		addButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		addButton.setForeground(Color.WHITE);
		addButton.setBackground(new Color(255, 0, 51));
		addButton.setPreferredSize(new Dimension(95, 30));
		addButton.addActionListener(this);
		buttonsPanel.add(addButton);
		
		//Bouton de modification d'un utilisateur / groupe 
		editButton = new JButton("Edit User");
		editButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		editButton.setForeground(Color.WHITE);
		editButton.setBackground(new Color(255, 0, 51));
		editButton.setPreferredSize(new Dimension(95, 30));
		editButton.addActionListener(this);
		buttonsPanel.add(editButton);
		
		//Bouton de suppression d'un utilisateur / groupe 
		deleteButton = new JButton("Delete User");
		deleteButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setBackground(new Color(255, 0, 51));
		deleteButton.setPreferredSize(new Dimension(95, 30));
		deleteButton.addActionListener(this);
		buttonsPanel.add(deleteButton);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void createJList(Mode mode2) {
		listUserGoupPanel.removeAll();
		if(mode2 == Mode.USER) {
			NavigableSet<UserRepository> userList= Controller.getInstance().getUsers();
			DefaultListModel<UserRepository> model = new DefaultListModel<>();

			for(Iterator<UserRepository> users = userList.iterator();users.hasNext();) {
				model.addElement(users.next());
			}
			
			listUsers = new JList<UserRepository>();
			listUsers.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				        if (e.getClickCount() == 1) {
				        	selectedUser = listUsers.getSelectedValue();
				         }
				}
			
			});
			listUsers.setModel(model);
			listUsers.setBackground(new Color(240, 240, 240));
			listUsers.setFont(new Font("Tahoma", Font.PLAIN, 15));
			JScrollPane scrollPan = new JScrollPane(listUsers,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPan.setBorder(null);
			listUserGoupPanel.add(scrollPan, BorderLayout.CENTER);
		}else {//Afficher les groupes
			NavigableSet<GroupRepository> groupServiceList= Controller.getInstance().getGroups(true);
			NavigableSet<GroupRepository> groupCampusList= Controller.getInstance().getGroups(false);

			DefaultListModel<GroupRepository> model = new DefaultListModel<>();
			for(Iterator<GroupRepository> groups = groupCampusList.iterator();groups.hasNext();) {
				model.addElement(groups.next());
			}
			for(Iterator<GroupRepository> groups = groupServiceList.iterator();groups.hasNext();) {
				model.addElement(groups.next());
			}
			
			listGroups = new JList<GroupRepository>();
			listGroups.clearSelection();
			listGroups.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
				        if (e.getClickCount() == 1) {
				        	selectedGroup = listGroups.getSelectedValue();
				         }
				}
			
			});
			listGroups.setModel(model);
			listGroups.setFont(new Font("Tahoma", Font.PLAIN, 15));
			listGroups.setBackground(new Color(240, 240, 240));
			JScrollPane scrollPan = new JScrollPane(listGroups, 
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPan.setBorder(null);
			listUserGoupPanel.add(scrollPan, BorderLayout.CENTER);
		}
		listUserGoupPanel.updateUI();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.userModeButton && mode == Mode.GROUP) {
			groupModeButton.setBackground(new Color(255, 153, 153));
			userModeButton.setBackground(new Color(240, 240, 240));
			mode = Mode.USER;
			selectedGroup = null;
			userModeButton.setOpaque(false);
			groupModeButton.setOpaque(true);
			this.deleteButton.setText("Delete User");
			this.addButton.setText("Add User");
			this.editButton.setText("Edit User");
			createJList(mode);
		}
		
		if(e.getSource() == this.groupModeButton && mode == Mode.USER) {
			selectedUser = null;
			userModeButton.setBackground(new Color(255, 153, 153));
			groupModeButton.setBackground(new Color(240, 240, 240));
			groupModeButton.setOpaque(false);
			userModeButton.setOpaque(true);
			mode = Mode.GROUP;
			this.deleteButton.setText("Delete Group");
			this.addButton.setText("Add Group");
			this.editButton.setText("Edit Group");
			createJList(mode);
		}
		
		if(e.getSource() == this.addButton) {
			if(mode == Mode.USER) {//Ajout d'un user
				new UserAddEditForm(null);
			}else {//Ajout d'un group
				new GroupAddForm();
			}
		}
		
		if(e.getSource() == this.editButton) {
			if(mode == Mode.USER) {//Modiifcation d'un user
				if(selectedUser != null) {
					new UserAddEditForm(selectedUser);
				}else {
					JOptionPane.showMessageDialog(this, "Select one user please !!");
				}
			}else {
				if(selectedGroup != null) {//Modification d'un groupe
					new GroupEditForm(selectedGroup);
				}else {
					JOptionPane.showMessageDialog(this, "Select one group please !!");
				}				
			}
		}
		if(e.getSource() == deleteButton) {
			if(mode == Mode.USER) {
				if(selectedUser != null) {
					Controller.getInstance().deleteUser(selectedUser, "admin");//suppression de user
				}else {
					JOptionPane.showMessageDialog(this, "Select one user please !!");
				}
			}else {
				if(selectedGroup != null)
					Controller.getInstance().deleteGroup(selectedGroup, "admin");//suppression de groupe
				else
					JOptionPane.showMessageDialog(this, "Select one group please !!");
			}
			createJList(mode);
		}
		selectedGroup = null;
		selectedUser = null;
		
	}
	public static AdminSide getInstance() {
		return admineSide;
	}

}

enum Mode{
	USER,GROUP
}
