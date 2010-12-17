/**
 * Name: SubListChangeRecord.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.constant.SerializableConstants;
import net.silencily.sailing.basic.wf.dto.ClonedObject;
import net.silencily.sailing.framework.persistent.Entity;
import net.silencily.sailing.utils.Tools;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;


/**
 * Loging subList's variational records 记录子条目的变化履历
 * 
 * @author Bis liyan
 */
public class SubListChangeRecord {
	/**
	 * 得到记录子条目的变化履历描述
	 * 
	 * @param oldList
	 * @param newList
	 * @throws Exception
	 * @return scription
	 */
	public static String getChangeRecord(List oldList, List newList)
			throws Exception {

		Description description = new Description();		
		if (oldList == null) {
			oldList = new ArrayList();
		}
		if (newList == null) {
			newList = new ArrayList();
		}
		description.append(delete(oldList, newList));
		description.append(add(oldList, newList));
		description.append(update(oldList, newList));

		return description.toString();
	}
	private static String[] add(List oldList, List newList){
		StringBuffer buffer = new StringBuffer();
		// 新增的条目
		List newItems = new ArrayList();
		for (int i = 0; i < newList.size(); i++) {

			Object item = newList.get(i);
			// 观察物资编码是否有变化。
			boolean existInOrignal = CollectionUtils.exists(oldList,
					new FindItemInObjectPredicateForAdd(item));
			if (!existInOrignal) {
				newItems.add(item);
			}
		}
		for (int i = 0; i < newItems.size(); i++) {
			try {
				appendOneRow(buffer, newItems.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return postDescription(buffer, "增加了以下条目");		
	}
	private static String[] delete(List oldList, List newList){
		StringBuffer buffer = new StringBuffer();		
		// 删除的条目
		List deleltedItems = new ArrayList();
		// 遍历旧的条目集合
		for (int i = 0; i < oldList.size(); i++) {
			// 得到一个条目
			Object item = oldList.get(i);
			// 观察条目内容是否有变化。
			boolean existInNew = CollectionUtils.exists(newList,
					new FindItemInObjectPredicateForDelOrUpd(item));
			if (!existInNew) {
				// 如果没有就代表被删除了
				deleltedItems.add(item);
			}
		}
		// 遍历删除条目集合
		for (int i = 0; i < deleltedItems.size(); i++) {
			try {
				appendDelOneRow(buffer, (ClonedObject)deleltedItems.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return postDescription(buffer, "删除了以下条目");
		
	}
	
	private static String[] update(List oldList, List newList){	
		StringBuffer buffer = new StringBuffer();		
		try {
			// 遍历旧集合
			for (int i = 0; i < oldList.size(); i++) {
				Object item = oldList.get(i);
								
				Object newItem = CollectionUtils.find(newList,
						new FindItemInObjectPredicateForDelOrUpd(item));
				// 从新集合中找到旧集合里存在的数据，就代表有更新。			
				if (newItem != null) {
					Class cls = Tools.getUnProxyClass(newItem.getClass());
					Field[] fields = cls.getDeclaredFields();
					// 取出所有可能更新的元素分旧，新两个。
					boolean isChange = false;
					boolean first = true;
					for (int j = 0; j < fields.length; j++) {						
						Field field = fields[j];
						if (!ClassOperateTools.isSpecialType(field))
							continue;
						Object newObj = ClassOperateTools.getValueByName(field.getName(), newItem);
						Object oldObj = ((ClonedObject)item).getProperties().get(field.getName());						
						
						//比对所有看看是否有变化
						isChange = ObjectOperateTools.isNotEqual(oldObj, newObj);
						
						if(isChange){
//							如果有更新则记录
							if(first){
								buffer.append(SerializableConstants.LI_PLACE_HOLDER1);
								first = false;
							}
							buffer.append("原" + field.getName() + " [ ");
							buffer.append(oldObj);
							buffer.append(" ] 更改后" + field.getName() + " [ ");
							buffer.append(newObj);
							buffer.append(" ] ");
							
						}
					}
					if(isChange){
						buffer.append(SerializableConstants.LI_PLACE_HOLDER2);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return postDescription(buffer, "对以下条目做了改动");
		
	}
	
	//得到一条描述
	public static void appendOneRow(StringBuffer buffer, Object obj)
			throws Exception {		
		if (obj == null) {
			return;
		}
		buffer.append(SerializableConstants.LI_PLACE_HOLDER1);
		Class cls = Tools.getUnProxyClass(obj.getClass());
		Field[] fields = cls.getDeclaredFields();
		boolean first = true;
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			
			if (!ClassOperateTools.isSpecialType(field)) 				
				continue;
			
			Object value = ClassOperateTools.getValueByName(field.getName(), obj);
			if (first) {
				first = false;
				buffer.append(field.getName() + " [ ");
				buffer.append(value);
			} else {
				buffer.append(" ] " + field.getName() + " [ ");
				buffer.append(value);
				//if (i == fields.length - 1) {				
					
				//}
			}
		}
		buffer.append(" ] ");
		buffer.append(SerializableConstants.LI_PLACE_HOLDER2);
	}
	//得到一条删除描述
	public static void appendDelOneRow(StringBuffer buffer, ClonedObject obj)
			throws Exception {		
		if (obj == null) {
			return;
		}
		buffer.append(SerializableConstants.LI_PLACE_HOLDER1);	
		
		Map properties = obj.getProperties();
		Set fieldNameSet = properties.keySet();
		Iterator it = fieldNameSet.iterator();
		boolean first = true;
		
		while (it.hasNext()) {
			String key = (String)it.next();
			Object value = properties.get(key);
			if (first) {
				first = false;
				buffer.append(key + " [ ");
				buffer.append(value);
			} else {
				buffer.append(" ] " + key + " [ ");
				buffer.append(value);
			}
		}
		
		buffer.append(" ] ");
		buffer.append(SerializableConstants.LI_PLACE_HOLDER2);
	}
	private static String[] postDescription(StringBuffer buffer, String title) {
		String[] temp = new String[2];
		temp[0] = title;
		temp[1] = buffer.toString();
		return temp;
	}

	public static class FindItemInObjectPredicateForDelOrUpd implements Predicate {
		
		private Object oldObject;

		public FindItemInObjectPredicateForDelOrUpd(Object object) {
			this.oldObject = object;
		}

		public boolean evaluate(Object newObject) {
			ClonedObject oldEnt = (ClonedObject)oldObject;
			Entity newEnt = (Entity)newObject;				
			return newEnt != null && StringUtils.equals(newEnt.getId(), oldEnt.getId());
		}
	}
	public static class FindItemInObjectPredicateForAdd implements Predicate {
		
		private Object oldObject;

		public FindItemInObjectPredicateForAdd(Object object) {
			this.oldObject = object;
		}

		public boolean evaluate(Object newObject) {
			Entity oldEnt = (Entity)oldObject;
			ClonedObject newEnt = (ClonedObject)newObject;				
			return newEnt != null && StringUtils.equals(newEnt.getId(), oldEnt.getId());
		}
	}
	public static void main(String[] args) throws Exception {
		/*com.qware.gr.domain.TblGrRunNote a = new com.qware.gr.domain.TblGrRunNote();
		a.setNoteRmk("123");
		a.setId("1");
		ClonedObject cloneA = ObjectOperateTools.falseClone(a);
		com.qware.gr.domain.TblGrRunNote b = new com.qware.gr.domain.TblGrRunNote();
		b.setId("2");
		b.setNoteEmpCd("abc");
		ClonedObject cloneB = ObjectOperateTools.falseClone(b);
		com.qware.gr.domain.TblGrRunNote c = new com.qware.gr.domain.TblGrRunNote();
		c.setId("3");
		c.setNoteEmpCd("111");
		com.qware.gr.domain.TblGrRunNote d = new com.qware.gr.domain.TblGrRunNote();
		d.setId("2");
		List oldList = new ArrayList();
		oldList.add(cloneA);
		oldList.add(cloneB);
		List newList = new ArrayList();
		d.setNoteEmpCd("cba");
		newList.add(d);
		newList.add(c);
		System.out.println(getChangeRecord(oldList, newList));*/
	}
}
