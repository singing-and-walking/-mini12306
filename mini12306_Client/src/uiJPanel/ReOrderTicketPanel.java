package uiJPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import orm.AvailableTrain;
import orm.Ticket;
import orm.TicketPrice;
import orm.User;

public class ReOrderTicketPanel extends MyPanel {
	User user;
	AvailableTrain aTrain;
	TicketPrice ticketPrice;
	JPanel center=new JPanel();
	JComboBox<String> seatTypes=new JComboBox<>();
	JButton submit=new JButton("�ύ����");
	Ticket oldTicket;
	public ReOrderTicketPanel(AvailableTrain aTrain,Ticket ticket,User user) {
		this.aTrain=aTrain;
		this.oldTicket=ticket;
		this.ticketPrice=aTrain.getTicketPrice();
		this.user=user;
		addListener();
	}
	@Override
	public JPanel getNorth() {
		JPanel panel =new JPanel();
		String message=aTrain.getDate()+"\t"+aTrain.getCode()+"��\t"
				+aTrain.getFrom_station_name()+"վ��"+aTrain.getFrom_start_time()+"��)"
				+"----"+aTrain.getTo_station_name()+"վ��"+aTrain.getTo_arrive_time()+"��)";
		String warn="��ܰ��ʾ��ͬһ�û�ͬһ�˳�����ͬһ����ֻ�ܹ���һ�ų�Ʊ��";
		JLabel lable1=new JLabel(message);
		JLabel lable2=new JLabel(warn);
		panel.add(lable1);
		panel.add(lable2);
		return panel;
	}
	@Override
	public JPanel getCenter() {
//		"������", "�ص���", "һ����", "������", "�߼�����", "����", "Ӳ��", "����", "Ӳ��", "����", "����","��ע"
		if(ticketPrice.getA9()!=0)
			seatTypes.addItem("������ ("+ticketPrice.getA9()+")");
		if(ticketPrice.getP()!=0)
			seatTypes.addItem("�ص��� ("+ticketPrice.getP()+")");
		if(ticketPrice.getM()!=0)
			seatTypes.addItem("һ���� ("+ticketPrice.getM()+")");
		if(ticketPrice.getO()!=0)
			seatTypes.addItem("������ ("+ticketPrice.getO()+")");
		if(ticketPrice.getA6()!=0)
			seatTypes.addItem("�߼����� ("+ticketPrice.getA6()+")");
		if(ticketPrice.getA4()!=0)
			seatTypes.addItem("���� ("+ticketPrice.getA4()+")");
		if(ticketPrice.getA3()!=0)
			seatTypes.addItem("Ӳ�� ("+ticketPrice.getA3()+")");
		if(ticketPrice.getA2()!=0)
			seatTypes.addItem("���� ("+ticketPrice.getA2()+")");
		if(ticketPrice.getA1()!=0)
			seatTypes.addItem("Ӳ�� ("+ticketPrice.getA1()+")");
		if(ticketPrice.getWZ()!=0)
			seatTypes.addItem("���� ("+ticketPrice.getWZ()+")");
		if(ticketPrice.getMIN()!=0)
			seatTypes.addItem("���� ("+ticketPrice.getMIN()+")");
		JLabel setSeatType=new JLabel("ϯ��");
		center.add(setSeatType,BorderLayout.CENTER);
		center.add(seatTypes,BorderLayout.CENTER);
		return center;
	}
	@Override
	public JPanel getSouth() {
		JPanel panel =new JPanel();
		panel.add(submit);
		return panel;
	}
	@Override
	protected void addListener() {
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				submit();
			}
		});
		
	}

	protected void submit() {
		Ticket ticket=new Ticket();
		ticket.setOrder_time(System.currentTimeMillis());
		ticket.setUser_name(user.getName());
		ticket.setTrain_no(aTrain.getTrain_no());
		ticket.setCode(aTrain.getCode());
		ticket.setDate(aTrain.getDate());
		ticket.setFrom_station_name(aTrain.getFrom_station_name());
		ticket.setFrom_station_no(aTrain.getFrom_station_no());
		ticket.setFrom_arrive_time(aTrain.getFrom_arrive_time());
		ticket.setFrom_start_time(aTrain.getFrom_start_time());
		ticket.setTo_station_name(aTrain.getTo_station_name());
		ticket.setTo_station_no(aTrain.getTo_station_no());
		ticket.setTo_arrive_time(aTrain.getTo_arrive_time());
		String typeAndprice=(String) seatTypes.getSelectedItem();
		logger.debug("typeAndprice:"+typeAndprice);
		String[] tp=typeAndprice.split("\\(");
		String type=tp[0].trim();
		String price=tp[1].substring(0, tp[1].length()-1);
		logger.debug("type:"+type+"\tprice:"+price);
		ticket.setSeat_type(type);
		ticket.setPrice(Double.valueOf(price));
		int[] carriageAndSeat_no=dao.getCarriage(type, aTrain.getReTickets());
		ticket.setCarriage_no(carriageAndSeat_no[0]);
		ticket.setSeat_no(carriageAndSeat_no[1]);
		if(dao.beginTransactionModel())
		{				
			boolean delete=dao.deleteTicket(oldTicket)&&dao.updateRemainingseats(oldTicket, "+");
			if(dao.getTicket(ticket.getUser_name(),ticket.getTrain_no(),ticket.getDate())==null)
			{
				if(delete&&dao.saveTicket(ticket)&&dao.updateRemainingseats(ticket,"-")&&dao.commit())
				{
					JOptionPane.showMessageDialog(center, "��ǩ�ɹ���");
				}
				else
				{
					dao.rollback();
					JOptionPane.showMessageDialog(center, "��ǩʧ�ܣ����Ժ����ԡ�");
				}
			}
			else
			{
				JOptionPane.showMessageDialog(center, "Ԥ��ʧ�ܣ������θ�����������Ԥ����");
			}
			dao.endTransactionModel();
		}

	}
	
}
