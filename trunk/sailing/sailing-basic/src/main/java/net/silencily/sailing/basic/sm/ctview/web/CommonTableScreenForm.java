package net.silencily.sailing.basic.sm.ctview.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.common.crud.domain.CommonTableScreen;
import net.silencily.sailing.common.crud.domain.CommonTableView;
import net.silencily.sailing.common.crud.service.CommonService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author 
 * @version
 */
public class CommonTableScreenForm extends BaseFormPlus
 {

	private CommonTableScreen bean;
	private ComboSupportList orderAscComboList;
	private ComboSupportList isDbFieldComboList;
	private String orderAsc;
	private List tableViews = new ArrayList();
	
	private static Map orderAscMap = new HashMap();
	static{
		orderAscMap.put("0", "升序");
		orderAscMap.put("1", "降序");
	}
	
	private static Map dbFieldMap = new HashMap();
	static{
		dbFieldMap.put("0", "否");
		dbFieldMap.put("1", "是");
	}
	
	public CommonTableScreen getBean() {
		if (bean == null) {
			if (StringUtils.isBlank(getOid())) {
				bean = new CommonTableScreen();
			} else {
				bean = (CommonTableScreen)((CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME)).load(
						CommonTableScreen.class, getOid());
			}
		}
		return bean;
	}

	public void setBean(CommonTableScreen bean) {
		this.bean = bean;
	}

	public void setTableViews(List tableViews) {
		this.tableViews = tableViews;
	}

	public List getTableViews() {
		return tableViews;
	}
	
	public CommonTableView getTableViews(int i) {
		
		while(tableViews.size()<=i){
			tableViews.add(null);
		}
		CommonTableView tableView = (CommonTableView) tableViews.get(i);
		
		String tableViewsId = request.getParameter("tableViews[" + i + "].id");
		//获取默认排序
		String order = request.getParameter("orderAsc");
		//获取画面标识
		String tableName = request.getParameter("bean.tableName");
		if(StringUtils.isNotBlank(tableViewsId)){
			tableView = (CommonTableView)((CommonService) ServiceProvider.getService(CommonService.SERVICE_NAME)).load(
					CommonTableView.class, tableViewsId);
		}
		
		if (null==tableView) {
			tableView = new CommonTableView();
		}
		if(StringUtils.isBlank(tableView.getId())){
			tableView.setId(Tools.getPKCode());
		}
		
		//将默认排序及画面标识赋予表CommonService
		tableView.setOrderAsc(order==null?"":order);
		tableView.setTableName(tableName);
		
		
		tableViews.set(i, tableView);
		
		return tableView;
	}
	
	public ComboSupportList getOrderAscComboList() {
		if(null == orderAscComboList)
		{
			orderAscComboList = new  ComboSupportList(getOrderAscMap());
		}
		return orderAscComboList;
	}
	
	public void setOrderAscComboList(ComboSupportList orderAscComboList) {
		this.orderAscComboList = orderAscComboList;
	}

	public static Map getOrderAscMap() {
		return orderAscMap;
	}

	public static void setOrderAscMap(Map orderAscMap) {
		CommonTableScreenForm.orderAscMap = orderAscMap;
	}
	
	public static Map getDbFieldMap() {
		return dbFieldMap;
	}

	public static void setDbFieldMap(Map dbFieldMap) {
		CommonTableScreenForm.dbFieldMap = dbFieldMap;
	}

	public ComboSupportList getIsDbFieldComboList() {
		if(null == isDbFieldComboList)
		{
			isDbFieldComboList = new  ComboSupportList(getDbFieldMap());
		}
		return isDbFieldComboList;
	}

	public void setIsDbFieldComboList(ComboSupportList isDbFieldComboList) {
		this.isDbFieldComboList = isDbFieldComboList;
	}

	public String getOrderAsc() {
		return orderAsc;
	}

	public void setOrderAsc(String orderAsc) {
		this.orderAsc = orderAsc;
	}
}
