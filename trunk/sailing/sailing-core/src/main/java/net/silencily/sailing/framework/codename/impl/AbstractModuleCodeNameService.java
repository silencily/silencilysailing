package net.silencily.sailing.framework.codename.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.silencily.sailing.framework.codename.CodeName;
import net.silencily.sailing.framework.codename.ModuleCodeNameService;

import org.apache.commons.lang.StringUtils;

/**
 * ����{@link CodeName}ʵ���ҵ���߼�ʵ��
 * @author zhangli
 * @since 2007-3-16
 * @version $Id: AbstractModuleCodeNameService.java,v 1.1 2010/12/10 10:54:27 silencily Exp $
 */
public abstract class AbstractModuleCodeNameService implements ModuleCodeNameService {
    
    protected int maxResultSet = MAX_RESULT_SET;
    
    public void setMaxResultSet(int maxResultSet) {
        this.maxResultSet = maxResultSet;
    }

    public Collection list(Class codeNameType) {
        if (codeNameType == null) {
            throw new NullPointerException("����load(Class)/map(Class)������null/empty");
        }
        if (!CodeName.class.isAssignableFrom(codeNameType)) {
            throw new ClassCastException("����codeNameType����CodeName����");
        }
        Collection result = listEntity(codeNameType);
        if (result.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        return result;
    }

    public CodeName load(Class codeNameType, String code) {
        if (codeNameType == null || StringUtils.isBlank(code)) {
            throw new NullPointerException("����load(Class, String)������null/empty");
        }
        if (!CodeName.class.isAssignableFrom(codeNameType)) {
            throw new ClassCastException("����codeNameType����CodeName����");
        }
        return loadEntity(codeNameType, code);
    }

    public Map map(Class codeNameType) {
        Collection c = list(codeNameType);
        if (c.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Map map = new LinkedHashMap(c.size());
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            CodeName cn = (CodeName) it.next();
            map.put(cn.getCode(), cn);
        }
        return map;
    }

    public Collection list(Class clazz, String parentCode) {
        if (clazz == null || StringUtils.isBlank(parentCode)) {
            throw new NullPointerException("������null/empty");
        }
        Collection c = listEntity(clazz, parentCode);
        return c.size() > 0 ? c : Collections.EMPTY_LIST;
    }

    protected abstract Collection listEntity(Class clazz);
    protected abstract CodeName loadEntity(Class clazz, String code);
    protected abstract Collection listEntity(Class clazz, String parentCode);
}
