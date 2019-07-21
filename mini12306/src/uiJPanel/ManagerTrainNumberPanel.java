package uiJPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

import org.jdesktop.swingx.JXDatePicker;

import orm.Schedule;
import orm.TimeAndPrice;
import orm.Train;
import uiTable.AdaptSchedule;
import uiTable.AdaptTimeAndPrice;
import uiTable.AdaptTrain;
import uiTable.Airline;
import uiTable.MyTableModel;
import uiTable.QueryTableModel;
import uiTable.ScheduleTableModel;
import uiTable.TimeAndPriceTableModel;
import uiTable.TrainTableModel;


public class ManagerTrainNumberPanel extends MyPanel {
	JLabel trainSize=new JLabel("��0����¼");
	JLabel timeAndPriceSize=new JLabel("��0����¼");
	JLabel scheduleSize=new JLabel("��0����¼");
	JButton submit=new JButton("��ѯ");
	JTextField iTrain_no=new JTextField(20);
	JRadioButton queryAll=new JRadioButton("�鿴ȫ������");
	JRadioButton query=new JRadioButton("�鿴ָ������");
	JRadioButton update=new JRadioButton("�޸�");
	JRadioButton add=new JRadioButton("���");
	ButtonGroup buttonGroup=new ButtonGroup();
	JPanel center=new JPanel();
	Set<Integer> TrainEditedIndexs=new HashSet<>();
	Set<Integer> TimeAndPriceEditedIndexs=new HashSet<>();
	Set<Integer> ScheduleEditedIndexs=new HashSet<>();
	//��ȡ��Ļ�ֱ���
	private final Dimension dScreen=Toolkit.getDefaultToolkit().getScreenSize();
	private final int width=dScreen.width;
	//�г����
	MyTableModel mTrain=new TrainTableModel();
	JTable tTrain=new JTable(mTrain);
	JScrollPane jspTrain=new JScrollPane(tTrain);
	//��վ�۸���
	MyTableModel mTimeAndPrice=new TimeAndPriceTableModel();
	JTable tTimeAndPrice=new JTable(mTimeAndPrice);
	JScrollPane jspTimeAndPrice=new JScrollPane(tTimeAndPrice);
	//�ճ̱��
	MyTableModel mSchedule=new ScheduleTableModel();
	JTable tSchedule=new JTable(mSchedule);
	JScrollPane jspSchedule=new JScrollPane(tSchedule);
	//���췽��
	public ManagerTrainNumberPanel() {
		queryAll.setSelected(true);
		// �����п��
        tTimeAndPrice.getColumnModel().getColumn(0).setPreferredWidth(width/12);
		iTrain_no.setEditable(false);
		buttonGroup.add(queryAll);
		buttonGroup.add(query);
		buttonGroup.add(update);
		buttonGroup.add(add);
		addListener();
		//table.setModel(arg0);
	}
	@Override
	public JPanel getNorth() {
		JPanel panel=new JPanel();
		JLabel tTrain_no=new JLabel("����������ţ�");
		panel.add(tTrain_no);
		panel.add(iTrain_no);
		panel.add(queryAll);
		panel.add(query);
		panel.add(update);
		panel.add(add);
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
		
		gc.weighty=0.2;
		center.add(trainSize,gc);
		gc.weighty=0.5;
		center.add(jspTrain,gc);
		gc.weighty=0.0;
		center.add(timeAndPriceSize,gc);
		gc.weighty=5;
		center.add(jspTimeAndPrice,gc);
		gc.weighty=0.2;
		center.add(scheduleSize,gc);
		gc.weighty=5;
		center.add(jspSchedule,gc);
		return center;
	}
	@Override
	protected void addListener() {
		// TODO Auto-generated method stub
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
				else if(update.isSelected())
				{
					submitUpdate();
				}
				else if(add.isSelected())
				{
					submitAdd();
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
		update.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectUpdate();
			}
		});
		add.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectAdd();
			}
		});
		mTrain.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int row=tTrain.getEditingRow();
				logger.debug("tTrain����µ�"+row+"�б��޸ġ�");
				if(row>=0)
					TrainEditedIndexs.add(row);
				if(add.isSelected())
				{
					String train_no=(String) mTrain.getValueAt(row, 0);
					String code= (String) mTrain.getValueAt(row, 1);
					for(int i=0;i<mTimeAndPrice.getTableDatas().size();i++)
					{
						if(train_no!=null)
							mTimeAndPrice.setValueAt(train_no, i, 0);
					}
					for(int i=0;i<mSchedule.getTableDatas().size();i++)
					{
						if(train_no!=null)
							mSchedule.setValueAt(train_no, i, 0);
					}
				}
			}
		});
		mTimeAndPrice.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int row=tTimeAndPrice.getEditingRow();
				logger.debug("tTimeAndPrice����µ�"+row+"�б��޸ġ�");	
				if(row>=0)
					TimeAndPriceEditedIndexs.add(row);
			}
		});
		mSchedule.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int row=tSchedule.getEditingRow();
				logger.debug("tSchedule����µ�"+row+"�б��޸ġ�");
				if(row>=0)
					ScheduleEditedIndexs.add(row);
			}
		});
	}
	private void submitQueryAll()
	{
		logger.debug("��ѯȫ������");
		List<TimeAndPrice> tpl=dao.getAllTimeAndPrices();
		mTimeAndPrice.setTableDatas(tpl);
		tTimeAndPrice.updateUI();
		updateLabelSize();
	}
	private void submitQuery()
	{
		logger.debug("��ѯָ������");
		String train_no=iTrain_no.getText();
		Train train=dao.getTrain(train_no);
		List<Train> tl=new ArrayList<>();
		if(train!=null)
			tl.add(train);
		List<TimeAndPrice> tpl=dao.getTimeAndPrices(train_no);
		List<Schedule> scl=dao.getSchedule(train_no);
		mTrain.setTableDatas(tl);
		mTimeAndPrice.setTableDatas(tpl);
		mSchedule.setTableDatas(scl);
		tTrain.updateUI();
		tTimeAndPrice.updateUI();
		tSchedule.updateUI();
		updateLabelSize();
		logger.debug("");
		
	}
	private void submitUpdate()
	{
		if(dao.beginTransactionModel())
		{
			boolean flage=false;
			for(int i:TrainEditedIndexs)
			{
				logger.debug("TrainEditedIndexs:"+i);
				Train train=((AdaptTrain)mTrain.getTableDatas().get(i)).getTrain();
				if(train.getTrain_no()!=null)
					flage=dao.updateTrain(train);
				if(!flage)
					break;
			}
			
			for(int i:TimeAndPriceEditedIndexs)
			{
				TimeAndPrice timeAndPrice=((AdaptTimeAndPrice)mTimeAndPrice.getTableDatas().get(i)).getTimeAndPrice();
				if(timeAndPrice.getTrain_no()!=null&&timeAndPrice.getStation_no()!=0)
					flage=dao.updateTimeAndPrice(timeAndPrice);
				if(!flage)
					break;
			}
					
			if(flage&&dao.commit())
			{
				JOptionPane.showMessageDialog(center, "�����޸ĳɹ���");
				TimeAndPriceEditedIndexs.clear();
				TrainEditedIndexs.clear();
				ScheduleEditedIndexs.clear();
			}
			else
			{
				dao.rollback();
				JOptionPane.showMessageDialog(center, "�����޸�ʧ�ܣ����Ժ����ԡ�");
			}
			dao.endTransactionModel();
		}
	}
	private void submitAdd()
	{
		if(dao.beginTransactionModel())
		{
			boolean flage=false;
			for(int i:TrainEditedIndexs)
			{
				logger.debug("TrainEditedIndexs:"+i);
				Train train=((AdaptTrain)mTrain.getTableDatas().get(i)).getTrain();
				if(train.getTrain_no()!=null)
					flage=dao.saveTrain(train);
				if(!flage)
					break;
			}
			
			for(int i:TimeAndPriceEditedIndexs)
			{
				TimeAndPrice timeAndPrice=((AdaptTimeAndPrice)mTimeAndPrice.getTableDatas().get(i)).getTimeAndPrice();
				if(timeAndPrice.getTrain_no()!=null&&timeAndPrice.getStation_no()!=0)
					flage=dao.saveTimeAndPrice(timeAndPrice);
				if(!flage)
					break;
			}
			
			for(int i:ScheduleEditedIndexs)
			{
				logger.debug("ScheduleEditedIndexs:"+i);
				Schedule schedule=((AdaptSchedule)mSchedule.getTableDatas().get(i)).getSchedule();
				if(schedule.getTrain_no()!=null&&schedule.getDate()!=null)
					flage=dao.saveSchedule(schedule);
				if(!flage)
					break;
			}
			
			if(flage&&dao.commit())
			{
				JOptionPane.showMessageDialog(center, "���ݱ���ɹ���");
				TimeAndPriceEditedIndexs.clear();
				TrainEditedIndexs.clear();
				ScheduleEditedIndexs.clear();
			}
			else
			{
				dao.rollback();
				JOptionPane.showMessageDialog(center, "���ݱ���ʧ�ܣ����Ժ����ԡ�");
			}
			dao.endTransactionModel();
		}

	}
	private void selectQueryAll()
	{
		logger.debug("queryAll");
		iTrain_no.setEditable(false);
		submit.setText("��ѯ");
		//����������
		mTrain.getTableDatas().clear();
		mTimeAndPrice.getTableDatas().clear();
		mSchedule.getTableDatas().clear();
		//ˢ��
		tTrain.updateUI();
		tTimeAndPrice.updateUI();
		tSchedule.updateUI();
		updateLabelSize();
	}
	private void selectQuery()
	{
		logger.debug("query");
		iTrain_no.setEditable(true);
		submit.setText("��ѯ");
	}
	private void selectUpdate()
	{
		logger.debug("update");
		iTrain_no.setEditable(false);
		submit.setText("�޸�");
	}
	private void selectAdd()
	{
		logger.debug("add");
		iTrain_no.setEditable(false);
		submit.setText("����");
		//����������
		mTrain.getTableDatas().clear();
		mTimeAndPrice.getTableDatas().clear();
		mSchedule.getTableDatas().clear();
		//�ṩ����д����
		mTrain.getTableDatas().add(new AdaptTrain(new Train()));
		for(int i=0;i<50;i++)
			mTimeAndPrice.getTableDatas().add(new AdaptTimeAndPrice(new TimeAndPrice()));
		for(int i=0;i<30;i++)
			mSchedule.getTableDatas().add(new AdaptSchedule(new Schedule()));
		//ˢ��
		tTrain.updateUI();
		tTimeAndPrice.updateUI();
		tSchedule.updateUI();
		updateLabelSize();
	}
	private void updateLabelSize()
	{
		int size=mTrain.getTableDatas().size();
		String str1="��"+size+"����¼";
		size=mTimeAndPrice.getTableDatas().size();
		String str2="��"+size+"����¼";
		size=mSchedule.getTableDatas().size();
		String str3="��"+size+"����¼";
		
		trainSize.setText(str1);
		timeAndPriceSize.setText(str2);
		scheduleSize.setText(str3);
	}
}
