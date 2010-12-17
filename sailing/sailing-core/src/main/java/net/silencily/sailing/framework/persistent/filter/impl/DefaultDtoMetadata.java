package net.silencily.sailing.framework.persistent.filter.impl;

import java.util.Collections;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.BaseDto;
import net.silencily.sailing.framework.persistent.filter.DtoMetadata;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * 遵循架构缺省持久化策略的<code>DTO</code>元信息, 按照架构持久化规范<ul>
 * <li>表名称:在<code>DTO</code>类名称中间的大写字母前加上一个下划线,把结果转换为小写字母
 * ,例如<code>DTO</code>的类名称是<code>InfoColumn</code>,那么表名称是<code>info_column
 * </code></li>
 * <li>列名称:在大写的属性名之前加上一个下划线, 比如属性<code>departmentName</code>对应的
 * 列名称为<code>department_name</code>, 结果为小写字母</li>
 * <li>属性名:与从属性名转换为列名称的过程相反, 去掉列名称中的下划线, 并把下划线后的第一个字
 * 母变为大写字母, 结果作为相应的属性名称, 例如列名称是<code>department_name</code>, 属性
 * 名称是<code>departmentName</code></li></ul>
 * <b>在违犯编程命名规范和<code>DTO</code>是内部类的情况下不保证结果的准确性</b>
 * 
 * @author scott
 * @since 2006-4-30
 * @version $Id: DefaultDtoMetadata.java,v 1.1 2010/12/10 10:54:21 silencily Exp $
 *
 */
public class DefaultDtoMetadata implements DtoMetadata {
    private Class dtoClass;
    
    private static Pattern pattern = null;
    
    private static PatternMatcher patternMatcher = new Perl5Matcher();

    public DefaultDtoMetadata() {
        this((Class) null);
    }
    
    public DefaultDtoMetadata(Class dtoClass) {
        this.dtoClass = dtoClass;
        try {
            pattern = new Perl5Compiler().compile("([\\S]+\\(\\s*)([^\\(\\)]+)(\\s*\\))", Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            throw new UnexpectedException("创建DefaultDtoMetadata时错误", e);
        }
    }

    public Class getDtoType() {
        return dtoClass;
    }
    
    public void setDtoType(Class dtoType) {
        this.dtoClass = dtoType;
    }

    public String getPropertyName(String columnName) {
        String ret = null;
        if (columnName != null) {
            if (columnName.indexOf('_') == -1) {
                return columnName.toLowerCase();
            }
            
            char[] cs = new char[columnName.length()];
            boolean upperFlag = false;
            int j = 0;
            for (int i = 0; i < columnName.length(); i++, j++) {
                char c = columnName.charAt(i);
                if (c == '_') {
                    upperFlag = true;
                    j--;
                } else if (upperFlag) {
                    if (Character.isLowerCase(c)) {
                        c = Character.toUpperCase(c);
                    }
                    cs[j] = c;
                    upperFlag = false;
                } else {
                    cs[j] = Character.toLowerCase(columnName.charAt(i));
                }
            }
            
            ret = new String(cs, 0, j);
        }
        
        return ret;
    }

    public String getColumnName(String propertyName) {
        if (StringUtils.isBlank(propertyName)) {
            return propertyName;
        }
        
        String ret = null;
        /* 匹配dbms函数的模式,比如 length(content) > 3000 */
        if (patternMatcher.matches(propertyName, pattern)) {
            MatchResult matchResult = patternMatcher.getMatch();
            if (matchResult.groups() > 1) {
                String s1 = matchResult.group(1);
                String s2 = getColumnName(matchResult.group(2));
                String s3 = matchResult.group(3);
                return s1 + s2 + s3;
            }
        }

        /* 以 "xxx.xxx.xxx" 表示的嵌套属性最后一个 "." 之前的属性都是表名称, 我们需要的是最后"."前后名称 */
        String[] props = propertyName.split("\\.");
        if (props.length > 1) {
            String tn = getColumnName(props[props.length - 2]);
            String cn = getColumnName(props[props.length - 1]);
            return tn + "." + cn;
        }

        int j = 0;
        char[] cs = new char[propertyName.length() * 2];
        for (int i = 0; i < propertyName.length(); i++, j++) {
            char c = propertyName.charAt(i);
            if (Character.isUpperCase(c)) {
                cs[j++] = '_';
                c = Character.toLowerCase(c);
            }
            cs[j] = c;
        }
        ret = new String(cs, 0, j);

        return ret;
    }

    public String getTableName() {
        if (this.dtoClass == null) {
            throw new IllegalStateException("当前DtoMetadata没有指定DTO类型, 无法评估表名称");
        }

        try {
            BaseDto dto = (BaseDto) dtoClass.newInstance();
            String tableName = dto.getTableName();
            if (StringUtils.isNotBlank(tableName)) {
                return tableName;
            }
        } catch (Exception e) {
            throw new UnexpectedException("不能实例化[" + dtoClass.getName() + "]", e);
        }

        String className = dtoClass.getName();
        if (className.lastIndexOf('.') > -1) {
            className = className.substring(className.lastIndexOf('.') + 1);
        }
        /* 把类名的第一个大写字母变为小写 */
        char initialChar = Character.toLowerCase(className.charAt(0));
        className = new StringBuffer(initialChar).append(className.substring(1)).toString();
        /* 遵循同属性->列名称的转换原则 */
        return getColumnName(className);
    }

    public String[] getPrimaryKey() {
        return new String[] {"id"};
    }

    public List getProperties() {
        return Collections.EMPTY_LIST;
    }

    /**
     * 一个独立的<code>DTO</code>元信息, 按照架构的规范映射属性和列名称, 不能提供更特定的信息
     */
    public static final DtoMetadata INDEPENDENT_DTO_METADATA = new DefaultDtoMetadata();
}
