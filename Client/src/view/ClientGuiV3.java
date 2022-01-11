package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import client.Client;
import data.DiscussionThread;
import data.Group;
import data.Message;
import data.Status;
import model.Model;

public class ClientGuiV3 extends JFrame implements ActionListener, TreeSelectionListener, NotificationListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Client client;
	private JLabel clientIdLabel;
	private DiscussionThread currentDiscussionThread;
	private Group currentGroup;
	private JPanel contentPane;
	private JButton showListThreadButton;
	private JPanel listThreadPanel;
	private JButton logOutButton;
	private JButton sendButton;
	private JTree listThread;
	private JPanel listMessagePanel;
	private JScrollPane scrollPane;
	private JButton newThreadButton;
	private JLabel clientFirstNameLabel;
	private JLabel clientLastNameLabel;
	private JTextArea messageTextArea;
	private JList<JLabel> chatRoomMessages;
	private List<Message> currentListMessage;

	/**
	 * Create the frame.
	 */
	public ClientGuiV3(Client client) {
		this.client = client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(20, 20));
		setContentPane(contentPane);

		// Panel Gauche
		// Debut
		JPanel leftPanel = new JPanel();
		leftPanel.setOpaque(false);
		leftPanel.setPreferredSize(new Dimension(250, 0));
		contentPane.add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 20));

		// Panel qui contient logo et id du client
		JPanel logoAndClientPanel = new JPanel();
		logoAndClientPanel.setOpaque(false);
		logoAndClientPanel.setPreferredSize(new Dimension(10, 250));
		leftPanel.add(logoAndClientPanel, BorderLayout.NORTH);
		logoAndClientPanel.setLayout(new BorderLayout(0, 10));
		// Logo
		JLabel logoLabel = new JLabel("Quick Chat");
		logoLabel.setForeground(new Color(255, 0, 51));
		logoLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		logoLabel.setIcon(new ImageIcon("res//myLogo (2).png"));
		logoLabel.setPreferredSize(new Dimension(46, 60));
		logoAndClientPanel.add(logoLabel, BorderLayout.NORTH);

		// Panel qui contient la photo et id du client
		JPanel ClientIdPanel = new MyPanel();
		ClientIdPanel.setOpaque(false);
		logoAndClientPanel.add(ClientIdPanel, BorderLayout.CENTER);
		ClientIdPanel.setLayout(new BorderLayout(0, 0));
		// Photo
		JLabel userImageLabel = new JLabel(new ImageIcon("res//user7.png"));
		ClientIdPanel.add(userImageLabel, BorderLayout.NORTH);
		// id Client
		clientIdLabel = new JLabel(Model.getInstance().getUsername());
		clientIdLabel.setForeground(new Color(255, 0, 51));
		clientIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		clientIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ClientIdPanel.add(clientIdLabel, BorderLayout.CENTER);
		// Fin panel du logo et client id

		// Panel qui contient la list des threads
		JPanel threadsPanel = new JPanel();
		threadsPanel.setOpaque(false);
		leftPanel.add(threadsPanel, BorderLayout.CENTER);
		threadsPanel.setLayout(new BorderLayout(0, 0));

		// Panel qui contient Label et Boutton pour afficher la liste des threads
		JPanel labelAndButtonPanel = new JPanel();
		labelAndButtonPanel.setBorder(null);
		labelAndButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		;
		labelAndButtonPanel.setOpaque(false);
		labelAndButtonPanel.setPreferredSize(new Dimension(10, 60));
		threadsPanel.add(labelAndButtonPanel, BorderLayout.NORTH);

		// Label "Active Conversation"
		JLabel activeConversationlabel = new JLabel("Active Conversations");
		activeConversationlabel.setIcon(new ImageIcon("res//messageIcon (1).png"));
		activeConversationlabel.setForeground(new Color(255, 0, 51));
		activeConversationlabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		labelAndButtonPanel.add(activeConversationlabel);

		// Boutton pour afficher List
		showListThreadButton = new JButton("");
		showListThreadButton.setBorder(null);
		showListThreadButton.setBackground(Color.WHITE);
		showListThreadButton.setIcon(new ImageIcon("res//dropDpwnArrow.png"));
		showListThreadButton.setPreferredSize(new Dimension(25, 23));
		showListThreadButton.addActionListener(this);
		labelAndButtonPanel.add(showListThreadButton);
		// Fin de Panel qui contient Label et Boutton pour afficher la liste des threads

		listThreadPanel = new MyPanel();
		listThreadPanel.setVisible(false);
		threadsPanel.add(listThreadPanel, BorderLayout.CENTER);
		listThreadPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

		listThread = new JTree();
		createTree();
		listThread.addTreeSelectionListener(this);
		JScrollPane scolPane = new JScrollPane(listThread, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scolPane.setOpaque(false);
		scolPane.setBorder(null);
		scolPane.setPreferredSize(new Dimension(200, 200));
		listThreadPanel.add(scolPane);

		// Fin Panel de list des threads

		// Debut Panel d'affichage des conversation et envoie des messages
		JPanel centerPanel = new MyPanel();
		centerPanel.setOpaque(false);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout(0, 0));

		JPanel messagePanel = new JPanel();
		messagePanel.setAutoscrolls(true);
		messagePanel.setOpaque(false);
		centerPanel.add(messagePanel, BorderLayout.CENTER);

		// Panel qui contient le contenue de la convesation
		listMessagePanel = new JPanel();
		listMessagePanel.setOpaque(false);
		listMessagePanel.setLayout(new BorderLayout());
		chatRoomMessages = new JList<>();
		chatRoomMessages.setOpaque(false);
		chatRoomMessages.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					if (currentListMessage != null)
						new SeenMessageUserListGUI(currentListMessage.get(chatRoomMessages.getSelectedIndex()));
				}
			}

		});
		listMessagePanel.add(chatRoomMessages, BorderLayout.CENTER);
		scrollPane = new JScrollPane(listMessagePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setBorder(new LineBorder(Color.RED, 1, true));
		scrollPane.setOpaque(false);

		messagePanel.setLayout(new BorderLayout(0, 0));
		messagePanel.add(scrollPane);

		// Zone de saisie de message
		JPanel writePanel = new JPanel();
		writePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		writePanel.setOpaque(false);
		writePanel.setPreferredSize(new Dimension(10, 60));
		centerPanel.add(writePanel, BorderLayout.SOUTH);

		// Message Area
		messageTextArea = new JTextArea();
		messageTextArea.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		messageTextArea.setColumns(20);
		messageTextArea.setRows(2);
		messageTextArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(messageTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		writePanel.add(scrollPane);

		// bouton d'envoie
		sendButton = new JButton("Send");
		sendButton.setBackground(Color.RED);
		sendButton.setForeground(Color.WHITE);
		sendButton.addActionListener(this);
		writePanel.add(sendButton);

		// Fin

		// Panel Droite
		// Debut
		JPanel rightPanel = new JPanel();
		rightPanel.setOpaque(false);
		rightPanel.setPreferredSize(new Dimension(250, 10));
		contentPane.add(rightPanel, BorderLayout.EAST);
		rightPanel.setLayout(new BorderLayout(0, 0));

		JPanel logOutAndInfoPanel = new MyPanel();
		logOutAndInfoPanel.setOpaque(false);
		logOutAndInfoPanel.setPreferredSize(new Dimension(10, 400));
		rightPanel.add(logOutAndInfoPanel, BorderLayout.NORTH);
		logOutAndInfoPanel.setLayout(new BorderLayout(0, 0));

		JLabel serviceIconLabel = new JLabel("");
		serviceIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serviceIconLabel.setIcon(new ImageIcon("res//servicesIcon.png"));
		logOutAndInfoPanel.add(serviceIconLabel, BorderLayout.NORTH);

		JPanel informationPanel = new JPanel();
		informationPanel.setOpaque(false);
		logOutAndInfoPanel.add(informationPanel, BorderLayout.CENTER);
		informationPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel nameLabel = new JLabel("Name  :");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setVerticalAlignment(SwingConstants.CENTER);
		nameLabel.setFont(new Font("Dialog", Font.PLAIN, 15));
		informationPanel.add(nameLabel);

		clientFirstNameLabel = new JLabel(Model.getInstance().getMe().getFirstname());
		clientFirstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		clientFirstNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informationPanel.add(clientFirstNameLabel);

		JLabel lastNameLabel = new JLabel("LastName :");
		lastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lastNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informationPanel.add(lastNameLabel);

		clientLastNameLabel = new JLabel(Model.getInstance().getMe().getName());
		clientLastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		clientLastNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informationPanel.add(clientLastNameLabel);

		JPanel logOutPanel = new JPanel();
		logOutPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 30));
		logOutPanel.setOpaque(false);
		logOutPanel.setPreferredSize(new Dimension(10, 100));
		logOutAndInfoPanel.add(logOutPanel, BorderLayout.SOUTH);

		logOutButton = new JButton("Log Out");
		logOutButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		logOutButton.setBackground(new Color(255, 0, 0));
		logOutButton.setForeground(Color.WHITE);
		logOutButton.setPreferredSize(new Dimension(100, 23));
		logOutButton.addActionListener(this);
		logOutPanel.add(logOutButton);

		JPanel newThreadPanel = new JPanel();
		newThreadPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 40));
		newThreadPanel.setPreferredSize(new Dimension(10, 100));
		newThreadPanel.setOpaque(false);
		rightPanel.add(newThreadPanel, BorderLayout.SOUTH);

		newThreadButton = new JButton("New Thread");
		newThreadButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		newThreadButton.setBackground(Color.RED);
		newThreadButton.setForeground(Color.WHITE);
		newThreadButton.setPreferredSize(new Dimension(100, 23));
		newThreadButton.addActionListener(this);
		newThreadPanel.add(newThreadButton);
		// Fin Panel droite
		Model.getInstance().addNotificationListener(this);
		setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (e.getWindow().isVisible())
					Client.getInstance().logout();
			}
		});
		this.setVisible(true);
		this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == showListThreadButton) {
			if (listThreadPanel.isVisible()) {
				listThreadPanel.setVisible(false);
			} else {
				listThreadPanel.setVisible(true);
			}
		}
		if (e.getSource() == newThreadButton) {
			new NewThreadGUI(client);
		}
		if (e.getSource() == sendButton) {
			if (currentDiscussionThread != null) {
				if (messageTextArea.getText().isBlank()) {
					JOptionPane.showMessageDialog(this, "You are sending an empty message !!!");
				} else {
					client.sendMessage(currentGroup, currentDiscussionThread, messageTextArea.getText());
					createlistMessagepanel();
				}
			} else {
				JOptionPane.showMessageDialog(this, "You need to select a discussion thread please !!!");
			}
			messageTextArea.setText("");
		}
		if (e.getSource() == logOutButton) {
			client.logout();
			System.exit(0);
		}
	}

	public void createTree() {
		Map<Group, List<DiscussionThread>> listThd = Model.getInstance().getTreeThread();
		DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Thread List");
		for (Iterator<Group> iterator = listThd.keySet().iterator(); iterator.hasNext();) {
			Group group = iterator.next();
			DefaultMutableTreeNode fils = new DefaultMutableTreeNode(group);
			for (Iterator<DiscussionThread> itDisc = listThd.get(group).iterator(); itDisc.hasNext();) {
				DiscussionThread discThd = itDisc.next();
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(discThd);
				fils.add(node);
			}
			racine.add(fils);
		}
		DefaultTreeModel model = (DefaultTreeModel) listThread.getModel();
		model.setRoot(racine);
		listThread.setFont(new Font("Tahoma", Font.PLAIN, 15));
		listThread.setOpaque(false);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) listThread.getLastSelectedPathComponent();

		/* if nothing is selected */
		if (node == null)
			return;
		if(!node.isLeaf() || node.isRoot())
			return;
		if (!node.isLeaf())
			return;

		/* recuperer le threadId */
		Object nodeInfo = node.getUserObject();
		currentDiscussionThread = (DiscussionThread) nodeInfo;
		currentGroup = (Group) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
		createlistMessagepanel();

	}

	private void createlistMessagepanel() {
		List<Message> listMessage = Model.getInstance().getMessages(currentDiscussionThread);
		DefaultListModel<JLabel> model = new DefaultListModel<>();
		/* aficher la conversation. */
		chatRoomMessages.removeAll();
		this.currentListMessage = listMessage;
		for (int i = 0; i < listMessage.size(); i++) {
			JLabel message = new JLabel();
			message.setIcon(new ImageIcon("res//user4.png"));
			String html = "<html><body style='width: %1spx'>%1s";
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			String date = dateFormat.format(listMessage.get(i).getTimestamp());
			String s;
			if (listMessage.get(i).getSender().getUserId() == 0)
				s = "<html>deleted user :<br>" + listMessage.get(i).getText() + "<br>" + date + "</html>";
			else
				s = "<html>" + listMessage.get(i).getSender().getName() + " "
						+ listMessage.get(i).getSender().getFirstname() + " :<br>" + listMessage.get(i).getText()
						+ "<br>" + date + "</html>";
			message.setText(String.format(html, 300, s));
			if (listMessage.get(i).getStatus() == Status.READ) {
				message.setForeground(new Color(0, 153, 0));
			} else if (listMessage.get(i).getStatus() == Status.RECEIVED) {
				message.setForeground(Color.ORANGE);
			} else if (listMessage.get(i).getStatus() == Status.PENDING) {
				message.setForeground(Color.RED);
			}
			message.setVerticalTextPosition(SwingConstants.TOP);
			message.setOpaque(false);
			model.addElement(message);
		}
		chatRoomMessages.setCellRenderer(new LabelAreaListCellRenderer());
		chatRoomMessages.setModel(model);
		chatRoomMessages.updateUI();

	}

	@Override
	public void newMessageArrived() {
		Thread monThread = new Thread() {

			@Override
			public void run() {

				// TODO Auto-generated method stub
				if (currentDiscussionThread != null)
					createlistMessagepanel();
				createTree();
			}
		};
		monThread.start();

	}

	@Override
	public void messageSeen() {
		// TODO Auto-generated method stub
		Thread monThread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listThread.updateUI();
				if (currentDiscussionThread != null)
					createlistMessagepanel();
			}
		};
		monThread.start();

	}

	@Override
	public void updateInfo() {
		// TODO Auto-generated method stub

		Thread monThread = new Thread() {

			@Override
			public void run() {
				clientLastNameLabel.setText(Model.getInstance().getMe().getName());
				clientFirstNameLabel.setText(Model.getInstance().getMe().getFirstname());
				if (currentDiscussionThread != null) {
					createlistMessagepanel();
				}
				createTree();
			}
		};
		monThread.start();
	}

}
