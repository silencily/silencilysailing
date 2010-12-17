/**
 * Name: Description.java
 * Author: Bis liyan
 */
package net.silencily.sailing.basic.wf.util;

import net.silencily.sailing.basic.wf.constant.SerializableConstants;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * Serialize the record of variational
 * 序列化变化的履历
 * @author Bis liyan
 */
public class Description {
	
	private StringBuffer description = new StringBuffer();
	
	public Description(){}
	/**
	  * Format the record of variational  
	  * 给变化的履历加格式
	  * @param str the record of variational 
	  */
	public void append(String[] str){
		boolean empty = (str[1] == null || "".equals(str[1]));
		if(!empty){
			description.append(str[0]);
			description.append(SerializableConstants.UL_PLACE_HOLDER1);
			description.append(str[1]);
			description.append(SerializableConstants.UL_PLACE_HOLDER2);
		}
	}
	
	public void append(String temp){
		if(StringUtils.isNotBlank(temp)){
			this.description.append(temp);
			this.description.append(SerializableConstants.UL_PLACE_HOLDER1);
		}
	}
	
	
	
	
	/**
	  * Get the final record
	  * 得到最终履历
	  * @return result final record
	  */
	public String toString(){
		String result = description.toString();
		//依赖spring
		result = HtmlUtils.htmlEscape(result);
		result = result.replaceAll(SerializableConstants.UL_PLACE_HOLDER1, "<ul>")
				.replaceAll(SerializableConstants.UL_PLACE_HOLDER2, "</ul>")
				.replaceAll(SerializableConstants.LI_PLACE_HOLDER1, "<li>")
				.replaceAll(SerializableConstants.LI_PLACE_HOLDER2, "</li>");
		
		if (result.length() > 4000) {
			result = result.substring(0, 3999);
		}
		return result;
	}
}
