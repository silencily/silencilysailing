package net.silencily.sailing.framework.persistent.filter;


/**
 * ����ϵͳ�в�ѯ���ݵ���Ϣ, ������������, ��ҳ����Ϣ. �������ͨ�ò�ѯ�ܹ��е�һ����, ����
 * �ռ�����. ���Լ���{@link ConditionInfo#isEmpty()}����Ƿ����ù�����, ��������������
 * <code>true</code>, ���ʵ������һ��������
 * @author scott
 * @since 2006-4-29 1:58
 * @version $Id: ConditionInfo.java,v 1.1 2010/12/10 10:54:17 silencily Exp $
 *
 */
public class ConditionInfo {
    /**
     * ���ڼܹ�������, �������ͨ���ǰ�ȫ�ȿ��Ƶ�����, ������Ӧ������
     */
    private Condition[] originalConditions;
    
    /**
     * ����Ӧ�õ�����, ��Щ�������ض���Ӧ�����õ�
     */
    private Condition[] appendConditions;

    /** ����Ӧ�õĳ־û���ķ�ҳ��Ϣ */
    private Paginater paginater;
    
    /** 
     * �Ƿ��ڵ�ǰִ�л����½�ֹӦ�ü�������, �������ڳ־û���������ҵ��ʵ��ʱ����һ��ҵ��ʵ��
     * ������������һ��ҵ��ʵ��ļ���, ȱʡ����½�ֹӦ���������, �����ʽ����
     */
    private boolean concealQuery = true;
    
    /** 
     * ��Ϊ��{@link ContextInfo#getContextCondition()}����<code>null</code>ʹ���Ŀ�
     * �����������ж��Ƿ�Ϊ<code>null</code>, Ϊ�˱�������鷳, ��ǰ��ķ����з���һ���յ�
     * ʵ��, �����־����˵�����ֿ�ʵ����
     */
    private boolean empty = true;

    public ConditionInfo() {
        this(new Condition[0]);
    }
    
    /**
     * ����һ��������Ԥ��������������<code>ConditionInfo</code>, Ԥ�����õ������������ڰ�ȫ
     * ϵͳ�е�����, ���綨��һ����Դʱ���Զ�����ڲ��ŵ�ѡ������, ʹ��½�û����������������ŵ�
     * ��Ϣ, ������캯��Ӧ���ڰ�ȫ�е���, ����Ӧ�õ�<code>API</code>
     * @param conditions
     */
    public ConditionInfo(Condition[] conditions) {
        this.originalConditions = conditions;
        this.appendConditions = new Condition[0];
        this.paginater = Paginater.NOT_PAGINATED;
        empty = false;
    }

    public Condition[] getAppendConditions() {
        if (appendConditions == null) {
            appendConditions = new Condition[0];
        }
        return appendConditions;
    }

    public void setAppendConditions(Condition[] appendConditions) {
        this.appendConditions = appendConditions;
        empty = false;
    }
    
    public void addAppendConditions(Condition condition) {
        if (condition != null) {
            Condition[] newConditions = new Condition[this.getAppendConditions().length + 1];
            System.arraycopy(this.appendConditions, 0, newConditions, 0, this.appendConditions.length);
            newConditions[newConditions.length - 1] = condition;
            this.appendConditions = newConditions;
            empty = false;
        }
    }

    public Condition[] getOriginalConditions() {
        if (originalConditions == null) {
            originalConditions = new Condition[0]; 
        }
        return originalConditions;
    }

    public void setOriginalConditions(Condition[] frameworkConditions) {
        this.originalConditions = frameworkConditions;
        empty = false;
    }

    public Paginater getPaginater() {
        if (this.paginater == null) {
            return Paginater.NOT_PAGINATED;
        }
        return paginater;
    }

    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
        empty = false;
    }
    
    /**
     * ��������������Ƿ���<code>empty</code>,����û�����ù��κ�����, �����Ǳ�־��δ���ù�
     * ����, ������˵������
     * @return �Ƿ����ù�����, ������������������һ�����ʵ�ʵ��, ��δ���ù���������<code>true</code>
     * @see {@link #empty}ע��
     */
    public boolean isEmpty() {
        return empty;
    }
    
    /** 
     * �Ƿ��ֹӦ�������������
     * @return �����ֹӦ����������<code>true</code>, ȱʡ������ǽ�ֹӦ��������� 
     */
    public boolean isConcealQuery() {
        return concealQuery;
    }
    
    /** 
     * ��ѯʱӦ�û��ֹ�������
     * @param concealQuery <code>true</code>��ֹӦ������, <code>false</code>��ѯʱ��Ҫ��ֹ�������
     */
    public void setConcealQuery(boolean concealQuery) {
        this.concealQuery = concealQuery;
    }
}
