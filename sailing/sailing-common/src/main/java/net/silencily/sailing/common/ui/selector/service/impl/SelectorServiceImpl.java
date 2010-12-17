package net.silencily.sailing.common.ui.selector.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.silencily.sailing.common.ui.selector.service.SelectorService;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;



/**
 * 
 * @author liuz
 *
 */
public class SelectorServiceImpl implements SelectorService {
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public JdbcTemplate getDao() {
		return jdbcTemplate;
	}
	
	public List listUserTree(String id){
		List list = listDeptTree(id);	
		List temp;
		String orgId;
		String orgStr;
		int orgLevel = 0;
		List result = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			Map obj = (Map) iter.next();
			orgId = (String) obj.get("NODEID");
			orgStr = (String) obj.get("ORG_LEVEL");
			orgLevel = Integer.parseInt(orgStr) + 1;
			obj.put("isSelect","0");
			temp = listUserByOrgId(orgId,orgLevel);
			result.add(obj);
			result.addAll(temp);
		}
		return result;
	}
	
	public List listUserByOrgId(String orgId,int Level){
		String lev = String.valueOf(Level);
		String sql = "select ? org_level,t.id nodeId,t.chinese_name nodeName,t.username userName, t.ORGANIZATIONID nodeFathId ,'1' isSelect from security_user t where t.organizationid = ? and t.enabled = 1 ";
		List list = getDao().queryForList(sql,new String[]{lev,orgId});
		return list;
	}
	
	public List listDeptTree(String currentId){
		String sql = " SELECT TO_CHAR(LEVEL) org_level , u.id nodeId,u.org_name nodeName, u.parent_id nodeFathId ,'1' isSelect "+
					 " FROM security_organization u "+
					 " START WITH u.id = ? "+
					 " CONNECT BY PRIOR u.id = u.parent_id order siblings by u.id";
		if(StringUtils.isBlank(currentId)){
			currentId = "0";
		}
		List list = getDao().queryForList(sql,new String[]{currentId});
		return list;
	}

	public List listAllCodeByParentId(String codeId) {
		String sql = " SELECT TO_CHAR(LEVEL-1) nodeLevel,c.code nodeId,c.name nodeName, c.type nodeFathId,c.description nodeDesc,c.id id "+
					 " FROM zxt_code c "+
					 " START WITH c.code = ? "+
					 " CONNECT BY PRIOR c.id = c.type " +
					 " order by nodeLevel,code_order";
		if(StringUtils.isBlank(codeId)){
			codeId = "0";
		}
		List list = getDao().queryForList(sql,new String[]{codeId});
		return list;
	}

	public List listCodeByParentId(String codeId) {
		String sql = "select (select count(*) from zxt_code z where z.type = c.id ) nodeType," +
				"c.code nodeId,c.name nodeName, c.type nodeFathId,c.description nodeDesc,c.id " +
				"from zxt_code c where type in " +
				"(select id from zxt_code where code = ?)  order by code_order";
		if(StringUtils.isBlank(codeId)){
			codeId = "0";
		}
		List list = getDao().queryForList(sql,new String[]{codeId});
		return list;
	}
	
	
}
