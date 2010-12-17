package net.silencily.sailing.common.dict.service;

import java.util.Map;

import net.silencily.sailing.framework.core.ServiceBase;
import net.silencily.sailing.framework.web.view.ComboSupportList;


/**
 * @author zhaoyf
 *	
 */
public interface BasicCodeService extends ServiceBase{

	static final String SERVICE_NAME = "common.CommonBasicCodeService";
	/**
	 * 
	 */
	public static final String DICT_SYSTEM_ROOT="systemroot";
	public static final String DICT_HR_ROOT="hrroot";
	
	
	public static final String DICT_HR_MODULE="";
	
	
	public static final String DICT_HR_TYPE_NATION="";
	
	
	/**
	 * 
	 * @param sid 根节点id
	 * @return 该子系统的基础代码列表，提供给画面生成树状结构用的字符串。
	 * 制作时间：下午02:13:52
	 * 制作人：zhaoyf
	 * 名称：getBasicCodeTree
	 */
	public String getBasicCodeTree(String sid);
	
	/**
	 * 
	 * @param root 根节点id
	 * @return 编码与汉字对应的map
	 * 制作时间：下午02:19:17
	 * 制作人：zhaoyf
	 * 名称：getDictListByRoot
	 */
	public Map getDictListByRoot(String root);
	
	/**
	 * 
	 * @param id - 字典序列ID（与各个子系统的基础编码维护表的ID一致）
	 * @param sid - 子系统ID
	 * @param bid - 基础代码ID
	 * @return
	 * 制作时间：下午02:21:31
	 * 制作人：zhaoyf
	 * 名称：getBasicCodeName
	 */
	public String getBasicCodeName(String sid,String bid,String id);
	
	/**
	 * 
	 * @param sid - 子系统ID
	 * @param bid - 基础代码ID
	 * @return 编码与汉字对应的map
	 * 制作时间：下午02:28:16
	 * 制作人：zhaoyf
	 * 名称：getBasicCodeList
	 */
	public Map getBasicCodeList(String sid,String bid);
	
	public ComboSupportList getComboListAll();
	/**
	 * 
	 * 功能描述 得到下拉框列表
	 * @param sid - 子系统ID
	 * @param bid - 基础代码ID
	 * @return ComboSupportList
	 * 2007-9-4下午01:26:41
	 */
	public ComboSupportList getComboList(String sid,String bid);
	
	
	/**
	 * 
	 * 功能描述 用code得到name
	 * @param code 编码
	 * @return 编码名
	 * Oct 5, 2007 1:26:46 PM
	 */
	public String getNameByCode(String code);
}
