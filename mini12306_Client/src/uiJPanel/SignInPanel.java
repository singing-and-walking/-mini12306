package uiJPanel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import netToolset.DAO;
import orm.User;
import uiJFrame.SignUp;
import uiJFrame.UserHome;

public class SignInPanel extends MyPanel{
	JPanel center=new JPanel();
	JTextField name=new JTextField(20);
	JPasswordField password=new JPasswordField(20);
	JButton submit=new JButton("��¼");
	JButton signUp=new JButton("ע��");
	//��ȡ��Ļ�ֱ���
	private final Dimension dScreen=Toolkit.getDefaultToolkit().getScreenSize();
	private final int screenWidth=dScreen.width;
	private final int screenHeight=dScreen.height;
	//Tool
	private DAO dao=new DAO();
	private Logger logger=LogManager.getLogger();
	public SignInPanel() {
		addListener();
	}
	@Override
	public JPanel getCenter() {
		center.setLayout(null);
		JLabel tname =new JLabel("�û�����");
		JLabel tpassword =new JLabel("���룺");
		//���þ���λ��
		tname.setBounds(screenWidth/100*45, screenHeight/100*30,screenWidth/100*15,screenHeight/100*3);
		name.setBounds(screenWidth/100*50, screenHeight/100*30,screenWidth/100*15,screenHeight/100*3);
		tpassword.setBounds(screenWidth/100*45, screenHeight/100*35, screenWidth/100*15,screenHeight/100*3);
		password.setBounds(screenWidth/100*50, screenHeight/100*35, screenWidth/100*15,screenHeight/100*3);
		submit.setBounds(screenWidth/100*50, screenHeight/100*50, screenWidth/100*10,screenHeight/100*5);
		signUp.setBounds(screenWidth/100*50,screenHeight/100*60,screenWidth/100*10,screenHeight/100*5);
		center.add(tname);
		center.add(name);
		center.add(tpassword);
		center.add(password);
		center.add(submit);
		center.add(signUp);
		return center;
	}

	protected void addListener()
	{
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitSignIn();
			}
		});
		signUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				signUp();
			}
		});
	}
	
	private void submitSignIn()
	{
		
		String name=this.name.getText();
		String password=new String(this.password.getPassword());
		User user=dao.getUser(name);
		if(user==null)
		{
			JOptionPane.showMessageDialog(center,"�û���"+name+"��������!");
			return;
		}
		String truePassword=user.getPassword();
		if(!password.equals(truePassword))
		{
			JOptionPane.showMessageDialog(center,"���벻��ȷ�����������롣");
			return;
		}
		else 
		{
			new UserHome(user);
			this.frame.dispose();
		}		
	}

	private void signUp() {
		logger.debug("ע��");
		new SignUp();
	}
}
