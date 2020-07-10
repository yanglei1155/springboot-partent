//package com.insigma.base;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.fr.data.AbstractTableData;
//import com.fr.general.data.TableDataException;
//
///**
// * 报表抽象类
// * @author xhy
// * @since:2019年3月26日下午8:19:10
// */
//public abstract class BaseReportData   extends AbstractTableData{
//	private static final long serialVersionUID = 1L;
//	protected Logger logger = LoggerFactory.getLogger(getClass());
//	static{
//		System.setProperty("spring.configfile", "spring-modules.xml");
//	}
//    /**
//     * 列名数组，保存程序数据集所有列名   
//     */
//    protected String[] columnNames;   
//    /**
//     * 定义程序数据集的列数量   
//     */
//    protected int columnNum;   
//    // 保存查询表的实际列数量   
//    // 保存查询得到列值   
//    /**
//     * 行object对象数据列表
//     */
//    protected List<Object[]> valueList ;
//    
//    /**
//     * 初始化数据
//     * @author xhy
//     * @since:2019年3月26日下午8:31:08
//     */
//    protected abstract void processData();
//    
//   
//    
//    public BaseReportData() {
//		super();
//	}
//	public BaseReportData(String[] columnNames, int columnNum, List<Object[]> valueList) {
//		super();
//		this.columnNames = columnNames;
//		this.columnNum = columnNum;
//		this.valueList = valueList;
//	}
//	
//	  @Override
//		public int getColumnCount() throws TableDataException {
//			return columnNum; 
//		}
//
//		@Override
//		public String getColumnName(int arg0) throws TableDataException {
//			return columnNames[arg0];
//		}
//
//		@Override
//		public int getRowCount() throws TableDataException {
//			processData();   
//	        return valueList.size();
//		}
//
//		@Override
//		public Object getValueAt(int rowIndex, int columnIndex) {
//			processData();   
//	        return ((Object[]) valueList.get(rowIndex))[columnIndex];
//		}
//
//
//		public String[] getColumnNames() {
//			return columnNames;
//		}
//
//		public void setColumnNames(String[] columnNames) {
//			this.columnNames = columnNames;
//		}
//		public int getColumnNum() {
//			return columnNum;
//		}
//		public void setColumnNum(int columnNum) {
//			this.columnNum = columnNum;
//		}
//
//		public List<Object[]> getValueList() {
//			return valueList;
//		}
//
//		public void setValueList(List<Object[]> valueList) {
//			this.valueList = valueList;
//		}
//	
//	
//	
//    
//    
//    
//    
//
//}
