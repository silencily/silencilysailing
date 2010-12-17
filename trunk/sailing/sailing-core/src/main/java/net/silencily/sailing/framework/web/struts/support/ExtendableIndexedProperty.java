package net.silencily.sailing.framework.web.struts.support;

import java.lang.reflect.InvocationTargetException;


/**
 * <p>����֧����װ<code>struts' form-bean</code>����������, ֧��<code>DTO</code>��������
 * ��<code>List</code>����. ��<code>jsp</code>ҳ���ϵ�����:<pre>
 * <code>&lt;input name="userDto[0].name value="coheg"&gt;</code></pre> 
 * ����װ��������ʱ����<code>getXXX(int)</code>ʱҪ����һ��<code>DTO</code>
 * ��ʵ��, ʹ��װ���̿��Լ���</p>
 * <p>�������ɹ�ִ�е�ǰ����<code>form-bean</code>����������ϱ������ֹ淶��<code>javabean</code>
 * ��������ϰ��, ��������<code>users</code>�����Ӧ�����<code>java bean</code>����<pre>
 *   public UserDto getUserDtos(int index) {
 *     return userDtos[index];
 *   }
 *   
 *   public UserDto[] getUserDtos() {
 *     return userDtos;
 *   }
 *   
 *   public void setUserDtos(int index, UserDto dto) {
 *     userDtos[index] = dto;
 *   }
 *   
 *   public void setUserDtos(UserDto[] dtos) {
 *     userDtos = dtos;
 *   }
 * </pre></p>
 * <p>����ӿ��Ƕ�<code>commons's PrpertyUtilsBean</code>����չ, ע����װ���<code>List</code>
 * �����������<code>null</code>Ԫ��, ��ȡ���ڰ�����<code>request</code>����ɲ������Ƶ��±�
 * �Ƿ�����, �������Ҫ�����Ĳ�����<code>null</code>Ԫ�ص���������, ʹ��{@link BaseActionForm#shrink shrink}
 * �����ų���<code>null</code>Ԫ��</p>
 * @author scotta
 * @since 2006-3-29
 * @version $Id: ExtendableIndexedProperty.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 * @see BaseActionForm#shrink
 * @see ListBasedExtendableIndexedProperty
 */
public interface ExtendableIndexedProperty {
    
    /**
     * ��չ<code>bean</code>����������ʹ������������Ե���<code>getXXX(int)</code>������
     * ��������������Ե�ֵ
     * @param bean      �����������ԵĶ���
     * @param propName  Ҫ����������, �������һ������������
     * @param index     Ҫ��ָ������<code>propName</code>���ֵ������ֵ
     */
    void extend(Object bean, String propName, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
