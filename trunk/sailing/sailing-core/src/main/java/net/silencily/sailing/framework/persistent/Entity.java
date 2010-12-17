package net.silencily.sailing.framework.persistent;

import java.io.Serializable;
import java.util.Date;

import net.silencily.sailing.framework.codename.DepartmentCodeName;
import net.silencily.sailing.framework.codename.UserCodeName;

import org.apache.commons.lang.StringUtils;

/**
 * <p>��Ҫ�־û�ҵ��ʵ��Ļ���, ������һ���־û�������ͬ������, ע��ϵͳ�е�ҵ��ʵ��̳������
 * �������, ���ǻ��ڱ�ʶ���ж������, ����û��<code>id</code>���Ե�ҵ��ʵ��������<code>code</code>
 * ���Ե�ҵ��ʵ��Ӧ��ͨ�����Ƿ���
 * <pre>
 *     public String getId() {
 *         return getCode();
 *     }
 * </pre>����֤ȱʡ�����</p>
 * <p>����Լ��ϵͳ�����е�ҵ��ʵ���Ӧ�����ݿ��������ƺͳ���<ul>
 * <li>{@link #id}:id,VARCHAR(32)</li>
 * <li>{@link #version}:version,integer(6)</li>
 * <li>{@link #creator}:operator_id,VARCHAR(50),�����ݵ�����</li>
 * <li>{@link #modifier}:last_modified_user,VARCHAR(50),�����ݵ�����</li>
 * <li>{@link #createdTime}:created_time,timestamp</li>
 * <li>{@link #modifiedTime}:last_modfied_time,timestamp</li></ul>
 * @author zhangli
 * @version $Id: Entity.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-4-24
 */
public abstract class Entity implements Serializable {
    
    /** ������ʵ���ı�ʶ�� */ 
    private String id;
    
    /** ����<code>optimistic lock</code>���� */
    private Integer version;
    
    /** �������ʵ�����û� */
    private UserCodeName creator = new UserCodeName();
    
    /** ����޸����ʵ�����û� */
    private UserCodeName modifier = new UserCodeName();
    
    /** ����ʱ�� */
    private Date createdTime = new Date();
    
    /** ����޸�ʱ�� */
    private Date modifiedTime;
    
    /** ���г�һ��ҵ��ʵ��ʱ,���ǰ�������������� */
    private Integer sequenceNo = new Integer(0);
    
    /** ���־û�������ҵ��ʵ�����������ݿ��ͬ������ʱ���������־ */
    private boolean flushFlag;

    ///////////////////////////////////////////////////////
	 public static final String DEL_FLG_TRUE="1";
	 public static final String DEL_FLG_FALSE="0";
	/**
	 * 1��ɾ��
	 * 0������������
	 */
	private String delFlg="0";
	/**
	 * ���Ż�����Ϣ���У���¼��ǰ�����û�����������Ϣ�ļ�¼
	 */
	private DepartmentCodeName creatorDept = new DepartmentCodeName();
	/**
	 * ���Ż�����Ϣ���У���¼��ǰ�����û�����������Ϣ�ļ�¼
	 */
	private DepartmentCodeName modifierDept;
	/**
	 * �û�������Ϣ���У���¼��ǰ�����û���Ϣ�ļ�¼
	 */
	private UserCodeName deleter;
	/**
	 * ���Ż�����Ϣ���У���¼��ǰ�����û�����������Ϣ�ļ�¼��ID
	 */
	private DepartmentCodeName deleterDept;
	/**
	 * ��¼�߼�ɾ��ʱ�Ļ���ʱ�䣬
	 * ʹ��java.util.Date��ȡ��ǰ����ʱ��
	 */
	private Date deletedTime;
	/////////////////////////////////////////////////////////
    
    
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        String oid = ((Entity) obj).getId();
        if (oid == null || oid.trim().length() == 0) {
            return false;
        }
        return oid.equals(getId());
    }

    public int hashCode() {
        if (getId() == null || getId().trim().length() == 0) {
            return super.hashCode();
        } else {
            return getClass().hashCode() * 29 + getId().hashCode();
        }
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public UserCodeName getCreator() {
        return creator;
    }

    public void setCreator(UserCodeName creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public UserCodeName getModifier() {
        return modifier;
    }

    public void setModifier(UserCodeName modifier) {
        this.modifier = modifier;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
    
    /**
     * ���־û�������ҵ��ʵ�����������ݿ��ͬ������ʱ���������־Ϊ<code>true</code>
     * @return ���ִ�������ݿ���²�������<code>true</code>
     */
    public boolean isFlushFlag() {
        return flushFlag;
    }

    public void setFlushFlag(boolean flushFlag) {
        this.flushFlag = flushFlag;
    }
    
    /////////////////////////////////////////////////////////////////////
	/**
	 * @return the creatorDept
	 */
	public DepartmentCodeName getCreatorDept() {
		return creatorDept;
	}
	/**
	 * @param creatorDept the creatorDept to set
	 */
	public void setCreatorDept(DepartmentCodeName creatorDept) {
		this.creatorDept = creatorDept;
	}
	/**
	 * @return the deletedTime
	 */
	public Date getDeletedTime() {
		return deletedTime;
	}
	/**
	 * @param deletedTime the deletedTime to set
	 */
	public void setDeletedTime(Date deletedTime) {
		this.deletedTime = deletedTime;
	}
	/**
	 * @return the deleter
	 */
	public UserCodeName getDeleter() {
		return deleter;
	}
	/**
	 * @param deleter the deleter to set
	 */
	public void setDeleter(UserCodeName deleter) {
		this.deleter = deleter;
	}
	/**
	 * @return the deleterDept
	 */
	public DepartmentCodeName getDeleterDept() {
		return deleterDept;
	}
	/**
	 * @param deleterDept the deleterDept to set
	 */
	public void setDeleterDept(DepartmentCodeName deleterDept) {
		this.deleterDept = deleterDept;
	}
	/**
	 * @return the delFlg
	 */
	public String getDelFlg() {
		return delFlg;
	}
	/**
	 * @param delFlg the delFlg to set
	 */
	public void setDelFlg(String delFlg) {
		if(StringUtils.isNotBlank(delFlg))
		{
			this.delFlg = delFlg;
		}
	}
	/**
	 * @return the modifierDept
	 */
	public DepartmentCodeName getModifierDept() {
		return modifierDept;
	}
	/**
	 * @param modifierDept the modifierDept to set
	 */
	public void setModifierDept(DepartmentCodeName modifierDept) {
		this.modifierDept = modifierDept;
	}
    /////////////////////////////////////////////////////////////////////
}
