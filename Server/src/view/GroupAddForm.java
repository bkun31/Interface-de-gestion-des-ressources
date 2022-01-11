package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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

public class GroupAddForm extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField groupNameField;
	private JButton addButton;
	private JButton restartButton;
	private JRadioButton serviceradioButton;
	private JPanel jListPanel;
	private JList<UserRepository> list;
	private boolean service = false;

	/**
	 * Create the frame.
	 */
	public GroupAddForm() {
		setBounds(100, 100, 640, 369);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel titleLabel = new JLabel("Add Group");
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
		groupNameField.setPreferredSize(new Dimension(7, 25));
		groupNameField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		groupNameField.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		groupNamePanel.add(groupNameField);
		groupNameField.setColumns(15);
		
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
		serviceradioButton.setForeground(Color.RED);
		serviceradioButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		serviceradioButton.setOpaque(false);
		serviceradioButton.addActionListener(this);
		groupTypePanel.add(serviceradioButton);
		
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
		
		addButton = new JButton("Add");
		addButton.setPreferredSize(new Dimension(100, 30));
		addButton.setForeground(Color.WHITE);
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		addButton.setBackground(new Color(255, 0, 51));
		addButton.addActionListener(this);
		buttonPanel.add(addButton);
		
		JPanel usersListPanel = new JPanel();
		usersListPanel.setOpaque(false);
		formPanel.add(usersListPanel, BorderLayout.CENTER);
		usersListPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Select the users you want to add to your group");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		usersListPanel.add(lblNewLabel, BorderLayout.NORTH);
		
		jListPanel = new JPanel();
		jListPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));
		jListPanel.setOpaque(false);
		usersListPanel.add(jListPanel, BorderLayout.CENTER);
		createUserJList();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	private void createUserJList() {
		NavigableSet<UserRepository> allUsers = Controller.getInstance().getUsers(service);
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
		if(e.getSource() == addButton) {
			if(verifieFields()) {
				if(Controller.getInstance().registerGroup(groupNameField.getText(), service, list.getSelectedValuesList(), "admin")) {
					JOptionPane.showMessageDialog(this, "Information updated !!!");
					AdminSide.getInstance().createJList(AdminSide.getInstance().mode);
					this.dispose();
				}else {
					JOptionPane.showMessageDialog(this, "Information not updated !!!");
				}
			}
		}
		if(e.getSource() == serviceradioButton) {
			service = serviceradioButton.isSelected();
			createUserJList();
		}
		
	}
	
	private boolean verifieFields() {
		boolean valid = true;
		if(groupNameField.getText().isBlank() ) {
			JOptionPane.showMessageDialog(this, "You left the group name field blank !!");
			valid = false;
		}
		if(list.getSelectedValuesList().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Select at least one user to put into the groupe !!");
			valid = false;
		}
		return valid;
		
	}

}
