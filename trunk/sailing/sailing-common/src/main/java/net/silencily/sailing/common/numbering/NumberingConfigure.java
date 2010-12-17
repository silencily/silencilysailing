/*
 * Copyright 2005-2010 the original author or authors.
 * 
 *      http://www.coheg.com.cn
 *
 * Project publicresource
 */
package net.silencily.sailing.common.numbering;

import java.text.DateFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.silencily.sailing.framework.persistent.BaseDto;

import org.apache.commons.lang.StringUtils;




/**
 * ���ڸ�����ϵͳ�е������ɱ�ŵ�����, ����ָ��Ҫ���ɱ�ŵĸ�ʽ, ��ʼ��ŵ����ֶ����ɾ�������
 * ���, �����������ɸ��ӵĴ��в�θ�ʽ�ı��, �������ʱ���,�豸����. ���ָ�������ڸ�ʽ��ô
 * �������ڸ�ʽ���ɵ����ڽ���Ϊ{@link #resetValue}ֻҪ���ֵ�����仯�ͻḴλ��ŵ�ǰ������
 * {@link #currentNo}, ʹ�������ʱ, �򵥵ص��÷���{@link #number()}�����������, У��
 * �Ȳ���. �������������Ϊ<code>service</code>��ģʽʹ�õ�, ��������಻������������ģ��
 * , ͨ��{@link NumberingService}ʹ�ñ��
 * @since 2007-1-12
 * @author scott
 * @version $Id: NumberingConfigure.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @see NumberingService
 */
public class NumberingConfigure extends BaseDto {
    
    public static final Double SEED_NO = new Double(-1); 
    
    /** 
     * ���ɱ�ŵ�ҵ������, ���������ȫ��Ψһ��, �涨��ȫ·����������ʽָ��ҵ������, ���λ
     * ��������ģ����, �����ǹ���, �ӹ���, һ�����ʴ�������ⵥ���������<tt>depository.warehouse.voucher</tt>
     * , ��Ϊ�ڹ������г����õ����, �����Թ�����������Ϊ��ŵ�ҵ������Ҳ�Ǻܺõ�ѡ��
     */
    private String businessName;
    
    /**
     * ������ɸ�ʽ, ��ŵ����ڲ��ֵĸ�ʽ������<code>{0,data,yyyy-MM-dd}</code>, ����
     * ���ֵĶ����ʽ��<code>{1,number,00000}</code>, ���е�<tt>yyyy-MM-dd</tt>��
     * <tt>00000</tt>���԰�����Ҫ����, ���綨���˸�ʽ<tt>��������{0,data,yyyy-MM-dd}-{1,number,00000}</tt>
     * ,��ŷ������ɵĸ�ʽ������<tt>��������2006-12-25-00101</tt>, ���Բ�ָ���������, ͨ��
     * �����ñ��ʱ, ��������һ�����ֵ��ַ�������
     */
    private String numberingFormat;
    
    /**
     * һ�����ֱ�ʾ��ǰ��ʹ�õı�ŵ�ֵ, ����ȡһ���µı��ʱ, ��ŷ���Ѽ�������ָ��<b>1</b>
     * ��Ϊ��ǰֵ����, ͬʱ�����ֵ���ص�������, ��ʾ��ǰ��ʹ�õ����ֵ
     */
    private Double currentNo = SEED_NO;
    
    /**
     * ���������õ�˵��, ����ά���������ʹ��
     */
    private String numberingNote;
    
    /**
     * �����ڸ�ʽ�е�����ֵ�����ֵ�ȽϷ����仯ʱ, ���ȸ�λ��ŵ�{@link #SEED_NO}Ȼ�������µ�
     * ����ֵ, ���ֵ�ǳ�����ʹ�õ�
     */
    private String resetValue;
    
    //- the business methods
    
    /** �����µı��, ע��������������ɱ��ʱ�����Ե���һ��, ִ��ʱ�޸���{@link #currentNo}���� */
    public Double next() {
        if (getCurrentNo() == null) {
            setCurrentNo(SEED_NO);
        }
        Double d = new Double(getCurrentNo().doubleValue() + 1);
        setCurrentNo(d);
        return d;
    }
    
    /**
     * ��֤��ǰ��Ÿ�ʽ�Ƿ���ȷ
     * @return �������ȷ����һ���쳣, �����ȷ����<code>null</code>
     */
    public IllegalArgumentException validate() {
        IllegalArgumentException ex = null;
        if (StringUtils.isNotBlank(getNumberingFormat())) {
            try {
                MessageFormat.format(getNumberingFormat(), new Object[] {net.silencily.sailing.utils.DBTimeUtil.getDBTime(), SEED_NO});
            } catch (Throwable e) {
                ex = new IllegalArgumentException();
                ex.initCause(e);
            }
        }
        return ex;
    }
    
    public String number() {
        IllegalArgumentException ex = validate();
        if (ex != null) {
            throw ex;
        }
        Double d = next();
        if (StringUtils.isBlank(getNumberingFormat())) {
            return d.toString();
        } else {
            MessageFormat mf = new MessageFormat(getNumberingFormat());
            List list = new ArrayList();
            Format[] formats = mf.getFormats();
            if (formats.length > 0) {
                for (int i = 0; i < formats.length; i++) {
                    if (NumberFormat.class.isInstance(formats[i])) {
                        list.add(d);
                    } else if (DateFormat.class.isInstance(formats[i])) {
                        String v = formats[i].format(getDate());
                        if (v != null && !v.equals(getResetValue())) {
                            setResetValue(v);
                            setCurrentNo(null);
                            d = next();
                            if (list.size() > 0) {
                                list.clear();
                                list.add(d);
                                list.add(getDate());
                            } else {
                                list.add(getDate());
                                list.add(d);
                            }
                            break; 
                        }
                        list.add(getDate());
                    }
                }
            }
            return MessageFormat.format(getNumberingFormat(), list.toArray(new Object[list.size()]));
        }
    }

    protected Date getDate() {
        return net.silencily.sailing.utils.DBTimeUtil.getDBTime();
    }
    
    //- getters & setters

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Double getCurrentNo() {
        return currentNo;
    }

    public void setCurrentNo(Double currentNo) {
        this.currentNo = currentNo;
    }

    public String getNumberingFormat() {
        return numberingFormat;
    }

    public void setNumberingFormat(String numberingFormat) {
        this.numberingFormat = numberingFormat;
    }

    public String getNumberingNote() {
        return numberingNote;
    }

    public void setNumberingNote(String numberingNote) {
        this.numberingNote = numberingNote;
    }

    public String getResetValue() {
        return resetValue;
    }

    protected void setResetValue(String resetValue) {
        this.resetValue = resetValue;
    }
    
}
