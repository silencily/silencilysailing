package net.silencily.sailing.code;

import java.util.Iterator;
import java.util.Set;

import net.silencily.sailing.common.dict.service.BasicCodeService;
import net.silencily.sailing.container.ServiceProvider;
import net.silencily.sailing.framework.web.view.ComboSupportList;
import net.silencily.sailing.utils.Tools;

/**
 * @author zhaoyf
 * 
 */
public class SysCodeList {
	private static SysCodeList me = new SysCodeList();

	public static SysCodeList factory() {
		return me;
	}

	public static void flush() {
		Set ps = Tools.getProperties(me);
		Iterator i = ps.iterator();
		while (i.hasNext()) {

		}
	}

	public static SysCodeList getMe() {
		return me;
	}

	private ComboSupportList administrationLevelList;

	private ComboSupportList archiveDegreeList;

	private ComboSupportList bloodTypeList;

	private ComboSupportList communityDegreeList;

	private ComboSupportList currEduDegreeList;

	private ComboSupportList currEduGradeList;

	private ComboSupportList currEduStyleList;

	private ComboSupportList currMajorList;

	private ComboSupportList deptPropertyList;

	private ComboSupportList eduStyleList;

	private ComboSupportList eduSystemList;

	private ComboSupportList enjoyLevelList;

	private ComboSupportList entryReasonList;

	private ComboSupportList familyParentageList;

	private ComboSupportList healthList;

	private ComboSupportList isnotList;

	private ComboSupportList isntList;

	private ComboSupportList jobSortList;

	private ComboSupportList leaveReasonList;

	private ComboSupportList leaveTypeList;

	private ComboSupportList manageSortList;

	private ComboSupportList marriageList;

	private ComboSupportList nameList;

	private ComboSupportList nationList;

	private ComboSupportList polityList;

	private ComboSupportList proSkillSortList;

	private ComboSupportList relationList;

	private ComboSupportList sexList;
	
	private ComboSupportList sortList;
	
	private ComboSupportList stateList;
	
	private ComboSupportList stationGradeList;

    private ComboSupportList stationList;
	
	private ComboSupportList stationSortList;
	
	private ComboSupportList studySortList;
	
	private ComboSupportList techGradeList;
	
	private ComboSupportList techTitleList;
	
	private ComboSupportList techTitleStructureList;
	
	private ComboSupportList techTypeList;
	
	private ComboSupportList trainStatTypeList;
	
	private ComboSupportList trainStyleList;
	
	private ComboSupportList trainTypeList;
	
	private ComboSupportList workDutyLevelList;
	
	private ComboSupportList workDutyList;
	private SysCodeList() {

	}

	/**
	 * 
	 * 功能描述 行政级别
	 * 
	 * @return Sep 28, 2007 9:26:15 PM
	 */
	public ComboSupportList getAdministrationLevelList() {
		if (null == administrationLevelList)
			administrationLevelList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XZJB);

