package net.silencily.sailing.basic.uf.funtree.web;

import java.util.List;

import net.silencily.sailing.struts.BaseFormPlus;

/**
 * һ�廯�칫��������ʾ��Form
 * @author huxf
 * @version $Id: DisplayFunTreeForm.java,v 1.1 2010/12/10 10:56:46 silencily Exp $
 * @since 2007-11-21
 */
public class DisplayFunTreeForm extends BaseFormPlus {
	/** ������ʾ�ù����� */
	private List treeList;

	private static final long serialVersionUID = 1L;

	public List getTreeList() {
		return treeList;
	}

	public void setTreeList(List treeList) {
		this.treeList = treeList;
	}
}
