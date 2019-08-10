package uiJFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uiJPanel.MyPanel;

public abstract class MyFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//����logger
	private Logger logger=LogManager.getLogger();
	//��ȡ��Ļ�ֱ���
	private final Dimension dScreen=Toolkit.getDefaultToolkit().getScreenSize();
	private final int width=dScreen.width;
	private final int height=dScreen.height;
	MyPanel myPanel;
	public MyFrame(MyPanel myPanel) {
		logger.info("width:"+width+"\theight:"+height);
		this.myPanel=myPanel;
		myPanel.setFrame(this);
		//��ȫ��ʱ�ߴ��λ��
		this.setSize(width/2,height/2 );
		this.setLocationRelativeTo(null);
		//��ʼ��ȫ��
		this.setExtendedState(MAXIMIZED_BOTH);
		//Ĭ�Ϲرմ�������
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//��ʼ����
		this.setLayout(new BorderLayout());
		this.add(myPanel.getNorth(),BorderLayout.NORTH);
		this.add(myPanel.getSouth(),BorderLayout.SOUTH);
		this.add(myPanel.getWest(),BorderLayout.WEST);
		this.add(myPanel.getEast(),BorderLayout.EAST);
		this.add(myPanel.getCenter(),BorderLayout.CENTER);
		this.setVisible(true);
		
	}
	
}
