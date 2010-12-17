package net.silencily.sailing.framework.codename;

import java.util.Collection;
import java.util.Map;

import net.silencily.sailing.framework.SystemConstants;
import net.silencily.sailing.framework.core.ServiceBase;

/**
 * ����{@link CodeName}�ķ���, ע����Ϊ<code>CodeName</code>����ʵ������, �������з�����
 * ����ֵ, ���������е�Ԫ��һ������<code>cast</code>Ϊ<code>Entity</code>
 * @author zhangli
 * @since 2007-3-16
 * @version $Id: ModuleCodeNameService.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @see CodeName
 */
public interface ModuleCodeNameService extends ServiceBase {
    
    String SERVICE_NAME = SystemConstants.MODULE_NAME + ".moduleCodeNameService";
    
    /**
     * ���ڿ���{@link #list(Class)}�������صĽ��������, �������������޻��׳�
     * {@link TooManyCodeNameDefinedException}, ����һ��ȱʡֵ, �����ʵ��
     * ����ͨ�������޸��������
     */
    int MAX_RESULT_SET = 1000;
    
    /**
     * ����ָ�����ͺ����Ƶ�{@link CodeName}, ���Է���<code>null</code>��ԭ������ĳЩʱ��
     * ���������˼�������, �����Ǹ��׶���Щ���벻��ʹ����, ��������Щ�����������Ȼ����Щ�����
     * ��Ϣ, �������ܾͳ����������, ���Ǵ������ֻ�Ӳ�ɾ�İ취��Ȼ�ǹ�ʱ�Ĳ���, �����������ķ���
     * �����������
     * @param clazz ʵ����{@link CodeName}��רҵ�Ⱦ���ʵ�����<code>Class</code>
     * @param code  Ҫ������<code>code</code>ֵ
     * @return Ҫ������{@link CodeName}, <b>���ܷ���<code>null</code></b>
     * @throws NullPointerException ����κ�һ��������<code>null</code>��<code>empty</code>
     * @throws ClassCastException �������<code>codeNameType</code>����<code>CodeName</code>����
     */
    CodeName load(Class codeNameType, String code);
    
    /**
     * <p>�г�ָ�����͵�����{@link CodeName}. �ٴ�ǿ������{@link CodeName}������ͳһ�ر���ϵ
     * ͳ��û���ض�ҵ���ܵĴ�����, ��Щ����������ܴ�, ��������{@link CodeName}�Ǵ�����ҵ
     * ������, ���羹Ȼ��{@link CodeName}���������ʱ�����ô���������<code>out of memory</code>
     * ,{@link #MAX_RESULT_SET}����Ŀ�ľ����������ֿ��ܵ�����, ������������������ʱ���µ�����
     * �����������, ����ӿڵ�ʵ������������������, �������˳���ǰ���{@link CodeName#getOrder()}����ֵ�����</p>
     * @param codeNameType {@link CodeName}����ʵ�����<code>Class</code>
     * @return ָ�����͵����д���, ���û�н��������<code>Collections.EMPTY_LIST</code>
     * @throws NullPointerException ���������<code>null</code>
     * @throws ClassCastException �������<code>codeNameType</code>����<code>CodeName</code>����
     */
    Collection list(Class codeNameType);
    
    /**
     * ִ����{@link #list(Class)��ͬ�Ĺ���, ����һ�������<code>Map</code>
     * @return <code>Map's key</code>��{@link CodeName#getCode()}��ֵ, <code>Map's value</code>
     * ����{@link CodeName}����, ��˳����{@link CodeName}�����˳����ͬ, ���û�н��������
     * <code>Collections.EMPTY_MAP</code>
     * @throws NullPointerException ���������<code>null</code>
     * @throws ClassCastException �������<code>codeNameType</code>����<code>CodeName</code>����
     * @see #list(Class)
     */
    Map map(Class codeNameType);
    
    /**
     * �г��Բ���Ϊ����������е��ӱ���
     * @param clazz      Ҫ�����Ĵ�������
     * @param parentCode ����
     * @return �Բ���Ϊ����������е��ӱ���, Ԫ�������ǲ���<code>clazz</code>ָ��������,
     *         ���û�����ݷ���<code>Collections.EMPTY_LIST</code>
     * @throws NullPointerException ���������<code>null</code>,<code>empty</code>
     */
    Collection list(Class clazz, String parentCode);

}
