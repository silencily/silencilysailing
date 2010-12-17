package net.silencily.sailing.common.ui.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 将实现了Treeable的接口的类的集合重新排序，并得到新的可以实现前端树的集合
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
	
	/** 初始化树节点ID，以这个节点为父亲 开始排序*/
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
		
		// 得到父节点下一个level的子节点,如果父节点为空那么就是最上层的节点
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
