package net.silencily.sailing.framework.codename;

/**
 * 系统中<code>enumeration</code>的基类, 架构提供了数据库, 页面类型转换, 要求子类必须实现
 * 静态的方法, 例如:
 * <pre>
 *      public static InstrumentState getByCode(Striug code) {
 *          return (InstrumentState) INSTANCES.get(code);
 *      }
 * </pre>
 * @author zhangli
 * @version $Id: EnumerationCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 * @since 2007-4-27
 */
public class EnumerationCodeName implements CodeName {
    
    /** 根据代码获得枚举实例的方法名 */
    public static final String METHOD_NAME = "getByCode";
    
    /** 枚举实例的方法的参数 */
    public static final Class[] METHOD_PARAMETERS = new Class[] {String.class};

    protected String code;
    
    protected String name;
    
    protected EnumerationCodeName(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    
    /**
     * 这两个方法是为了从页面组装属性时使用, 如果没有这个<code>write</code>方法, 按照
     * <code>java bean</code>的规范无法组装这个属性
     */
    public void setCode(String code) {}
    public void setName(String name) {}

    public String getName() {
        return name;
    }
    
    public String toString() {
        return getName();
    }
}
