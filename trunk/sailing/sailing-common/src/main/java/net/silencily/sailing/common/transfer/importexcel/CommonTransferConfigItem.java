package net.silencily.sailing.common.transfer.importexcel;


/**
 * ���� ����������, һ�����������������һ��<code>dbf</code>��<code>excel</code>����
 * ���ݿ��֮��Ķ�Ӧ��ϵ
 * @author Scott Captain
 * @since 2006-8-16
 * @version $Id: CommonTransferConfigItem.java,v 1.1 2010/12/10 10:54:15 silencily Exp $
 *
 */
public class CommonTransferConfigItem {
    /** ���ݿ��������� */
    private String columnName;
    
    /** <code>dbf</code>��<code>excel</code>�������� */
    private String fieldName;
    
    /** һ���е��������� */
    private String label;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("column name=[").append(columnName).append("],");
        sb.append("field name=[").append(fieldName).append("],");
        sb.append("label=[").append(label).append("]");
        return sb.toString();
    }
}
