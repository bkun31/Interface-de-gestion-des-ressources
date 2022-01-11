package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import data.Message;
import data.Status;
import data.User;

public class SeenMessageUserListGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public SeenMessageUserListGUI(Message message) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 447);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel userListPanel = new JPanel();
		userListPanel.setOpaque(false);
		userListPanel.setPreferredSize(new Dimension(350, 350));
		contentPane.add(userListPanel);
		userListPanel.setLayout(new BorderLayout(0, 0));
		
		JList <JLabel>userList = new JList<>();
		DefaultListModel<JLabel> model = new DefaultListModel<>();
		userList.setOpaque(false);
		for(Iterator<Status> status = message.getUserSatus().keySet().iterator();status.hasNext();) {
			Status statu = status.next();
			List<User> listUser = message.getUserSatus().get(statu);
			for(int i = 0; i < listUser.size(); i++) {
				JLabel label = new JLabel(listUser.get(i).getFirstname() + " " + listUser.get(i).getName() +"      "+ statu);
				label.setIcon(new ImageIcon("res//user4.png"));
				Color col = Color.BLACK;
				if(statu == Status.READ) {
					col = Color.GREEN;
				}else if(statu == Status.RECEIVED) {
					col = Color.ORANGE;
				}else if(statu == Status.PENDING) {
					col = Color.RED;
				}
				label.setForeground(col);
				model.addElement(label);
			}
		}
		userList.setModel(model);
		userList.setCellRenderer(new LabelAreaListCellRenderer());
		userList.setBackground(new Color(0, 0, 0, 0));
		JScrollPane scrollPane = new MyScrollPane(userList);
		userListPanel.add(scrollPane);
		scrollPane.setBorder(new LineBorder(Color.red));
		this.setAlwaysOnTop(true);
		setVisible(true);
	}
}
