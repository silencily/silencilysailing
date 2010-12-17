package net.silencily.sailing.common.basiccode.service.impl;

import java.util.List;

import net.silencily.sailing.common.basiccode.service.CommonBasicCodeInitService;
import net.silencily.sailing.common.dict.domain.CommonBasicCode;
import net.silencily.sailing.framework.core.ContextInfo;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;


public class CommonBasicCodeInitImpl implements CommonBasicCodeInitService {

	private HibernateTemplate ht;

	public HibernateTemplate getHt() {
		return ht;
	}

	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	public List load(String id) {
		ContextInfo.recoverQuery();
		DetachedCriteria d = DetachedCriteria.forClass(CommonBasicCode.class);
		d.add(Restrictions.eq("subid", id));
		List l = ht.findByCriteria(d);
		return l;
	}

	public String reset(String subID) {
		String rsInfo = "";
		CommonBasicCode bc = null;
		ContextInfo.recoverQuery();
		DetachedCriteria d = DetachedCriteria.forClass(CommonBasicCode.class);
		d.add(Restrictions.eq("subid", subID));
		List l = ht.findByCriteria(d);
		if (l.size() == 0) {
			rsInfo = "��¼�����ڣ��������";
		} else {
			for (int i = 0; i < l.size(); i++) {
				bc = new CommonBasicCode();
				bc = (CommonBasicCode) l.get(i);
				ht.delete(bc);
			}
			rsInfo = "���óɹ�";
		}
		return rsInfo;
	}

	public List find(String id) {
		ContextInfo.recoverQuery();
		DetachedCriteria d = DetachedCriteria.forClass(CommonBasicCode.class);
		d.add(Restrictions.eq("id", id));
		List l = ht.findByCriteria(d);
		return l;
	}

}
