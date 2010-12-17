package net.silencily.sailing.common.dict.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.common.dict.service.CommonBasicCodeService;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.struts.BaseFormPlus;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


/**
 * @author zhaoyifei
 *
 */
public class CommonBasicCodeImpl implements BasicCodeService ,CommonBasicCodeService{

	private HibernateTemplate ht;
	private CommonBasicCode root;
	
	private static Map codes;
	
	
	private void loadCodes()
	{
		codes=new HashMap();
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.isNotNull("code"));
		List l= this.ht.findByCriteria(dc);
		Map m=new HashMap();
		Iterator i=l.iterator();
		while(i.hasNext())
		{
			CommonBasicCode hbc=(CommonBasicCode)i.next();
			codes.put(hbc.getCode(),hbc.getName());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.qware.common.dict.service.DataDictService#getBasicCodeList(java.lang.String, java.lang.String)
	 */
	public Map getBasicCodeList(String sid, String bid) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.eq("subid",sid));
		dc.add(Restrictions.eq("typeCode",bid));
		dc.addOrder(Order.asc("showSequence"));
		List l= this.ht.findByCriteria(dc);
		//原来是Hashmap  现在改为　LinkedHashMap　为了解决排序问题　　by wutao
		Map m=new LinkedHashMap();		
		Iterator i=l.iterator();
		while(i.hasNext())
		{
			CommonBasicCode hbc=(CommonBasicCode)i.next();
			m.put(hbc.getCode(),hbc.getName());
		}
		return m;
	}
	public String getBasicCodeName(String sid, String bid, String id) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.eq("subid",sid));
		dc.add(Restrictions.eq("typeCode",bid));
		dc.add(Restrictions.eq("code",id));
		List l= this.ht.findByCriteria(dc);
		if(l.size()!=0)
		{
			CommonBasicCode hbc=(CommonBasicCode)l.get(0);
			return hbc.getName();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see com.qware.common.dict.service.DataDictService#getBasicCodeTree(java.lang.String)
	 */
	public String getBasicCodeTree(String sid) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.qware.common.dict.service.DataDictService#getDictListByRoot(java.lang.String)
	 */
	public Map getDictListByRoot(String root) {
		// TODO Auto-generated method stub
		CommonBasicCode parent = load(root);
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.eq("parent", parent));
		dc.addOrder(Order.asc("showSequence"));
		List l= this.ht.findByCriteria(dc);
		Map m=new HashMap();
		Iterator i=l.iterator();
		while(i.hasNext())
		{
			CommonBasicCode hbc=(CommonBasicCode)i.next();
			m.put(hbc.getCode(),hbc.getName());
		}
		return m;
	}

	public HibernateTemplate getHt() {
		return ht;
	}

	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	public void delete(CommonBasicCode config) {
		// TODO Auto-generated method stub
		BaseFormPlus.getCodes(config.getSubid()).put(config.getTypeCode(), null);
		this.ht.delete(config);
        loadCodes();
	}

	public List list(CommonBasicCode config) {
		// TODO Auto-generated method stub
		ContextInfo.recoverQuery();
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.eq("parent", config));
		dc.add(Restrictions.lt("deleteState", "2"));
		dc.addOrder(Order.asc("showSequence"));
	return this.ht.findByCriteria(dc);
	}

	public CommonBasicCode load(String id) {
		// TODO Auto-generated method stub
		return (CommonBasicCode) this.ht.load(CommonBasicCode.class, id);
	}
	public CommonBasicCode get(String id) {
		// TODO Auto-generated method stub
		return (CommonBasicCode) this.ht.get(CommonBasicCode.class, id);
	}
	public CommonBasicCode newInstance(String parentId) {
		// TODO Auto-generated method stub
		if("root".equals(parentId))
			return null;
		CommonBasicCode parent = get(parentId);
		if(parent==null)
			return null;
		if(parent.getLayerNum().intValue()<2)
			return null;
		if("2".equals(parent.getDeleteState()))
			return null;
		if(new Integer(3).equals(parent.getLayerNum()))
			parent=parent.getParent();
		CommonBasicCode r = new CommonBasicCode();
		r.setId(Tools.getPKCode());
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class)
			.add(Restrictions.eq("parent", parent))
			.setProjection(Projections.max("showSequence"));
		List list = this.ht.findByCriteria(dc);
		int i=1;
		if (list.size() > 0 && list.get(0) != null) {
			i = ((Integer)list.get(0)).intValue();
			i++;
		}
		r.setParent(parent);
		r.setSubid(r.getParent().getSubid());
		r.setTypeCode(r.getParent().getTypeCode());
		r.setShowSequence(new Integer(i));
		return r;
	}

	public void saveOrUpdate(CommonBasicCode config) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(config.getCode()) && "1".equals(config.getDeleteState())) {
			config.setCode(getMaxCode(config));
		}
		this.ht.saveOrUpdate(config);
		BaseFormPlus.getCodes(config.getSubid()).put(config.getTypeCode(), null);
		loadCodes();
	}

	public CommonBasicCode getRoot() {
		return root;
	}

	public void setRoot(CommonBasicCode root) {
		this.root = root;
	}
	public ComboSupportList getComboListAll() {
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.isNotNull("code"));
		dc.addOrder(Order.asc("showSequence"));
		List l= this.ht.findByCriteria(dc);
		
		ComboSupportList supportList = new ComboSupportList("code", "name");
		supportList.addAll(l);		
		return supportList;
	}
	public ComboSupportList getComboList(String sid, String bid) {
		// TODO Auto-generated method stub
		
		List unitList = new ArrayList();
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.eq("subid",sid));
		dc.add(Restrictions.eq("typeCode",bid));
		dc.add(Restrictions.isNotNull("code"));
		dc.addOrder(Order.asc("showSequence"));
		List l= this.ht.findByCriteria(dc);
		
		ComboSupportList supportList = new ComboSupportList("code", "name");
		supportList.addAll(l);		
		return supportList;
	}
	public String getNameByCode(String code) {
		// TODO Auto-generated method stub
		if(codes==null || codes.isEmpty())
			loadCodes();
		if(!codes.containsKey(code))
		return null;
		return (String)codes.get(code);
	}
	
	/**
	 * 功能描述 获取指定代码的信息
	 * @param basicCd 要查询的代码
	 * @return List
	 */
	public List getListByCode(String basicCd) {
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class)
		.add(Restrictions.eq("code", basicCd));
		return this.ht.findByCriteria(dc);
	}
	
	/**
	 * 功能描述 生成新代码
	 * @param config 新增的基础编码对象
	 * @return String 新生成的代码
	 */
	private String getMaxCode(CommonBasicCode config) {
		String rtnCode = "";
		String subId = config.getSubid();
		String typeCode = config.getTypeCode();
		
		DetachedCriteria dc = DetachedCriteria.forClass(CommonBasicCode.class);
		dc.add(Restrictions.eq("subid", subId));
		dc.add(Restrictions.eq("typeCode", typeCode));
		dc.add(Restrictions.eq("deleteState", "1"));
		dc.add(Restrictions.like("code", subId + typeCode + "%"));
		dc.setProjection(Projections.max("code"));
		List list = this.ht.findByCriteria(dc);
		if (list.size()>0) {
			String maxCode = (String)list.get(0);
			if (null != maxCode) {
				maxCode = maxCode.substring(maxCode.length()-5);
				int maxNumber = Integer.parseInt(maxCode) + 1;
				rtnCode = subId + typeCode + "-" + maxNumber;
			} else {
				rtnCode = subId + typeCode + "-10001";
			}
		} else {
			rtnCode = subId + typeCode + "-10001";
		}
		return rtnCode;		
	}
}
