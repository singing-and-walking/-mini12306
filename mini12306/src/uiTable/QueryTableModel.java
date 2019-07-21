package uiTable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import orm.AvailableTrain;

public class QueryTableModel extends MyTableModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String[] columnNames= {"����", "����վ--����վ", "����ʱ��--����ʱ��", "������", "�ص���", "һ����", "������", "�߼�����", "����", "Ӳ��", "����", "Ӳ��", "����", "����","��ע"};
	public QueryTableModel() {
		super(columnNames);
	}
	@Override
	public void setTableDatas(List datas) {
		tableDatas.clear();
		for(Object aTrain:datas)
		{
			AdaptAvaTrain a=new  AdaptAvaTrain();
			a.setaTrain((AvailableTrain) aTrain);
			tableDatas.add(a);
		}	
	}
}
