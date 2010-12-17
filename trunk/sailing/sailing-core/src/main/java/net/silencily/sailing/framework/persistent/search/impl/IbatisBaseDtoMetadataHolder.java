package net.silencily.sailing.framework.persistent.search.impl;

import java.util.Collections;
import java.util.List;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.persistent.BaseDto;
import net.silencily.sailing.framework.persistent.search.MetadataHolder;
import net.silencily.sailing.framework.utils.IbatisUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ����<code>BaseDto</code>Ԫ���ݱ�����, <code>BaseDto's getListStatement</code>����
 * ֵ����Ϊ<code>IBATIS's mapping statement id</code>, <code>domain object</code>
 * �����������ݿ�������֮���ӳ������Ǻ��Դ�Сдȥ���»��������������������ƥ���˵����һһ��Ӧ
 * 
 * @author scott
 * @since 2006-4-19
 * @version $Id: IbatisBaseDtoMetadataHolder.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 *
 */
public class IbatisBaseDtoMetadataHolder implements MetadataHolder {
    private Class domainType;
    private BaseDto dto;
    protected String queryStatement;
    
    /** ����<code>IBATIS</code>�������г��������ݵ�����<code>id</code> */
    private static final String LIST_STATEMENT = "listStatement";

    public IbatisBaseDtoMetadataHolder(Class domainType) {
        this.domainType = domainType;
        create();
    }

    public Class getDomainType() {
        return this.domainType;
    }

    public void setDomainType(Class domainType) {
        this.domainType = domainType;
        create();
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
        String ret = null;
        if (propertyName != null) {
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
        }
        
        return ret;
    }

    public List getProperties() {
        return Collections.EMPTY_LIST;
    }

    public String getQueryStatement() {
        return this.queryStatement;
    }
    
    /* convenient for test, don't overwrite */
    protected void create() {
        try {
            this.dto = (BaseDto) this.domainType.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("���ܴ���domain�����ʵ��", e);
        }
        String id = null;
        try {
            id = BeanUtils.getProperty(dto, LIST_STATEMENT);
            if (StringUtils.isBlank(id)) {
                throw new NullPointerException("û����dto��ָ���г��������ݵ�sqlmap id");
            }
            this.queryStatement = IbatisUtils.getSql(id, null);
        } catch (Exception e) {
            throw new UnexpectedException("���ܶ�ȡdto��" + LIST_STATEMENT + "]����", e);
        }
    }
}
