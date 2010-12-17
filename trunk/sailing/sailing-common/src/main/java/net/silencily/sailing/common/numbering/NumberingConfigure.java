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
 * 用于各个子系统中单据生成编号的配置, 可以指定要生成编号的格式, 开始编号的数字而生成具有日期
 * 编号, 不适用于生成复杂的带有层次格式的编号, 比如物资编码,设备编码. 如果指定了日期格式那么
 * 按照日期格式生成的日期将作为{@link #resetValue}只要这个值发生变化就会复位编号当前的数字
 * {@link #currentNo}, 使用这个类时, 简单地调用方法{@link #number()}可以完成自增, 校验
 * 等操作. 这个类的设计是作为<code>service</code>的模式使用的, 所以这个类不是完整的领域模型
 * , 通过{@link NumberingService}使用编号
 * @since 2007-1-12
 * @author scott
 * @version $Id: NumberingConfigure.java,v 1.1 2010/12/10 10:54:18 silencily Exp $
 * @see NumberingService
 */
public class NumberingConfigure extends BaseDto {
    
    public static final Double SEED_NO = new Double(-1); 
    
    /** 
     * 生成编号的业务名称, 这个名称是全局唯一的, 规定以全路经类名的形式指定业务名称, 最高位
     * 的名称是模块名, 依次是功能, 子功能, 一个物资代保管入库单编号配置是<tt>depository.warehouse.voucher</tt>
     * , 因为在工作流中常常用到编号, 所以以工作流名称作为编号的业务名称也是很好的选择
     */
    private String businessName;
    
    /**
     * 编号生成格式, 编号的日期部分的格式定义是<code>{0,data,yyyy-MM-dd}</code>, 数字
     * 部分的定义格式是<code>{1,number,00000}</code>, 其中的<tt>yyyy-MM-dd</tt>和
     * <tt>00000</tt>可以按照需要定义, 比如定义了格式<tt>物资领用{0,data,yyyy-MM-dd}-{1,number,00000}</tt>
     * ,编号服务生成的格式类似于<tt>物资领用2006-12-25-00101</tt>, 可以不指定这个属性, 通过
     * 服务获得编号时, 仅仅返回一个数字的字符串表现
     */
    private String numberingFormat;
    
    /**
     * 一个数字表示当前已使用的编号的值, 当获取一个新的编号时, 编号服务把检索到的指加<b>1</b>
     * 作为当前值返回, 同时把这个值返回到计数器, 表示当前已使用的最大值
     */
    private Double currentNo = SEED_NO;
    
    /**
     * 这个编号配置的说明, 用于维护编号配置使用
     */
    private String numberingNote;
    
    /**
     * 当日期格式中的日期值与这个值比较发生变化时, 首先复位编号到{@link #SEED_NO}然后设置新的
     * 日期值, 这个值是程序中使用的
     */
    private String resetValue;
    
    //- the business methods
    
    /** 返回新的编号, 注意这个方法在生成编号时仅可以调用一次, 执行时修改了{@link #currentNo}属性 */
    public Double next() {
        if (getCurrentNo() == null) {
            setCurrentNo(SEED_NO);
        }
        Double d = new Double(getCurrentNo().doubleValue() + 1);
        setCurrentNo(d);
        return d;
    }
    
    /**
     * 验证当前编号格式是否正确
     * @return 如果不正确返回一个异常, 如果正确返回<code>null</code>
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