		return administrationLevelList;
	}
	
	/**
	 * 
	 * 功能描述 档案身份
	 * 
	 * @return Sep 28, 2007 9:27:07 PM
	 */
	public ComboSupportList getArchiveDegreeList() {
		if (null == archiveDegreeList)
			archiveDegreeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_DASF);

		return archiveDegreeList;
	}

	/**
	 * 
	 * 功能描述
	 * 
	 * @return Sep 28, 20079:23:12 PM
	 */
	private BasicCodeService getBasicCodeService() {
		return (BasicCodeService) ServiceProvider.getService(BasicCodeService.SERVICE_NAME);
	}

	/**
	 * 
	 * 功能描述 血型
	 * @return
	 * Oct 11, 2007 8:21:34 PM
	 */
	public ComboSupportList getBloodTypeList() {
		if(bloodTypeList==null)
			bloodTypeList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_XUEX);
		return bloodTypeList;
	}

	/**
	 * 
	 * 功能描述 所有制身份
	 * 
	 * @return Sep 28, 20077:33:47 PM
	 */
	public ComboSupportList getCommunityDegreeList() {
		if (communityDegreeList == null)
			communityDegreeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_SFEN);

		return communityDegreeList;
	}

	/**
	 * 
	 * 功能描述 学位
	 * 
	 * @return Sep 28, 2007 9:29:17 PM
	 */
	public ComboSupportList getCurrEduDegreeList() {
		if (null == currEduDegreeList)
			currEduDegreeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XUEW);

		return currEduDegreeList;
	}

	/**
	 * 
	 * 功能描述 学历
	 * 
	 * @return Sep 28, 2007 9:29:30 PM
	 */
	public ComboSupportList getCurrEduGradeList() {
		if (null == currEduGradeList)
			currEduGradeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XUEL);

		return currEduGradeList;
	}

	/**
	 * 功能描述 现学历对应教育方式
	 */
	public ComboSupportList getCurrEduStyleList() {
	
		if (currEduStyleList == null) {
			currEduStyleList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JYFS);
		}
		return currEduStyleList;
	}

	/**
	 * 
	 * 功能描述 所学专业
	 * 
	 * @return Sep 28, 2007 9:29:05 PM
	 */
	public ComboSupportList getCurrMajorList() {
		if (null == currMajorList)
			currMajorList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_SXZY);

		return currMajorList;
	}

	/**
	 * 功能描述 部门性质
	 * @return the deptPropertyList
	 */
	public ComboSupportList getDeptPropertyList() {
		if(deptPropertyList==null)
			deptPropertyList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_BMXZ);
		return deptPropertyList;
	}

	/**
	 * 
	 * 功能描述 教育方式
	 * 
	 * @return Sep 29, 2007 10:51:19 AM
	 */
	public ComboSupportList getEduStyleList() {
		if (null == eduStyleList)
			eduStyleList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JYFS);

		return eduStyleList;
	}

	/**
	 * 
	 * 功能描述 学制
	 * 
	 * @return Sep 29, 2007 10:57:00 AM
	 */
	public ComboSupportList getEduSystemList() {
		if (null == eduSystemList)
			eduSystemList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XUEZ);

		return eduSystemList;
	}

	/**
	 * 
	 * 功能描述 享受待遇级别
	 * 
	 * @return Sep 28, 2007 9:26:51 PM
	 */
	public ComboSupportList getEnjoyLevelList() {
		if (null == enjoyLevelList)
			enjoyLevelList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_DYJB);

		return enjoyLevelList;
	}

	/**
	 * 
	 * 功能描述 调入原因
	 * 
	 * @return Sep 28, 20077:33:47 PM
	 */
	public ComboSupportList getEntryReasonList() {
		if (entryReasonList == null)
			entryReasonList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_DRYY);

		return entryReasonList;
	}

	/**
	 * 
	 * 功能描述 家庭出身
	 * @return
	 * Oct 11, 2007 8:22:05 PM
	 */
	public ComboSupportList getFamilyParentageList() {
		if(familyParentageList==null)
			familyParentageList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_JTCS);
		return familyParentageList;
	}

	/**
	 * 
	 * 功能描述 健康状况
	 * @return
	 * Oct 11, 2007 8:20:08 PM
	 */
	public ComboSupportList getHealthList() {
		if(healthList==null)
			healthList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_JKZK);
		return healthList;
	}

	/**
	 * 
	 * 功能描述 是否班组
	 * 
	 * @return Sep 28, 20079:23:51 PM
	 */
	public ComboSupportList getIsnotList() {
		if (null == isnotList)
			isnotList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_SHIF);

		return isnotList;
	}

	/**
	 * 
	 * 功能描述 是否
	 * @return
	 * Oct 12, 2007 11:34:47 AM
	 */
	public ComboSupportList getIsntList() {
		if(isntList==null)
			isntList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_ISNT);
		return isntList;
	}

	/**
	 * 
	 * 功能描述 作业类别
	 * 
	 * @return Sep 28, 20077:33:47 PM
	 */
	public ComboSupportList getJobSortList() {
		if (jobSortList == null)
			jobSortList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_ZYLB);

		return jobSortList;
	}

	/**
	 * 
	 * 功能描述 调出原因
	 * @return Org 29, 2007 10:42:00 AM
	 */
	public ComboSupportList getLeaveReasonList() {
		if (null == leaveReasonList)
			leaveReasonList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_DCYY);
		return leaveReasonList;
	}

	/**
	 * 
	 * 功能描述 调出类型
	 * @return the leaveTypeList
	 */
	public ComboSupportList getLeaveTypeList() {
		if (null == leaveTypeList)
			leaveTypeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_DCLX);
		return leaveTypeList;
	}

	/**
	 * 
	 * 功能描述 经营管理类别
	 * 
	 * @return Sep 28, 2007 9:25:31 PM
	 */
	public ComboSupportList getManageSortList() {
		if (null == manageSortList)
			manageSortList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JGLB);

		return manageSortList;
	}

	/**
	 * 
	 * 功能描述 婚姻状况
	 * @return
	 * Oct 11, 2007 8:20:48 PM
	 */
	public ComboSupportList getMarriageList() {
		if(marriageList==null)
			marriageList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_HYZK);
		return marriageList;
	}

	/**
	 * 功能描述 奖惩名称
	 * @return the nameList
	 */
	public ComboSupportList getNameList() {
		if(nameList==null)
			nameList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_JCMC);
		return nameList;
	}

	/**
	 * 功能描述 民族
	 * 
	 * @return the nationList
	 */
	public ComboSupportList getNationList() {
		if (null == nationList)
			nationList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_MINZ);

		return nationList;
	}

	/**
	 * 
	 * 功能描述 政治面貌
	 * 
	 * @return Sep 28, 20079:23:36 PM
	 */
	public ComboSupportList getPolityList() {
		if (null == polityList)
			polityList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_ZZMM);

		return polityList;
	}

	/**
	 * 
	 * 功能描述 专业技术人员类别
	 * 
	 * @return Sep 28, 2007 9:25:53 PM
	 */
	public ComboSupportList getProSkillSortList() {
		if (null == proSkillSortList)
			proSkillSortList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_ZYJS);

		return proSkillSortList;
	}

	/**
	 * 
	 * 功能描述 家庭成员关系
	 * 
	 * @return Sep 29, 2007 6:22:30 PM
	 */
	public ComboSupportList getRelationList() {
		if (null == relationList)
			relationList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_CYGX);

		return relationList;
	}

	/**
	 * 功能描述 性别
	 * 
	 * @return the sexList
	 */
	public ComboSupportList getSexList() {
		if (null == sexList)
			sexList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XBIE);

		return sexList;
	}

	/**
	 * 功能描述 奖惩类别
	 * @return the sortList
	 */
	public ComboSupportList getSortList() {
		if (sortList == null)
			sortList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JCLB);
		return sortList;
	}

	/**
	 * 
	 * 功能描述 员工状态
	 * 
	 * @return Sep 28, 20079:24:04 PM
	 */
	public ComboSupportList getStateList() {
		if (null == stateList)
			stateList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_YGZT);

		return stateList;
	} 
	
	/**
	 * 
	 * 功能描述 岗级
	 * 
	 * @return Sep 28, 20079:21:55 PM
	 */
	public ComboSupportList getStationGradeList() {
		if (null == stationGradeList)
			stationGradeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_GJI);

		return stationGradeList;
	}

	/**
	 * 
	 * 功能描述 岗位
	 * 
	 * @return Sep 28, 20077:35:12 PM
	 */
	public ComboSupportList getStationList() {
		if (null == stationList)
			stationList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_GWEI);

		return stationList;
	}
	/**
	 * 
	 * 功能描述 岗位类别
	 * 
	 * @return Sep 28, 2007 9:27:22 PM
	 */
	public ComboSupportList getStationSortList() {
		if (null == stationSortList)
			stationSortList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_GWLB);

		return stationSortList;
	}

	/**
	 * 功能描述 学习类型
	 * @return
	 */
	public ComboSupportList getStudySortList() {
		if(studySortList==null)
			studySortList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_XXLX);
		return studySortList;
	}
	/**
	 * 
	 * 功能描述 技术等级
	 * 
	 * @return Sep 28, 2007 9:28:18 PM
	 */
	public ComboSupportList getTechGradeList() {
		if (null == techGradeList)
			techGradeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JSDJ);

		return techGradeList;
	}

	/**
	 * 
	 * 功能描述 技术职称
	 * 
	 * @return Sep 28, 2007 9:28:31 PM
	 */
	public ComboSupportList getTechTitleList() {
		if (null == techTitleList)
			techTitleList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JSZC);

		return techTitleList;
	}

	/**
	 * 
	 * 功能描述 职称构成
	 * 
	 * @return Sep 28, 2007 9:28:46 PM
	 */
	public ComboSupportList getTechTitleStructureList() {
		if (null == techTitleStructureList)
			techTitleStructureList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_ZCGC);

		return techTitleStructureList;
	}

	/**
	 * 
	 * 功能描述 技术工种
	 * 
	 * @return Sep 28, 20077:34:37 PM
	 */
	public ComboSupportList getTechTypeList() {
		if (null == techTypeList)
			techTypeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_JSGZ);

		return techTypeList;
	}

	/**
	 * 
	 * 功能描述 培训统计类型
	 * @return
	 * Oct 11, 2007 7:45:06 PM
	 */
	public ComboSupportList getTrainStatTypeList() {
		if(trainStatTypeList==null)
			trainStatTypeList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_PTLX);
		return trainStatTypeList;
	}

	/**
	 * 功能描述 培训方式
	 * @return
	 */
	public ComboSupportList getTrainStyleList() {
		if(trainStyleList==null)
			trainStyleList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_PXFS);
		return trainStyleList;
	}

	/**
	 * 功能描述 培训类型
	 * @return
	 */
	public ComboSupportList getTrainTypeList() {
		if(trainTypeList==null)
			trainTypeList=getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR,SysCodeInfo.HR_CD_PXLX);
		return trainTypeList;
	}

	/**
	 * 
	 * 功能描述 行政职务
	 * 
	 * @return Sep 28, 2007 9:26:35 PM
	 */
	public ComboSupportList getWorkDutyLevelList() {
		if (null == workDutyLevelList)
			workDutyLevelList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XZZW);

		return workDutyLevelList;
	}

	/**
	 * 功能描述 行政职务
	 */
	public ComboSupportList getWorkDutyList() {
		if (techTypeList == null) {
			techTypeList = getBasicCodeService().getComboList(SysCodeInfo.SYS_CD_HR, SysCodeInfo.HR_CD_XZZW);
		}
		return workDutyList;
	}

}
