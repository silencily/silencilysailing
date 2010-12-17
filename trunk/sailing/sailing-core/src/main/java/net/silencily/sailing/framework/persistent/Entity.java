package net.silencily.sailing.framework.persistent;

import java.io.Serializable;
import java.util.Date;

import net.silencily.sailing.framework.codename.DepartmentCodeName;
import net.silencily.sailing.framework.codename.UserCodeName;

import org.apache.commons.lang.StringUtils;

/**
 * <p>需要持久化业务实体的基类, 定义了一个持久化类所共同的属性, 注意系统中的业务实体继承这个类
 * 的相等性, 就是基于标识符判断相等性, 对于没有<code>id</code>属性的业务实体比如具有<code>code</code>
 * 属性的业务实体应该通过覆盖方法
 * <pre>
 *     public String getId() {
 *         return getCode();
 *     }
 * </pre>来保证缺省相等性</p>
 * <p>按照约定系统中所有的业务实体对应的数据库表的列名称和长度<ul>
 * <li>{@link #id}:id,VARCHAR(32)</li>
 * <li>{@link #version}:version,integer(6)</li>
 * <li>{@link #creator}:operator_id,VARCHAR(50),向后兼容的名称</li>
 * <li>{@link #modifier}:last_modified_user,VARCHAR(50),向后兼容的名称</li>
 * <li>{@link #createdTime}:created_time,timestamp</li>
 * <li>{@link #modifiedTime}:last_modfied_time,timestamp</li></ul>
 * @author zhangli
 * @version $Id: Entity.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 * @since 2007-4-24
 */
public abstract class Entity implements Serializable {
    
    /** 这个类的实例的标识符 */ 
    private String id;
    
    /** 用于<code>optimistic lock</code>机制 */
    private Integer version;
    
    /** 创建这个实例的用户 */
    private UserCodeName creator = new UserCodeName();
    
    /** 最后修改这个实例的用户 */
    private UserCodeName modifier = new UserCodeName();
    
    /** 创建时间 */
    private Date createdTime = new Date();
    
    /** 最后修改时间 */
    private Date modifiedTime;
    
    /** 当列出一组业务实体时,它们按照这个次序排列 */
    private Integer sequenceNo = new Integer(0);
    
    /** 当持久化层对这个业务实体做了与数据库的同步操作时设置这个标志 */
    private boolean flushFlag;

    ///////////////////////////////////////////////////////
	 public static final String DEL_FLG_TRUE="1";
	 public static final String DEL_FLG_FALSE="0";
	/**
	 * 1：删除
	 * 0或其他：正常
	 */
	private String delFlg="0";
	/**
	 * 部门基本信息表中，记录当前操作用户所属部门信息的记录
	 */
	private DepartmentCodeName creatorDept = new DepartmentCodeName();
	/**
	 * 部门基本信息表中，记录当前操作用户所属部门信息的记录
	 */
	private DepartmentCodeName modifierDept;
	/**
	 * 用户基本信息表中，记录当前操作用户信息的记录
	 */
	private UserCodeName deleter;
	/**
	 * 部门基本信息表中，记录当前操作用户所属部门信息的记录的ID
	 */
	private DepartmentCodeName deleterDept;
	/**
	 * 记录逻辑删除时的机器时间，
	 * 使用java.util.Date获取当前机器时间
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
     * 当持久化层对这个业务实体做了与数据库的同步操作时设置这个标志为<code>true</code>
     * @return 如果执行了数据库更新操作返回<code>true</code>
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
