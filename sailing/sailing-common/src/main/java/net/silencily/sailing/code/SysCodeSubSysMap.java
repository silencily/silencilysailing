package net.silencily.sailing.code;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoyf
 *
 */
public class SysCodeSubSysMap extends HashMap implements Map {
	
	public Object get(Object key) {
		// TODO Auto-generated method stub
		//SysCodeModuleMap value=(SysCodeModuleMap)super.get(key);
		Object value = super.get(key);		
		if(value==null)
		{
			SysCodeModuleMap scm=new SysCodeModuleMap();
			scm.setSubSysCode(key.toString());
			value=scm;
			this.put(key, value);
		}
		 return value;
	}

	
}
