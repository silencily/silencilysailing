package net.silencily.sailing.framework.web.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.core.User;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.Paginater;
import net.silencily.sailing.framework.web.view.ComboSupportList;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * <p>所有使用<code>struts</code>的<code>ActionForm</code>都要扩展这个基类, 提供了下列功能
 * <li>处理上传文件大小越界的异常, (<code>MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED</code)</li>
 * <li>处理用户从浏览器点击<code>cancel(Globals.CANCEL_KEY)</code></li>
 * <li>处理如果没有配置<code>input</code>时按照架构异常机制处理</li>
 * <li>支持<code>shrink</code>索引和<code>Map</code>属性</li></ul>
 * 虽然仍然支持<code>struts</code>的属性错误验证机制, 但不推荐使用这种方式; 因为架构本身必须
 * 提供错误信息外部化的机制, 所以相应也有配置文件, 再使用<code>struts</code>的配置一要求转换
 * 编码, 二是导致错误信息配置分散, 不利于维护和构建</p>
 * <p>注意其中的两个<code>protected</code>方法的使用, 为子类的扩展提供方便</p>
 * @author scott
 * @since 2006-3-28
 * @version $Id: BaseActionForm.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @see #validateInternal(ActionMapping, HttpServletRequest)
 * @see #postPropertiesProcess()
 */
public abstract class BaseActionForm extends ActionForm {
	
    /** 标志排除索引属性中的<code>null</code>元素的属性名称 */
    public static final String SHRINK_PROPERTY_NAME = "shrink";
    
    /**
     * 在页面组装索引属性时, 可能会出现不连续的索引号, 造成其中一些元素是<code>null</code>, 如果
     * 指定了这个属性是<code>true</code>, 架构自动会清除掉<code>null</code>元素, 形成序号连续
     * 的索引属性. 缺省情况下这个属性是<code>true</code>
     */
    protected boolean shrink = true;
    
    /**
     * 当一次页面请求到来时的<code>request</code>
     */
    protected HttpServletRequest request;
    
    /** 业务对象 id 默认的参数名 */
    protected String oid;
    
    /** Dispatch Action 中默认的 method name parameter */
    protected String step;
    
    /**
     * 保存页面的分页信息
     */
    private Paginater paginater = new Paginater();
    
    /**
     * 保存页面的检索条件
     */
    private Map conditions = new LinkedHashMap(30);
    
    /** 验证过程中发生的异常, 这个异常要延时进入<code>action</code>中处理 */
    private Exception exception;
    
    /** 提供给 ui 的翻页 List */
    private ComboSupportList paginaterList;

    public void setShrink(boolean shrink) {
        this.shrink = shrink;
    }
    
