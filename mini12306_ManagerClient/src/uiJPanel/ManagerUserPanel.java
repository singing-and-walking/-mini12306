package uiJPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import orm.User;
import uiTable.AdaptUser;
import uiTable.MyTableModel;
import uiTable.UserTableModel;

public class ManagerUserPanel extends MyPanel {
	JLabel userSize=new JLabel("��0����¼");
	JButton submit=new JButton("��ѯ");
	JTextField iUser_name=new JTextField(20);
	JPanel center=new JPanel();
	JRadioButton queryAll=new JRadioButton("�鿴ȫ���û�");
	JRadioButton query=new JRadioButton("�鿴ָ���û�");
	ButtonGroup buttonGroup=new ButtonGroup();
	//�û����
	MyTableModel mUser=new UserTableModel();
	JTable tUser=new JTable(mUser);
	JScrollPane jspTrain=new JScrollPane(tUser);
	//���췽��
	public ManagerUserPanel() {
		queryAll.setSelected(true);
		iUser_name.setEditable(false);
		buttonGroup.add(queryAll);
		buttonGroup.add(query);
		addListener();
	}
	@Override
	public JPanel getNorth() {
		JPanel panel=new JPanel();
		JLabel tUser_name=new JLabel("�û�����");
		panel.add(tUser_name);
		panel.add(iUser_name);
		panel.add(queryAll);
		panel.add(query);
		panel.add(submit);
		return panel;
	}
	@Override
	public JPanel getCenter() {
		center.setLayout(new GridBagLayout());
		GridBagConstraints gc=new GridBagConstraints();
		gc.weightx=1;
		gc.gridwidth=0;
		gc.fill=GridBagConstraints.BOTH;
		
		gc.weighty=1;
		center.add(userSize,gc);
		gc.weighty=10;
		center.add(jspTrain,gc);
		return center;
	}
	@Override
	protected void addListener() {
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(queryAll.isSelected())
				{
					submitQueryAll();
				}
				else if(query.isSelected())
				{
					submitQuery();
				}
			}
			
		});
		queryAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectQueryAll();
			}
		});
		query.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectQuery();
			}
		});	
		tUser.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row=tUser.getSelectedRow();
				int column=tUser.getSelectedColumn();
				if(column==2)
				{
					User user=((AdaptUser)mUser.getTableDatas().get(row)).getUser();
					int op=JOptionPane.showConfirmDialog(center, "ȷ��ע���˺�\""+user.getName()+"\"��");
					if(op==JOptionPane.OK_OPTION)
						logout(row,user);
				}
			}
		});
	}
	private void submitQueryAll()
	{
		mUser.setTableDatas(dao.getAllUser());
		tUser.updateUI();
		updateLabelSize();
	}
	private void submitQuery()
	{
		String user_name=iUser_name.getText();
		User user=dao.getUser(user_name);
		List<User> tl=new ArrayList<>();
		if(user!=null)
			tl.add(user);
		mUser.setTableDatas(tl);
		tUser.updateUI();
		updateLabelSize();
		
	}
	private void selectQueryAll()
	{
		iUser_name.setEditable(false);
	}
	private void selectQuery()
	{
		iUser_name.setEditable(true);
	}
	private void logout(int row,User user)
	{
		if(dao.deleteUser(user))
		{
			JOptionPane.showMessageDialog(center, "ɾ���ɹ���");
			mUser.getTableDatas().remove(row);
			tUser.updateUI();
			updateLabelSize();
		}
		else
			JOptionPane.showMessageDialog(center, "ɾ��ʧ�ܣ����Ժ����ԡ�");
	}
	private void updateLabelSize()
	{
		int size=mUser.getTableDatas().size();
		String str1="��"+size+"����¼";
		userSize.setText(str1);

	}

}
