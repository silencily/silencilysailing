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
 * <p>����ʹ��<code>struts</code>��<code>ActionForm</code>��Ҫ��չ�������, �ṩ�����й���
 * <li>�����ϴ��ļ���СԽ����쳣, (<code>MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED</code)</li>
 * <li>�����û�����������<code>cancel(Globals.CANCEL_KEY)</code></li>
 * <li>�������û������<code>input</code>ʱ���ռܹ��쳣���ƴ���</li>
 * <li>֧��<code>shrink</code>������<code>Map</code>����</li></ul>
 * ��Ȼ��Ȼ֧��<code>struts</code>�����Դ�����֤����, �����Ƽ�ʹ�����ַ�ʽ; ��Ϊ�ܹ��������
 * �ṩ������Ϣ�ⲿ���Ļ���, ������ӦҲ�������ļ�, ��ʹ��<code>struts</code>������һҪ��ת��
 * ����, ���ǵ��´�����Ϣ���÷�ɢ, ������ά���͹���</p>
 * <p>ע�����е�����<code>protected</code>������ʹ��, Ϊ�������չ�ṩ����</p>
 * @author scott
 * @since 2006-3-28
 * @version $Id: BaseActionForm.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @see #validateInternal(ActionMapping, HttpServletRequest)
 * @see #postPropertiesProcess()
 */
public abstract class BaseActionForm extends ActionForm {
	
    /** ��־�ų����������е�<code>null</code>Ԫ�ص��������� */
    public static final String SHRINK_PROPERTY_NAME = "shrink";
    
    /**
     * ��ҳ����װ��������ʱ, ���ܻ���ֲ�������������, �������һЩԪ����<code>null</code>, ���
     * ָ�������������<code>true</code>, �ܹ��Զ��������<code>null</code>Ԫ��, �γ��������
     * ����������. ȱʡ��������������<code>true</code>
     */
    protected boolean shrink = true;
    
    /**
     * ��һ��ҳ��������ʱ��<code>request</code>
     */
    protected HttpServletRequest request;
    
    /** ҵ����� id Ĭ�ϵĲ����� */
    protected String oid;
    
    /** Dispatch Action ��Ĭ�ϵ� method name parameter */
    protected String step;
    
    /**
     * ����ҳ��ķ�ҳ��Ϣ
     */
    private Paginater paginater = new Paginater();
    
    /**
     * ����ҳ��ļ�������
     */
    private Map conditions = new LinkedHashMap(30);
    
    /** ��֤�����з������쳣, ����쳣Ҫ��ʱ����<code>action</code>�д��� */
    private Exception exception;
    
    /** �ṩ�� ui �ķ�ҳ List */
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
            /* ���ﲻ��ҪΪ�½��� Condition ������������, ������������������Զ����õ�, �������û�������� */
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
     * <p>��֤�����Ĵ���, �������ʵ�����Լ�����֤�߼�, ��������ܵ������<ol>
     * <li>����֤ʧ��ʱ��ת��������<code>struts's config</code>�е�<code>input</code>:
     * ��Ҫ���ذ���������Ϣ��<code>ActionErrors</code>�в�������<code>input</code></li>
     * <li>����֤ʧ��ʱ��<code>Action</code>�д������ش�����Ϣ, ��Ҫ����ʵ�־���Ĵ�����,
     * ���뷵��<code>null</code>, Ȼ����<code>Action</code>�д���</li>
     * </ol><b>ע��:</b>�������ǰһ�ַ���<code>Action</code>�ķ�������ִ��; ͬʱ����������
     * ��֤����Ӧ�����<code>form-bean</code>�е�����ֵ�Ƿ�Ϸ�, ����Ӧ�ô���ҵ���߼������
     * �������ݿ⽻���Ĳ���</p>
     * <p>�������֤�����з����������, ���ӳٵ�����<code>action</code>���ɼܹ����쳣������ͳ
     * һ����</p>
     * @see #validateInternal(ActionMapping, HttpServletRequest)
     */
     public final ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        try {
            ActionErrors errors = validateInternal(mapping, request);
            
            if (errors != null) {
                return errors;
            }
        } catch (Throwable e) {
           /* �� RequestProcessor �����Ǵ�����ĳЩ�쳣, ʹ���ǲ��� sendError �������. ��Щ�쳣���ܵ�����
            * �������û����ȷ�ر� populate, �Ӷ����ܵ�������ķ���������ȷִ��. ������������쳣�����ȴ���
            * ��������쳣, ������������쳣
            */
           if (this.exception == null) {
               this.exception = new Exception("��֤form����ʱ�������", e);
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
     * ���������Ҫ��<code>form-bean</code>��ִ����֤�͸����������, ͬʱ���Բ���<code>struts</code>
     * ����֤������, ����ͨ������<code>null</code>ִ���Լ��ķ���
     * @see #validate(ActionMapping, HttpServletRequest)
     */
    protected ActionErrors validateInternal(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }
    
    /**
     * �õ���ǰ��¼�û�
     * @see ContextInfo#getCurrentUser()
     * @return ��ǰ��¼�û�
     */
    public User getRemoteUser() {
    	return ContextInfo.getCurrentUser();
    }
    
	/**
	 * �õ��� map ��װ�ĵ�ǰ�û���ɫ�Ա�ҳ����� 
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
     * ��ǰ�Ƿ���������, һ������ҵ���߼��е��ж�
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
	 * ���ݲ�ѯ������ key �õ���ѯ������ֵ, ���Ҳ���ʱ���� null
	 * @param key the condition key
	 * @return condition value
	 */
	public Object getConditionValue(String key) {
		Condition condition = (Condition) getConditions().get(key);
		return condition == null ? null : condition.getValue();
	}
	
	/**
	 * ���ݲ�ѯ������ key �õ���ѯ������ֵ, ��������ӵ� list �з���, list ����0 �� 1 ��Ԫ��,
	 * ���������Ϊ extend combo �ṩ��, ���� extend combo ��ѯʱ����ֵ
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