    public boolean isShrink() {
        return this.shrink;
    }
    
    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
    }
    
    public Paginater getPaginater() {
        return this.paginater;
    }

    public Map getConditions() {
        return conditions;
    }
    
    public Condition getConditions(String propertyName) {
        Condition c = (Condition) this.conditions.get(propertyName); 
        if (c == null) {
            /* 这里不需要为新建的 Condition 设置属性名称, 如果条件有属性名会自动设置的, 否则就是没有属性名 */
            c = new Condition();
            this.conditions.put(propertyName, c);
        }
        return c;
    }

    public void setConditions(Map conditions) {
        this.conditions = conditions;
    }
    
    public void setConditions(String propertyName, Condition condition) {
        this.conditions.put(propertyName, condition);
    }

    /**
     * <p>验证发生的错误, 如果子类实现了自己的验证逻辑, 错误处理可能的情况是<ol>
     * <li>当验证失败时跳转到配置在<code>struts's config</code>中的<code>input</code>:
     * 需要返回包含错误信息的<code>ActionErrors</code>中并且配置<code>input</code></li>
     * <li>当验证失败时在<code>Action</code>中处理单独地错误信息, 需要子类实现具体的处理方案,
     * 必须返回<code>null</code>, 然后在<code>Action</code>中处理</li>
     * </ol><b>注意:</b>如果采用前一种方案<code>Action</code>的方法不再执行; 同时这里所作的
     * 验证仅仅应该针对<code>form-bean</code>中的属性值是否合法, 决不应该存在业务逻辑方面或发
     * 生与数据库交互的操作</p>
     * <p>如果在验证过程中发生程序错误, 将延迟到进入<code>action</code>后由架构的异常处理器统
     * 一处理</p>
     * @see #validateInternal(ActionMapping, HttpServletRequest)
     */
     public final ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        try {
            ActionErrors errors = validateInternal(mapping, request);
            
            if (errors != null) {
                return errors;
            }
        } catch (Throwable e) {
           /* 在 RequestProcessor 中我们处理了某些异常, 使他们不再 sendError 到浏览器. 这些异常可能导致了
            * 请求参数没有正确地被 populate, 从而可能导致子类的方法不能正确执行. 所以如果发生异常就首先处理
            * 最根本的异常, 而忽略这里的异常
            */
           if (this.exception == null) {
               this.exception = new Exception("验证form属性时程序错误", e);
           }
        }
        
        return null;
    }
     
    public void setException(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return this.exception;
    }
    
    /**
     * 如果子类需要在<code>form-bean</code>中执行验证就覆盖这个方法, 同时可以采用<code>struts</code>
     * 的验证处理方案, 或者通过返回<code>null</code>执行自己的方案
     * @see #validate(ActionMapping, HttpServletRequest)
     */
    protected ActionErrors validateInternal(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
    
    /**
     * 得到当前登录用户
     * @see ContextInfo#getCurrentUser()
     * @return 当前登录用户
     */
    public User getRemoteUser() {
    	return ContextInfo.getCurrentUser();
    }
    
	/**
	 * 得到用 map 封装的当前用户角色以便页面调用 
	 * @see ContextInfo#getCurrentUser()
	 * @see User#getRoles()
	 * @return map of remote user roles, key : role code, value : role code
	 */
	public Map getRemoteUserRoleMap() {
		List roles = ContextInfo.getCurrentUser().getRoles();
		Map map = new HashMap(roles.size());
		for (Iterator iter = roles.iterator(); iter.hasNext(); ) {
			String role = (String) iter.next();
			map.put(role, role);
		}
		return map;
	}
    
    
    /**
     * 当前是否新增操作, 一般用于业务逻辑中的判断
     * @return whether is create
     */
	public boolean isCreate() {
		return StringUtils.isBlank(getOid());
	}    
    
	/**
	 * @return the oid
	 */
	public String getOid() {
		if (StringUtils.isBlank(oid)) {
			oid = request.getParameter("oid");
		}
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	/**
	 * @return the step
	 */
	public String getStep() {
        if (step == null) {
            step = request.getParameter("step");
        }
		return step;
	}

	/**
	 * @param step the step to set
	 */
	public void setStep(String step) {
		this.step = step;
	}
	
	/**
	 * 根据查询条件的 key 得到查询条件的值, 查找不到时返回 null
	 * @param key the condition key
	 * @return condition value
	 */
	public Object getConditionValue(String key) {
		Condition condition = (Condition) getConditions().get(key);
		return condition == null ? null : condition.getValue();
	}
	
	/**
	 * 根据查询条件的 key 得到查询条件的值, 并将其填从到 list 中返回, list 中有0 或 1 个元素,
	 * 这个方法是为 extend combo 提供的, 用于 extend combo 查询时回显值
	 * @param key the condition key
	 * @return list fill with condition value
	 */
	public List getConditionValues(String key) {
		List list = new ArrayList(1);
		Object value = getConditionValue(key);
		if (value != null) {
			list.add(getConditionValue(key));			
		}
		return list;
	}
	
	/**
	 * @return the paginaterList
	 */
	public ComboSupportList getPaginaterList() {
		if (paginaterList == null) {
			Map paginaterMap = new LinkedHashMap();
			for (int i = 0; i < paginater.getPageCount(); i++) {
				paginaterMap.put(new Integer(i), i + "/" + paginater.getPageCount());
			}
			if (paginaterMap.isEmpty()) {
				paginaterMap.put(new Integer(1), "1 / 1");
			}
			paginaterList = new ComboSupportList(paginaterMap);
		}
		List selectedValues = new ArrayList();
		selectedValues.add(new Integer(paginater.getPage()));
		paginaterList.setHeaderValue(String.valueOf(paginater.getPage()));
		paginaterList.setHeaderText(paginater.getPage() + "/" + paginater.getPageCount());
		return paginaterList;
	}	
}
