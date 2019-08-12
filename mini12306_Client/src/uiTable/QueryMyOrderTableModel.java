package uiTable;

import java.util.List;

import orm.Ticket;

public class QueryMyOrderTableModel extends MyTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String[] columnNames= {"�µ�����", "����", "�˳�����", "����վ", "�г���վʱ��", "�г�����ʱ��", "Ŀ��վ", "�г�����ʱ��", "��λ���", "�۸�", "�����", "��λ��","��ǩ","��Ʊ"};
	public QueryMyOrderTableModel() {
		// TODO Auto-generated constructor stub
		super(columnNames);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setTableDatas(List<?> datas) {
		tableDatas.clear();
		for(Ticket ticket:(List<Ticket>)datas)
			tableDatas.add(new AdaptTicket(ticket));
	}

}
