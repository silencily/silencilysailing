/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.domain.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.silencily.sailing.framework.web.view.ComboSupportList;

import org.springframework.util.Assert;


/**
 * @since 2006-7-27
 * @author java2enterprise
 * @version $Id: FlatTreeUtils.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public abstract class FlatTreeUtils {
	
	/**
	 * �� {@link FlatTreeNode} �ļ������л����ַ����� /publicresource/web/public/scripts/codeTree.js ����
	 * @param treeNodes ����, Ԫ�����ͱ����� {@link FlatTreeNode}
	 * @param allNodeSelectable �Ƿ����нڵ㶼���Ա�ѡ��, ��������չʾ����ʱ����Ϊ true, ��������ѡ��ʱֵ�� {@link FlatTreeNode#isCanbeSelected()} ����
	 * @return ���л�����ַ���
	 * ��ʽ���£�
	 * id###name###�Ƿ����ӽڵ�0/1###�Ƿ�Ҷ��###ͼƬ����###�Ƿ񲻿�ѡ(0/1)|||other
	 */
	public static String serialize(Collection treeNodes, boolean allNodeSelectable) {
		/*Assert.notNull(treeNodes, " treeNodes required. ");
		
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Iterator iter = treeNodes.iterator(); iter.hasNext(); ) {
			Object forEach = iter.next();
			Assert.isInstanceOf(FlatTreeNode.class, forEach);
			FlatTreeNode node = (FlatTreeNode) forEach;
			if (!first) {
				buffer.append(ComboSupportList.ELEMENT_SEPARATOR);
			}
			buffer.append(node.getIdentity());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.getCaptain());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.isHasChildren() ? 0 : 1);
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.isLeaf());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.getImageType());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			
			if (allNodeSelectable) {
				buffer.append(0);
			} else {
				// ע�� codeTree.js �еĲ����� "�Ƿ񲻿�ѡ", ����Ҫ��
				buffer.append(node.isCanbeSelected() ? 0 : 1);
			}
			first = false;
		}
		return buffer.toString();*/
		return serialize(treeNodes, allNodeSelectable, false, false);
	}
	
	public  static String serialize(Collection treeNodes, boolean allNodeSelectable, boolean isFolderIcon){
		return serialize(treeNodes, allNodeSelectable, true, isFolderIcon);
	}
	
	private  static String serialize(Collection treeNodes, boolean allNodeSelectable, boolean customIcon, boolean isFolderIcon){
		Assert.notNull(treeNodes, " treeNodes required. ");
		
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Iterator iter = treeNodes.iterator(); iter.hasNext(); ) {
			Object forEach = iter.next();
			Assert.isInstanceOf(FlatTreeNode.class, forEach);
			FlatTreeNode node = (FlatTreeNode) forEach;
			if (!first) {
				buffer.append(ComboSupportList.ELEMENT_SEPARATOR);
			}
			buffer.append(node.getIdentity());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.getCaptain());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			if(customIcon){
				buffer.append(isFolderIcon ? 0 : 1);
			}else{
				buffer.append(node.isHasChildren() ? 0 : 1);
			}
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.isLeaf());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			buffer.append(node.getImageType());
			buffer.append(ComboSupportList.TEXT_VALUE_SEPARATOR);
			
			if (allNodeSelectable) {
				buffer.append(0);
			} else {
				// ע�� codeTree.js �еĲ����� "�Ƿ񲻿�ѡ", ����Ҫ��
				buffer.append(node.isCanbeSelected() ? 0 : 1);
			}
			first = false;
		}
		return buffer.toString();
	}
	
	private static String SEP_SLASH = "/";
	public static String getFatherPath(FlatTreeNodePlus node){
		StringBuffer sb = new StringBuffer(SEP_SLASH);
		ArrayList arr = new ArrayList(0);
		for(FlatTreeNodePlus curNode = node;
			curNode != null && curNode.getTreeFather() != null && 
			curNode.getTreeFather().getId() != null && 
			!"".equals(curNode.getTreeFather().getId()); curNode = curNode.getTreeFather()){
			if("root".equals(curNode.getId())){
				break;
			}
			arr.add(curNode.getCaptain());
			
		}
		for(int i = arr.size() - 1;i >= 0 ; i--){
			sb.append(arr.get(i).toString()).append(SEP_SLASH);
		}
		return sb.toString();
	}
	
}
