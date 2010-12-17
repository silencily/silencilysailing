package net.silencily.sailing.framework.utils;

import java.util.ArrayList;
import java.util.Collection;

import net.silencily.sailing.exception.UnexpectedException;

/**
 * 支持任意地插入, 添加和检索元素而不会抛出{@link IndexOutOfBoundsException}的<code>List</code>
 * , 典型地用于从页面组装基于索引的数据, 对<code>shrink</code>机制的替换, 这个<code>utility</code>
 * 的几个方法返回值已经排除了其中的<code>null</code>元素
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
                throw new UnexpectedException("不能创建" + entity.getName() + "的实例", e);
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
