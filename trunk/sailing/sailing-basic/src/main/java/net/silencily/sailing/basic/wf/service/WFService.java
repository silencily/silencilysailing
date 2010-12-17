package net.silencily.sailing.basic.wf.service;

import java.util.List;

import net.silencily.sailing.basic.sm.domain.TblCmnRole;
import net.silencily.sailing.basic.sm.domain.TblCmnUser;
import net.silencily.sailing.basic.wf.WorkflowInfo;
import net.silencily.sailing.basic.wf.domain.TblWfOperationInfo;
import net.silencily.sailing.basic.wf.domain.TblWfParticularInfo;
import net.silencily.sailing.framework.core.ServiceBase;


public interface WFService extends ServiceBase {
	
	public static final String SERVICE_NAME = "wf.wFService";


	//WFOPERATION信息表操作。
	/**
	 * 
	 * 功能描述 通过工作流信息表的PK，查找工作流的具体实例,有级联关系。
	 * @param id
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public TblWfOperationInfo findWfOperInfoById(String id);
	
	public TblWfOperationInfo findWfOperInfoByName(String name);
	
	/**
	 * 
	 * 功能描述 从数据库中导入所有的工作流实例。
	 * @param 
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public List findAllWfOper();
	
	/**
	 * 
	 * 功能描述 存储一个工作流实例,目前只存储工作流信息表，不进行XML文件生成
	 * @param wfinfo
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void saveWfOperInfo(TblWfOperationInfo wfinfo);
	
	/**
	 * 
	 * 功能描述  删除工作流实例信息，只是修改工作流信息表的删除标志字段
	 * @param id
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void deleteWfOperInfo(String id);
	
	/**
	 * 
	 * 功能描述  更新修改的工作流实例信息。
	 * @param wfInfo
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void updateWfOperInfo(TblWfOperationInfo wfInfo);
	
	//对具体流程信息表的操作	
	
	/**
	 * 
	 * 功能描述 存储一个步骤定义信息,同时存储表单权限信息，有级连关系。
	 * @param wfinfo
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void saveWfParInfo(TblWfParticularInfo wfinfo);
	
	/**
	 * 
	 * 功能描述  删除工作流实例信息，只是修改步骤定义信息表的删除标志字段
	 * @param wfinfo
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void deleteWfParInfo(String id);
	
	/**
	 * 
	 * 功能描述  更新具体流程定义信息表（步骤定义信息表）。
	 * @param wfInfo
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */
	public void updateWfParInfo(TblWfParticularInfo wfInfo);

	/**
	 * 
	 * 功能描述  根据表名，得到授权字段的列表。
	 * @param str
	 * @return
	 * 2007-12-5 上午10:26:31
	 * @version 1.0
	 * @author wenjb
	 */	
	//public List readWFForm(Class str);
	
	/**
	 * 
	 * 功能描述  根据表名，得到角色的列表。
	 * @return
	 * 2007-12-14 上午17:50:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findRole();
	
	/**
	 * 
	 * 功能描述  根据标识，得到角色。
	 * @param roleCd
	 * @return
	 * 2008-02-21 上午11:50:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblCmnRole findroleCdRole(String roleCd);
	
	/**
	 * 
	 * 功能描述  根据id，得到角色。
	 * @param id
	 * @return
	 * 2008-02-21 上午12:50:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblCmnRole findidRole(String id);
	/**
	 * 
	 * 功能描述  根据id 得到步骤信息表的列表
	 * @param id
	 * @return
	 * 2007-12-20 上午11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblWfParticularInfo findWfParticularInfoById(String id);
	
	/**
	 * 
	 * 功能描述  复合查询
	 * @param wfInfo
	 * @return
	 * 2007-12-20 上午11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findsearch(TblWfOperationInfo wfInfo);
	
	/**
	 * 
	 * 功能描述  删除
	 * @param id
	 * @return
	 * 2007-12-20 上午11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public void deleteWfFormInfo(String id);
	
	/**
	 * 
	 * 功能描述  编辑版本号
	 * @param id
	 * @return
	 * 2008-1-17 上午11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblWfOperationInfo findWfOperInfoByEdition(String name);
	
	/**
	 * 
	 * 功能描述  判断step名称不能重复
	 * @param stepName,tblWfOperationInfo
	 * @return
	 * 2008-1-24 上午11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public boolean findbooleanOpinstepName(String stepName,TblWfOperationInfo tblWfOperationInfo); 
	
	/**
	 * 记录应用数据在特定步骤下的状态
	 * @param appBean 应用实体bean
	 * @param beanId  应用实体bean的id
	 * @param stepId
	 * @throws Exception 
	 */
	public void recAppDataStatus(WorkflowInfo appBean,String stepId) throws Exception;
	/**
	 * 恢复应用数据在特定步骤下的状态
	 * @param appBean
	 * @param beanId
	 * @param stepid
	 */
	public void resetAppDataStatus(WorkflowInfo appBean,String stepId);
	
	/**
	 * 
	 * 功能描述  通过管理员标识获得管理员信息
	 * @param empCd
	 * @return
	 * 2008-3-06 上午11:08:31
	 * @version 1.0
	 * @author yangxl
	 */
	public TblCmnUser findempCdUser(String empCd);
	
	/**
	 * 清除工作流步骤数据
	 * @param beanId
	 */
	public void cleanAppDataStatus(String beanId);
	
	/**
	 * 
	 * 功能描述 或多所有开启流程
	 * @param
	 * @return
	 * 2008-3-14 下午午13:38:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findWfOperInfo();
	
	/**
	 * 
	 * 功能描述 或多所有开启流程
	 * @param ooid,stid
	 * @return
	 * 2008-3-20 下午午13:38:31
	 * @version 1.0
	 * @author yangxl
	 */
	public String findTblWfParticularInfoName(String ooid,String stid);
	
	/**
	 * 
	 * 功能描述 获得所有流程信息
	 * @return
	 * 2008-5-29 下午午13:38:31
	 * @version 1.0
	 * @author yangxl
	 */
	public List findWfOperInfoAll();

	public void saveHistoryEntryData(String entryStepId,String stepChangeContent,String masterOid) throws Exception;
	
	public String searchHistoryEntryData(String entryStepId) throws Exception;
	
	public String searchStepFieldComment(Class entry,String entryField) throws Exception;
}
