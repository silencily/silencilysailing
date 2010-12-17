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
 * ��ѭ�ܹ�ȱʡ�־û����Ե�<code>DTO</code>Ԫ��Ϣ, ���ռܹ��־û��淶<ul>
 * <li>������:��<code>DTO</code>�������м�Ĵ�д��ĸǰ����һ���»���,�ѽ��ת��ΪСд��ĸ
 * ,����<code>DTO</code>����������<code>InfoColumn</code>,��ô��������<code>info_column
 * </code></li>
 * <li>������:�ڴ�д��������֮ǰ����һ���»���, ��������<code>departmentName</code>��Ӧ��
 * ������Ϊ<code>department_name</code>, ���ΪСд��ĸ</li>
 * <li>������:���������ת��Ϊ�����ƵĹ����෴, ȥ���������е��»���, �����»��ߺ�ĵ�һ����
 * ĸ��Ϊ��д��ĸ, �����Ϊ��Ӧ����������, ������������<code>department_name</code>, ����
 * ������<code>departmentName</code></li></ul>
 * <b>��Υ����������淶��<code>DTO</code>���ڲ��������²���֤�����׼ȷ��</b>
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
            throw new UnexpectedException("����DefaultDtoMetadataʱ����", e);
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
        /* ƥ��dbms������ģʽ,���� length(content) > 3000 */
        if (patternMatcher.matches(propertyName, pattern)) {
            MatchResult matchResult = patternMatcher.getMatch();
            if (matchResult.groups() > 1) {
                String s1 = matchResult.group(1);
                String s2 = getColumnName(matchResult.group(2));
                String s3 = matchResult.group(3);
                return s1 + s2 + s3;
            }
        }

        /* �� "xxx.xxx.xxx" ��ʾ��Ƕ���������һ�� "." ֮ǰ�����Զ��Ǳ�����, ������Ҫ�������"."ǰ������ */
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
            throw new IllegalStateException("��ǰDtoMetadataû��ָ��DTO����, �޷�����������");
        }

        try {
            BaseDto dto = (BaseDto) dtoClass.newInstance();
            String tableName = dto.getTableName();
            if (StringUtils.isNotBlank(tableName)) {
                return tableName;
            }
        } catch (Exception e) {
            throw new UnexpectedException("����ʵ����[" + dtoClass.getName() + "]", e);
        }

        String className = dtoClass.getName();
        if (className.lastIndexOf('.') > -1) {
            className = className.substring(className.lastIndexOf('.') + 1);
        }
        /* �������ĵ�һ����д��ĸ��ΪСд */
        char initialChar = Character.toLowerCase(className.charAt(0));
        className = new StringBuffer(initialChar).append(className.substring(1)).toString();
        /* ��ѭͬ����->�����Ƶ�ת��ԭ�� */
        return getColumnName(className);
    }

    public String[] getPrimaryKey() {
        return new String[] {"id"};
    }

    public List getProperties() {
        return Collections.EMPTY_LIST;
    }

    /**
     * һ��������<code>DTO</code>Ԫ��Ϣ, ���ռܹ��Ĺ淶ӳ�����Ժ�������, �����ṩ���ض�����Ϣ
     */
    public static final DtoMetadata INDEPENDENT_DTO_METADATA = new DefaultDtoMetadata();
}
