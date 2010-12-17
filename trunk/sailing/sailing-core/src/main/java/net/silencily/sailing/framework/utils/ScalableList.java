package net.silencily.sailing.framework.utils;

import java.util.ArrayList;
import java.util.Collection;

import net.silencily.sailing.exception.UnexpectedException;

/**
 * ֧������ز���, ��Ӻͼ���Ԫ�ض������׳�{@link IndexOutOfBoundsException}��<code>List</code>
 * , ���͵����ڴ�ҳ����װ��������������, ��<code>shrink</code>���Ƶ��滻, ���<code>utility</code>
 * �ļ�����������ֵ�Ѿ��ų������е�<code>null</code>Ԫ��
 * @author zhangli
 * @version $Id: ScalableList.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-6-8
 */
public class ScalableList extends ArrayList {
    
    private Class entity;
    
    public ScalableList(Class entity) {
        super();
        this.entity = entity;
    }
    
    public ScalableList(int initialCapacity) {
        super(initialCapacity);
    }

    public void add(int index, Object element) {
        scale(index);
        super.add(index, element);
    }

    public boolean addAll(int index, Collection c) {
        scale(index);
        return super.addAll(index, c);
    }

    public Object get(int index) {
        if (index >= size()) {
            scale(index);
            Object o = null;
            try {
                o = entity.newInstance();
                set(index, o);
            } catch (Exception e) {
                throw new UnexpectedException("���ܴ���" + entity.getName() + "��ʵ��", e);
            }
        }
        return super.get(index);
    }

    public Object set(int index, Object element) {
        scale(index);
        return super.set(index, element);
    }

    private void scale(int index) {
        if (size() <= index) {
            for (int i = 0; i <= index; i++) add(null);
        }
    }
}
