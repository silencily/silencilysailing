/**
 * Name: MainBeanChangeRecord.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.constant.SerializableConstants;
import net.silencily.sailing.basic.wf.dto.ClonedObject;
import net.silencily.sailing.basic.wf.service.WFService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.utils.Tools;



/**
 * Loging mainBean's variational records 记录主bean的变化履历
 * 
 * @author Bis liyan
 */
public class MainBeanChangeRecord {
	public static void main(String[] args) throws Exception {
		/*TblGrRunNote a = new TblGrRunNote();
		a.setNoteRmk("123");
		ClonedObject co = ObjectOperateTools.falseClone(a);
		a.setNoteRmk("321");
		System.out.println(getChangeRecord(co, a));*/
	}

	/**
	 * 得到记录主bean的变化履历描述
	 * 
	 * @param oldInfo
	 * @param newInfo           
	 * @throws Exception
	 * @return scription
	 */
	public static String getChangeRecord(ClonedObject oldInfo,
			WorkflowInfo newInfo) throws Exception {

		Description description = new Description();

		validate(oldInfo, newInfo);

		description.append(execute(oldInfo, newInfo));

		return description.toString();
	}

	private static void validate(ClonedObject oldInfo, WorkflowInfo newInfo)
			throws Exception {

		if (oldInfo == null) {
			throw new Exception("oldInfo is not a pojo");
		}
		if (newInfo == null) {
			throw new Exception("newInfo is not a pojo");
		}
	}

	private static String[] execute(ClonedObject oldInfo, WorkflowInfo newInfo) {
		StringBuffer buffer = new StringBuffer();
		boolean isChange = false;
		boolean first = true;
		try {
			//得到中文名称
			WFService serviceTemp = (WFService)ServiceProvider.getService(WFService.SERVICE_NAME);
			Map map = ClassOperateTools.getFieldMap(newInfo);

			Set fieldNameSet = map.keySet();
			Iterator it = fieldNameSet.iterator();
			while (it.hasNext()) {
				isChange = false;
				String fieldName = (String) it.next();				
				
				isChange = ObjectOperateTools.isNotEqual(oldInfo.getProperties().get(fieldName), ClassOperateTools.getValueByName(fieldName, newInfo));

				if (isChange) {
					if (first) {
						buffer.append(SerializableConstants.LI_PLACE_HOLDER1);
						first = false;
					}
					buffer.append("原" + serviceTemp.searchStepFieldComment(Tools.getUnProxyClass(newInfo.getClass()),((String)map.get(fieldName))) + " [ ");
					buffer.append(oldInfo.getProperties().get(fieldName));
					buffer.append(" ] 更改后" +serviceTemp.searchStepFieldComment(Tools.getUnProxyClass(newInfo.getClass()),((String)map.get(fieldName))) + " [ ");
					buffer.append(ClassOperateTools.getValueByName(fieldName,
							newInfo));
					buffer.append(" ] ");
				}
			}

			if (isChange) {
				buffer.append(SerializableConstants.LI_PLACE_HOLDER2);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postDescription(buffer, "对以下条目做了改动");
	}

	private static String[] postDescription(StringBuffer buffer, String title) {
		String[] temp = new String[2];
		temp[0] = title;
		temp[1] = buffer.toString();
		return temp;
	}

}
