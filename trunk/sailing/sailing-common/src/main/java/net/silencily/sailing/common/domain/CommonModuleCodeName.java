package net.silencily.sailing.common.domain;

import net.silencily.sailing.common.CommonConstants;
import net.silencily.sailing.framework.codename.AbstractModuleCodeName;


/**
 * @author zhangli
 * @since 2007-3-22
 * @version $Id: CommonModuleCodeName.java,v 1.1 2010/12/10 10:54:16 silencily Exp $
 */
public class CommonModuleCodeName extends AbstractModuleCodeName {

    protected String getInternalModuleName() {
        return CommonConstants.MODULE_NAME;
    }
}
