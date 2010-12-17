/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.systemcode.spi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.silencily.sailing.business.Initializable;
import net.silencily.sailing.common.systemcode.SystemCode;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionConstants;
import net.silencily.sailing.framework.utils.DaoHelper;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.RowMapper;

/**
 * @since 2006-9-20
 * @author java2enterprise
 * @version $Id: JdbcSystemCodeService.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 */
public class JdbcSystemCodeService extends AbstractSystemCodeService implements SystemCodeCRUDService, Initializable {
	
	private static transient Log logger = LogFactory.getLog(JdbcSystemCodeService.class);
	
	/** 从 spring 配置中获取子模块配置 */
	private Map systemModuleMap = new HashMap();
	
	/**
	 * @param systemModuleMap the systemModuleMap to set
	 */
	public void setSystemModuleMap(Map systemModuleMap) {
		this.systemModuleMap = systemModuleMap;
	}

	public void delete(String systemModuleName, String code) {
		validateSystemModuleName(systemModuleName);
		DaoHelper.getJdbcTemplate().update(getDeleteSql(systemModuleName), new Object[] {code}, new int[] {Types.VARCHAR});
	}

	public void save(String systemModuleName, SystemCode systemCode) {
		innerSave(systemModuleName, systemCode, true);
	}

	private void innerSave(String systemModuleName, SystemCode systemCode, boolean checkUnique) 
		throws IllegalArgumentException, DataAccessException {
		validateSystemModuleName(systemModuleName);
		if (checkUnique) {
			checkUnique(systemCode);	
		}		
		DaoHelper.getJdbcTemplate().update(
			getSaveSql(systemModuleName), 
			new Object[] {systemCode.getCode(), systemCode.getName(), systemCode.getParentCode(), systemCode.getDescription(), systemCode.getSequenceNo()}, 
			new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER}
		);
	}

	private void checkUnique(SystemCode systemCode) {
		for (Iterator iter = systemModuleMap.keySet().iterator(); iter.hasNext(); ) {
			String systemModuleName = (String) iter.next();
			int count = DaoHelper.getJdbcTemplate().queryForInt(
				getCountSql(systemModuleName), 
				new Object[] {systemCode.getCode()}, 
				new int[] {Types.VARCHAR}				
			);
			if (count > 0) {
				throw new DataIntegrityViolationException("代码 [" + systemCode.getCode() + "] 在子系统 [" + systemModuleName + "] 已存在");
			}
		}
	}

	public void update(String systemModuleName, SystemCode systemCode) {
		validateSystemModuleName(systemModuleName);
		DaoHelper.getJdbcTemplate().update(
			getUpdateSql(systemModuleName), 
			new Object[] {systemCode.getName(), systemCode.getParentCode(), systemCode.getDescription(), systemCode.getSequenceNo(), systemCode.getCode()}, 
			new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR}
		);
	}

	public SystemCode load(String systemModuleName, String code) {
		validateSystemModuleName(systemModuleName);
		try {
			SystemCode systemCode = (SystemCode) DaoHelper.getJdbcTemplate().queryForObject(
				getLoadSql(systemModuleName), 
				new Object[] {code}, 
				new int[] {Types.VARCHAR}, 
				new SystemCodeRowMapper()
			);
			
			setHasChildren(systemModuleName, code, systemCode);
			return systemCode;
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
	}

	private void setHasChildren(String systemModuleName, String code, SystemCode systemCode) throws DataAccessException {
		int childrenCount = DaoHelper.getJdbcTemplate().queryForInt(
			getChildrenCountSql(systemModuleName), 
			new Object[] {code}, 
			new int[] {Types.VARCHAR}				
		);
		
		boolean hasChildren = childrenCount > 0;
		systemCode.setHasChildren(hasChildren);
	}
	
	public List findByParentCode(String systemModuleName, String parentCode) {
		validateSystemModuleName(systemModuleName);		
		String sql = getFindByParentCodeSql(systemModuleName) + " order by sequence_no ";
		return DaoHelper.getJdbcTemplate().query(
			sql, 
			new Object[] {parentCode}, 
			new int[] {Types.VARCHAR},
			new SystemCodeRowMapper()
		);
	}
	
	public List findByParentCodeWithContextInfo(String systemModuleName, String parentCode) {
		validateSystemModuleName(systemModuleName);
		StringBuffer sql = new StringBuffer(getFindByParentCodeSql(systemModuleName));
		List args = new ArrayList();
		args.add(parentCode);
		List types = new ArrayList();
		types.add(new Integer(Types.VARCHAR));
		
		// 从 context info 中加载查询条件和分页信息
		if (ContextInfo.getContextCondition() != null) {
			Condition[] conditions = ContextInfo.getContextCondition().getAppendConditions();
//			Paginater paginater = ContextInfo.getContextCondition().getPaginater();
			
//			if (paginater != null && !paginater.isNotPaginated()) {
//				sql.append(" and rownum > ? and rownum <= ? ");
//				long firstResult = paginater.getPage() * paginater.getPageSize();
//				long lastResults = firstResult + paginater.getPageSize();			
//				args.add(new Long(firstResult));
//				args.add(new Long(lastResults));			
//				types.add(new Integer(Types.NUMERIC));
//				types.add(new Integer(Types.NUMERIC));			
//			}
			
			for (int i = 0; i < conditions.length; i++) {
				if (conditions[i].getName() != null) {
					sql.append(" and ");
					sql.append(conditions[i].getName());
					sql.append(" ");
					sql.append(conditions[i].getOperator());
					sql.append(" ? ");
				
					Object value = conditions[i].getValue();
					if (String.class.isInstance(value) && ConditionConstants.LIKE.equals(conditions[i].getOperator())) {
						value = "%" + value + "%";
					}
								
					args.add(value);
					types.add(new Integer(Types.VARCHAR));
				}
			}		
			
			sql.append(" order by sequence_no ");
		}
		
		List list = DaoHelper.getJdbcTemplate().query(
			sql.toString(), 
			args.toArray(), 
			ArrayUtils.toPrimitive((Integer[]) types.toArray(new Integer[0])),
			new SystemCodeRowMapper()
		);	
		
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			SystemCode systemCode = (SystemCode) iter.next();
			setHasChildren(systemModuleName, systemCode.getCode(), systemCode);
		}
		
		return list;
	}
	
	private static class SystemCodeRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			SystemCode systemCode = new SystemCode();
			systemCode.setCode(rs.getString("code"));
			systemCode.setName(rs.getString("name"));
			systemCode.setParentCode(rs.getString("parent_code"));
			systemCode.setDescription(rs.getString("description"));
			systemCode.setSequenceNo(new Integer(rs.getInt("sequence_no")));
			return systemCode;
		}		
	}
		
	public String[] getRegisteredSystemModuleNames() {
		return (String[]) systemModuleMap.keySet().toArray(new String[0]);
	}
	
	private String getTableName(String systemModuleName) {
		return "platform_" + systemModuleName + "_code";
	}
	
	private String getDetectSql(String systemModuleName) {
		return " select * from " + getTableName(systemModuleName) + " where rownum < 1";
	}
	
	private String getCreateTableDDL(String systemModuleName) {
		StringBuffer ddl = new StringBuffer();
		ddl.append(" create table ");
		ddl.append(getTableName(systemModuleName));
		ddl.append(" (code varchar2(4000), name varchar2(4000), parent_code varchar2(4000), description varchar2(4000), sequence_no number(10, 0), primary key(code))");
		return ddl.toString();
	}
		
	public String getCreateForeignKeyDDL(String systemModuleName) {
		StringBuffer ddl = new StringBuffer();
		ddl.append(" alter table ");
		ddl.append(getTableName(systemModuleName));
		ddl.append(" add constraint fk_");
		ddl.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(net.silencily.sailing.utils.DBTimeUtil.getDBTime()));
		ddl.append(" foreign key (parent_code) references ");
		ddl.append(getTableName(systemModuleName));
		ddl.append(" (code) ");
		return ddl.toString();
	}
	
	private String getSaveSql(String systemModuleName) {
		return " insert into  " + getTableName(systemModuleName) + " (code, name, parent_code, description, sequence_no) values (?, ?, ?, ?, ?) ";
	}
	
	private String getUpdateSql(String systemModuleName) {
		return " update " + getTableName(systemModuleName) + " set name = ?, parent_code = ?, description = ?, sequence_no = ? where code = ? ";
	}
	
	private String getDeleteSql(String systemModuleName) {
		return " delete from " + getTableName(systemModuleName) + " where code = ? ";
	}
	
	private String getLoadSql(String systemModuleName) {
		return " select * from " + getTableName(systemModuleName) + " where code = ? ";
	}
	
	private String getFindByParentCodeSql(String systemModuleName) {
		return " select * from " + getTableName(systemModuleName) + " where parent_code = ? ";
	}

	private String getChildrenCountSql(String systemModuleName) {
		return " select count(*) from " + getTableName(systemModuleName) + " where parent_code = ? ";
	}
	
	private String getCountSql(String systemModuleName) {
		return " select count(*) from " + getTableName(systemModuleName) + " where code = ? ";
	}
	
	public void init() {
		initTableIfNecessary();
	}
	
	private void initTableIfNecessary() {
		for (Iterator iter = systemModuleMap.entrySet().iterator(); iter.hasNext(); ) {
			Entry entry = (Entry) iter.next();
			String systemModuleName = (String) entry.getKey();
			String systemModuleDescription = (String) entry.getValue();
			
			try {
				DaoHelper.getJdbcTemplate().queryForList(getDetectSql(systemModuleName), new Object[0]);
			} catch (BadSqlGrammarException e) {
				if (logger.isInfoEnabled()) {
					logger.info("未检测到代码表, 开始创建 : " + getCreateTableDDL(systemModuleName) + " ||| " + getCreateForeignKeyDDL(systemModuleName));
				}
				DaoHelper.getJdbcTemplate().execute(getCreateTableDDL(systemModuleName));
				DaoHelper.getJdbcTemplate().execute(getCreateForeignKeyDDL(systemModuleName));
				
				if (logger.isInfoEnabled()) {
					logger.info("创建根节点");
				}
				SystemCode root = new SystemCode();
				root.setCode(SystemCode.TREE_NODE_ROOT_ID);
				root.setName(systemModuleDescription);
				innerSave(systemModuleName, root, false);			
			}
		}
	}
	
	private void validateSystemModuleName(String systemModuleName) throws IllegalArgumentException {
		boolean validate = systemModuleMap.get(systemModuleName) != null;
		if (!validate) {
			StringBuffer exceptionInfo = new StringBuffer();
			exceptionInfo.append("错误的 systemModuleName, 值必须是注册的子模块之一 : ");
			for (Iterator iter = systemModuleMap.entrySet().iterator(); iter.hasNext(); ) {
				Entry entry = (Entry) iter.next();
				exceptionInfo.append(" [");
				exceptionInfo.append(entry.getKey());
				exceptionInfo.append(" ] ");
			}
			throw new IllegalArgumentException(exceptionInfo.toString());
		}
	}
	
	
}
