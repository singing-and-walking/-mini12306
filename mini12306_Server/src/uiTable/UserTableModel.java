package uiTable;

import java.util.List;

import orm.User;

public class UserTableModel extends MyTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String[] columnNames= {"�û���","����","ע��"};
	public UserTableModel() {
		super(columnNames);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTableDatas(List<?> datas) {
		tableDatas.clear();
		for(User user:(List<User>)datas)
		{
			tableDatas.add(new AdaptUser(user));
		}

	}

}
