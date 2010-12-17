package net.silencily.sailing.framework.web.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.silencily.sailing.exception.UnexpectedException;
import net.silencily.sailing.framework.core.ContextInfo;
import net.silencily.sailing.framework.persistent.filter.Condition;
import net.silencily.sailing.framework.persistent.filter.ConditionConstants;
import net.silencily.sailing.framework.persistent.filter.ConditionInfo;
import net.silencily.sailing.framework.persistent.filter.Paginater;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * <p>如果<code>BaseActionForm</code>中包含<code>Condition</code>就设置到<code>ContextInfo</code>
 * 中, 同时判断是否执行分页操作. 如果一个<code>web</code>请求要让架构执行分页操作, 那么在
 * 请求中一定要包含设置<code>Paginater</code>的属性的参数, 比如<tt>paginater.page=0</tt>
 * 或者<tt>paginater.pageSize=20</tt>等,在<code>struts</code>中这个类要在<code>RequestProcessor's populate</code>
 * 之后执行</p>
 * <p>对于同名属性,它的值在区域内的情况(比如"创建时间在__到__")这样处理属性名: 仅仅在保存属
 * 性的<code>map</code>区分出<code>key</code>就可以了,在<tt>jsp</tt>内我们可以利用
 * <code>struts's map</code>属性语法, 下面是实际应用中的一个例子<pre>
 *    &lt;tr class="tab_5"&gt;
 *       &lt;td width="20%"&gt;发布时间&lt;/td&gt;
 *       &lt;td width="80%"&gt;
 *           &lt;input name="conditions(publishedTime1).name" value="publishedTime" type="hidden"/&gt;
 *           &lt;input name="conditions(publishedTime1).operator" value="&gt;=" type="hidden"/&gt;
 *           &lt;input name="conditions(publishedTime1).type" value="java.util.Date" type="hidden"/&gt;
 *           &lt;input name="conditions(publishedTime1).value" value="&lt;c:out value='${theForm.conditions['publishedTime1'].value}'/&gt;" type="text"/&gt;
 *           &lt;input name="conditions(publishedTime2).name" value="publishedTime" type="hidden"/&gt;
 *           &lt;input name="conditions(publishedTime2).operator" value="&lt;=" type="hidden"/&gt;
 *           &lt;input name="conditions(publishedTime2).type" value="java.util.Date" type="hidden"/&gt;
 *           &lt;input name="conditions(publishedTime2).value" value="&lt;c:out value='${theForm.conditions['publishedTime2'].value}'/&gt;" type="text"/&gt;
 *       &lt;/td&gt;
 *   &lt;/tr&gt;
 * </pre>仔细看上面的片断, 可以看到尽管<code>key</code>成了<tt>publishedTime1</tt>和<tt>publishedTime2</tt>
 * 而属性名<tt>conditions(publishedTime1).name</tt>仍然是<tt>publishedTime</tt>
 * </p>
 * <p>如果条件的名称是<code>empty</code>, 或者没有组合条件, 这个条件就忽略不计</p>
 * @author scott
 * @since 2006-5-7
 * @version $Id: RetrieveParametersForContextInfo.java,v 1.1 2010/12/10 10:54:20 silencily Exp $
 *
 */
public class RetrieveParametersForContextInfo implements CustomRequestProcessorTemplate.ProcessPopulateExecutor {
	
    public void execute(HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping) throws ServletException {
        ConditionInfo info = new ConditionInfo();
        ContextInfo.setContextCondition(info);
        
        if (form == null || !(form instanceof BaseActionForm)) {
            return;
        }
        BaseActionForm theForm = (BaseActionForm) form;
        List conditions = new ArrayList(theForm.getConditions().size());
//        conditions.addAll(theForm.getConditions().values());
//        Collections.sort(conditions);
        
        /* 过滤掉既没有名称又没有CompositionConditions的条件 */
        for (Iterator it = theForm.getConditions().values().iterator(); it.hasNext(); ) {
            Condition cond = (Condition) it.next();
            String name = cond.getName();
            /* 在创建条件时, Condition.value 属性仍然是 form 提交的 string */
            if (StringUtils.isNotBlank(name) || cond.getCompositeConditions().length > 0) {
                String value = (String) cond.getValue();
                Class type = cond.getType();
                if (type == null) {
                    type = ConditionConstants.DEFAULT_TYPE;
                }
                try {
                    Object result = BeanUtilsBean.getInstance().getConvertUtils().convert(value, type);
                    cond.setValue(result);
                    conditions.add(cond);
                } catch (Throwable e) {
                    String msg = "无法把条件[" 
                        + cond.getName() 
                        + "]值[" 
                        + cond.getValue() 
                        + "]转成类型[" 
                        + type.getName() 
                        + "]";
                    theForm.setException(new UnexpectedException(msg, e));
                    return;
                }
            }
        }
        Collections.sort(conditions);
        info.setAppendConditions((Condition[]) conditions.toArray(new Condition[conditions.size()]));
        
        Paginater paginater = theForm.getPaginater();
        if (paginater.isNotPaginated()) {
            paginater = Paginater.NOT_PAGINATED;
        }
        info.setPaginater(paginater);
        ContextInfo.setContextCondition(info);
    }
}
