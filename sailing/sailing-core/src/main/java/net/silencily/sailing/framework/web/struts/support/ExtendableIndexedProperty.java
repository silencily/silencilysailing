package net.silencily.sailing.framework.web.struts.support;

import java.lang.reflect.InvocationTargetException;


/**
 * <p>用于支持组装<code>struts' form-bean</code>中索引属性, 支持<code>DTO</code>数组属性
 * 或<code>List</code>属性. 在<code>jsp</code>页面上的例子:<pre>
 * <code>&lt;input name="userDto[0].name value="coheg"&gt;</code></pre> 
 * 在组装这类属性时调用<code>getXXX(int)</code>时要返回一个<code>DTO</code>
 * 的实例, 使组装过程可以继续</p>
 * <p>这个组件成功执行的前提是<code>form-bean</code>在这个属性上必须体现规范的<code>javabean</code>
 * 风格的命名习惯, 比如属性<code>users</code>必须对应下面的<code>java bean</code>方法<pre>
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
 * <p>这个接口是对<code>commons's PrpertyUtilsBean</code>的扩展, 注意组装后的<code>List</code>
 * 或数组可能有<code>null</code>元素, 这取决于包含在<code>request</code>中组成参数名称的下标
 * 是否连续, 如果你需要连续的不包含<code>null</code>元素的索引属性, 使用{@link BaseActionForm#shrink shrink}
 * 方法排除掉<code>null</code>元素</p>
 * @author scotta
 * @since 2006-3-29
 * @version $Id: ExtendableIndexedProperty.java,v 1.1 2010/12/10 10:54:28 silencily Exp $
 * @see BaseActionForm#shrink
 * @see ListBasedExtendableIndexedProperty
 */
public interface ExtendableIndexedProperty {
    
    /**
     * 扩展<code>bean</code>的索引属性使得其它组件可以调用<code>getXXX(int)</code>这样的
     * 索引方法获得属性的值
     * @param bean      包含索引属性的对象
     * @param propName  要操作的属性, 这个属性一定是索引属性
     * @param index     要在指定属性<code>propName</code>获得值的索引值
     */
    void extend(Object bean, String propName, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}
