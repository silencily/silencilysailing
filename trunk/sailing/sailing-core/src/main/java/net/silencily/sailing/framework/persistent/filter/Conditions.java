package net.silencily.sailing.framework.persistent.filter;

import net.silencily.sailing.framework.persistent.filter.impl.DefaultDtoMetadata;

/**
 * ��ʾһ���־û���������������, ���������һ������<code>Condition</code>���, ����
 * ����֮������߼���/�߼�������
 * @author scott
 * @since 2006-4-30
 * @version $Id: Conditions.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public interface Conditions {
    
    /**
     * ������������Ӧ�õ�<code>DTO</code>Ԫ��Ϣ
     * @return ��������Ӧ�õ�<code>DTO</code>Ԫ��Ϣ
     */
    DtoMetadata getDtoMetadata();

    /**
     * �����ʵ������������ת���ɳ־û������ʹ�õ�<code>where</code>�Ӿ���ַ���������ֵ
     * ����, ������ʵ��û�а����κ�<code>Condition</code>, ����һ���淶�Ŀ�����
     * {@link StatementAndParameters#EMPTY_CONDITION <code>EMPTY_CONDITION</code>}
     * @return �����ϲ���������<code>where</code>�Ӿ估����ֵ��<code>StatementAndParameters</code>
     *         ,������<code>null</code>
     */
    StatementAndParameters getResult();
    
    /**
     * �����Ѿ�Ӧ���˵�����, ��Щ�����Ѿ����ڹ���<code>sql</code>���
     * @return �Ѿ�Ӧ���˵�����
     */
    Condition[] getAppliedConditions();
    
    /**
     * �ϲ���һ��<code>Conditions</code>, �����������֮����߼���ϵȡ���ڵڶ����������
     * �е�һ��������<code>prepend</code>
     * @param other Ҫ�ϲ�����һ������
     * @throws NullPointerException ���Ҫ�ϲ���������<code>null</code>
     */
    void join(Conditions other);
    
    /**
     * Ϊ��ǰ�������������һ���µ�����, �������׷�ӵ�ԭ��������ĩ��
     * @param other Ҫ׷�ӵ�����
     * @throws NullPointerException ���������<code>null</code>
     */
    void appendCondition(Condition[] other);
    
    /** ��ʾһ���յ�<code>Conditions</code> */
    Conditions EMPTY_CONDITIONS = new Conditions() {
        private DtoMetadata dtoMetadata = new DefaultDtoMetadata();
        
        public StatementAndParameters getResult() {
            return StatementAndParameters.EMPTY_CONDITION;
        }

        public void join(Conditions other) {
            throw new UnsupportedOperationException("���ܶԿ�����Conditionsִ��join����");
        }

        public void appendCondition(Condition[] other) {
            throw new UnsupportedOperationException("���ܶԿ�����Conditionsִ��appendCondition����");
        }

        public DtoMetadata getDtoMetadata() {
            return dtoMetadata;
        }

        public Condition[] getAppliedConditions() {
            return new Condition[0];
        }
    };
}
