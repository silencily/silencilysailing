package net.silencily.sailing.common.ui.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * ��ʵ����Treeable�Ľӿڵ���ļ����������򣬲��õ��µĿ���ʵ��ǰ�����ļ���
 * <p>
 * <code>
 * new TreeTemplate(origList).getResultList();
 * </code>
 * 
 */
public class TreeTemplate {

	private List origList;

	private List resultList = new ArrayList();

	private boolean isFirstGet = true;


	public TreeTemplate(List origList) {
		this.origList = origList;
	}

	public List getResultList() {
		if (isFirstGet) {
			sortList(null, 0);
			isFirstGet = false;
		}
		return resultList;
	}
	
	/** ��ʼ�����ڵ�ID��������ڵ�Ϊ���� ��ʼ����*/
	public List getResultList(String firstId) {
		if (isFirstGet){
			if( null != firstId ){
				sortList(firstId, 0);
				isFirstGet = true;
			}else  {
				sortList(null, 0);
				isFirstGet = false;
			}
		}
		return resultList;
	}

	public void setOrigList(List origList) {
		this.origList = origList;
	}

	private void sortList(String parentId, int level) {
		
		// �õ����ڵ���һ��level���ӽڵ�,������ڵ�Ϊ����ô�������ϲ�Ľڵ�
		for (Iterator iter = origList.iterator(); iter.hasNext();) {
			Treeable element = (Treeable) iter.next();
			if( (null == parentId && StringUtils.isBlank(element.getParentId()))|| 
				(null != parentId && parentId.equals(element.getParentId()))
			  ){
					element.setLevel(new Integer(level));
					resultList.add(element);
					String id = element.getId();
					sortList(id, level + 1);
			}
		}
	}
	
}
