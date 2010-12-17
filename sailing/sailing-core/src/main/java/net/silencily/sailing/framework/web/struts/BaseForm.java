package net.silencily.sailing.framework.web.struts;

import java.util.List;


/**
 * 基于<code>struts</code>的前端<code>form-bean</code>基类
 * @author zhangli
 * @version $Id: BaseForm.java,v 1.1 2010/12/10 10:54:19 silencily Exp $
 * @since 2007-4-29
 */
@SuppressWarnings("serial")
public class BaseForm extends BaseActionForm {

    private List list;
    
    public boolean isShrink() {
        return false;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
