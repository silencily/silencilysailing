package net.silencily.sailing.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import net.silencily.sailing.framework.codename.CodeName;
import net.silencily.sailing.framework.codename.EnumerationCodeName;
import net.silencily.sailing.framework.persistent.filter.Column;
import net.silencily.sailing.framework.utils.CodeNameConverter;
import net.silencily.sailing.framework.utils.ColumnConverter;
import net.silencily.sailing.framework.utils.DateConverter;
import net.silencily.sailing.framework.utils.EnumerationConverter;
import net.silencily.sailing.framework.utils.SqlDateConverter;
import net.silencily.sailing.framework.utils.SqlTimestampConverter;
import net.silencily.sailing.framework.web.struts.InitializingPlugIn;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;


public class InitializingPlugInConver extends InitializingPlugIn {


    /**
     * ע�����ֵ����ת����, ֻҪת����������<code>Converter</code>����λ���������<code>
     * support</code>�Ӱ��о���Ϊת����ע��
     * @param convertUtilsBean
     */
    protected void registryConverters(ConvertUtilsBean convertUtilsBean) {
        convertUtilsBean.register(new StringConverterWith(), String.class);
        convertUtilsBean.register(new DateConverter(), Date.class);
        convertUtilsBean.register(new SqlDateConverter(), java.sql.Date.class);
        convertUtilsBean.register(new SqlTimestampConverter(), Timestamp.class);
        convertUtilsBean.register(new ColumnConverter(), Column.class);
        convertUtilsBean.register(new CodeNameConverter(), CodeName.class);
        convertUtilsBean.register(new EnumerationConverter(), EnumerationCodeName.class);
        
        // ע��Ĭ��ֵΪ null ������ת����
        convertUtilsBean.register(new IntegerConverter(null), Integer.class);
        convertUtilsBean.register(new LongConverter(null), Long.class);
        convertUtilsBean.register(new DoubleConverter(null), Double.class);
        convertUtilsBean.register(new BigDecimalConverter(null), BigDecimal.class);
    }
    
}
