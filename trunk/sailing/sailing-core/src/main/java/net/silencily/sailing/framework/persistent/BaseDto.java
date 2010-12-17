package net.silencily.sailing.framework.persistent;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;

/**
 * <p>所有<code>DTO</code>类的基接口, 仅仅用于提供共有的方法实现, 比如:<code>equals(Object)<code>,
 * 没有用于任何其他的<code>API</code>, 可以选择不继承这个超类, 但如果在应用中使用到通用的值时自己
 * 负责处理</p>
 * 
 * @author scott
 * @since 2006-3-25
 * @version $Id: BaseDto.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public abstract class BaseDto implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8051620069724285025L;

	/** 唯一地标识了这个实体的<code>id</code> */
    protected String id;
    
    /** 当创建一个新的业务实体时, 这个属性是创建时间 */
    protected Date createdTime;
    
    /** 当修改一个实体时这个属性是修改时间 */
    protected Date lastModifiedTime;
    
    /** 当创建一个实体时这个属性用于标志当前执行用户的唯一标识, 这个值的解释属于安全系统 */
    protected UserWrapper createdUser;
    
    /** 最后一次修改实体的用户 */
    protected UserWrapper lastModifiedUser;
    
    /** 序列号, 一般用于列表排序, 可以为 null */
    protected Integer sequenceNo;
    
    /** used by optimistic lock, 通常情况下如果不指定这个值认为不需要并发控制 */
    protected Integer version;
    
    /** 这个实体相关的数据库表或视图名称, 可以是<code>null</code> */
    protected String tableName;
    
    /** 如果这个实体与一个数据库表相关, 这个值就是表的主键名称, 不支持联合主键, 可以返回
     * <code>null</code>
     */
    protected String primaryKey;
    
    /**
     * 这个业务实体所属的系统/模块名称, 这个属性用于解决<code>O/R mapping</code>自定义类
     * 型处理器中检索<code>code</code>中定义的代码
     */
    protected String moduleName;

    /**
     * @hibernate.id generator-class="uuid.hex" type="string" column="id"  unsaved-value="null" length="32"
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @hibernate.property name="createdTime" column="created_time" type="timestamp" not-null="false" unique="false"
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    
    /**
     * @hibernate.property name="lastModifiedTime" column="last_modified_time" type="timestamp" not-null="false" unique="false"
     */
    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    
    /**
     * @hibernate.property name="createdUser" column="operator_id" type="user_wrapper" not-null="false" unique="false" length="50"
     */
    public UserWrapper getCreatedUser() {
    	if (createdUser == null) {
    		createdUser = new UserWrapper();
    	}
        return createdUser;
    }

    public void setCreatedUser(UserWrapper operator) {
        this.createdUser = operator;
    }
    
    /**
     * @hibernate.version column="version"
     */
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    /**
     * @hibernate.property name="lastModifiedOperator" column="last_modified_operator_id" type="user_wrapper" not-null="false" unique="false" length="50"
	 * @return the lastModifiedOperator
	 */
	public UserWrapper getLastModifiedUser() {
		if (lastModifiedUser == null) {
			lastModifiedUser = new UserWrapper();
		}
		return lastModifiedUser;
	}

	/**
	 * @param lastModifiedOperator the lastModifiedOperator to set
	 */
	public void setLastModifiedUser(UserWrapper lastModifiedOperator) {
		this.lastModifiedUser = lastModifiedOperator;
	}

	/**
	 * @hibernate.property name="sequenceNo" column="sequence_no" not-null="false" unique="false" type="integer"
	 * @return the sequenceNo
	 */
	public Integer getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(Integer sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (this == o) {
            return true;
        }
        
        if (getId() == null) {
            return false;
        }
        
        if (!(o instanceof BaseDto)) {
            return false;
        }

        return (getId().equals(((BaseDto) o).getId()));
    }
    
    public int hashCode() {
        if (StringUtils.isBlank(getId())) {
            return super.hashCode();
        }
        return getId().hashCode();
    }
    
    /**
     * 当前登陆用户是否纪录创建人, 一般用于权限判断
     * @return whether remote user is created user
     */
    public boolean isRemoteUserCreatedUser() {
    	String remoteUsername = ContextInfo.getCurrentUser().getUsername();
    	String createdUsername = getCreatedUser().getUsername();
    	return StringUtils.equals(remoteUsername, createdUsername);
    }
    
    /**
     * 根据创建时间排序的 Comparator, 默认是降序排列
     *
     */
    public static class CreatedTimeComparator implements Comparator {
    	
    	/** 是否升序排列 */
    	private boolean asc = false;
    	
    	public CreatedTimeComparator() {		
    	}
    	
    	public CreatedTimeComparator(boolean asc) {
    		this.asc = asc;
    	}
    	
		public int compare(Object o1, Object o2) {
			BaseDto dto1 = (BaseDto) o1;
			BaseDto dto2 = (BaseDto) o2;
			if (dto1.getCreatedTime() == null || dto2.getCreatedTime() == null) {
				return 0;
			}
			if (asc) {
				return dto1.getCreatedTime().compareTo(dto2.getCreatedTime());
			}
			return dto2.getCreatedTime().compareTo(dto1.getCreatedTime());
		}
    	
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
