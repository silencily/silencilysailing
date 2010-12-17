package net.silencily.sailing.framework.persistent;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.hibernate3.entity.UserWrapper;

/**
 * <p>����<code>DTO</code>��Ļ��ӿ�, ���������ṩ���еķ���ʵ��, ����:<code>equals(Object)<code>,
 * û�������κ�������<code>API</code>, ����ѡ�񲻼̳��������, �������Ӧ����ʹ�õ�ͨ�õ�ֵʱ�Լ�
 * ������</p>
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

	/** Ψһ�ر�ʶ�����ʵ���<code>id</code> */
    protected String id;
    
    /** ������һ���µ�ҵ��ʵ��ʱ, ��������Ǵ���ʱ�� */
    protected Date createdTime;
    
    /** ���޸�һ��ʵ��ʱ����������޸�ʱ�� */
    protected Date lastModifiedTime;
    
    /** ������һ��ʵ��ʱ����������ڱ�־��ǰִ���û���Ψһ��ʶ, ���ֵ�Ľ������ڰ�ȫϵͳ */
    protected UserWrapper createdUser;
    
    /** ���һ���޸�ʵ����û� */
    protected UserWrapper lastModifiedUser;
    
    /** ���к�, һ�������б�����, ����Ϊ null */
    protected Integer sequenceNo;
    
    /** used by optimistic lock, ͨ������������ָ�����ֵ��Ϊ����Ҫ�������� */
    protected Integer version;
    
    /** ���ʵ����ص����ݿ�����ͼ����, ������<code>null</code> */
    protected String tableName;
    
    /** ������ʵ����һ�����ݿ�����, ���ֵ���Ǳ����������, ��֧����������, ���Է���
     * <code>null</code>
     */
    protected String primaryKey;
    
    /**
     * ���ҵ��ʵ��������ϵͳ/ģ������, ����������ڽ��<code>O/R mapping</code>�Զ�����
     * �ʹ������м���<code>code</code>�ж���Ĵ���
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
     * ��ǰ��½�û��Ƿ��¼������, һ������Ȩ���ж�
     * @return whether remote user is created user
     */
    public boolean isRemoteUserCreatedUser() {
    	String remoteUsername = ContextInfo.getCurrentUser().getUsername();
    	String createdUsername = getCreatedUser().getUsername();
    	return StringUtils.equals(remoteUsername, createdUsername);
    }
    
    /**
     * ���ݴ���ʱ������� Comparator, Ĭ���ǽ�������
     *
     */
    public static class CreatedTimeComparator implements Comparator {
    	
    	/** �Ƿ��������� */
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
