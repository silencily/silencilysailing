package net.silencily.sailing.basic.sm.domain;

import java.util.List;

import net.silencily.sailing.common.domain.tree.FlatTreeNode;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.struts.BaseFormPlus;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class TblCmnModel extends BaseFormPlus implements FlatTreeNode {

	// public List sysList;
	private String name;
	private String code;

	// private List children;

	public List getChildren() {

		HibernateTemplate ht = (HibernateTemplate) ServiceProvider
				.getService("system.hibernateTemplate");
		Session se = ht.getSessionFactory().getCurrentSession();
		Query query = se
				.createQuery("select c from TblCmnEntity c where c.delFlg='0' and c.fatherModule='"
						+ code + "'");
		return query.list();
		// return new ArrayList();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		// TODO Auto-generated method stub
		return this;
	}

	public String getImageType() {
		// TODO Auto-generated method stub
		return "0";
	}

	public boolean isCanbeSelected() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isHasChildren() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	public List getSysList() {

		return null;
	}

	public String getCaptain() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public String getIdentity() {
		// TODO Auto-generated method stub
		return this.code;
	}

}
