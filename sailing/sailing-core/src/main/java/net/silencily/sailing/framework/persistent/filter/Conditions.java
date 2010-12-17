package net.silencily.sailing.framework.persistent.filter;

import net.silencily.sailing.framework.persistent.filter.impl.DefaultDtoMetadata;

/**
 * 表示一个持久化层操作的条件组合, 这个条件有一个或多个<code>Condition</code>组成, 各个
 * 条件之间采用逻辑与/逻辑或连接
 * @author scott
 * @since 2006-4-30
 * @version $Id: Conditions.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public interface Conditions {
    
    /**
     * 返回这组条件应用的<code>DTO</code>元信息
     * @return 这组条件应用的<code>DTO</code>元信息
     */
    DtoMetadata getDtoMetadata();

    /**
     * 把这个实例包含的条件转换成持久化层可以使用的<code>where</code>子句的字符串和条件值
     * 数组, 如果这个实例没有包含任何<code>Condition</code>, 返回一个规范的空条件
     * {@link StatementAndParameters#EMPTY_CONDITION <code>EMPTY_CONDITION</code>}
     * @return 包含合并了条件的<code>where</code>子句及参数值的<code>StatementAndParameters</code>
     *         ,不返回<code>null</code>
     */
    StatementAndParameters getResult();
    
    /**
     * 返回已经应用了的条件, 这些条件已经用于构造<code>sql</code>语句
     * @return 已经应用了的条件
     */
    Condition[] getAppliedConditions();
    
    /**
     * 合并另一个<code>Conditions</code>, 两个组合条件之间的逻辑关系取决于第二个组合条件
     * 中第一个条件的<code>prepend</code>
     * @param other 要合并的另一个条件
     * @throws NullPointerException 如果要合并的条件是<code>null</code>
     */
    void join(Conditions other);
    
    /**
     * 为当前的组合条件增加一组新的条件, 这个条件追加到原来条件的末端
     * @param other 要追加的条件
     * @throws NullPointerException 如果参数是<code>null</code>
     */
    void appendCondition(Condition[] other);
    
    /** 表示一个空的<code>Conditions</code> */
    Conditions EMPTY_CONDITIONS = new Conditions() {
        private DtoMetadata dtoMetadata = new DefaultDtoMetadata();
        
        public StatementAndParameters getResult() {
            return StatementAndParameters.EMPTY_CONDITION;
        }

        public void join(Conditions other) {
            throw new UnsupportedOperationException("不能对空条件Conditions执行join操作");
        }

        public void appendCondition(Condition[] other) {
            throw new UnsupportedOperationException("不能对空条件Conditions执行appendCondition操作");
        }

        public DtoMetadata getDtoMetadata() {
            return dtoMetadata;
        }

        public Condition[] getAppliedConditions() {
            return new Condition[0];
        }
    };
}
