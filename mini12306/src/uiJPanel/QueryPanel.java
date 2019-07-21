package uiJPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import databaseAccess.DAO;
import orm.AvailableTrain;
import uiJFrame.OrderTicket;
import uiJFrame.QueryTransfer;
import uiTable.AdaptAvaTrain;
import uiTable.AdaptTicketPrice;
import uiTable.MyTableModel;
import uiTable.QueryTableModel;

public class QueryPanel extends MyPanel{
	JButton submit=new JButton("��ѯ");
	JButton transfer=new JButton("��������");
	JTextField iFrom=new JTextField(20);
	JTextField iTo=new JTextField(20);
	JXDatePicker datepick = new JXDatePicker(new Date(System.currentTimeMillis()));
	
	MyTableModel tableModel=new QueryTableModel();
	JTable table=new JTable(tableModel);
	JScrollPane jsPanel=new JScrollPane(table);
	//��ȡ��Ļ�ֱ���
	private final Dimension dScreen=Toolkit.getDefaultToolkit().getScreenSize();
	private final int width=dScreen.width;
	private final int height=dScreen.height;
	//Tool
	private DAO dao=new DAO();
	private Logger logger=LogManager.getLogger();
	//���췽��
	public QueryPanel() {
		iFrom.setText("��ͷ");
		iTo.setText("�Ϻ�");
		// �����п��
        table.getColumnModel().getColumn(1).setPreferredWidth(width/10);
        table.getColumnModel().getColumn(2).setPreferredWidth(width/10);

		addListener();
	}
	@Override
	public JPanel getNorth() {
		JPanel panel=new JPanel();
//		panel.setLayout(new GridLayout(1,7));
		JLabel tForm =new JLabel("������");
		JLabel tTo =new JLabel("Ŀ�ĵ�");
		JLabel tDate =new JLabel("Ŀ�ĵ�");
		panel.add(tForm);
		panel.add(iFrom);
		panel.add(tTo);
		panel.add(iTo);
		panel.add(tDate);
		panel.add(datepick);
		panel.add(submit);
		panel.add(transfer);
		return panel;
	}
	@Override
	public JPanel getCenter() {
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(jsPanel,BorderLayout.CENTER);
		return panel;
	}

	protected void addListener()
	{
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitQuery();
			}
		});
		transfer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 transfer();
			}
		});
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) 
				{
					logger.debug("��⵽���޸�");
					return;//����Ƿ����ڱ��޸ģ�
				}
				int row=table.getSelectedRow();
				int column=table.getSelectedColumn();
				if(tableModel.getTableDatas().get(row).getClass()==AdaptAvaTrain.class)
				{
					logger.debug("row:"+row+"\ttableModel.getTableDatas().size():"+tableModel.getTableDatas().size());
					if(column==14)
						order(row);
					else if(row==tableModel.getTableDatas().size()-1||tableModel.getTableDatas().get(row+1).getClass()!=AdaptTicketPrice.class)
						showPrice(row);
					else 
						hidePrice(row);

				}
				else
				{
					
				}
			}
			
		});
	}	
	private void submitQuery()
	{
		String fromStation=iFrom.getText();
		String toStation=iTo.getText();
		Date date=new Date(datepick.getDate().getTime());
		fromStation=fromStation+"%";
		toStation=toStation+"%";
		List<AvailableTrain> aTrains=dao.getAvailableTrains(fromStation, toStation, date);
		for(AvailableTrain aTrain:aTrains)
			dao.getReTickets(aTrain);
		tableModel.setTableDatas(aTrains);
		table.updateUI();	
	}
	private void showPrice(int row)
	{
		AvailableTrain aTrain=((AdaptAvaTrain)tableModel.getTableDatas().get(row)).getaTrain();
		dao.getTicketPrice(aTrain);
		AdaptTicketPrice atp=new AdaptTicketPrice();
		atp.setaTrain(aTrain);
		tableModel.getTableDatas().add(row+1,atp);
		table.updateUI();
	}
	private void hidePrice(int row)
	{
		tableModel.getTableDatas().remove(row+1);
		table.updateUI();
	}
	private void order(int row)
	{
		logger.debug("Ԥ����"+row);
		AvailableTrain aTrain=((AdaptAvaTrain)tableModel.getTableDatas().get(row)).getaTrain();
		if(aTrain.getTicketPrice()==null)
		{
			logger.debug("��Ԥ��ʱticketPrice==null");
			dao.getTicketPrice(aTrain);
			if(aTrain.getTicketPrice()==null)
				logger.debug("getTicketPriceʧ��");
		}
		new OrderTicket(aTrain);
	}
	private void transfer()
	{
		new QueryTransfer();
	}
}
